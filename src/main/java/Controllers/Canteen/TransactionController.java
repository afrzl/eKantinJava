package Controllers.Canteen;

import Managers.CartManager;
import Managers.ProductManager;
import Models.*;
import Utils.AlertUtils;
import Utils.SceneLoader;
import Utils.Session;
import Views.Canteen.CardProduct;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.UnaryOperator;

public class TransactionController implements Initializable, Observer {
    @FXML
    private Button cancelButton;

    @FXML
    private FontAwesomeIconView nav_dashboard_btn;

    @FXML
    private FontAwesomeIconView nav_product_btn;

    @FXML
    private FontAwesomeIconView nav_signout_btn;

    @FXML
    private FontAwesomeIconView nav_transaction_btn;

    @FXML
    private Button payButton;

    @FXML
    private TextField payField;

    @FXML
    private TableColumn<CartItem, String> priceCol;

    @FXML
    private GridPane productGrid;

    @FXML
    private TableColumn<CartItem, String> productNameCol;

    @FXML
    private TableView<CartItem> cartTable;

    @FXML
    private TableColumn<CartItem, Integer> qtyCol;

    @FXML
    private Label returnLabel;

    @FXML
    private Label topbarTime;

    @FXML
    private Label topbarUsername;

    @FXML
    private Label totalLabel;

    private ProductManager productManager;
    private CartManager cartManager;

    @FXML
    void handleNavDashboard(MouseEvent event) {
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        SceneLoader.loadScene(stage, "/Views/Canteen/dashboard.fxml", "Dashboard");
    }

    @FXML
    void handleNavProduct(MouseEvent event) {
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        SceneLoader.loadScene(stage, "/Views/Canteen/product.fxml", "Produk");
    }

    @FXML
    void handleNavSignOut(MouseEvent event) {
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        SceneLoader.loadScene(stage, "/Views/login.fxml", "Login");
    }

    @FXML
    void handleNavTransaction(MouseEvent event) {

    }

    @FXML
    void handlePayField() {
        try {
            int total = parseCurrency(totalLabel.getText());

            String payText = payField.getText();
            if (payText == null || payText.trim().isEmpty()) {
                payText = "0";
            }

            payText = payText.replaceAll("[^\\d]", "");

            int payAmount = Integer.parseInt(payText);
            int change = payAmount - total;

            NumberFormat formatter = NumberFormat.getCurrencyInstance(new Locale("id", "ID"));
            String formattedChange = formatter.format(change);

            returnLabel.setText(formattedChange);
        } catch (NumberFormatException e) {
            returnLabel.setText("Rp0");
        }
    }

    private int parseCurrency(String formattedCurrency) {
        try {
            NumberFormat format = NumberFormat.getCurrencyInstance(new Locale("id", "ID"));
            Number number = format.parse(formattedCurrency);
            return number.intValue();
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    @FXML
    void handleClickCartTable(MouseEvent event) {
        if (event.getClickCount() == 2) {
            Optional<ButtonType> result = AlertUtils.showConfirmationDialog(
                    "Konfirmasi",
                    "Apakah Anda yakin ingin menghapus produk ini dari keranjang?"
            );

            if (result.isPresent() && result.get() == ButtonType.OK) {
                CartItem selectedItem = cartTable.getSelectionModel().getSelectedItem();
                if (selectedItem != null) {
                    cartManager.removeProductFromCart(selectedItem);
                }
            }
        }
    }

    @FXML
    void handlePayButton(ActionEvent event) {
        ArrayList<TransactionDetail> transactionDetails = new ArrayList<>();

        ObservableList<CartItem> cartItems = cartTable.getItems();
        for(CartItem cartItem : cartItems) {
            TransactionDetail transactionDetail = new TransactionDetail(null, null, cartItem.getProduct(), cartItem.getQty(), cartItem.getTotalPrice());
            transactionDetails.add(transactionDetail);
        }

        int totalPrice = parseCurrency(totalLabel.getText());
        String payText = payField.getText();
        if (payText == null || payText.trim().isEmpty()) {
            payText = "0";
        }

        payText = payText.replaceAll("[^\\d]", "");

        int totalPaid = Integer.parseInt(payText);
        int changeBack = totalPaid - totalPrice;

        Transaction transaction = new Transaction(null, Session.getUserLoggedIn(), LocalDateTime.now(), totalPrice, totalPaid, changeBack, transactionDetails);

        TransactionDAO transactionDAO = new TransactionDAO();
        transactionDAO.insertTransaction(transaction);
        AlertUtils.showSuccessAlert("Sukses", "Transaksi Berhasil");
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadCart();
        loadProduct();

        productNameCol.setCellValueFactory(new PropertyValueFactory<>("productName"));
        qtyCol.setCellValueFactory(cellData -> cellData.getValue().qtyProperty().asObject());
        priceCol.setCellValueFactory(cellData -> cellData.getValue().getFormattedTotalPrice());

        NumberFormat formatter = NumberFormat.getCurrencyInstance(new Locale("id", "ID"));
        totalLabel.setText(formatter.format(0));
        returnLabel.setText(formatter.format(0));

        UnaryOperator<TextFormatter.Change> filter = change -> {
            String text = change.getText();

            if (text.matches("[0-9]*") || change.isDeleted() || change.isContentChange()) {
                return change;
            }

            return null;
        };

        TextFormatter<String> textFormatter = new TextFormatter<>(filter);
        payField.setTextFormatter(textFormatter);

        payField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.isEmpty()) {
                return;
            }

            String plainText = newValue.replaceAll("[^\\d]", "");

            try {
                NumberFormat formatter2 = NumberFormat.getInstance(new Locale("id", "ID"));
                String formattedText = formatter2.format(Long.parseLong(plainText));
                payField.setText(formattedText);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        });
    }

    private void loadCart() {
        cartManager = new CartManager();
        cartManager.addObserver(this);
    }

    private void loadProduct() {
        productManager = new ProductManager();
        productManager.loadProductsFromDatabase();
        addProductsToGrid();
    }

    private void addProductsToGrid() {
        int col = 0;
        int row = 0;

        for (Product product : productManager.getProducts()) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/Views/Canteen/cardProduct.fxml"));
                loader.setControllerFactory(c -> new CardProduct(product, cartManager));
                AnchorPane cardProduct = loader.load();

                productGrid.add(cardProduct, col, row);

                col++;
                if (col == 2) {
                    col = 0;
                    row++;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void update(Observable o, Object arg) {
        if (o instanceof CartManager && arg instanceof List) {
            List<CartItem> cartData = (List<CartItem>) arg;
            ObservableList<CartItem> observableCartData = FXCollections.observableArrayList(cartData);
            Platform.runLater(() -> {
                cartTable.setItems(observableCartData);
                updateTotalLabel(cartData);
            });
        }
    }

    private void updateTotalLabel(List<CartItem> cartData) {
        int total = cartData.stream().mapToInt(CartItem::getTotalPrice).sum();
        NumberFormat formatter = NumberFormat.getCurrencyInstance(new Locale("id", "ID"));
        String formattedTotal = formatter.format(total);
        totalLabel.setText(formattedTotal);
        handlePayField();
    }
}

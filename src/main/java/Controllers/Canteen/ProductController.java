package Controllers.Canteen;

import Managers.ProductManager;
import Models.*;
import Utils.AlertUtils;
import Utils.ImageUploader;
import Utils.SceneLoader;
import Utils.Session;
import Views.Canteen.ProductTableView;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class ProductController implements Initializable {
    @FXML
    private FontAwesomeIconView nav_dashboard_btn;

    @FXML
    private FontAwesomeIconView nav_product_btn;

    @FXML
    private FontAwesomeIconView nav_signout_btn;

    @FXML
    private FontAwesomeIconView nav_transaction_btn;

    @FXML
    private TableColumn<?, ?> product_col_desc;

    @FXML
    private TableColumn<?, ?> product_col_image;

    @FXML
    private TableColumn<?, ?> product_col_name;

    @FXML
    private TableColumn<?, ?> product_col_no;

    @FXML
    private TableColumn<?, ?> product_col_price;

    @FXML
    private TableColumn<?, ?> product_col_stock;

    @FXML
    private TableView<Product> product_table;

    @FXML
    private Label topbarUsername;

    @FXML
    private Label topbarTime;

    @FXML
    private ComboBox<Category> categoryField;

    @FXML
    private Button clearButton;

    @FXML
    private Button createButton;

    @FXML
    private Button deleteButton;

    @FXML
    private TextArea descriptionField;

    @FXML
    private TextField nameField;

    @FXML
    private TextField priceField;

    @FXML
    private TextField stockField;

    @FXML
    private Button updateButton;

    @FXML
    private Button uploadImageButton;

    @FXML
    private ImageView imageView;

    private ProductManager productManager;
    private ProductTableView productTableView;
    private ProductDAO productDAO;
    private CategoryDAO categoryDAO;
    private File selectedImageFile;

    public ProductController() {
        this.productDAO = new ProductDAO();
        this.categoryDAO = new CategoryDAO();
    }

    @FXML
    void handleNavDashboard(MouseEvent event) {
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        SceneLoader.loadScene(stage, "/Views/Canteen/dashboard.fxml", "Dashboard");
    }

    @FXML
    void handleNavProduct(MouseEvent event) {

    }

    @FXML
    void handleNavSignOut(MouseEvent event) {
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        SceneLoader.loadScene(stage, "/Views/login.fxml", "Login");
    }

    @FXML
    void handleNavTransaction(MouseEvent event) {
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        SceneLoader.loadScene(stage, "/Views/Canteen/transaction.fxml", "Transaksi");
    }

    @FXML
    void handleUploadImage(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg"));
        selectedImageFile = fileChooser.showOpenDialog(new Stage());

        if (selectedImageFile != null) {
            Image image = new Image(selectedImageFile.toURI().toString());
            imageView.setImage(image);
            System.out.println("Selected file: " + selectedImageFile.getName());
        }
    }

    @FXML
    void handleClear() {
        nameField.clear();
        categoryField.getSelectionModel().clearSelection();
        categoryField.setValue(null);
        descriptionField.clear();
        priceField.clear();
        stockField.clear();
        imageView.setImage(null);

        product_table.getSelectionModel().clearSelection();

        createButton.setVisible(true);
        updateButton.setVisible(false);
        deleteButton.setVisible(false);
    }

    @FXML
    void handleSave(ActionEvent event) {
        if (validateInput()) {
            try {
                String name = nameField.getText();
                String description = descriptionField.getText();
                int price = Integer.parseInt(priceField.getText());
                int stock = Integer.parseInt(stockField.getText());
                Category category = categoryField.getValue();
                User canteen = Session.getUserLoggedIn();

                String imagePath = "";
                if (selectedImageFile != null) {
                    imagePath = ImageUploader.uploadImage(selectedImageFile, "images");
                }

                Product newProduct = new Product(null, name, description, imagePath, price, stock, category, canteen);
                productManager.addProduct(newProduct);

                AlertUtils.showSuccessAlert("Sukses", "Produk berhasil disimpan");
                handleClear();
            } catch (IOException e) {
                e.printStackTrace();
                AlertUtils.showErrorAlert("Error", "Gagal upload gambar");
            } catch (Exception e) {
                e.printStackTrace();
                AlertUtils.showErrorAlert("Error", "Gagal menyimpan produk");
            }
        }
    }

    @FXML
    void handleUpdate(ActionEvent event) {
        Product selectedProduct = product_table.getSelectionModel().getSelectedItem();
        if (selectedProduct != null && validateInput()) {
            try {
                String name = nameField.getText();
                String description = descriptionField.getText();
                int price = Integer.parseInt(priceField.getText());
                int stock = Integer.parseInt(stockField.getText());
                Category category = categoryField.getValue();

                String imagePath = selectedProduct.getImage();

                if (selectedImageFile != null) {
                    if (imagePath != null && !imagePath.isEmpty()) {
                        File imageFile = new File(imagePath);
                        if (imageFile.exists()) {
                            imageFile.delete();
                        }
                    }

                    imagePath = ImageUploader.uploadImage(selectedImageFile, "images");
                }

                selectedProduct.setName(name);
                selectedProduct.setDescription(description);
                selectedProduct.setPrice(price);
                selectedProduct.setStock(stock);
                selectedProduct.setCategory(category);
                selectedProduct.setImage(imagePath);

                productManager.updateProduct(selectedProduct);

                AlertUtils.showSuccessAlert("Sukses", "Produk berhasil diperbarui");

                handleClear();
            } catch (IOException e) {
                e.printStackTrace();
                AlertUtils.showErrorAlert("Error", "Gagal upload gambar");
            } catch (Exception e) {
                e.printStackTrace();
                AlertUtils.showErrorAlert("Error", "Gagal memperbarui produk");
            }
        }
    }
    @FXML
    void handleDelete(ActionEvent event) {
        Product selectedProduct = product_table.getSelectionModel().getSelectedItem();
        if (selectedProduct != null) {
            Optional<ButtonType> result = AlertUtils.showConfirmationDialog(
                    "Konfirmasi Hapus Produk",
                    "Apakah Anda yakin ingin menghapus produk ini?"
            );

            if (result.isPresent() && result.get() == ButtonType.OK) {
                productManager.removeProduct(selectedProduct);

                String imagePath = selectedProduct.getImage();
                if (imagePath != null && !imagePath.isEmpty()) {
                    File imageFile = new File(imagePath);
                    if (imageFile.exists()) {
                        imageFile.delete();
                    }
                }

                AlertUtils.showSuccessAlert("Sukses", "Produk berhasil dihapus");
                handleClear();
            }
        } else {
            AlertUtils.showErrorAlert("Error", "Pilih produk yang akan dihapus");
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        updateTime();
        topbarUsername.setText(Session.getUserLoggedIn().getName());
        loadProduct();
        loadCategoryComboBox();
    }

    private void loadCategoryComboBox() {
        List<Category> categories = categoryDAO.getAllCategories();
        ObservableList<Category> categoryList = FXCollections.observableArrayList(categories);
        categoryField.setItems(categoryList);
    }

    private void loadProduct() {
        productManager = new ProductManager();
        productTableView = new ProductTableView(product_table);

        productManager.addObserver(productTableView);

        productManager.loadProductsFromDatabase();
    }

    private void updateTime() {
        Timeline clock = new Timeline(new KeyFrame(Duration.ZERO, e -> {
            LocalDateTime now = LocalDateTime.now();
            topbarTime.setText(now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        }), new KeyFrame(Duration.seconds(1)));

        clock.setCycleCount(Timeline.INDEFINITE);
        clock.play();
    }

    @FXML
    void handleTableClick(MouseEvent event) {
        if (event.getClickCount() == 1) {
            Product selectedProduct = product_table.getSelectionModel().getSelectedItem();
            if (selectedProduct != null) {
                nameField.setText(selectedProduct.getName());
                categoryField.setValue(selectedProduct.getCategory());
                descriptionField.setText(selectedProduct.getDescription());
                priceField.setText(String.valueOf(selectedProduct.getPrice()));
                stockField.setText(String.valueOf(selectedProduct.getStock()));
                String imagePath = selectedProduct.getImage();
                if (imagePath != null && !imagePath.isEmpty()) {
                    File imageFile = new File(imagePath);
                    if (imageFile.exists()) {
                        Image image = new Image(imageFile.toURI().toString());
                        imageView.setImage(image);
                        imageView.setFitWidth(200);
                        imageView.setPreserveRatio(true);
                    } else {
                        imageView.setImage(null);
                    }
                } else {
                    imageView.setImage(null);
                }

                createButton.setVisible(false);
                updateButton.setVisible(true);
                deleteButton.setVisible(true);
            }
        }
    }

    private boolean validateInput() {
        StringBuilder errors = new StringBuilder();

        if (nameField.getText().isEmpty()) {
            errors.append("Nama produk harus diisi.\n");
        }
        if (descriptionField.getText().isEmpty()) {
            errors.append("Deskripsi produk harus diisi.\n");
        }
        if (priceField.getText().isEmpty()) {
            errors.append("Harga produk harus diisi.\n");
        } else {
            try {
                Integer.parseInt(priceField.getText());
            } catch (NumberFormatException e) {
                errors.append("Harga produk harus berupa angka yang valid.\n");
            }
        }
        if (stockField.getText().isEmpty()) {
            errors.append("Stok produk harus diisi.\n");
        } else {
            try {
                Integer.parseInt(stockField.getText());
            } catch (NumberFormatException e) {
                errors.append("Stok produk harus berupa angka yang valid.\n");
            }
        }
        if (categoryField.getValue() == null) {
            errors.append("Kategori produk harus dipilih.\n");
        }
        if (!errors.isEmpty()) {
            AlertUtils.showErrorAlert("Validasi Error", errors.toString());
            return false;
        }
        return true;
    }
}

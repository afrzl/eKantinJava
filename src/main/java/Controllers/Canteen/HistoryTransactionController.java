package Controllers.Canteen;

import Managers.ProductManager;
import Managers.TransactionManager;
import Models.ProductDAO;
import Models.Transaction;
import Models.TransactionDAO;
import Utils.SceneLoader;
import Utils.Session;
import Views.Canteen.HistoryTransactionTableView;
import Views.Canteen.ProductTableView;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class HistoryTransactionController implements Initializable {
    @FXML
    private TableColumn<?, ?> historyDetailCol;

    @FXML
    private TableColumn<?, ?> historyIdCol;

    @FXML
    private TableView<Transaction> historyTable;

    @FXML
    private TableColumn<?, ?> historyTimeCol;

    @FXML
    private TableColumn<?, ?> historyTotalCol;

    @FXML
    private FontAwesomeIconView nav_dashboard_btn;

    @FXML
    private FontAwesomeIconView nav_product_btn;

    @FXML
    private FontAwesomeIconView nav_signout_btn;

    @FXML
    private FontAwesomeIconView nav_transaction_btn;

    @FXML
    private Label topbarTime;

    @FXML
    private Label topbarUsername;

    private TransactionManager transactionManager;
    private HistoryTransactionTableView historyTransactionTableView;
    private TransactionDAO transactionDAO;

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
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        SceneLoader.loadScene(stage, "/Views/Canteen/transaction.fxml", "Transaksi");
    }

    @FXML
    void handleNavHistory(MouseEvent event) {
//        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
//        SceneLoader.loadScene(stage, "/Views/Canteen/historyTransactions.fxml", "Riwayat Transaksi");
    }

    @FXML
    void handleTableClick(MouseEvent event) {

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        updateTime();
        topbarUsername.setText(Session.getUserLoggedIn().getName());
        loadTransactions();
    }

    private void updateTime() {
        Timeline clock = new Timeline(new KeyFrame(Duration.ZERO, e -> {
            LocalDateTime now = LocalDateTime.now();
            topbarTime.setText(now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        }), new KeyFrame(Duration.seconds(1)));

        clock.setCycleCount(Timeline.INDEFINITE);
        clock.play();
    }

    private void loadTransactions() {
        transactionManager = new TransactionManager();
        historyTransactionTableView = new HistoryTransactionTableView(historyTable);

        transactionManager.addObserver(historyTransactionTableView);

        transactionManager.loadTransactionsFromDatabase();
    }
}

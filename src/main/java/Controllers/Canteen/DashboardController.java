package Controllers.Canteen;

import Utils.SceneLoader;
import Utils.Session;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class DashboardController implements Initializable {
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

    @FXML
    void handleNavDashboard(MouseEvent event) {

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

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        updateTime();
        topbarUsername.setText(Session.getUserLoggedIn().getName());
    }

    private void updateTime() {
        Timeline clock = new Timeline(new KeyFrame(Duration.ZERO, e -> {
            LocalDateTime now = LocalDateTime.now();
            topbarTime.setText(now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        }), new KeyFrame(Duration.seconds(1)));

        clock.setCycleCount(Timeline.INDEFINITE);
        clock.play();
    }
}

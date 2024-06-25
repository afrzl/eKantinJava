package Controllers.Admin;

import Utils.AlertUtils;
import Utils.SceneLoader;
import Utils.Session;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.util.Duration;

import javax.swing.text.html.Option;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.ResourceBundle;

public class CanteenController implements Initializable {
    @FXML
    private FontAwesomeIconView navCategoryBtn;

    @FXML
    private FontAwesomeIconView navDashboardBtn;

    @FXML
    private FontAwesomeIconView navProductBtn;

    @FXML
    private FontAwesomeIconView navSignoutBtn;

    @FXML
    private FontAwesomeIconView navUserBtn;

    @FXML
    private Label topbarTime;

    @FXML
    private Label topbarUsername;

    @FXML
    void handleNavCategory(MouseEvent event) {
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        SceneLoader.loadScene(stage, "/Views/Admin/category.fxml", "Data Kategori");
    }

    @FXML
    void handleNavDashboard(MouseEvent event) {
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        SceneLoader.loadScene(stage, "/Views/Admin/dashboard.fxml", "Dashboard");
    }

    @FXML
    void handleNavProduct(MouseEvent event) {
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        SceneLoader.loadScene(stage, "/Views/Admin/product.fxml", "Data Produk");
    }

    @FXML
    void handleNavSignOut(MouseEvent event) {
        Optional<ButtonType> confirmation = AlertUtils.showConfirmationDialog("Konfirmasi", "Apakah anda yakin akan logout?");
        if (confirmation.isPresent() && confirmation.get() == ButtonType.OK) {
            Session.destroySession();
            Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            SceneLoader.loadScene(stage, "/Views/login.fxml", "Login");
        }
    }

    @FXML
    void handleUserNav(MouseEvent event) {
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        SceneLoader.loadScene(stage, "/Views/Admin/canteen.fxml", "Data Kantin");
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
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

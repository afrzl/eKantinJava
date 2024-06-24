package Controllers;

import Models.User;
import Models.UserDAO;
import Utils.AlertUtils;
import Utils.SceneLoader;
import Utils.Session;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class LoginController implements Initializable {
    private UserDAO userDAO;

    @FXML
    private TextField emailField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Button loginButton;

    public LoginController() {
        this.userDAO = new UserDAO();
    }

    private boolean validation() {
        return emailField.getText().isEmpty() || passwordField.getText().isEmpty();
    }

    @FXML
    void handleLogin(ActionEvent event) {
        String email = emailField.getText();
        String password = passwordField.getText();

        if(validation()) {
            AlertUtils.showErrorAlert("Error", "Email dan password harus diisi!");
            return;
        }

        User user = userDAO.auth(email, password);
        if (user != null) {
            Session.setUserLoggedIn(user);

            if (user.getRole().equals("admin")) {
                AlertUtils.showSuccessAlert("Sukses", "Berhasil login sebagai admin!");
            } else if (user.getRole().equals("canteen")) {
                AlertUtils.showSuccessAlert("Sukses", "Berhasil login sebagai kantin!");
                Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
                SceneLoader.loadScene(stage, "/Views/Canteen/dashboard.fxml", "Dashboard");
            } else {
                AlertUtils.showSuccessAlert("Sukses", "Berhasil login!");
            }
        } else {
            AlertUtils.showErrorAlert("Gagal", "Akun tidak ditemukan!");
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        emailField.setText("kantin");
        passwordField.setText("admin");
    }
}

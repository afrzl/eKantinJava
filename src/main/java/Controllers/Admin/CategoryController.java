package Controllers.Admin;

import Managers.CategoryManager;
import Models.*;
import Utils.AlertUtils;
import Utils.SceneLoader;
import Utils.Session;
import Views.Admin.CategoryTableView;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.ResourceBundle;

public class CategoryController implements Initializable {
    @FXML
    private TableColumn<Category, String> categoryNameCol;

    @FXML
    private TableColumn<Category, Integer> categoryNoCol;

    @FXML
    private TableView<Category> categoryTable;

    @FXML
    private Button clearButton;

    @FXML
    private Button createButton;

    @FXML
    private Button deleteButton;

    @FXML
    private TextField nameField;

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
    private Button updateButton;

    private CategoryManager categoryManager;
    private CategoryTableView categoryTableView;
    private CategoryDAO categoryDAO;

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

        loadCategory();
    }

    private void loadCategory() {
        categoryManager = new CategoryManager();
        categoryTableView = new CategoryTableView(categoryTable);

        categoryManager.addObserver(categoryTableView);
        categoryManager.loadCategoriesFromDatabase();
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
    void handleClear() {
        nameField.clear();

        createButton.setVisible(true);
        updateButton.setVisible(false);
        deleteButton.setVisible(false);
    }

    @FXML
    void handleDelete(ActionEvent event) {
        Category selectedCategory = categoryTable.getSelectionModel().getSelectedItem();
        if (selectedCategory != null) {
            Optional<ButtonType> result = AlertUtils.showConfirmationDialog(
                    "Konfirmasi Hapus Kategori",
                    "Apakah Anda yakin ingin menghapus kategori "+ selectedCategory.getName() +"?"
            );

            if (result.isPresent() && result.get() == ButtonType.OK) {
                categoryManager.removeProduct(selectedCategory);

                AlertUtils.showSuccessAlert("Sukses", "Kategori berhasil dihapus");
                handleClear();
            }
        } else {
            AlertUtils.showErrorAlert("Error", "Pilih kategori yang akan dihapus");
        }
    }

    @FXML
    void handleSave(ActionEvent event) {
        if (validateInput()) {
            try {
                String name = nameField.getText();

                Category newCategory = new Category(null, name);
                categoryManager.addCategory(newCategory);

                AlertUtils.showSuccessAlert("Sukses", "Kategori berhasil disimpan");
                handleClear();
            } catch (Exception e) {
                e.printStackTrace();
                AlertUtils.showErrorAlert("Error", "Gagal menyimpan kategori");
            }
        }
    }

    @FXML
    void handleTableClick(MouseEvent event) {
        if (event.getClickCount() == 1) {
            Category selectedCategory = categoryTable.getSelectionModel().getSelectedItem();
            if (selectedCategory != null) {
                nameField.setText(selectedCategory.getName());

                createButton.setVisible(false);
                updateButton.setVisible(true);
                deleteButton.setVisible(true);
            }
        }
    }

    @FXML
    void handleUpdate(ActionEvent event) {
        Category selectedCategory = categoryTable.getSelectionModel().getSelectedItem();
        if (selectedCategory != null && validateInput()) {
            try {
                String name = nameField.getText();

                selectedCategory.setName(name);

                categoryManager.updateCategory(selectedCategory);

                AlertUtils.showSuccessAlert("Sukses", "Kategori berhasil diperbarui");

                handleClear();
            } catch (Exception e) {
                e.printStackTrace();
                AlertUtils.showErrorAlert("Error", "Gagal memperbarui kategori");
            }
        }
    }

    private boolean validateInput() {
        StringBuilder errors = new StringBuilder();

        if (nameField.getText().isEmpty()) {
            errors.append("Nama kategori harus diisi.\n");
        }

        if (!errors.isEmpty()) {
            AlertUtils.showErrorAlert("Validasi Error", errors.toString());
            return false;
        }
        return true;
    }
}

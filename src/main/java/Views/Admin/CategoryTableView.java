package Views.Admin;

import Managers.CategoryManager;
import Models.Category;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.List;
import java.util.Observable;
import java.util.Observer;

public class CategoryTableView implements Observer {
    private TableView<Category> tableView;

    public CategoryTableView(TableView<Category> tableView) {
        this.tableView = tableView;
        initializeTable();
    }

    private void initializeTable() {
        TableColumn<Category, Integer> noCol = (TableColumn<Category, Integer>) tableView.getColumns().get(0);
        noCol.setCellValueFactory(column -> new ReadOnlyObjectWrapper<>(tableView.getItems().indexOf(column.getValue()) + 1));

        TableColumn<Category, String> categoryNameCol = (TableColumn<Category, String>) tableView.getColumns().get(1);
        categoryNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));

        tableView.getColumns().clear();

        tableView.getColumns().addAll(noCol, categoryNameCol);
    }

    @Override
    public void update(Observable o, Object arg) {
        if (o instanceof CategoryManager) {
            CategoryManager categoryManager = (CategoryManager) o;
            List<Category> categories = categoryManager.getCategories();

            tableView.getItems().clear();
            tableView.getItems().addAll(categories);
        }
    }
}

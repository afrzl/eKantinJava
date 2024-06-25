package Views.Canteen;

import Managers.ProductManager;
import Models.Product;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Callback;

import java.io.File;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

public class ProductTableView implements Observer {
    private TableView<Product> tableView;

    public ProductTableView(TableView<Product> tableView) {
        this.tableView = tableView;
        initializeTable();
    }

    private void initializeTable() {
        TableColumn<Product, Integer> noCol = (TableColumn<Product, Integer>) tableView.getColumns().get(0);
        noCol.setCellValueFactory(column -> new ReadOnlyObjectWrapper<>(tableView.getItems().indexOf(column.getValue()) + 1));

        TableColumn<Product, String> imageCol = (TableColumn<Product, String>) tableView.getColumns().get(1);
        imageCol.setCellValueFactory(new PropertyValueFactory<>("image"));

        imageCol.setCellFactory(new Callback<TableColumn<Product, String>, TableCell<Product, String>>() {
            @Override
            public TableCell<Product, String> call(TableColumn<Product, String> param) {
                return new TableCell<Product, String>() {
                    private final ImageView imageView = new ImageView();

                    @Override
                    protected void updateItem(String imagePath, boolean empty) {
                        super.updateItem(imagePath, empty);

                        if (imagePath == null || empty) {
                            setGraphic(null);
                        } else {
                            Image image = new Image(new File(imagePath).toURI().toString());
                            imageView.setImage(image);
                            imageView.setFitWidth(100);
                            imageView.setPreserveRatio(true);
                            setGraphic(imageView);
                        }
                    }
                };
            }
        });

        TableColumn<Product, String> nameCol = (TableColumn<Product, String>) tableView.getColumns().get(2);
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<Product, String> descCol = (TableColumn<Product, String>) tableView.getColumns().get(3);
        descCol.setCellValueFactory(new PropertyValueFactory<>("description"));

        TableColumn<Product, Integer> priceCol = (TableColumn<Product, Integer>) tableView.getColumns().get(4);
        priceCol.setCellValueFactory(new PropertyValueFactory<>("price"));

        TableColumn<Product, Integer> stockCol = (TableColumn<Product, Integer>) tableView.getColumns().get(5);
        stockCol.setCellValueFactory(new PropertyValueFactory<>("stock"));

        tableView.getColumns().clear();

        tableView.getColumns().addAll(noCol, imageCol, nameCol, descCol, priceCol, stockCol);
    }

    @Override
    public void update(Observable o, Object arg) {
        if (o instanceof ProductManager) {
            ProductManager productManager = (ProductManager) o;
            List<Product> products = productManager.getProducts();

            tableView.getItems().clear();
            tableView.getItems().addAll(products);
        }
    }
}

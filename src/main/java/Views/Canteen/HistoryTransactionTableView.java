package Views.Canteen;

import Managers.ProductManager;
import Managers.TransactionManager;
import Models.Product;
import Models.Transaction;
import Models.TransactionDetail;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Callback;

import java.io.File;
import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;
import java.util.Observable;
import java.util.Observer;

public class HistoryTransactionTableView implements Observer {
    private TableView<Transaction> tableView;

    public HistoryTransactionTableView(TableView<Transaction> tableView) {
        this.tableView = tableView;
        initializeTable();
    }

    private void initializeTable() {
        TableColumn<Transaction, String> historyIdCol = (TableColumn<Transaction, String>) tableView.getColumns().get(0);
        historyIdCol.setCellValueFactory(cellData -> cellData.getValue().getFormattedId());

        TableColumn<Transaction, LocalDateTime> historyTimeCol = (TableColumn<Transaction, LocalDateTime>) tableView.getColumns().get(1);
        historyTimeCol.setCellValueFactory(new PropertyValueFactory<>("timestamp"));

        TableColumn<Transaction, Integer> historyTotalCol = (TableColumn<Transaction, Integer>) tableView.getColumns().get(2);
        historyTotalCol.setCellValueFactory(new PropertyValueFactory<>("totalPrice"));
        historyTotalCol.setCellFactory(column -> new TableCell<Transaction, Integer>() {
            private final NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("id", "ID"));

            @Override
            protected void updateItem(Integer totalPrice, boolean empty) {
                super.updateItem(totalPrice, empty);
                if (totalPrice == null || empty) {
                    setText(null);
                } else {
                    setText(currencyFormat.format(totalPrice));
                }
            }
        });

        TableColumn<Transaction, String> historyDetailCol = (TableColumn<Transaction, String>) tableView.getColumns().get(3);
        historyDetailCol.setCellValueFactory(cellData -> {
            List<TransactionDetail> transactionDetails = cellData.getValue().getTransactionDetail();
            StringBuilder detailBuilder = new StringBuilder();

            // Membuat NumberFormat untuk mata uang Indonesia (Rupiah)
            NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("id", "ID"));

            for (TransactionDetail detail : transactionDetails) {
                detailBuilder.append(detail.getProduct().getName()); // Menggunakan nama produk sebagai contoh, sesuaikan dengan properti yang ingin ditampilkan
                detailBuilder.append(" - Qty: ");
                detailBuilder.append(detail.getQty());
                detailBuilder.append(", Harga: ");
                detailBuilder.append(currencyFormat.format(detail.getPrice())); // Format harga ke dalam format mata uang Rupiah
                detailBuilder.append("\n");
            }
            return new ReadOnlyObjectWrapper<>(detailBuilder.toString());
        });

        tableView.getColumns().clear();

        tableView.getColumns().addAll(historyIdCol, historyTimeCol, historyTotalCol, historyDetailCol);
    }

    @Override
    public void update(Observable o, Object arg) {
        if (o instanceof TransactionManager) {
            TransactionManager transactionManager = (TransactionManager) o;
            List<Transaction> transactions = transactionManager.getTransactions();

            tableView.getItems().clear();
            tableView.getItems().addAll(transactions);
        }
    }
}

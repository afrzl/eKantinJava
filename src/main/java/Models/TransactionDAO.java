package Models;

import Utils.Database;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class TransactionDAO {
    public List<Transaction> getAllTransaction() {
        return getAllTransactions(true);
    }

    public List<Transaction> getAllTransactions(boolean withDetails) {
        List<Transaction> transactions = new ArrayList<>();
        try {
            Connection conn = Database.getInstance().getConn();
            String query = "SELECT * FROM transactions";
            Statement statement = conn.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                Transaction transaction = createTransactionFromResultSet(resultSet, withDetails);
                transactions.add(transaction);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return transactions;
    }

    public Transaction getTransactionById(int id) {
        return getTransactionById(id, false);
    }

    public Transaction getTransactionById(int id, boolean withDetails) {
        Transaction transaction = null;
        try {
            Connection conn = Database.getInstance().getConn();
            String query = "SELECT * FROM transactions WHERE id = ?";
            PreparedStatement preparedStatement = conn.prepareStatement(query);
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                transaction = createTransactionFromResultSet(resultSet, withDetails);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return transaction;
    }

    public void insertTransaction(Transaction transaction) {
        String query = "INSERT INTO transactions (canteen_id, total_price, total_paid, change_back, timestamp) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = Database.getInstance().getConn();
             PreparedStatement preparedStatement = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

            preparedStatement.setInt(1, transaction.getCanteen().getId());
            preparedStatement.setInt(2, transaction.getTotalPrice());
            preparedStatement.setInt(3, transaction.getTotalPaid());
            preparedStatement.setInt(4, transaction.getChangeBack());
            preparedStatement.setTimestamp(5, Timestamp.valueOf(transaction.getTimestamp()));

            preparedStatement.executeUpdate();

            try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    int id = generatedKeys.getInt(1);
                    transaction.setId(id);
                }
            }

            if (transaction.getTransactionDetail() != null) {
                insertTransactionDetails(transaction.getId(), transaction.getTransactionDetail());
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void deleteTransaction(int id) {
        String query = "DELETE FROM transactions WHERE id = ?";
        try (Connection conn = Database.getInstance().getConn();
             PreparedStatement preparedStatement = conn.prepareStatement(query)) {

            deleteTransactionDetails(id);

            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private Transaction createTransactionFromResultSet(ResultSet resultSet, boolean withDetails) throws SQLException {
        int id = resultSet.getInt("id");
        int canteenId = resultSet.getInt("canteen_id");
        int totalPrice = resultSet.getInt("total_price");
        int totalPaid = resultSet.getInt("total_paid");
        int changeBack = resultSet.getInt("change_back");
        LocalDateTime timestamp = resultSet.getTimestamp("timestamp").toLocalDateTime();

        UserDAO userDAO = new UserDAO();
        User canteen = userDAO.getUserById(canteenId);

        ArrayList<TransactionDetail> transactionDetails = withDetails ? getTransactionDetailsByTransactionId(id) : null;

        return new Transaction(id, canteen, timestamp, totalPrice, totalPaid, changeBack, transactionDetails);
    }

    private ArrayList<TransactionDetail> getTransactionDetailsByTransactionId(int transactionId) {
        TransactionDetailDAO transactionDetailDAO = new TransactionDetailDAO();
        return transactionDetailDAO.getTransactionDetailsByTransactionId(transactionId);
    }

    private void insertTransactionDetails(int transactionId, List<TransactionDetail> transactionDetails) {
        TransactionDetailDAO transactionDetailDAO = new TransactionDetailDAO();
        for (TransactionDetail detail : transactionDetails) {
            detail.setTransaction(new Transaction(transactionId, null, null, null, null, null, null));
            transactionDetailDAO.insertTransactionDetail(detail);
        }
    }

    private void deleteTransactionDetails(int transactionId) {
        TransactionDetailDAO transactionDetailDAO = new TransactionDetailDAO();
        List<TransactionDetail> details = transactionDetailDAO.getTransactionDetailsByTransactionId(transactionId);
        for (TransactionDetail detail : details) {
            transactionDetailDAO.deleteTransactionDetail(detail.getId());
        }
    }
}
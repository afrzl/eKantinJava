package Models;

import Utils.Database;

import java.sql.*;
import java.util.ArrayList;

public class TransactionDetailDAO {
    public ArrayList<TransactionDetail> getTransactionDetailsByTransactionId(int transactionId) {
        ArrayList<TransactionDetail> transactionDetails = new ArrayList<>();
        try {
            Connection conn = Database.getInstance().getConn();
            String query = "SELECT * FROM transaction_details WHERE transaction_id = ?";
            PreparedStatement preparedStatement = conn.prepareStatement(query);
            preparedStatement.setInt(1, transactionId);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                int productId = resultSet.getInt("product_id");
                int transaction_id = resultSet.getInt("transaction_id");
                int qty = resultSet.getInt("qty");
                int price = resultSet.getInt("price");

                Product product = getProductById(productId);
                Transaction transaction = getTransactionById(transaction_id);

                TransactionDetail transactionDetail = new TransactionDetail(id, transaction, product, qty, price);
                transactionDetails.add(transactionDetail);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return transactionDetails;
    }

    public void insertTransactionDetail(TransactionDetail transactionDetail) {
        String query = "INSERT INTO transaction_details (transaction_id, product_id, qty, price) VALUES (?, ?, ?, ?)";
        try (Connection conn = Database.getInstance().getConn();
             PreparedStatement preparedStatement = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

            preparedStatement.setInt(1, transactionDetail.getTransaction().getId());
            preparedStatement.setInt(2, transactionDetail.getProduct().getId());
            preparedStatement.setInt(3, transactionDetail.getQty());
            preparedStatement.setInt(4, transactionDetail.getPrice());

            preparedStatement.executeUpdate();

            try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    int id = generatedKeys.getInt(1);
                    transactionDetail.setId(id);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void updateTransactionDetail(TransactionDetail transactionDetail) {
        String query = "UPDATE transaction_details SET transaction_id = ?, product_id = ?, qty = ?, price = ? WHERE id = ?";
        try (Connection conn = Database.getInstance().getConn();
             PreparedStatement preparedStatement = conn.prepareStatement(query)) {

            preparedStatement.setInt(1, transactionDetail.getTransaction().getId());
            preparedStatement.setInt(2, transactionDetail.getProduct().getId());
            preparedStatement.setInt(3, transactionDetail.getQty());
            preparedStatement.setInt(4, transactionDetail.getPrice());
            preparedStatement.setInt(5, transactionDetail.getId());

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void deleteTransactionDetail(int id) {
        String query = "DELETE FROM transaction_details WHERE id = ?";
        try (Connection conn = Database.getInstance().getConn();
             PreparedStatement preparedStatement = conn.prepareStatement(query)) {

            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private Product getProductById(int productId) {
        ProductDAO productDAO = new ProductDAO();
        return productDAO.getProductById(productId);
    }

    private Transaction getTransactionById(int transactionId) {
        TransactionDAO transactionDAO = new TransactionDAO();
        return transactionDAO.getTransactionById(transactionId);
    }


}

package Models;

import Utils.Database;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductDAO {
    public List<Product> getAllProducts() {
        List<Product> products = new ArrayList<>();
        try {
            Connection conn = Database.getInstance().getConn();
            String query = "SELECT * FROM products";
            Statement statement = conn.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                Product product = createProductFromResultSet(resultSet);
                products.add(product);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return products;
    }

    public Product getProductById(int id) {
        Product product = null;
        try {
            Connection conn = Database.getInstance().getConn();
            String query = "SELECT * FROM products WHERE id = ?";
            PreparedStatement preparedStatement = conn.prepareStatement(query);
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                product = createProductFromResultSet(resultSet);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return product;
    }

    public void insertProduct(Product product) {
        String query = "INSERT INTO products (name, description, image, price, stock, category_id, canteen_id) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = Database.getInstance().getConn();
             PreparedStatement preparedStatement = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

            setProductPreparedStatement(preparedStatement, product);
            preparedStatement.executeUpdate();

            try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    int id = generatedKeys.getInt(1);
                    product.setId(id);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void updateProduct(Product product) {
        String query = "UPDATE products SET name = ?, description = ?, image = ?, price = ?, stock = ?, category_id = ?, canteen_id = ? WHERE id = ?";
        try (Connection conn = Database.getInstance().getConn();
             PreparedStatement preparedStatement = conn.prepareStatement(query)) {

            setProductPreparedStatement(preparedStatement, product);
            preparedStatement.setInt(8, product.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void deleteProduct(Product product) {
        String query = "DELETE FROM products WHERE id = ?";
        try (Connection conn = Database.getInstance().getConn();
             PreparedStatement preparedStatement = conn.prepareStatement(query)) {

            preparedStatement.setInt(1, product.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private Product createProductFromResultSet(ResultSet resultSet) throws SQLException {
        int id = resultSet.getInt("id");
        String name = resultSet.getString("name");
        String description = resultSet.getString("description");
        String image = resultSet.getString("image");
        int price = resultSet.getInt("price");
        int stock = resultSet.getInt("stock");
        int categoryId = resultSet.getInt("category_id");
        int canteenId = resultSet.getInt("canteen_id");

        CategoryDAO categoryDAO = new CategoryDAO();
        Category category = categoryDAO.getCategoryById(categoryId);

        UserDAO userDAO = new UserDAO();
        User canteen = userDAO.getUserById(canteenId);

        return new Product(id, name, description, image, price, stock, category, canteen);
    }

    private void setProductPreparedStatement(PreparedStatement preparedStatement, Product product) throws SQLException {
        preparedStatement.setString(1, product.getName());
        preparedStatement.setString(2, product.getDescription());
        preparedStatement.setString(3, product.getImage());
        preparedStatement.setInt(4, product.getPrice());
        preparedStatement.setInt(5, product.getStock());
        preparedStatement.setInt(6, product.getCategory().getId());
        preparedStatement.setInt(7, product.getCanteen().getId());
    }
}

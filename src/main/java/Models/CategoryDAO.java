package Models;

import Utils.Database;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CategoryDAO {
    public List<Category> getAllCategories() {
        return getAllCategories(false);
    }

    public List<Category> getAllCategories(boolean withProducts) {
        List<Category> categories = new ArrayList<>();
        try {
            Connection conn = Database.getInstance().getConn();
            String query = "SELECT * FROM categories";
            Statement statement = conn.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                ArrayList<Product> products = withProducts ? getProductsByCategoryId(id) : new ArrayList<>();
                Category category = new Category(id, name, products);
                categories.add(category);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return categories;
    }

    public Category getCategoryById(int id) {
        return getCategoryById(id, false); // Default parameter false
    }

    public Category getCategoryById(int id, boolean withProducts) {
        Category category = null;
        try {
            Connection conn = Database.getInstance().getConn();
            String query = "SELECT * FROM categories WHERE id = ?";
            PreparedStatement preparedStatement = conn.prepareStatement(query);
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                String name = resultSet.getString("name");
                ArrayList<Product> products = withProducts ? getProductsByCategoryId(id) : new ArrayList<>();
                category = new Category(id, name, products);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return category;
    }

    public void insertCategory(Category category) {
        String query = "INSERT INTO categories (name) VALUES (?)";
        try (Connection conn = Database.getInstance().getConn();
             PreparedStatement preparedStatement = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

            preparedStatement.setString(1, category.getName());
            preparedStatement.executeUpdate();

            try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    int id = generatedKeys.getInt(1);
                    category.setId(id);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void updateCategory(Category category) {
        String query = "UPDATE categories SET name = ? WHERE id = ?";
        try (Connection conn = Database.getInstance().getConn();
             PreparedStatement preparedStatement = conn.prepareStatement(query)) {

            preparedStatement.setString(1, category.getName());
            preparedStatement.setInt(2, category.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void deleteCategory(int id) {
        String query = "DELETE FROM categories WHERE id = ?";
        try (Connection conn = Database.getInstance().getConn();
             PreparedStatement preparedStatement = conn.prepareStatement(query)) {

            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private ArrayList<Product> getProductsByCategoryId(int categoryId) {
        ArrayList<Product> products = new ArrayList<>();
        try {
            Connection conn = Database.getInstance().getConn();
            String query = "SELECT * FROM products WHERE category_id = ?";
            PreparedStatement preparedStatement = conn.prepareStatement(query);
            preparedStatement.setInt(1, categoryId);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                String description = resultSet.getString("description");
                String image = resultSet.getString("image");
                int price = resultSet.getInt("price");
                int stock = resultSet.getInt("stock");
                int canteenId = resultSet.getInt("canteen_id");

                UserDAO userDAO = new UserDAO();
                User canteen = userDAO.getUserById(canteenId);

                Product product = new Product(id, name, description, image, price, stock, null, canteen);
                products.add(product);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return products;
    }
}

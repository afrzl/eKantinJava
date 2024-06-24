package Models;

import Utils.Database;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDAO {
    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        try {
            Connection conn = Database.getInstance().getConn();
            String query = "SELECT * FROM users";
            Statement statement = conn.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String email = resultSet.getString("email");
                String name = resultSet.getString("name");
                String password = resultSet.getString("password");
                String phone = resultSet.getString("phone");
                String role = resultSet.getString("role");
                User user = new User(id, email, name, password, phone, role);
                users.add(user);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return users;
    }

    public User getUserById(int id) {
        User user = null;
        try {
            Connection conn = Database.getInstance().getConn();
            String query = "SELECT * FROM users WHERE id = ?";
            PreparedStatement preparedStatement = conn.prepareStatement(query);
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                String email = resultSet.getString("email");
                String name = resultSet.getString("name");
                String password = resultSet.getString("password");
                String phone = resultSet.getString("phone");
                String role = resultSet.getString("role");

                user = new User(id, email, name, password, phone, role);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return user;
    }

    public void insertUser(User user) {
        String query = "INSERT INTO users (email, name, password, phone, role) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = Database.getInstance().getConn();
             PreparedStatement preparedStatement = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

            setUserPreparedStatement(preparedStatement, user);
            preparedStatement.executeUpdate();

            try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    int id = generatedKeys.getInt(1);
                    user.setId(id);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void updateUser(User user) {
        String query = "UPDATE users SET email = ?, name = ?, password = ?, phone = ?, role = ? WHERE id = ?";
        try (Connection conn = Database.getInstance().getConn();
             PreparedStatement preparedStatement = conn.prepareStatement(query)) {

            setUserPreparedStatement(preparedStatement, user);
            preparedStatement.setInt(6, user.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void deleteUser(int id) {
        String query = "DELETE FROM users WHERE id = ?";
        try (Connection conn = Database.getInstance().getConn();
             PreparedStatement preparedStatement = conn.prepareStatement(query)) {

            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private User createUserFromResultSet(ResultSet resultSet) throws SQLException {
        int id = resultSet.getInt("id");
        String email = resultSet.getString("email");
        String name = resultSet.getString("name");
        String password = resultSet.getString("password");
        String phone = resultSet.getString("phone");
        String role = resultSet.getString("role");
        return new User(id, email, name, password, phone, role);
    }

    public User auth(String email, String password) {
        User user = null;
        String query = "SELECT * FROM users WHERE email = ?";
        try (Connection conn = Database.getInstance().getConn();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, email);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                String hashedPassword = rs.getString("password");
                if (BCrypt.checkpw(password, hashedPassword)) {
                    int id = rs.getInt("id");
                    String name = rs.getString("name");
                    String phone = rs.getString("phone");
                    String role = rs.getString("role");
                    user = new User(id, email, name, password, phone, role);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return user;
    }

    private void setUserPreparedStatement(PreparedStatement preparedStatement, User user) throws SQLException {
        String hashedPassword = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt(10));
        preparedStatement.setString(1, user.getEmail());
        preparedStatement.setString(2, user.getName());
        preparedStatement.setString(3, hashedPassword);
        preparedStatement.setString(4, user.getPhone());
        preparedStatement.setString(5, user.getRole());
    }
}

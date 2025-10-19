package dao;

import model.User;
import model.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class UserDAO {
    private static final Logger logger = Logger.getLogger(UserDAO.class.getName());

    public boolean createUser(User user) {
        String sql = "INSERT INTO users (username, password, role, email, phone, full_name) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, user.getUsername());
            pstmt.setString(2, user.getPassword());
            pstmt.setString(3, user.getRole());
            pstmt.setString(4, user.getEmail());
            pstmt.setString(5, user.getPhone());
            pstmt.setString(6, user.getFullName());

            int result = pstmt.executeUpdate();
            logger.info("Пользователь создан: " + user.getUsername());
            return result > 0;

        } catch (SQLException e) {
            logger.severe("Ошибка создания пользователя: " + e.getMessage());
            return false;
        }
    }

    public User authenticate(String username, String password) {
        String sql = "SELECT * FROM users WHERE username = ? AND password = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, username);
            pstmt.setString(2, password);

            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                User user = new User();
                user.setId(rs.getInt("id"));
                user.setUsername(rs.getString("username"));
                user.setPassword(rs.getString("password"));
                user.setRole(rs.getString("role"));
                user.setEmail(rs.getString("email"));
                user.setPhone(rs.getString("phone"));
                user.setFullName(rs.getString("full_name"));

                logger.info("Пользователь аутентифицирован: " + username);
                return user;
            }
        } catch (SQLException e) {
            logger.severe("Ошибка аутентификации: " + e.getMessage());
        }
        return null;
    }

    public boolean usernameExists(String username) {
        String sql = "SELECT id FROM users WHERE username = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();
            return rs.next();

        } catch (SQLException e) {
            logger.severe("Ошибка проверки username: " + e.getMessage());
            return false;
        }
    }

    public boolean emailExists(String email) {
        String sql = "SELECT id FROM users WHERE email = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, email);
            ResultSet rs = pstmt.executeQuery();
            return rs.next();

        } catch (SQLException e) {
            logger.severe("Ошибка проверки email: " + e.getMessage());
            return false;
        }
    }

    public boolean phoneExists(String phone) {
        String sql = "SELECT id FROM users WHERE phone = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, phone);
            ResultSet rs = pstmt.executeQuery();
            return rs.next();

        } catch (SQLException e) {
            logger.severe("Ошибка проверки phone: " + e.getMessage());
            return false;
        }
    }

    public boolean fullNameExists(String fullName) {
        String sql = "SELECT id FROM users WHERE full_name = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, fullName);
            ResultSet rs = pstmt.executeQuery();
            return rs.next();

        } catch (SQLException e) {
            logger.severe("Ошибка проверки full_name: " + e.getMessage());
            return false;
        }
    }

    // Проверка уникальности при обновлении (исключая текущего пользователя)
    public boolean emailExistsForOtherUser(String email, int userId) {
        String sql = "SELECT id FROM users WHERE email = ? AND id != ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, email);
            pstmt.setInt(2, userId);
            ResultSet rs = pstmt.executeQuery();
            return rs.next();

        } catch (SQLException e) {
            logger.severe("Ошибка проверки email: " + e.getMessage());
            return false;
        }
    }

    public boolean phoneExistsForOtherUser(String phone, int userId) {
        String sql = "SELECT id FROM users WHERE phone = ? AND id != ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, phone);
            pstmt.setInt(2, userId);
            ResultSet rs = pstmt.executeQuery();
            return rs.next();

        } catch (SQLException e) {
            logger.severe("Ошибка проверки phone: " + e.getMessage());
            return false;
        }
    }

    public boolean fullNameExistsForOtherUser(String fullName, int userId) {
        String sql = "SELECT id FROM users WHERE full_name = ? AND id != ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, fullName);
            pstmt.setInt(2, userId);
            ResultSet rs = pstmt.executeQuery();
            return rs.next();

        } catch (SQLException e) {
            logger.severe("Ошибка проверки full_name: " + e.getMessage());
            return false;
        }
    }

    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        String sql = "SELECT * FROM users";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                User user = new User();
                user.setId(rs.getInt("id"));
                user.setUsername(rs.getString("username"));
                user.setPassword(rs.getString("password"));
                user.setRole(rs.getString("role"));
                user.setEmail(rs.getString("email"));
                user.setPhone(rs.getString("phone"));
                user.setFullName(rs.getString("full_name"));
                users.add(user);
            }
        } catch (SQLException e) {
            logger.severe("Ошибка получения пользователей: " + e.getMessage());
        }
        return users;
    }

    public List<User> getClients() {
        List<User> clients = new ArrayList<>();
        String sql = "SELECT * FROM users WHERE role = 'CLIENT'";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                User user = new User();
                user.setId(rs.getInt("id"));
                user.setUsername(rs.getString("username"));
                user.setPassword(rs.getString("password"));
                user.setRole(rs.getString("role"));
                user.setEmail(rs.getString("email"));
                user.setPhone(rs.getString("phone"));
                user.setFullName(rs.getString("full_name"));
                clients.add(user);
            }
        } catch (SQLException e) {
            logger.severe("Ошибка получения клиентов: " + e.getMessage());
        }
        return clients;
    }

    public boolean updateUser(User user) {
        String sql = "UPDATE users SET email = ?, phone = ?, full_name = ? WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, user.getEmail());
            pstmt.setString(2, user.getPhone());
            pstmt.setString(3, user.getFullName());
            pstmt.setInt(4, user.getId());

            int result = pstmt.executeUpdate();
            logger.info("Пользователь обновлен: " + user.getUsername());
            return result > 0;

        } catch (SQLException e) {
            logger.severe("Ошибка обновления пользователя: " + e.getMessage());
            return false;
        }
    }

    public User getUserById(int userId) {
        String sql = "SELECT * FROM users WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                User user = new User();
                user.setId(rs.getInt("id"));
                user.setUsername(rs.getString("username"));
                user.setPassword(rs.getString("password"));
                user.setRole(rs.getString("role"));
                user.setEmail(rs.getString("email"));
                user.setPhone(rs.getString("phone"));
                user.setFullName(rs.getString("full_name"));
                return user;
            }
        } catch (SQLException e) {
            logger.severe("Ошибка получения пользователя: " + e.getMessage());
        }
        return null;
    }
}
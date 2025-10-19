package service;

import model.User;
import dao.UserDAO;
import java.util.logging.Logger;

public class UserService {
    private static final Logger logger = Logger.getLogger(UserService.class.getName());
    private UserDAO userDAO;

    public UserService() {
        this.userDAO = new UserDAO();
    }

    public String validateProfileUpdate(User user) {
        if (user == null || user.getId() == 0) {
            return "Невалидные данные пользователя";
        }

        if (userDAO.emailExistsForOtherUser(user.getEmail(), user.getId())) {
            return "Email уже используется другим пользователем";
        }

        if (userDAO.phoneExistsForOtherUser(user.getPhone(), user.getId())) {
            return "Номер телефона уже используется другим пользователем";
        }

        if (userDAO.fullNameExistsForOtherUser(user.getFullName(), user.getId())) {
            return "ФИО уже используется другим пользователем";
        }

        return null; // Валидация пройдена
    }

    public boolean updateUser(User user) {
        String validationError = validateProfileUpdate(user);
        if (validationError != null) {
            logger.warning("Ошибка валидации обновления: " + validationError);
            return false;
        }

        boolean success = userDAO.updateUser(user);
        if (success) {
            logger.info("Пользователь обновлен: " + user.getUsername());
        } else {
            logger.severe("Ошибка обновления пользователя: " + user.getUsername());
        }
        return success;
    }

    public User getUserById(int userId) {
        User user = userDAO.getUserById(userId);
        if (user != null) {
            logger.info("Получен пользователь ID: " + userId);
        } else {
            logger.warning("Пользователь не найден ID: " + userId);
        }
        return user;
    }

    public java.util.List<User> getClients() {
        return userDAO.getClients();
    }
}
package service;

import model.User;
import dao.UserDAO;
import java.util.logging.Logger;

public class AuthService {
    private static final Logger logger = Logger.getLogger(AuthService.class.getName());
    private UserDAO userDAO;

    public AuthService() {
        this.userDAO = new UserDAO();
    }

    public User login(String username, String password) {
        if (username == null || username.trim().isEmpty() ||
                password == null || password.trim().isEmpty()) {
            logger.warning("Попытка входа с пустыми данными");
            return null;
        }

        User user = userDAO.authenticate(username, password);
        if (user != null) {
            logger.info("Успешный вход: " + username);
        } else {
            logger.warning("Неудачный вход: " + username);
        }
        return user;
    }

    public String validateRegistration(User user) {
        if (user == null || user.getUsername() == null || user.getPassword() == null) {
            return "Невалидные данные пользователя";
        }

        if (userDAO.usernameExists(user.getUsername())) {
            return "Имя пользователя уже занято";
        }

        if (userDAO.emailExists(user.getEmail())) {
            return "Email уже используется";
        }

        if (userDAO.phoneExists(user.getPhone())) {
            return "Номер телефона уже используется";
        }

        if (userDAO.fullNameExists(user.getFullName())) {
            return "ФИО уже используется";
        }

        if (user.getPassword().length() < 6) {
            return "Пароль должен содержать минимум 6 символов";
        }

        return null; // Валидация пройдена
    }

    public boolean register(User user) {
        String validationError = validateRegistration(user);
        if (validationError != null) {
            logger.warning("Ошибка валидации регистрации: " + validationError);
            return false;
        }

        boolean success = userDAO.createUser(user);
        if (success) {
            logger.info("Пользователь зарегистрирован: " + user.getUsername());
        } else {
            logger.severe("Ошибка регистрации: " + user.getUsername());
        }
        return success;
    }
}
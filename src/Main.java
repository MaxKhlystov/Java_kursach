import controller.AuthController;
import view.LoginView;
import view.RegistrationView;
import model.DatabaseConnection;

import javax.swing.*;
import java.util.logging.Logger;

public class Main {
    private static final Logger logger = Logger.getLogger(Main.class.getName());

    public static void main(String[] args) {
        // Настройка форматирования логов
        System.setProperty("java.util.logging.SimpleFormatter.format",
                "[%1$tF %1$tT] [%4$-7s] %5$s %n");

        // Настройка внешнего вида
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            logger.warning("Не удалось установить системный внешний вид: " + e.getMessage());
        }

        // Инициализация базы данных
        try {
            DatabaseConnection.getConnection();
            logger.info("Приложение инициализировано");
        } catch (Exception e) {
            logger.severe("Ошибка инициализации базы данных: " + e.getMessage());
            JOptionPane.showMessageDialog(null,
                    "Ошибка подключения к базе данных: " + e.getMessage(),
                    "Ошибка",
                    JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }

        // Запуск приложения
        SwingUtilities.invokeLater(() -> {
            // Создаем View
            LoginView loginView = new LoginView();
            RegistrationView registrationView = new RegistrationView();

            // Создаем Controller и связываем с View
            AuthController authController = new AuthController();
            authController.setLoginView(loginView);
            authController.setRegistrationView(registrationView);

            // Устанавливаем обработчики для View
            loginView.setLoginListener(e -> {
                String username = loginView.getUsername();
                String password = loginView.getPassword();

                if (username.isEmpty() || password.isEmpty()) {
                    loginView.showError("Заполните все поля");
                    return;
                }

                authController.handleLogin(username, password);
            });

            loginView.setRegisterListener(e -> authController.showRegistration());

            registrationView.setRegisterListener(e -> {
                String username = registrationView.getUsername();
                String password = registrationView.getPassword();
                String confirmPassword = registrationView.getConfirmPassword();
                String email = registrationView.getEmail();
                String phone = registrationView.getPhone();
                String fullName = registrationView.getFullName();
                String role = registrationView.getRole();

                if (username.isEmpty() || password.isEmpty() || email.isEmpty() ||
                        phone.isEmpty() || fullName.isEmpty()) {
                    registrationView.showError("Заполните все поля");
                    return;
                }

                authController.handleRegister(username, password, confirmPassword, email, phone, fullName, role);
            });

            registrationView.setBackListener(e -> authController.showLogin());

            // Показываем окно логина
            loginView.showView();
            logger.info("Приложение запущено");
        });

        // Обработчик завершения приложения
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            DatabaseConnection.closeConnection();
            logger.info("Приложение завершено");
        }));
    }
}
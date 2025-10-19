package controller;

import model.User;
import service.AuthService;
import view.LoginView;
import view.RegistrationView;
import view.ClientView;
import view.MechanicView;

public class AuthController {
    private AuthService authService;
    private LoginView loginView;
    private RegistrationView registrationView;
    private ClientView currentClientView;
    private MechanicView currentMechanicView;

    public AuthController() {
        this.authService = new AuthService();
    }

    public void setLoginView(LoginView loginView) {
        this.loginView = loginView;
    }

    public void setRegistrationView(RegistrationView registrationView) {
        this.registrationView = registrationView;
    }

    public void handleLogin(String username, String password) {
        User user = authService.login(username, password);
        if (user != null) {
            loginView.hideView();
            openDashboard(user);
        } else {
            loginView.showError("Неверное имя пользователя или пароль");
        }
    }

    public void handleRegister(String username, String password, String confirmPassword,
                               String email, String phone, String fullName, String role) {
        if (!password.equals(confirmPassword)) {
            registrationView.showError("Пароли не совпадают");
            return;
        }

        User user = new User(username, password, role, email, phone, fullName);
        String validationError = authService.validateRegistration(user);
        if (validationError != null) {
            registrationView.showError(validationError);
            return;
        }

        boolean success = authService.register(user);

        if (success) {
            registrationView.showSuccess("Регистрация прошла успешно!");
            registrationView.hideView();
            loginView.showView();
        } else {
            registrationView.showError("Ошибка регистрации. Попробуйте еще раз.");
        }
    }

    public void showRegistration() {
        loginView.hideView();
        registrationView.showView();
    }

    public void showLogin() {
        registrationView.hideView();
        loginView.showView();
    }

    public void handleLogout() {
        if (currentClientView != null) {
            currentClientView.hideView();
            currentClientView = null;
        }
        if (currentMechanicView != null) {
            currentMechanicView.hideView();
            currentMechanicView = null;
        }
        loginView.clearFields();
        loginView.showView();
    }

    private void openDashboard(User user) {
        if ("MECHANIC".equals(user.getRole())) {
            MechanicController mechanicController = new MechanicController(user, this);
            MechanicView mechanicView = new MechanicView(mechanicController, user);
            mechanicController.setView(mechanicView);
            currentMechanicView = mechanicView;
            mechanicView.showView();
        } else {
            ClientController clientController = new ClientController(user, this);
            ClientView clientView = new ClientView(clientController, user);
            clientController.setView(clientView);
            currentClientView = clientView;
            clientView.showView();
        }
    }
}
package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class LoginView extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton registerButton;

    public LoginView() {
        initializeUI();
    }

    private void initializeUI() {
        setTitle("Автосервис - Вход");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 300);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel titleLabel = new JLabel("Автосервис", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        JPanel formPanel = new JPanel(new GridLayout(3, 2, 10, 10));

        formPanel.add(new JLabel("Имя пользователя:"));
        usernameField = new JTextField();
        formPanel.add(usernameField);

        formPanel.add(new JLabel("Пароль:"));
        passwordField = new JPasswordField();
        formPanel.add(passwordField);

        formPanel.add(new JLabel());

        JPanel buttonPanel = new JPanel(new FlowLayout());
        loginButton = new JButton("Войти");
        registerButton = new JButton("Регистрация");

        buttonPanel.add(loginButton);
        buttonPanel.add(registerButton);
        formPanel.add(buttonPanel);

        mainPanel.add(formPanel, BorderLayout.CENTER);

        add(mainPanel);
    }

    // Методы для установки обработчиков (View не знает о контроллере)
    public void setLoginListener(ActionListener listener) {
        loginButton.addActionListener(listener);
        passwordField.addActionListener(listener); // Enter для входа
    }

    public void setRegisterListener(ActionListener listener) {
        registerButton.addActionListener(listener);
    }

    // Методы для получения данных
    public String getUsername() {
        return usernameField.getText().trim();
    }

    public String getPassword() {
        return new String(passwordField.getPassword());
    }

    // Методы управления view
    public void showView() {
        setVisible(true);
    }

    public void hideView() {
        setVisible(false);
    }

    public void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Ошибка", JOptionPane.ERROR_MESSAGE);
    }

    public void clearFields() {
        usernameField.setText("");
        passwordField.setText("");
    }
}
package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class RegistrationView extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JPasswordField confirmPasswordField;
    private JTextField emailField;
    private JTextField phoneField;
    private JTextField fullNameField;
    private JComboBox<String> roleComboBox;
    private JButton registerButton;
    private JButton backButton;

    public RegistrationView() {
        initializeUI();
    }

    private void initializeUI() {
        setTitle("Автосервис - Регистрация");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(450, 450);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel titleLabel = new JLabel("Регистрация", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        JPanel formPanel = new JPanel(new GridLayout(7, 2, 10, 10));

        formPanel.add(new JLabel("Имя пользователя:"));
        usernameField = new JTextField();
        formPanel.add(usernameField);

        formPanel.add(new JLabel("Пароль:"));
        passwordField = new JPasswordField();
        formPanel.add(passwordField);

        formPanel.add(new JLabel("Подтвердите пароль:"));
        confirmPasswordField = new JPasswordField();
        formPanel.add(confirmPasswordField);

        formPanel.add(new JLabel("ФИО:"));
        fullNameField = new JTextField();
        formPanel.add(fullNameField);

        formPanel.add(new JLabel("Email:"));
        emailField = new JTextField();
        formPanel.add(emailField);

        formPanel.add(new JLabel("Телефон:"));
        phoneField = new JTextField();
        formPanel.add(phoneField);

        formPanel.add(new JLabel("Роль:"));
        String[] roles = {"CLIENT", "MECHANIC"};
        roleComboBox = new JComboBox<>(roles);
        formPanel.add(roleComboBox);

        mainPanel.add(formPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout());
        registerButton = new JButton("Зарегистрироваться");
        backButton = new JButton("Назад");

        buttonPanel.add(registerButton);
        buttonPanel.add(backButton);

        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);
    }

    // Методы для установки обработчиков
    public void setRegisterListener(ActionListener listener) {
        registerButton.addActionListener(listener);
    }

    public void setBackListener(ActionListener listener) {
        backButton.addActionListener(listener);
    }

    // Методы для получения данных
    public String getUsername() { return usernameField.getText().trim(); }
    public String getPassword() { return new String(passwordField.getPassword()); }
    public String getConfirmPassword() { return new String(confirmPasswordField.getPassword()); }
    public String getEmail() { return emailField.getText().trim(); }
    public String getPhone() { return phoneField.getText().trim(); }
    public String getFullName() { return fullNameField.getText().trim(); }
    public String getRole() { return (String) roleComboBox.getSelectedItem(); }

    // Методы управления view
    public void showView() {
        setVisible(true);
    }

    public void hideView() {
        setVisible(false);
        clearFields();
    }

    public void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Ошибка", JOptionPane.ERROR_MESSAGE);
    }

    public void showSuccess(String message) {
        JOptionPane.showMessageDialog(this, message, "Успех", JOptionPane.INFORMATION_MESSAGE);
    }

    public void clearFields() {
        usernameField.setText("");
        passwordField.setText("");
        confirmPasswordField.setText("");
        emailField.setText("");
        phoneField.setText("");
        fullNameField.setText("");
        roleComboBox.setSelectedIndex(0);
    }
}
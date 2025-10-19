package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import model.User;
import model.Car;
import model.Repair;
import model.Notification;
import service.RepairService;
import controller.ClientController;
import java.util.List;

public class ClientView extends JFrame {
    private User currentUser;
    private JTextArea contentArea;
    private JPanel dynamicButtonsPanel;

    public ClientView(ClientController controller, User user) {
        this.currentUser = user;
        initializeUI();
        setupController(controller);
    }

    private void initializeUI() {
        setTitle("Автосервис - Клиент: " + currentUser.getFullName());
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE); // Изменено для обработки закрытия
        setSize(900, 700);
        setLocationRelativeTo(null);

        // Обработчик закрытия окна
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                controller.ClientController controller = getControllerFromWindow();
                if (controller != null) {
                    controller.handleLogout();
                }
            }

            private ClientController getControllerFromWindow() {
                // Получаем контроллер из кнопок
                Component[] components = getContentPane().getComponents();
                for (Component comp : components) {
                    if (comp instanceof JPanel) {
                        JPanel panel = (JPanel) comp;
                        for (Component btn : panel.getComponents()) {
                            if (btn instanceof JButton) {
                                ActionListener[] listeners = ((JButton) btn).getActionListeners();
                                for (ActionListener listener : listeners) {
                                    // Находим контроллер через рефлексию (упрощенный способ)
                                    try {
                                        java.lang.reflect.Field field = listener.getClass().getDeclaredField("this$0");
                                        field.setAccessible(true);
                                        Object outerInstance = field.get(listener);
                                        if (outerInstance instanceof ClientController) {
                                            return (ClientController) outerInstance;
                                        }
                                    } catch (Exception e) {
                                        // Игнорируем исключения
                                    }
                                }
                            }
                        }
                    }
                }
                return null;
            }
        });

        createMenuBar();
        createButtonPanel();
        createContentArea();
    }

    private void createMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        JMenu systemMenu = new JMenu("Система");
        JMenuItem profileMenuItem = new JMenuItem("Личный кабинет");
        JMenuItem logoutMenuItem = new JMenuItem("Выйти из аккаунта");
        JMenuItem exitMenuItem = new JMenuItem("Выйти из приложения");

        systemMenu.add(profileMenuItem);
        systemMenu.addSeparator();
        systemMenu.add(logoutMenuItem);
        systemMenu.add(exitMenuItem);

        JMenu carMenu = new JMenu("Автомобили");
        JMenuItem addCarMenuItem = new JMenuItem("Добавить автомобиль");
        JMenuItem myCarsMenuItem = new JMenuItem("Мои автомобили");
        carMenu.add(addCarMenuItem);
        carMenu.add(myCarsMenuItem);

        JMenu repairMenu = new JMenu("Ремонты");
        JMenuItem myRepairsMenuItem = new JMenuItem("Мои ремонты");
        repairMenu.add(myRepairsMenuItem);

        JMenu notificationMenu = new JMenu("Уведомления");
        JMenuItem viewNotificationsMenuItem = new JMenuItem("Просмотреть уведомления");
        notificationMenu.add(viewNotificationsMenuItem);

        JMenu helpMenu = new JMenu("Помощь");
        JMenuItem userGuideMenuItem = new JMenuItem("Руководство пользователя");
        JMenuItem aboutMenuItem = new JMenuItem("О программе");
        helpMenu.add(userGuideMenuItem);
        helpMenu.addSeparator();
        helpMenu.add(aboutMenuItem);

        menuBar.add(systemMenu);
        menuBar.add(carMenu);
        menuBar.add(repairMenu);
        menuBar.add(notificationMenu);
        menuBar.add(helpMenu);

        setJMenuBar(menuBar);
    }

    private void createButtonPanel() {
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        buttonPanel.setBorder(BorderFactory.createTitledBorder("Быстрые действия"));

        JButton profileButton = new JButton("Личный кабинет");
        JButton logoutButton = new JButton("Выйти из аккаунта");
        JButton addCarButton = new JButton("Добавить авто");
        JButton myCarsButton = new JButton("Мои автомобили");
        JButton myRepairsButton = new JButton("Мои ремонты");
        JButton notificationsButton = new JButton("Уведомления");

        buttonPanel.add(profileButton);
        buttonPanel.add(addCarButton);
        buttonPanel.add(myCarsButton);
        buttonPanel.add(myRepairsButton);
        buttonPanel.add(notificationsButton);
        buttonPanel.add(logoutButton);

        add(buttonPanel, BorderLayout.NORTH);
    }

    private void createContentArea() {
        contentArea = new JTextArea();
        contentArea.setEditable(false);
        contentArea.setFont(new Font("Arial", Font.PLAIN, 14));
        contentArea.setMargin(new Insets(10, 10, 10, 10));

        JScrollPane scrollPane = new JScrollPane(contentArea);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Информация"));

        add(scrollPane, BorderLayout.CENTER);

        // Панель для динамических кнопок
        dynamicButtonsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        add(dynamicButtonsPanel, BorderLayout.SOUTH);
    }

    private void setupController(ClientController controller) {
        // Получаем JMenuBar и устанавливаем обработчики
        JMenuBar menuBar = getJMenuBar();
        JMenu systemMenu = menuBar.getMenu(0);
        JMenu carMenu = menuBar.getMenu(1);
        JMenu repairMenu = menuBar.getMenu(2);
        JMenu notificationMenu = menuBar.getMenu(3);
        JMenu helpMenu = menuBar.getMenu(4);

        // Обработчики для меню
        systemMenu.getItem(0).addActionListener(e -> showProfileDialog(controller));
        systemMenu.getItem(2).addActionListener(e -> controller.handleLogout());
        systemMenu.getItem(3).addActionListener(e -> System.exit(0));
        carMenu.getItem(0).addActionListener(e -> showAddCarDialog(controller));
        carMenu.getItem(1).addActionListener(e -> controller.handleViewCars());
        repairMenu.getItem(0).addActionListener(e -> controller.handleViewRepairs());
        notificationMenu.getItem(0).addActionListener(e -> controller.handleViewNotifications());
        helpMenu.getItem(0).addActionListener(e -> controller.handleShowUserGuide());
        helpMenu.getItem(2).addActionListener(e -> controller.handleShowAbout());

        // Обработчики для кнопок
        JPanel buttonPanel = (JPanel) getContentPane().getComponent(0);
        ((JButton) buttonPanel.getComponent(0)).addActionListener(e -> showProfileDialog(controller));
        ((JButton) buttonPanel.getComponent(1)).addActionListener(e -> showAddCarDialog(controller));
        ((JButton) buttonPanel.getComponent(2)).addActionListener(e -> controller.handleViewCars());
        ((JButton) buttonPanel.getComponent(3)).addActionListener(e -> controller.handleViewRepairs());
        ((JButton) buttonPanel.getComponent(4)).addActionListener(e -> controller.handleViewNotifications());
        ((JButton) buttonPanel.getComponent(5)).addActionListener(e -> controller.handleLogout());
    }

    // Методы отображения данных
    public void displayWelcome(int unreadCount) {
        String notificationInfo = unreadCount > 0 ?
                String.format("\n📢 У вас %d непрочитанных уведомлений", unreadCount) :
                "\n📢 Уведомлений нет";

        contentArea.setText("Добро пожаловать, " + currentUser.getFullName() + "!\n\n" +
                "Вы вошли как клиент. Используйте меню или кнопки для работы с приложением.\n\n" +
                "Доступные функции:\n" +
                "• Личный кабинет (изменение данных)\n" +
                "• Добавление и управление автомобилями\n" +
                "• Просмотр истории ремонтов\n" +
                "• Просмотр уведомлений о статусе ремонтов" +
                notificationInfo);
    }

    public void displayCars(List<Car> cars, ClientController controller) {
        StringBuilder sb = new StringBuilder();
        sb.append("МОИ АВТОМОБИЛИ\n\n");

        if (cars.isEmpty()) {
            sb.append("У вас нет зарегистрированных автомобилей.\n");
        } else {
            for (int i = 0; i < cars.size(); i++) {
                Car car = cars.get(i);
                sb.append((i + 1) + ". ").append(car.toString()).append("\n")
                        .append("   Гос. номер: ").append(car.getLicensePlate()).append("\n")
                        .append("   Дата регистрации: ").append(car.getRegistrationDate()).append("\n\n");
            }
        }

        contentArea.setText(sb.toString());

        // Динамические кнопки удаления
        dynamicButtonsPanel.removeAll();
        for (Car car : cars) {
            JButton deleteButton = new JButton("Удалить " + car.getBrand() + " " + car.getModel());
            final int carId = car.getId();
            deleteButton.addActionListener(e -> {
                int confirm = JOptionPane.showConfirmDialog(this,
                        "Вы уверены, что хотите удалить этот автомобиль?\nВсе связанные ремонты также будут удалены.",
                        "Подтверждение удаления",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.WARNING_MESSAGE);

                if (confirm == JOptionPane.YES_OPTION) {
                    controller.handleDeleteCar(carId);
                }
            });
            dynamicButtonsPanel.add(deleteButton);
        }

        dynamicButtonsPanel.revalidate();
        dynamicButtonsPanel.repaint();
    }

    public void displayRepairs(List<Car> cars, RepairService repairService) {
        StringBuilder sb = new StringBuilder();
        sb.append("МОИ РЕМОНТЫ\n\n");

        if (cars.isEmpty()) {
            sb.append("У вас нет автомобилей для отображения ремонтов.\n");
        } else {
            for (Car car : cars) {
                List<Repair> repairs = repairService.getCarRepairs(car.getId());

                sb.append("🚗 Автомобиль: ").append(car.getBrand()).append(" ").append(car.getModel())
                        .append(" (").append(car.getLicensePlate()).append(")\n");

                if (repairs.isEmpty()) {
                    sb.append("   Ремонтов не найдено\n\n");
                } else {
                    for (Repair repair : repairs) {
                        sb.append("   📋 ").append(repair.getDescription()).append("\n")
                                .append("      Статус: ").append(repair.getStatusText()).append("\n")
                                .append("      Стоимость: ").append(String.format("%.2f", repair.getCost())).append(" руб.\n")
                                .append("      Дата начала: ").append(repair.getStartDate()).append("\n");

                        if (repair.getEndDate() != null) {
                            sb.append("      Дата окончания: ").append(repair.getEndDate()).append("\n");
                        }
                        sb.append("\n");
                    }
                }
            }
        }

        contentArea.setText(sb.toString());
        dynamicButtonsPanel.removeAll();
        dynamicButtonsPanel.revalidate();
        dynamicButtonsPanel.repaint();
    }

    public void displayNotifications(List<Notification> notifications) {
        StringBuilder sb = new StringBuilder();
        sb.append("УВЕДОМЛЕНИЯ\n\n");

        if (notifications.isEmpty()) {
            sb.append("У вас нет уведомлений.\n");
        } else {
            for (Notification notification : notifications) {
                String status = notification.isRead() ? "✅ Прочитано" : "🔴 Новое";
                sb.append(status).append(" - ").append(notification.getCreatedAt().toLocalDate())
                        .append("\n").append(notification.getMessage()).append("\n\n");
            }
        }

        contentArea.setText(sb.toString());
        dynamicButtonsPanel.removeAll();
        dynamicButtonsPanel.revalidate();
        dynamicButtonsPanel.repaint();
    }

    // Диалоговые окна
    private void showProfileDialog(ClientController controller) {
        JDialog dialog = new JDialog(this, "Личный кабинет", true);
        dialog.setSize(400, 400);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new GridLayout(7, 2, 10, 10));

        JTextField usernameField = new JTextField(currentUser.getUsername());
        JTextField emailField = new JTextField(currentUser.getEmail());
        JTextField phoneField = new JTextField(currentUser.getPhone());
        JTextField fullNameField = new JTextField(currentUser.getFullName());

        usernameField.setEditable(false);

        dialog.add(new JLabel("Логин:"));
        dialog.add(usernameField);
        dialog.add(new JLabel("ФИО:"));
        dialog.add(fullNameField);
        dialog.add(new JLabel("Email:"));
        dialog.add(emailField);
        dialog.add(new JLabel("Телефон:"));
        dialog.add(phoneField);

        JButton saveButton = new JButton("Сохранить");
        JButton cancelButton = new JButton("Отмена");

        saveButton.addActionListener(e -> {
            String email = emailField.getText().trim();
            String phone = phoneField.getText().trim();
            String fullName = fullNameField.getText().trim();

            if (email.isEmpty() || phone.isEmpty() || fullName.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "Заполните все поля", "Ошибка", JOptionPane.ERROR_MESSAGE);
                return;
            }

            controller.handleUpdateProfile(email, phone, fullName);
            dialog.dispose();
        });

        cancelButton.addActionListener(e -> dialog.dispose());

        dialog.add(saveButton);
        dialog.add(cancelButton);
        dialog.setVisible(true);
    }

    private void showAddCarDialog(ClientController controller) {
        JDialog dialog = new JDialog(this, "Добавить автомобиль", true);
        dialog.setSize(400, 400);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new GridLayout(8, 2, 10, 10));

        JTextField brandField = new JTextField();
        JTextField modelField = new JTextField();
        JTextField yearField = new JTextField();
        JTextField vinField = new JTextField();
        JTextField licensePlateField = new JTextField();

        dialog.add(new JLabel("Марка:"));
        dialog.add(brandField);
        dialog.add(new JLabel("Модель:"));
        dialog.add(modelField);
        dialog.add(new JLabel("Год:"));
        dialog.add(yearField);
        dialog.add(new JLabel("VIN:"));
        dialog.add(vinField);
        dialog.add(new JLabel("Гос. номер:"));
        dialog.add(licensePlateField);

        JButton addButton = new JButton("Добавить");
        JButton cancelButton = new JButton("Отмена");

        addButton.addActionListener(e -> {
            try {
                String brand = brandField.getText().trim();
                String model = modelField.getText().trim();
                int year = Integer.parseInt(yearField.getText().trim());
                String vin = vinField.getText().trim();
                String licensePlate = licensePlateField.getText().trim();

                if (brand.isEmpty() || model.isEmpty() || vin.isEmpty() || licensePlate.isEmpty()) {
                    JOptionPane.showMessageDialog(dialog, "Заполните все поля", "Ошибка", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                controller.handleAddCar(brand, model, year, vin, licensePlate);
                dialog.dispose();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialog, "Проверьте правильность числовых полей", "Ошибка", JOptionPane.ERROR_MESSAGE);
            }
        });

        cancelButton.addActionListener(e -> dialog.dispose());

        dialog.add(addButton);
        dialog.add(cancelButton);
        dialog.setVisible(true);
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

    public void showSuccess(String message) {
        JOptionPane.showMessageDialog(this, message, "Успех", JOptionPane.INFORMATION_MESSAGE);
    }

    public void showInfo(String message) {
        JOptionPane.showMessageDialog(this, message, "Информация", JOptionPane.INFORMATION_MESSAGE);
    }

    public void updateProfileInfo(User user) {
        this.currentUser = user;
        setTitle("Автосервис - Клиент: " + currentUser.getFullName());
    }

    public void displayUserGuide(String guide) {
        JTextArea guideArea = new JTextArea(guide);
        guideArea.setEditable(false);
        guideArea.setFont(new Font("Arial", Font.PLAIN, 14));
        guideArea.setMargin(new Insets(10, 10, 10, 10));

        JScrollPane scrollPane = new JScrollPane(guideArea);
        scrollPane.setPreferredSize(new Dimension(600, 400));

        JOptionPane.showMessageDialog(this, scrollPane, "Руководство пользователя", JOptionPane.INFORMATION_MESSAGE);
    }

    public void displayAbout(String about) {
        JOptionPane.showMessageDialog(this, about, "О программе", JOptionPane.INFORMATION_MESSAGE);
    }
}
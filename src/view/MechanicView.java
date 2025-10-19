package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import model.User;
import model.Car;
import model.Repair;
import service.CarService;
import service.UserService;
import controller.MechanicController;
import java.util.List;

public class MechanicView extends JFrame {
    private User currentUser;
    private JTextArea contentArea;
    private JPanel dynamicButtonsPanel;

    public MechanicView(MechanicController controller, User user) {
        this.currentUser = user;
        initializeUI();
        setupController(controller);
    }

    private void initializeUI() {
        setTitle("Автосервис - Механик: " + currentUser.getFullName());
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE); // Изменено для обработки закрытия
        setSize(900, 700);
        setLocationRelativeTo(null);

        // Обработчик закрытия окна
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                controller.MechanicController controller = getControllerFromWindow();
                if (controller != null) {
                    controller.handleLogout();
                }
            }

            private MechanicController getControllerFromWindow() {
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
                                        if (outerInstance instanceof MechanicController) {
                                            return (MechanicController) outerInstance;
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
        JMenuItem viewCarsMenuItem = new JMenuItem("Просмотреть все автомобили");
        carMenu.add(viewCarsMenuItem);

        JMenu repairMenu = new JMenu("Ремонты");
        JMenuItem addRepairMenuItem = new JMenuItem("Добавить ремонт");
        JMenuItem viewRepairsMenuItem = new JMenuItem("Просмотреть все ремонты");
        repairMenu.add(addRepairMenuItem);
        repairMenu.add(viewRepairsMenuItem);

        JMenu helpMenu = new JMenu("Помощь");
        JMenuItem userGuideMenuItem = new JMenuItem("Руководство пользователя");
        JMenuItem aboutMenuItem = new JMenuItem("О программе");
        helpMenu.add(userGuideMenuItem);
        helpMenu.addSeparator();
        helpMenu.add(aboutMenuItem);

        menuBar.add(systemMenu);
        menuBar.add(carMenu);
        menuBar.add(repairMenu);
        menuBar.add(helpMenu);

        setJMenuBar(menuBar);
    }

    private void createButtonPanel() {
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        buttonPanel.setBorder(BorderFactory.createTitledBorder("Быстрые действия"));

        JButton profileButton = new JButton("Личный кабинет");
        JButton logoutButton = new JButton("Выйти из аккаунта");
        JButton addRepairButton = new JButton("Добавить ремонт");
        JButton viewCarsButton = new JButton("Все автомобили");
        JButton viewRepairsButton = new JButton("Все ремонты");

        buttonPanel.add(profileButton);
        buttonPanel.add(addRepairButton);
        buttonPanel.add(viewCarsButton);
        buttonPanel.add(viewRepairsButton);
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

    private void setupController(MechanicController controller) {
        // Получаем JMenuBar и устанавливаем обработчики
        JMenuBar menuBar = getJMenuBar();
        JMenu systemMenu = menuBar.getMenu(0);
        JMenu carMenu = menuBar.getMenu(1);
        JMenu repairMenu = menuBar.getMenu(2);
        JMenu helpMenu = menuBar.getMenu(3);

        // Обработчики для меню
        systemMenu.getItem(0).addActionListener(e -> showProfileDialog(controller));
        systemMenu.getItem(2).addActionListener(e -> controller.handleLogout());
        systemMenu.getItem(3).addActionListener(e -> System.exit(0));
        carMenu.getItem(0).addActionListener(e -> controller.handleViewCars());
        repairMenu.getItem(0).addActionListener(e -> showAddRepairDialog(controller));
        repairMenu.getItem(1).addActionListener(e -> controller.handleViewRepairs());
        helpMenu.getItem(0).addActionListener(e -> controller.handleShowUserGuide());
        helpMenu.getItem(2).addActionListener(e -> controller.handleShowAbout());

        // Обработчики для кнопок
        JPanel buttonPanel = (JPanel) getContentPane().getComponent(0);
        ((JButton) buttonPanel.getComponent(0)).addActionListener(e -> showProfileDialog(controller));
        ((JButton) buttonPanel.getComponent(1)).addActionListener(e -> showAddRepairDialog(controller));
        ((JButton) buttonPanel.getComponent(2)).addActionListener(e -> controller.handleViewCars());
        ((JButton) buttonPanel.getComponent(3)).addActionListener(e -> controller.handleViewRepairs());
        ((JButton) buttonPanel.getComponent(4)).addActionListener(e -> controller.handleLogout());
    }

    // Методы отображения данных
    public void displayWelcome() {
        contentArea.setText("Добро пожаловать, " + currentUser.getFullName() + "!\n\n" +
                "Вы вошли как механик. Используйте меню или кнопки для работы с приложением.\n\n" +
                "Доступные функции:\n" +
                "• Личный кабинет (изменение данных)\n" +
                "• Просмотр всех автомобилей клиентов\n" +
                "• Добавление новых ремонтов\n" +
                "• Управление статусами ремонтов\n" +
                "• Отслеживание выполнения работ");
    }

    public void displayAllCars(List<Car> cars, UserService userService) {
        StringBuilder sb = new StringBuilder();
        sb.append("ВСЕ АВТОМОБИЛИ КЛИЕНТОВ\n\n");

        if (cars.isEmpty()) {
            sb.append("В системе нет автомобилей.\n");
        } else {
            for (int i = 0; i < cars.size(); i++) {
                Car car = cars.get(i);
                User owner = userService.getUserById(car.getOwnerId());
                String ownerName = (owner != null) ? owner.getFullName() : "Неизвестно";

                sb.append((i + 1) + ". ").append(car.toString()).append("\n")
                        .append("   Гос. номер: ").append(car.getLicensePlate()).append("\n")
                        .append("   Владелец: ").append(ownerName).append("\n")
                        .append("   Телефон: ").append(owner != null ? owner.getPhone() : "Неизвестно").append("\n")
                        .append("   Дата регистрации: ").append(car.getRegistrationDate()).append("\n\n");
            }
        }

        contentArea.setText(sb.toString());
        dynamicButtonsPanel.removeAll();
        dynamicButtonsPanel.revalidate();
        dynamicButtonsPanel.repaint();
    }

    public void displayAllRepairs(List<Repair> repairs, CarService carService, UserService userService, MechanicController controller) {
        StringBuilder sb = new StringBuilder();
        sb.append("ВСЕ РЕМОНТЫ\n\n");

        if (repairs.isEmpty()) {
            sb.append("В системе нет ремонтов.\n");
        } else {
            for (int i = 0; i < repairs.size(); i++) {
                Repair repair = repairs.get(i);
                Car car = carService.getCarById(repair.getCarId());
                User owner = (car != null) ? userService.getUserById(car.getOwnerId()) : null;
                String carInfo = (car != null) ? car.toString() : "Автомобиль не найден";
                String ownerInfo = (owner != null) ? owner.getFullName() : "Неизвестно";

                sb.append((i + 1) + ". 🚗 Автомобиль: ").append(carInfo).append("\n")
                        .append("   👤 Владелец: ").append(ownerInfo).append("\n")
                        .append("   📋 Описание: ").append(repair.getDescription()).append("\n")
                        .append("   📊 Статус: ").append(repair.getStatusText()).append("\n")
                        .append("   💰 Стоимость: ").append(String.format("%.2f", repair.getCost())).append(" руб.\n")
                        .append("   📅 Дата начала: ").append(repair.getStartDate()).append("\n");

                if (repair.getEndDate() != null) {
                    sb.append("   ✅ Дата окончания: ").append(repair.getEndDate()).append("\n");
                }

                sb.append("\n");
            }
        }

        contentArea.setText(sb.toString());

        // Динамические кнопки изменения статуса
        dynamicButtonsPanel.removeAll();
        for (Repair repair : repairs) {
            if (!"COMPLETED".equals(repair.getStatus()) && !"CANCELLED".equals(repair.getStatus())) {
                JButton statusButton = new JButton("Изменить статус ремонта #" + repair.getId());
                final int repairId = repair.getId();
                final String currentStatus = repair.getStatus();
                statusButton.addActionListener(e -> {
                    int confirm = JOptionPane.showConfirmDialog(this,
                            "Изменить статус ремонта на: " + getNextStatusText(currentStatus) + "?",
                            "Подтверждение изменения статуса",
                            JOptionPane.YES_NO_OPTION);

                    if (confirm == JOptionPane.YES_OPTION) {
                        controller.handleUpdateRepairStatus(repairId, currentStatus);
                    }
                });
                dynamicButtonsPanel.add(statusButton);
            }
        }

        dynamicButtonsPanel.revalidate();
        dynamicButtonsPanel.repaint();
    }

    // Диалоговые окна
    private void showProfileDialog(MechanicController controller) {
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

    private void showAddRepairDialog(MechanicController controller) {
        JDialog dialog = new JDialog(this, "Добавить ремонт", true);
        dialog.setSize(500, 500);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout(10, 10));

        // Основная панель
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Панель выбора клиента
        JPanel clientPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel clientLabel = new JLabel("Выберите клиента:");
        JComboBox<User> clientComboBox = new JComboBox<>();
        JCheckBox filterCheckBox = new JCheckBox("Показать только автомобили выбранного клиента");

        // Заполняем комбобокс клиентами
        List<User> clients = controller.getClients();
        for (User client : clients) {
            clientComboBox.addItem(client);
        }

        clientPanel.add(clientLabel);
        clientPanel.add(clientComboBox);
        clientPanel.add(filterCheckBox);

        // Панель выбора автомобиля
        JPanel carPanel = new JPanel(new BorderLayout());
        JLabel carLabel = new JLabel("Выберите автомобиль:");
        JComboBox<Car> carComboBox = new JComboBox<>();

        // Заполняем комбобокс всеми автомобилями
        List<Car> allCars = controller.getAllCars();
        for (Car car : allCars) {
            carComboBox.addItem(car);
        }

        carPanel.add(carLabel, BorderLayout.NORTH);
        carPanel.add(carComboBox, BorderLayout.CENTER);

        // Панель данных ремонта
        JPanel repairPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        JTextField descriptionField = new JTextField();
        JTextField costField = new JTextField("0.0");

        repairPanel.add(new JLabel("Описание ремонта:"));
        repairPanel.add(descriptionField);
        repairPanel.add(new JLabel("Стоимость:"));
        repairPanel.add(costField);
        repairPanel.add(new JLabel("Статус:"));
        repairPanel.add(new JLabel("Диагностика (автоматически)"));

        // Панель кнопок
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton addButton = new JButton("Добавить ремонт");
        JButton cancelButton = new JButton("Отмена");

        // Обработчик изменения выбора клиента
        clientComboBox.addActionListener(e -> {
            if (filterCheckBox.isSelected()) {
                User selectedClient = (User) clientComboBox.getSelectedItem();
                if (selectedClient != null) {
                    carComboBox.removeAllItems();
                    List<Car> clientCars = controller.getClientCars(selectedClient.getId());
                    for (Car car : clientCars) {
                        carComboBox.addItem(car);
                    }
                }
            }
        });

        // Обработчик чекбокса фильтра
        filterCheckBox.addActionListener(e -> {
            if (filterCheckBox.isSelected()) {
                User selectedClient = (User) clientComboBox.getSelectedItem();
                if (selectedClient != null) {
                    carComboBox.removeAllItems();
                    List<Car> clientCars = controller.getClientCars(selectedClient.getId());
                    for (Car car : clientCars) {
                        carComboBox.addItem(car);
                    }
                }
            } else {
                carComboBox.removeAllItems();
                List<Car> allCarsList = controller.getAllCars();
                for (Car car : allCarsList) {
                    carComboBox.addItem(car);
                }
            }
        });

        addButton.addActionListener(e -> {
            try {
                Car selectedCar = (Car) carComboBox.getSelectedItem();
                String description = descriptionField.getText().trim();
                double cost = Double.parseDouble(costField.getText().trim());

                if (selectedCar == null) {
                    JOptionPane.showMessageDialog(dialog, "Выберите автомобиль", "Ошибка", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (description.isEmpty()) {
                    JOptionPane.showMessageDialog(dialog, "Введите описание ремонта", "Ошибка", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                controller.handleAddRepair(selectedCar.getId(), description, cost);
                dialog.dispose();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialog, "Проверьте правильность числовых полей", "Ошибка", JOptionPane.ERROR_MESSAGE);
            }
        });

        cancelButton.addActionListener(e -> dialog.dispose());

        buttonPanel.add(addButton);
        buttonPanel.add(cancelButton);

        // Собираем интерфейс
        mainPanel.add(clientPanel, BorderLayout.NORTH);
        mainPanel.add(carPanel, BorderLayout.CENTER);
        mainPanel.add(repairPanel, BorderLayout.SOUTH);

        dialog.add(mainPanel, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }

    // Вспомогательные методы
    private String getNextStatusText(String currentStatus) {
        switch (currentStatus) {
            case "DIAGNOSTICS": return "В ремонте";
            case "IN_REPAIR": return "Ремонт завершен";
            default: return "Неизвестно";
        }
    }

    // Методы управления view
    public void showView() {
        displayWelcome();
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
        setTitle("Автосервис - Механик: " + currentUser.getFullName());
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
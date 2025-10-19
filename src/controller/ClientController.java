package controller;

import model.User;
import model.Car;
import model.Repair;
import model.Notification;
import service.CarService;
import service.RepairService;
import service.NotificationService;
import service.UserService;
import view.ClientView;
import java.util.List;

public class ClientController {
    private User currentUser;
    private ClientView view;
    private CarService carService;
    private RepairService repairService;
    private NotificationService notificationService;
    private UserService userService;
    private AuthController authController;

    public ClientController(User user, AuthController authController) {
        this.currentUser = user;
        this.authController = authController;
        this.carService = new CarService();
        this.repairService = new RepairService();
        this.notificationService = new NotificationService();
        this.userService = new UserService();
    }

    public void setView(ClientView view) {
        this.view = view;
        // Показываем welcome с актуальным количеством уведомлений
        view.displayWelcome(getUnreadNotificationsCount());
    }

    public void handleViewCars() {
        List<Car> cars = carService.getClientCars(currentUser.getId());
        view.displayCars(cars, this);
    }

    public void handleViewRepairs() {
        List<Car> cars = carService.getClientCars(currentUser.getId());
        view.displayRepairs(cars, repairService);
    }

    public void handleViewNotifications() {
        List<Notification> notifications = notificationService.getUserNotifications(currentUser.getId());
        // Помечаем уведомления как прочитанные
        for (Notification notification : notifications) {
            if (!notification.isRead()) {
                notificationService.markNotificationAsRead(notification.getId());
            }
        }
        view.displayNotifications(notifications);
        // Обновляем welcome сообщение
        view.displayWelcome(getUnreadNotificationsCount());
    }

    public void handleAddCar(String brand, String model, int year, String vin, String licensePlate) {
        Car car = new Car(brand, model, year, vin, licensePlate, currentUser.getId());
        boolean success = carService.addCar(car);

        if (success) {
            view.showSuccess("Автомобиль добавлен успешно!");
            handleViewCars(); // Обновляем список
        } else {
            view.showError("Ошибка при добавлении автомобиля");
        }
    }

    public void handleDeleteCar(int carId) {
        boolean success = carService.deleteCar(carId);
        if (success) {
            view.showSuccess("Автомобиль удален успешно!");
            handleViewCars(); // Обновляем список
        } else {
            view.showError("Ошибка при удалении автомобиля");
        }
    }

    public void handleUpdateProfile(String email, String phone, String fullName) {
        currentUser.setEmail(email);
        currentUser.setPhone(phone);
        currentUser.setFullName(fullName);

        boolean success = userService.updateUser(currentUser);
        if (success) {
            view.showSuccess("Данные успешно обновлены!");
            view.updateProfileInfo(currentUser);
        } else {
            view.showError("Ошибка обновления данных. Возможно, данные уже используются.");
        }
    }

    public void handleShowUserGuide() {
        String guide = """
            РУКОВОДСТВО ПОЛЬЗОВАТЕЛЯ - КЛИЕНТ

            1. ЛИЧНЫЙ КАБИНЕТ
               • Просмотр и редактирование личных данных
               • Изменение контактной информации

            2. АВТОМОБИЛИ
               • Добавление новых автомобилей
               • Просмотр списка своих автомобилей
               • Удаление автомобилей

            3. РЕМОНТЫ
               • Просмотр истории ремонтов по всем автомобилям
               • Отслеживание текущего статуса ремонта
               • Просмотр стоимости ремонтов

            4. УВЕДОМЛЕНИЯ
               • Получение уведомлений о смене статуса ремонта
               • Просмотр истории уведомлений

            СТАТУСЫ РЕМОНТА:
            • Диагностика - автомобиль находится на диагностике
            • В ремонте - ремонт выполняется
            • Ремонт завершен - работа завершена, автомобиль готов к выдаче
            """;
        view.displayUserGuide(guide);
    }

    public void handleShowAbout() {
        String about = "Автосервис v2.0\n\nКлиентское приложение для управления автомобилями и ремонтами.\nТекущий пользователь: " + currentUser.getFullName() + "\nРоль: Клиент";
        view.displayAbout(about);
    }

    public void handleLogout() {
        authController.handleLogout();
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public int getUnreadNotificationsCount() {
        return notificationService.getUnreadCount(currentUser.getId());
    }
}
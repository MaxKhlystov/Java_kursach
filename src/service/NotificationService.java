package service;

import model.Notification;
import dao.NotificationDAO;
import java.util.List;
import java.util.logging.Logger;

public class NotificationService {
    private static final Logger logger = Logger.getLogger(NotificationService.class.getName());
    private NotificationDAO notificationDAO;

    public NotificationService() {
        this.notificationDAO = new NotificationDAO();
    }

    public List<Notification> getUserNotifications(int userId) {
        List<Notification> notifications = notificationDAO.getNotificationsByUser(userId);
        logger.info("Получены уведомления пользователя " + userId + ": " + notifications.size() + " шт.");
        return notifications;
    }

    public boolean markNotificationAsRead(int notificationId) {
        boolean success = notificationDAO.markAsRead(notificationId);
        if (success) {
            logger.info("Уведомление помечено как прочитанное: " + notificationId);
        } else {
            logger.warning("Ошибка пометки уведомления как прочитанного: " + notificationId);
        }
        return success;
    }

    public int getUnreadCount(int userId) {
        int count = notificationDAO.getUnreadCount(userId);
        logger.info("Непрочитанных уведомлений у пользователя " + userId + ": " + count);
        return count;
    }

    public void addNotification(int userId, String message) {
        Notification notification = new Notification(userId, message);
        boolean success = notificationDAO.addNotification(notification);
        if (success) {
            logger.info("Уведомление добавлено для пользователя: " + userId);
        } else {
            logger.warning("Ошибка добавления уведомления для пользователя: " + userId);
        }
    }
}
package service;

import model.Repair;
import dao.RepairDAO;
import dao.NotificationDAO;
import model.Notification;
import java.util.List;
import java.util.logging.Logger;

public class RepairService {
    private static final Logger logger = Logger.getLogger(RepairService.class.getName());
    private RepairDAO repairDAO;
    private NotificationDAO notificationDAO;

    public RepairService() {
        this.repairDAO = new RepairDAO();
        this.notificationDAO = new NotificationDAO();
    }

    public boolean addRepair(Repair repair) {
        if (repair == null || repair.getDescription() == null) {
            logger.warning("Попытка добавить невалидный ремонт");
            return false;
        }

        boolean success = repairDAO.addRepair(repair);
        if (success) {
            logger.info("Ремонт добавлен для автомобиля ID: " + repair.getCarId());
        } else {
            logger.severe("Ошибка добавления ремонта для автомобиля ID: " + repair.getCarId());
        }
        return success;
    }

    public List<Repair> getCarRepairs(int carId) {
        List<Repair> repairs = repairDAO.getRepairsByCar(carId);
        logger.info("Получены ремонты автомобиля " + carId + ": " + repairs.size() + " шт.");
        return repairs;
    }

    public List<Repair> getAllRepairs() {
        List<Repair> repairs = repairDAO.getAllRepairs();
        logger.info("Получены все ремонты: " + repairs.size() + " шт.");
        return repairs;
    }

    public boolean updateRepairStatus(int repairId, String status) {
        boolean success = repairDAO.updateRepairStatus(repairId, status);
        if (success) {
            logger.info("Статус ремонта обновлен: " + repairId + " -> " + status);
        } else {
            logger.warning("Ошибка обновления статуса ремонта: " + repairId);
        }
        return success;
    }

    public boolean moveToNextStatus(int repairId, String currentStatus) {
        String nextStatus = getNextStatus(currentStatus);
        if (nextStatus != null) {
            return updateRepairStatus(repairId, nextStatus);
        }
        return false;
    }

    public boolean completeRepair(int repairId) {
        boolean success = repairDAO.completeRepair(repairId);
        if (success) {
            logger.info("Ремонт завершен: " + repairId);
        } else {
            logger.warning("Ошибка завершения ремонта: " + repairId);
        }
        return success;
    }

    private String getNextStatus(String currentStatus) {
        switch (currentStatus) {
            case "DIAGNOSTICS": return "IN_REPAIR";
            case "IN_REPAIR": return "COMPLETED";
            default: return null;
        }
    }

    public void addRepairNotification(int userId, String carInfo, String status) {
        String message = String.format("Статус ремонта автомобиля %s изменен: %s",
                carInfo, getStatusText(status));

        Notification notification = new Notification(userId, message);
        notificationDAO.addNotification(notification);
    }

    private String getStatusText(String status) {
        switch (status) {
            case "DIAGNOSTICS": return "Диагностика";
            case "IN_REPAIR": return "В ремонте";
            case "COMPLETED": return "Ремонт завершен";
            case "CANCELLED": return "Отменен";
            default: return status;
        }
    }

    public Repair getRepairById(int repairId) {
        Repair repair = repairDAO.getRepairById(repairId);
        if (repair != null) {
            logger.info("Получен ремонт ID: " + repairId);
        } else {
            logger.warning("Ремонт не найден ID: " + repairId);
        }
        return repair;
    }
}
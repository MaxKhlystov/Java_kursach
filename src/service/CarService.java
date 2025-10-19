package service;

import model.Car;
import dao.CarDAO;
import java.util.List;
import java.util.logging.Logger;

public class CarService {
    private static final Logger logger = Logger.getLogger(CarService.class.getName());
    private CarDAO carDAO;

    public CarService() {
        this.carDAO = new CarDAO();
    }

    public boolean addCar(Car car) {
        if (car == null || car.getBrand() == null || car.getModel() == null) {
            logger.warning("Попытка добавить невалидный автомобиль");
            return false;
        }

        boolean success = carDAO.addCar(car);
        if (success) {
            logger.info("Автомобиль добавлен: " + car.getBrand() + " " + car.getModel());
        } else {
            logger.severe("Ошибка добавления автомобиля: " + car.getBrand() + " " + car.getModel());
        }
        return success;
    }

    public boolean deleteCar(int carId) {
        boolean success = carDAO.deleteCar(carId);
        if (success) {
            logger.info("Автомобиль удален ID: " + carId);
        } else {
            logger.severe("Ошибка удаления автомобиля ID: " + carId);
        }
        return success;
    }

    public List<Car> getClientCars(int clientId) {
        List<Car> cars = carDAO.getCarsByOwner(clientId);
        logger.info("Получены автомобили клиента " + clientId + ": " + cars.size() + " шт.");
        return cars;
    }

    public List<Car> getAllCars() {
        List<Car> cars = carDAO.getAllCars();
        logger.info("Получены все автомобили: " + cars.size() + " шт.");
        return cars;
    }

    public Car getCarById(int carId) {
        Car car = carDAO.getCarById(carId);
        if (car != null) {
            logger.info("Получен автомобиль ID: " + carId);
        } else {
            logger.warning("Автомобиль не найден ID: " + carId);
        }
        return car;
    }
}
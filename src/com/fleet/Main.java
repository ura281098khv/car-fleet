package com.fleet;

import com.fleet.model.Car;
import com.fleet.service.CarService;
import com.fleet.util.Validator;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Scanner;

public class Main {
    private static final Scanner scanner = new Scanner(System.in);
    private static final CarService service = new CarService();

    public static void main(String[] args) {
        System.out.println("=== Автопарк ===");
        boolean running = true;

        while (running) {
            printMenu();
            String choice = scanner.nextLine().trim();
            try {
                switch (choice) {
                    case "1" -> addCarManually();
                    case "2" -> addCarsFromFile();
                    case "3" -> showAllCars();
                    case "4" -> filterAndSort();
                    case "5" -> mapTitles();
                    case "0" -> {
                        running = false;
                        System.out.println("До свидания!");
                    }
                    default -> System.out.println("Неверный пункт меню.");
                }
            } catch (Exception e) {
                System.err.println("Ошибка: " + e.getMessage());
            }
        }
        scanner.close();
    }

    private static void printMenu() {
        System.out.println("\n--- Меню ---");
        System.out.println("1. Добавить машину вручную");
        System.out.println("2. Загрузить машины из файла");
        System.out.println("3. Показать все машины");
        System.out.println("4. Фильтр по году + сортировка по пробегу");
        System.out.println("5. Преобразование: Марка Модель (год)");
        System.out.println("0. Выход");
        System.out.print("Выберите пункт: ");
    }

    private static void addCarManually() {
        System.out.print("Марка: ");
        String brand = Validator.validateText(scanner.nextLine(), "Марка");

        System.out.print("Модель: ");
        String model = Validator.validateText(scanner.nextLine(), "Модель");

        System.out.print("Год выпуска: ");
        int year = Validator.validateYear(scanner.nextLine());

        System.out.print("Пробег (км): ");
        double mileage = Validator.validateMileage(scanner.nextLine());

        System.out.print("Расход топлива (л/100км): ");
        double fuel = Validator.validateFuelConsumption(scanner.nextLine());

        service.addCar(new Car(brand, model, year, mileage, fuel));
        System.out.println("✔ Машина добавлена.");
    }

    private static void addCarsFromFile() {
        System.out.print("Путь к файлу (например, data/cars.txt): ");
        String pathStr = scanner.nextLine().trim();

        if (pathStr.isEmpty()) {
            throw new IllegalArgumentException("Путь не может быть пустым");
        }

        Path path = Paths.get(pathStr);
        if (!Files.exists(path)) {
            throw new IllegalArgumentException("Файл не найден: " + path.toAbsolutePath());
        }
        if (!Files.isReadable(path)) {
            throw new IllegalArgumentException("Нет прав на чтение файла");
        }

        try {
            int count = service.loadFromFile(path);
            System.out.println("✔ Загружено машин: " + count);
        } catch (IOException e) {
            throw new RuntimeException("Ошибка чтения файла: " + e.getMessage(), e);
        }
    }

    private static void showAllCars() {
        if (service.size() == 0) {
            System.out.println("Коллекция пуста.");
            return;
        }
        System.out.println("Всего машин: " + service.size());
        service.getAll().forEach(System.out::println);
    }

    private static void filterAndSort() {
        if (service.size() == 0) {
            System.out.println("Коллекция пуста.");
            return;
        }

        System.out.print("Минимальный год выпуска: ");
        int minYear = Validator.validateYear(scanner.nextLine());

        System.out.print("Сортировать по пробегу? (y/n): ");
        boolean sort = scanner.nextLine().trim().equalsIgnoreCase("y");

        List<String> result = service.processCars(minYear, sort);
        if (result.isEmpty()) {
            System.out.println("Нет машин, удовлетворяющих условию.");
        } else {
            System.out.println("Результат (filter + sorted + map):");
            result.forEach(System.out::println);
        }
    }

    private static void mapTitles() {
        if (service.size() == 0) {
            System.out.println("Коллекция пуста.");
            return;
        }
        System.out.println("Марка Модель (год):");
        service.mapToTitles().forEach(System.out::println);
    }
}
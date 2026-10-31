package com.fleet.service;

import com.fleet.model.Car;
import com.fleet.util.Validator;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class CarService {
    private final List<Car> cars = new ArrayList<>();

    public void addCar(Car car) {
        cars.add(car);
    }

    public List<Car> getAll() {
        return cars;
    }

    public int size() {
        return cars.size();
    }

    /**
     * Формат строки: brand;model;year;mileage;fuelConsumption
     */
    public int loadFromFile(Path path) throws IOException {
        int loaded = 0;
        List<String> errors = new ArrayList<>();

        try (BufferedReader reader = Files.newBufferedReader(path)) {
            String line;
            int lineNumber = 0;
            while ((line = reader.readLine()) != null) {
                lineNumber++;
                if (line.trim().isEmpty()) continue;
                try {
                    cars.add(parseLine(line));
                    loaded++;
                } catch (IllegalArgumentException e) {
                    errors.add("Строка " + lineNumber + ": " + e.getMessage());
                }
            }
        }

        if (!errors.isEmpty()) {
            System.err.println("Предупреждения при чтении файла:");
            errors.forEach(err -> System.err.println("  " + err));
        }
        return loaded;
    }

    private Car parseLine(String line) {
        String[] parts = line.split(";");
        if (parts.length != 5) {
            throw new IllegalArgumentException(
                    "ожидается 5 полей (brand;model;year;mileage;fuelConsumption)");
        }
        String brand = Validator.validateText(parts[0], "Марка");
        String model = Validator.validateText(parts[1], "Модель");
        int year = Validator.validateYear(parts[2]);
        double mileage = Validator.validateMileage(parts[3]);
        double fuel = Validator.validateFuelConsumption(parts[4]);
        return new Car(brand, model, year, mileage, fuel);
    }

    /** Фильтр: машины не старше minYear */
    public List<Car> filterByYear(int minYear) {
        return cars.stream()
                .filter(c -> c.getYear() >= minYear)
                .collect(Collectors.toList());
    }

    /** map(): список строк "Марка Модель (год)" */
    public List<String> mapToTitles() {
        return cars.stream()
                .map(c -> c.getBrand() + " " + c.getModel() + " (" + c.getYear() + ")")
                .collect(Collectors.toList());
    }

    /** Сортировка по пробегу */
    public List<Car> sortedByMileage() {
        return cars.stream()
                .sorted(Comparator.comparingDouble(Car::getMileage))
                .collect(Collectors.toList());
    }

    /** Комбинированная обработка: фильтр + сортировка + map */
    public List<String> processCars(int minYear, boolean sortByMileage) {
        var stream = cars.stream()
                .filter(c -> c.getYear() >= minYear);

        if (sortByMileage) {
            stream = stream.sorted(Comparator.comparingDouble(Car::getMileage));
        }

        return stream
                .map(c -> String.format("%s %s (%d) — пробег %.0f км, расход %.1f л/100км",
                        c.getBrand(), c.getModel(), c.getYear(),
                        c.getMileage(), c.getFuelConsumption()))
                .collect(Collectors.toList());
    }
}
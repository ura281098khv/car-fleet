package com.fleet.model;

import java.util.Objects;

public class Car {
    private String brand;
    private String model;
    private int year;
    private double mileage;
    private double fuelConsumption;

    public Car(String brand, String model, int year, double mileage, double fuelConsumption) {
        this.brand = brand;
        this.model = model;
        this.year = year;
        this.mileage = mileage;
        this.fuelConsumption = fuelConsumption;
    }

    public String getBrand() { return brand; }
    public String getModel() { return model; }
    public int getYear() { return year; }
    public double getMileage() { return mileage; }
    public double getFuelConsumption() { return fuelConsumption; }

    public void setBrand(String brand) { this.brand = brand; }
    public void setModel(String model) { this.model = model; }
    public void setYear(int year) { this.year = year; }
    public void setMileage(double mileage) { this.mileage = mileage; }
    public void setFuelConsumption(double fuelConsumption) { this.fuelConsumption = fuelConsumption; }

    @Override
    public String toString() {
        return String.format("Car{%s %s, %d г., пробег %.0f км, расход %.1f л/100км}",
                brand, model, year, mileage, fuelConsumption);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Car)) return false;
        Car car = (Car) o;
        return year == car.year &&
                Double.compare(car.mileage, mileage) == 0 &&
                Double.compare(car.fuelConsumption, fuelConsumption) == 0 &&
                Objects.equals(brand, car.brand) &&
                Objects.equals(model, car.model);
    }

    @Override
    public int hashCode() {
        return Objects.hash(brand, model, year, mileage, fuelConsumption);
    }
}
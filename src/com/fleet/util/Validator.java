package com.fleet.util;

import java.util.regex.Pattern;

public class Validator {
    private static final Pattern TEXT_PATTERN =
            Pattern.compile("^[\\p{L}0-9\\s.,:;!?'\"-]{1,200}$");
    private static final int MIN_YEAR = 1950;

    public static String validateText(String value, String fieldName) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException(fieldName + " не может быть пустым");
        }
        String trimmed = value.trim();
        if (!TEXT_PATTERN.matcher(trimmed).matches()) {
            throw new IllegalArgumentException(fieldName + " содержит недопустимые символы");
        }
        return trimmed;
    }

    public static int validateYear(String value) {
        int year;
        try {
            year = Integer.parseInt(value.trim());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Год должен быть целым числом");
        }
        int currentYear = java.time.Year.now().getValue();
        if (year < MIN_YEAR || year > currentYear) {
            throw new IllegalArgumentException(
                    "Год должен быть в диапазоне " + MIN_YEAR + "–" + currentYear);
        }
        return year;
    }

    public static double validateMileage(String value) {
        double mileage;
        try {
            mileage = Double.parseDouble(value.trim().replace(",", "."));
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Пробег должен быть числом");
        }
        if (mileage < 0 || mileage > 2_000_000) {
            throw new IllegalArgumentException("Пробег должен быть в диапазоне 0–2 000 000 км");
        }
        return mileage;
    }

    public static double validateFuelConsumption(String value) {
        double fc;
        try {
            fc = Double.parseDouble(value.trim().replace(",", "."));
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Расход топлива должен быть числом");
        }
        if (fc <= 0 || fc > 100) {
            throw new IllegalArgumentException("Расход должен быть в диапазоне 0–100 л/100км");
        }
        return fc;
    }
}
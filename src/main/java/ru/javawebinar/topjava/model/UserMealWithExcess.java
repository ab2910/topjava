package ru.javawebinar.topjava.model;

import java.time.LocalDateTime;

public class UserMealWithExcess {
    private final LocalDateTime dateTime;

    private final String description;

    private final int calories;

    private final Excess excess;

    public UserMealWithExcess(LocalDateTime dateTime, String description, int calories, Excess excess) {
        this.dateTime = dateTime;
        this.description = description;
        this.calories = calories;
        this.excess = excess;
    }

    @Override
    public String toString() {
        return "UserMealWithExcess{" +
                "dateTime=" + dateTime +
                ", description='" + description + '\'' +
                ", calories=" + calories +
                ", excess=" + excess.isPresent() +
                '}';
    }

    public static class Excess {
        private final int caloriesPerDay;

        private int dailyCalorage;

        public Excess(int caloriesPerDay, int calories) {
            this.caloriesPerDay = caloriesPerDay;
            dailyCalorage = calories;
        }

        public boolean isPresent() {
            return dailyCalorage > caloriesPerDay;
        }

        public Excess addCalories(int calories) {
            dailyCalorage += calories;
            return this;
        }
    }
}

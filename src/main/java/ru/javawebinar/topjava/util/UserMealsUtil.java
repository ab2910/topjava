package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.model.UserMealWithExcess;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static ru.javawebinar.topjava.util.TimeUtil.isBetweenHalfOpen;

public class UserMealsUtil {
    public static void main(String[] args) {
        List<UserMeal> meals = Arrays.asList(
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед", 1000),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение", 100),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак", 1000),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Обед", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин", 410)
        );

        List<UserMealWithExcess> mealsTo = filteredByCycles(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000);
        System.out.println("------> Filtering by cycles:");
        mealsTo.forEach(System.out::println);

        mealsTo = filteredByStreams(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000);
        System.out.println("\n------> Filtering by streams:");
        mealsTo.forEach(System.out::println);
    }

    public static List<UserMealWithExcess> filteredByCycles(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        if (meals == null || meals.size() == 0) return new ArrayList<>();

        /* local collection storing daily calorage for all the dates */
        Map<LocalDate, Integer> dailyCalorage = new HashMap<>();

        /* first run through 'meals': count daily calorage */
        for (UserMeal meal : meals) {
            if (meal != null && meal.getDateTime() != null) {
                dailyCalorage.merge(meal.getDate(), meal.getCalories(), Integer::sum);
            }
        }

        List<UserMealWithExcess> result = new ArrayList<>();

        /* second run through 'meals': search for meals lying within arg time limits and saving them to result list
         excess field is calculated by comparing of daily calorage got by date from map with arg limit of calories per day */
        for (UserMeal meal : meals) {
            if (meal != null && meal.getDateTime() != null
                    && isBetweenHalfOpen(meal.getTime(), startTime, endTime)) {
                result.add(new UserMealWithExcess(meal.getDateTime(), meal.getDescription(), meal.getCalories(),
                        dailyCalorage.getOrDefault(meal.getDate(), 0) > caloriesPerDay));
            }
        }

        return result;
    }

    public static List<UserMealWithExcess> filteredByStreams(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        Map<LocalDate, Integer> dailyCalorage = getNullCheckedStream(meals)
                .collect(Collectors.toMap(UserMeal::getDate, UserMeal::getCalories, Integer::sum));

        return getNullCheckedStream(meals)
                .filter(meal -> isBetweenHalfOpen(meal.getTime(), startTime, endTime))
                .map(meal -> new UserMealWithExcess(meal.getDateTime(), meal.getDescription(), meal.getCalories(),
                        dailyCalorage.getOrDefault(meal.getDate(), 0) > caloriesPerDay))
                .collect(Collectors.toList());
    }

    public static<E> Stream<E> getNullCheckedStream(Collection<E> collection) {
        return Optional.ofNullable(collection)
                .map(Collection::stream).orElseGet(Stream::empty)
                .filter(Objects::nonNull);
    }
}
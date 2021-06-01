package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.model.UserMealWithExcess;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;

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

        List<UserMealWithExcess> mealsTo = filteredByCycle(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000);
        System.out.println("------> Filtering by cycles:");
        mealsTo.forEach(System.out::println);

        mealsTo = filteredByStream(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000);
        System.out.println("\n------> Filtering by streams:");
        mealsTo.forEach(System.out::println);
    }

    public static List<UserMealWithExcess> filteredByCycle(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        Map<LocalDate, UserMealWithExcess.Excess> dailyCalorage = new HashMap<>();
        List<UserMealWithExcess> result = new ArrayList<>();

        for (UserMeal meal : meals) {
            UserMealWithExcess.Excess excess = dailyCalorage.merge(
                    meal.getDate(),
                    new UserMealWithExcess.Excess(caloriesPerDay, meal.getCalories()),
                    (exc1, exc2) -> exc1.addCalories(meal.getCalories())
            );
            if (isBetweenHalfOpen(meal.getTime(), startTime, endTime)) {
                result.add(new UserMealWithExcess(meal.getDateTime(), meal.getDescription(), meal.getCalories(), excess));
            }
        }
        return result;
    }

    public static List<UserMealWithExcess> filteredByStream(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        return meals.stream().collect(new CustomCollector(startTime, endTime, caloriesPerDay));
    }

    public static class CustomCollector implements Collector<UserMeal, List<UserMealWithExcess>, List<UserMealWithExcess>> {
        private final Map<LocalDate, UserMealWithExcess.Excess> dailyCalorage = new ConcurrentHashMap<>();
        private final LocalTime startTime;
        private final LocalTime endTime;
        private final int caloriesPerDay;

        public CustomCollector(LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
            this.startTime = startTime;
            this.endTime = endTime;
            this.caloriesPerDay = caloriesPerDay;
        }

        @Override
        public Supplier<List<UserMealWithExcess>> supplier() {
            return ArrayList::new;
        }

        @Override
        public BiConsumer<List<UserMealWithExcess>, UserMeal> accumulator() {
            return (c, e) -> {
                UserMealWithExcess.Excess excess = dailyCalorage.merge(
                        e.getDate(),
                        new UserMealWithExcess.Excess(caloriesPerDay, e.getCalories()),
                        (exc1, exc2) -> exc1.addCalories(e.getCalories())
                );

                if (isBetweenHalfOpen(e.getTime(), startTime, endTime)) {
                    c.add(new UserMealWithExcess(e.getDateTime(), e.getDescription(), e.getCalories(), excess));
                }
            };
        }

        @Override
        public BinaryOperator<List<UserMealWithExcess>> combiner() {
            return (l, r) -> {
                l.addAll(r);
                return l;
            };
        }

        @Override
        public Function<List<UserMealWithExcess>, List<UserMealWithExcess>> finisher() {
            return c -> c;
        }

        @Override
        public Set<Characteristics> characteristics() {
            return EnumSet.of(Characteristics.CONCURRENT);
        }
    }
}

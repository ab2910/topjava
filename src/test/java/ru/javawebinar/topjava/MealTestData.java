package ru.javawebinar.topjava;

import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.javawebinar.topjava.model.AbstractBaseEntity.START_SEQ;

public class MealTestData {
    public static final int MEAL_START_ID = START_SEQ + 2;

    public static final int MEAL_NOT_FOUND_ID = 327;

    public static final List<Meal> USER_MEALS_SORTED = Arrays.asList(
            new Meal(MEAL_START_ID + 6, LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин", 410),
            new Meal(MEAL_START_ID + 5, LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Обед", 500),
            new Meal(MEAL_START_ID + 4, LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак", 1000),
            new Meal(MEAL_START_ID + 3, LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение", 100),
            new Meal(MEAL_START_ID + 2, LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин", 500),
            new Meal(MEAL_START_ID + 1, LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед", 1000),
            new Meal(MEAL_START_ID, LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500));

    public static final List<Meal> USER_MEALS_SORTED_FILTERED = Arrays.asList(
            USER_MEALS_SORTED.get(4),
            USER_MEALS_SORTED.get(5),
            USER_MEALS_SORTED.get(6));

    public static final LocalDate FILTER_DATE = LocalDate.of(2020, Month.JANUARY, 30);

    public static final Meal ADMIN_MEAL = new Meal(MEAL_START_ID + 7,
            LocalDateTime.of(2021, Month.JUNE, 11, 10, 0), "Еда 1 пользователя 2", 1);

    public static Meal getNew() {
        return new Meal(null,
                LocalDateTime.of(2021, Month.JUNE, 20, 11, 0), "Еда 3 пользователя 2", 777);
    }

    public static Meal getUpdated() {
        Meal updated = new Meal(ADMIN_MEAL.getId(), ADMIN_MEAL.getDateTime(), ADMIN_MEAL.getDescription(), ADMIN_MEAL.getCalories());
        updated.setDateTime(LocalDateTime.of(2021, Month.JUNE, 21, 12, 0));
        updated.setDescription("Подменённая еда 1 пользователя 2");
        updated.setCalories(888);
        return updated;
    }

    public static void assertMatch(Meal actual, Meal expected) {
        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }

    public static void assertMatch(Iterable<Meal> actual, Iterable<Meal> expected) {
        assertThat(actual).usingDefaultElementComparator().isEqualTo(expected);
    }
}

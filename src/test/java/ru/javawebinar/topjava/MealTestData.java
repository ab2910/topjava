package ru.javawebinar.topjava;

import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.javawebinar.topjava.model.AbstractBaseEntity.START_SEQ;

public class MealTestData {
    public static final int MEAL_START_ID = START_SEQ + 2;

    public static final int MEAL_NOT_FOUND_ID = 327;

    public static final Meal userMeal1Day1 = new Meal(MEAL_START_ID, LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500);
    public static final Meal userMeal2Day1 = new Meal(MEAL_START_ID + 1, LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед", 1000);
    public static final Meal userMeal3Day1 = new Meal(MEAL_START_ID + 2, LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин", 500);
    public static final Meal userMeal4Day2 = new Meal(MEAL_START_ID + 3, LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение", 100);
    public static final Meal userMeal5Day2 = new Meal(MEAL_START_ID + 4, LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак", 1000);
    public static final Meal userMeal6Day2 = new Meal(MEAL_START_ID + 5, LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Обед", 500);
    public static final Meal userMeal7Day2 = new Meal(MEAL_START_ID + 6, LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин", 410);

    public static final Meal adminMeal1 = new Meal(MEAL_START_ID + 7, LocalDateTime.of(2021, Month.JUNE, 11, 10, 0), "Еда 1 пользователя 2", 1);
    public static final Meal adminMeal2 = new Meal(MEAL_START_ID + 8, LocalDateTime.of(2021, Month.JUNE, 12, 10, 0), "Еда 1 пользователя 2", 2011);

    public static final LocalDate DAY1 = LocalDate.of(2020, Month.JANUARY, 30);
    public static final LocalDate DAY2 = LocalDate.of(2020, Month.JANUARY, 31);

    public static Meal getNew() {
        return new Meal(null,
                LocalDateTime.of(2021, Month.JUNE, 20, 11, 0),
                "Еда 3 пользователя 2",
                777);
    }

    public static Meal getUpdated(Meal meal) {
        Meal updated = new Meal(meal.getId(), meal.getDateTime(), meal.getDescription(), meal.getCalories());
        updated.setDateTime(LocalDateTime.of(2021, Month.JUNE, 21, 12, 0));
        updated.setDescription("Подменённая еда");
        updated.setCalories(777);
        return updated;
    }

    public static void assertMatch(Meal actual, Meal expected) {
        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }

    public static void assertMatch(Iterable<Meal> actual, Iterable<Meal> expected) {
        assertThat(actual).usingDefaultElementComparator().isEqualTo(expected);
    }
}

package ru.javawebinar.topjava.storage;

import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.util.List;

public interface MealStorage {
    void add(LocalDateTime localDateTime, String description, int calories);

    List<Meal> getAll();

    Meal getById(long id);

    boolean updateById(long id, LocalDateTime localDateTime, String description, int calories);

    Meal deleteById(long id);
}

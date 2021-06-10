package ru.javawebinar.topjava.storage;

import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public class MealMemoryStorage implements MealStorage {
    private static AtomicLong getId = new AtomicLong(0);
    private static Map<Long, Meal> storage = new ConcurrentHashMap<>();

    static {
        storage.put(getId.incrementAndGet(), new Meal(getId.get(), LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0),"Завтрак", 500));
        storage.put(getId.incrementAndGet(), new Meal(getId.get(), LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед", 1000));
        storage.put(getId.incrementAndGet(), new Meal(getId.get(), LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин", 500));
        storage.put(getId.incrementAndGet(), new Meal(getId.get(), LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение", 100));
        storage.put(getId.incrementAndGet(), new Meal(getId.get(), LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак", 1000));
        storage.put(getId.incrementAndGet(), new Meal(getId.get(), LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Обед", 500));
        storage.put(getId.incrementAndGet(), new Meal(getId.get(), LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин", 410));
    }

    @Override
    public void add(LocalDateTime localDateTime, String description, int calories) {
        long id = getId.incrementAndGet();
        storage.put(id, new Meal(id, localDateTime, description, calories));
    }

    @Override
    public List<Meal> getAll() {
        return new ArrayList<>(storage.values());
    }

    @Override
    public Meal getById(long id) {
        return storage.getOrDefault(id, new Meal(0L, LocalDateTime.now(), "-", 0));
    }

    @Override
    public boolean updateById(long id, LocalDateTime localDateTime, String description, int calories) {
        Meal oldMeal = deleteById(id);
        if (oldMeal == null) return false;
        storage.put(id, new Meal(id, localDateTime, description, calories));
        return true;
    }

    @Override
    public Meal deleteById(long id) {
        return storage.remove(id);
    }
}

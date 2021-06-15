package ru.javawebinar.topjava.repository.inmemory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.DateTimeUtil;
import ru.javawebinar.topjava.util.MealsUtil;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Repository
public class InMemoryMealRepository implements MealRepository {
    private static final Logger log = LoggerFactory.getLogger(InMemoryMealRepository.class);

    private final Map<Integer, Map<Integer, Meal>> repository = new ConcurrentHashMap<>();
    private final AtomicInteger counter = new AtomicInteger(0);

    {
        MealsUtil.meals.forEach(meal -> save(meal, 1));
        save(new Meal(LocalDateTime.now(), "Еда 1 для пользователя 2", 100), 2);
        save(new Meal(LocalDateTime.now(), "Еда 2 для пользователя 2", 200), 2);
    }

    @Override
    public Meal save(Meal meal, int userId) {
        log.info("save {} for userId={}", meal, userId);

        if (meal.isNew()) {
            meal.setId(counter.incrementAndGet());
            repository.merge(
                    userId,
                    new ConcurrentHashMap<Integer, Meal>() {{ put(meal.getId(), meal); }},
                    (oldMap, newMap) -> {
                        oldMap.put(meal.getId(), meal);
                        return oldMap;
            });
            return meal;
        }

        if (isMealNotOfUser(meal.getId(), userId)) return null;
        return repository.get(userId).computeIfPresent(meal.getId(), (id, oldMeal) -> meal);
    }

    @Override
    public boolean delete(int id, int userId) {
        log.info("delete Meal#{} for userId={}", id, userId);
        if (isMealNotOfUser(id, userId)) return false;
        return repository.get(userId).remove(id) != null;
    }

    @Override
    public Meal get(int id, int userId) {
        log.info("get Meal#{} for userId={}", id, userId);
        if (isMealNotOfUser(id, userId)) return null;
        return repository.get(userId).get(id);
    }

    @Override
    public List<Meal> getAll(int userId) {
        log.info("getAll for userId={}", userId);
        return getStream(userId, meal -> true).collect(Collectors.toList());
    }

    @Override
    public List<Meal> getAllFiltered(LocalDate startDate, LocalDate endDate, int userId) {
        log.info("getAll filtered by dates={}, {} for userId={}", startDate, endDate, userId);
        return getStream(userId, meal -> DateTimeUtil.isBetweenDates(meal.getDate(), startDate, endDate)).collect(Collectors.toList());
    }

    private boolean isMealNotOfUser(int id, int userId) {
        return repository.getOrDefault(userId, new ConcurrentHashMap<>()).get(id) == null;
    }

    private Stream<Meal> getStream(int userId, Predicate<Meal> filter) {
        return repository.get(userId).values().stream()
                .filter(filter)
                .sorted(Comparator.comparing(Meal::getDateTime).reversed());
    }
}

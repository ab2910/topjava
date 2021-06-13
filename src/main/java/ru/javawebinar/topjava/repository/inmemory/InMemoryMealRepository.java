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
import java.util.stream.Collectors;

@Repository
public class InMemoryMealRepository implements MealRepository {
    private final Map<Integer, Meal> repository = new ConcurrentHashMap<>();
    private final Map<Integer, Set<Integer>> userToMeal = new ConcurrentHashMap<>();
    private final AtomicInteger counter = new AtomicInteger(0);
    private static final Logger log = LoggerFactory.getLogger(InMemoryUserRepository.class);

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
            repository.put(meal.getId(), meal);
            userToMeal.merge(userId, new HashSet<>(Collections.singleton(meal.getId())), (oldSet, newSet) -> {
                oldSet.add(meal.getId());
                return oldSet;
            });
            return meal;
        }
        if (isMealNotOfUser(meal.getId(), userId)) return null;
        return repository.computeIfPresent(meal.getId(), (id, oldMeal) -> meal);
    }

    @Override
    public boolean delete(int id, int userId) {
        log.info("delete {} for userId={}", id, userId);
        if (isMealNotOfUser(id, userId)) return false;
        userToMeal.get(userId).remove(id);
        return repository.remove(id) != null;
    }

    @Override
    public Meal get(int id, int userId) {
        log.info("get {} for userId={}", id, userId);
        if (isMealNotOfUser(id, userId)) return null;
        return repository.get(id);
    }

    @Override
    public List<Meal> getAll(int userId) {
        log.info("getAll for userId={}", userId);
        return userToMeal.get(userId).stream()
                .map(repository::get)
                .sorted(Comparator
                        .comparing(Meal::getDateTime)
                        .reversed())
                .collect(Collectors.toList());
    }

    @Override
    public List<Meal> getAll(LocalDate startDate, LocalDate endDate, int userId) {
        log.info("getAll filtered for userId={}", userId);
        return getAll(userId).stream()
                .filter(meal -> DateTimeUtil.isBetweenDates(meal.getDate(), startDate, endDate))
                .collect(Collectors.toList());
    }

    private boolean isMealNotOfUser(int id, int userId) {
        return !userToMeal.getOrDefault(userId, new HashSet<>(0)).contains(id);
    }
}

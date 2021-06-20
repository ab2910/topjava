package ru.javawebinar.topjava.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.bridge.SLF4JBridgeHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit4.SpringRunner;
import ru.javawebinar.topjava.MealTestData;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.util.List;

import static org.junit.Assert.assertThrows;
import static ru.javawebinar.topjava.UserTestData.*;
import static ru.javawebinar.topjava.MealTestData.*;

@ContextConfiguration({
        "classpath:spring/spring-app.xml",
        "classpath:spring/spring-db.xml"
})
@RunWith(SpringRunner.class)
@Sql(scripts = "classpath:db/populateDB.sql", config = @SqlConfig(encoding = "UTF-8"))
public class MealServiceTest {

    @Autowired
    private MealService service;

    static {
        SLF4JBridgeHandler.install();
    }

    /*
        user meal ids = 100002 — 100008;
        admin meal ids = 100009, 100010;
        not found id = 327
     */

    @Test
    public void get() {
        Meal meal = service.get(ADMIN_MEAL.getId(), ADMIN_ID);
        assertMatch(meal, ADMIN_MEAL);
    }

    @Test
    public void getForAnotherUser() {
        assertThrows(NotFoundException.class, () -> service.get(ADMIN_MEAL.getId(), USER_ID));
    }

    @Test
    public void getNotExisting() {
        assertThrows(NotFoundException.class, () -> service.get(MEAL_NOT_FOUND_ID, USER_ID));
    }

    @Test
    public void delete() {
        service.delete(ADMIN_MEAL.getId(), ADMIN_ID);
        assertThrows(NotFoundException.class, () -> service.get(ADMIN_MEAL.getId(), ADMIN_ID));
    }

    @Test
    public void deleteForAnotherUser() {
        assertThrows(NotFoundException.class, () -> service.delete(ADMIN_MEAL.getId(), USER_ID));
    }

    @Test
    public void deleteNotExisting() {
        assertThrows(NotFoundException.class, () -> service.delete(MEAL_NOT_FOUND_ID, USER_ID));
    }

    @Test
    public void getBetweenInclusive() {
        List<Meal> meals = service.getBetweenInclusive(FILTER_DATE, FILTER_DATE, USER_ID);
        MealTestData.assertMatch(meals, USER_MEALS_SORTED_FILTERED);
    }

    @Test
    public void getAll() {
        List<Meal> meals = service.getAll(USER_ID);
        MealTestData.assertMatch(meals, USER_MEALS_SORTED);
    }

    @Test
    public void update() {
        Meal updated = MealTestData.getUpdated();
        service.update(updated, ADMIN_ID);
        assertMatch(service.get(updated.getId(), ADMIN_ID), MealTestData.getUpdated());
    }

    @Test
    public void updateForAnotherUser() {
        Meal updated = MealTestData.getUpdated();
        assertThrows(NotFoundException.class, () -> service.update(updated, USER_ID));
    }

    @Test
    public void updateNotExisting() {
        Meal updated = MealTestData.getUpdated();
        updated.setId(MEAL_NOT_FOUND_ID);
        assertThrows(NotFoundException.class, () -> service.update(updated, ADMIN_ID));
    }

    @Test
    public void updateWithDuplicatingDateTime() {
        Meal updated = MealTestData.getUpdated();
        updated.setDateTime(service.get(updated.getId()+1, ADMIN_ID).getDateTime());
        assertThrows(DataAccessException.class, () -> service.update(updated, ADMIN_ID));
    }

    @Test
    public void updateWithDuplicatingDateTimeOfAnotherUser() {
        Meal updated = MealTestData.getUpdated();
        updated.setDateTime(service.get(updated.getId()-1, USER_ID).getDateTime());
        service.update(updated, ADMIN_ID);
        assertMatch(service.get(updated.getId(), ADMIN_ID), updated);
    }

    @Test
    public void create() {
        Meal created = service.create(MealTestData.getNew(), ADMIN_ID);
        Integer newId = created.getId();
        Meal newMeal = MealTestData.getNew();
        newMeal.setId(newId);
        assertMatch(created, newMeal);
        assertMatch(service.get(newId, ADMIN_ID), newMeal);
    }

    @Test
    public void createWithDuplicatingDateTime() {
        Meal newMeal = MealTestData.getNew();
        newMeal.setDateTime(service.get(MEAL_START_ID + 8, ADMIN_ID).getDateTime());
        assertThrows(DataAccessException.class, () -> service.create(newMeal, ADMIN_ID));
    }

    @Test
    public void createWithDuplicatingDateTimeOfAnotherUser() {
        Meal newMeal = MealTestData.getNew();
        newMeal.setDateTime(service.get(MEAL_START_ID + 4, USER_ID).getDateTime());
        Meal created = service.create(newMeal, ADMIN_ID);
        Integer newId = created.getId();
        newMeal.setId(newId);
        assertMatch(created, newMeal);
        assertMatch(service.get(newId, ADMIN_ID), newMeal);
    }
}

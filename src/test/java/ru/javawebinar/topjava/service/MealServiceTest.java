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

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertThrows;
import static ru.javawebinar.topjava.UserTestData.*;
import static ru.javawebinar.topjava.MealTestData.*;

@ContextConfiguration({
        "classpath:spring/spring-app-jdbc.xml",
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

    @Test
    public void get() {
        assertMatch(service.get(adminMeal1.getId(), ADMIN_ID), adminMeal1);
    }

    @Test
    public void getForAnotherUser() {
        assertThrows(NotFoundException.class, () -> service.get(adminMeal1.getId(), USER_ID));
    }

    @Test
    public void getNotExisting() {
        assertThrows(NotFoundException.class, () -> service.get(MEAL_NOT_FOUND_ID, USER_ID));
    }

    @Test
    public void delete() {
        service.delete(adminMeal1.getId(), ADMIN_ID);
        assertThrows(NotFoundException.class, () -> service.get(adminMeal1.getId(), ADMIN_ID));
    }

    @Test
    public void deleteForAnotherUser() {
        assertThrows(NotFoundException.class, () -> service.delete(adminMeal1.getId(), USER_ID));
    }

    @Test
    public void deleteNotExisting() {
        assertThrows(NotFoundException.class, () -> service.delete(MEAL_NOT_FOUND_ID, USER_ID));
    }

    @Test
    public void getAll() {
        List<Meal> expected = Arrays.asList(userMeal7Day2, userMeal6Day2, userMeal5Day2, userMeal4Day2,
                userMeal3Day1, userMeal2Day1, userMeal1Day1);
        List<Meal> actual = service.getAll(USER_ID);
        MealTestData.assertMatch(actual, expected);
    }

    @Test
    public void getAllEmpty() {
        List<Meal> expected = Collections.emptyList();
        service.delete(adminMeal1.getId(), ADMIN_ID);
        service.delete(adminMeal2.getId(), ADMIN_ID);
        List<Meal> actual = service.getAll(ADMIN_ID);
        MealTestData.assertMatch(actual, expected);
    }

    @Test
    public void getBetweenInclusive() {
        List<Meal> expected = Arrays.asList(userMeal3Day1, userMeal2Day1, userMeal1Day1);
        List<Meal> actual = service.getBetweenInclusive(DAY1, DAY1, USER_ID);
        MealTestData.assertMatch(actual, expected);
    }

    @Test
    public void getBetweenInclusiveEmpty() {
        List<Meal> expected = Collections.emptyList();
        List<Meal> actual = service.getBetweenInclusive(DAY1, DAY1, ADMIN_ID);
        MealTestData.assertMatch(actual, expected);
    }

    @Test
    public void getBetweenInclusiveHalfInterval1() {
        List<Meal> expected = Arrays.asList(userMeal3Day1, userMeal2Day1, userMeal1Day1);
        List<Meal> actual = service.getBetweenInclusive(null, DAY1, USER_ID);
        MealTestData.assertMatch(actual, expected);
    }

    @Test
    public void getBetweenInclusiveHalfInterval2() {
        List<Meal> expected = Arrays.asList(userMeal7Day2, userMeal6Day2, userMeal5Day2, userMeal4Day2);
        List<Meal> actual = service.getBetweenInclusive(DAY2, null, USER_ID);
        MealTestData.assertMatch(actual, expected);
    }

    @Test
    public void getBetweenInclusiveNoInterval() {
        List<Meal> expected = Arrays.asList(userMeal7Day2, userMeal6Day2, userMeal5Day2, userMeal4Day2,
                userMeal3Day1, userMeal2Day1, userMeal1Day1);
        List<Meal> actual = service.getBetweenInclusive(null, null, USER_ID);
        MealTestData.assertMatch(actual, expected);
    }

    @Test
    public void update() {
        Meal toUpdate = MealTestData.getUpdated(adminMeal1);
        service.update(toUpdate, ADMIN_ID);
        assertMatch(service.get(toUpdate.getId(), ADMIN_ID), MealTestData.getUpdated(adminMeal1));
    }

    @Test
    public void updateForAnotherUser() {
        assertThrows(NotFoundException.class, () -> service.update(MealTestData.getUpdated(adminMeal1), USER_ID));
    }

    @Test
    public void updateNotExisting() {
        Meal toUpdate = MealTestData.getUpdated(adminMeal1);
        toUpdate.setId(MEAL_NOT_FOUND_ID);
        assertThrows(NotFoundException.class, () -> service.update(toUpdate, ADMIN_ID));
    }

    @Test
    public void updateWithDuplicatingDateTimeOfThisUser() {
        Meal toUpdate = MealTestData.getUpdated(adminMeal1);
        toUpdate.setDateTime(service.get(adminMeal2.getId(), ADMIN_ID).getDateTime());
        assertThrows(DataAccessException.class, () -> service.update(toUpdate, ADMIN_ID));
    }

    @Test
    public void updateWithDuplicatingDateTimeOfAnotherUser() {
        Meal toUpdate = MealTestData.getUpdated(adminMeal1);
        LocalDateTime newDateTime = service.get(userMeal1Day1.getId(), USER_ID).getDateTime();
        toUpdate.setDateTime(newDateTime);
        service.update(toUpdate, ADMIN_ID);
        Meal expected = MealTestData.getUpdated(adminMeal1);
        expected.setDateTime(newDateTime);
        assertMatch(service.get(toUpdate.getId(), ADMIN_ID), expected);
    }

    @Test
    public void create() {
        Meal created = service.create(MealTestData.getNew(), ADMIN_ID);
        Integer newId = created.getId();
        Meal expected = MealTestData.getNew();
        expected.setId(newId);
        assertMatch(created, expected);
        assertMatch(service.get(newId, ADMIN_ID), expected);
    }

    @Test
    public void createWithDuplicatingDateTimeOfThisUser() {
        Meal toCreate = MealTestData.getNew();
        toCreate.setDateTime(service.get(adminMeal1.getId(), ADMIN_ID).getDateTime());
        assertThrows(DataAccessException.class, () -> service.create(toCreate, ADMIN_ID));
    }

    @Test
    public void createWithDuplicatingDateTimeOfAnotherUser() {
        Meal toCreate = MealTestData.getNew();
        LocalDateTime newDateTime = service.get(userMeal1Day1.getId(), USER_ID).getDateTime();
        toCreate.setDateTime(newDateTime);
        Meal created = service.create(toCreate, ADMIN_ID);
        Integer newId = created.getId();
        Meal expected = MealTestData.getNew();
        expected.setId(newId);
        expected.setDateTime(newDateTime);
        assertMatch(created, expected);
        assertMatch(service.get(newId, ADMIN_ID), expected);
    }
}

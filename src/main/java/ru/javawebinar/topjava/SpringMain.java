package ru.javawebinar.topjava;

import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.web.meal.MealRestController;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.Arrays;

public class SpringMain {
    public static void main(String[] args) {
        // java 7 automatic resource management (ARM)
        try (ConfigurableApplicationContext appCtx = new ClassPathXmlApplicationContext("spring/spring-app.xml")) {
            System.out.println("\nBean definition names:");
            Arrays.stream(appCtx.getBeanDefinitionNames())
                    .filter(name -> !name.startsWith("org.springframework"))
                    .sorted()
                    .forEach(name -> System.out.format(":: %s :: ", name));
            System.out.println();

            MealRestController mealRestController = appCtx.getBean(MealRestController.class);

            System.out.println("\ncreate meal for user 1:");
            System.out.println("\t" + mealRestController.create(new Meal(null, LocalDateTime.now(), "Новая еда", 666)));

            System.out.println("\nget meal 4 belonging to user 1:");
            Meal meal = mealRestController.get(4);
            System.out.println("\t" + meal);

            System.out.println("\nget all meals belonging to user 1:");
            System.out.println("\t" + mealRestController.getAll());

            System.out.println("\nupdate and get meal 4 belonging to user 1:");
            mealRestController.update(new Meal(meal.getId(), meal.getDateTime(), "Апдейт еды", meal.getCalories()), meal.getId());
            System.out.println("\t" + mealRestController.get(4));

            System.out.println("\ndelete and try to get meal 4 belonging to user 1:");
            mealRestController.delete(4);
            //System.out.println("\t" + mealRestController.get(4)); // NFE

            System.out.println("\nget all meals belonging to user 1:");
            System.out.println("\t" + mealRestController.getAll());

            System.out.println("\nget all meals between null dates and times belonging to user 1");
            System.out.println("\t" + mealRestController.getAllFiltered(null, null, null, null));

            System.out.println("\nget all meals between dates belonging to user 1");
            System.out.println("\t" + mealRestController.getAllFiltered(
                    LocalDate.of(2020, Month.JANUARY, 31),
                    null,
                    LocalDate.of(2020, Month.JANUARY, 31),
                    null));

            System.out.println("\nget all meals between times belonging to user 1");
            System.out.println("\t" + mealRestController.getAllFiltered(
                    null,
                    LocalTime.of(7, 0),
                    null,
                    LocalTime.of(12, 0)));

            System.out.println("\nget all meals half interval belonging to user 1");
            System.out.println("\t" + mealRestController.getAllFiltered(
                    LocalDate.of(2020, Month.JANUARY, 31),
                    null,
                    null,
                    LocalTime.of(12, 0)));

            /*System.out.println("\ntry to update meal 8 not belonging to user 1:");
            mealRestController.update(new Meal(8, meal.getDateTime(), "Апдейт еды другого пользователя", meal.getCalories()), 8);;
            System.out.println("\t" + mealRestController.get(8));*/ // NPE

            /*System.out.println("\ntry to update not-consistent meal 4 belonging to user 1:");
            mealRestController.update(new Meal(5, meal.getDateTime(), "Апдейт еды другого пользователя", meal.getCalories()), 4);
            System.out.println("\t" + mealRestController.get(8));*/ // IAE

            /*System.out.println("\ntry to get meal 8 not belonging to user 1:");
            meal = mealRestController.get(8);
            System.out.println("\t" + meal);*/ // NPE

            /*System.out.println("\ntry to delete meal 8 not belonging to user 1:");
            mealRestController.delete(8);*/ // NPE

            /*List<User> users = Arrays.asList(
                    new User("user", "e@ma.il", "password", Role.USER),
                    new User("User", "e1@ma.il", "password", Role.USER),
                    new User("an user", "e2@ma.il", "password", Role.USER),
                    new User("user an", "e3@ma.il", "password", Role.USER),
                    new User("super user", "e4@ma.il", "password", Role.USER),
                    new User("God", "e5@ma.il", "password", Role.USER, Role.ADMIN),
                    new User("user1", "e6@ma.il", "password", Role.USER),
                    new User("user2", "e7@ma.il", "password", Role.USER),
                    new User("user2", "e9@ma.il", "password", Role.USER),
                    new User("user2", "e8@ma.il", "password", Role.USER)
            );

            UserRepository userRepository = appCtx.getBean(InMemoryUserRepository.class);
            users.forEach(userRepository::save);

            AdminRestController adminUserController = appCtx.getBean(AdminRestController.class);
            adminUserController.create(new User(null, "userName", "email@mail.ru", "password", Role.ADMIN));

            System.out.println("\nget all users sorted by name, then email");
            adminUserController.getAll().forEach(user -> System.out.println("\t"+user));*/
        }
    }
}

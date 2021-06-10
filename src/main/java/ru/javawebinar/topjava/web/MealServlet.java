package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.MealTo;
import ru.javawebinar.topjava.storage.MealMemoryStorage;
import ru.javawebinar.topjava.storage.MealStorage;
import ru.javawebinar.topjava.util.MealsUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

public class MealServlet extends HttpServlet {
    private static final Logger log = getLogger(MealServlet.class);
    private static final MealStorage mealStorage = new MealMemoryStorage();
    private static final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        log.debug("forward to meals by GET");

        String action = request.getParameter("action");
        if (action == null) {
            List<MealTo> mealsTo = MealsUtil.filteredByStreams(mealStorage.getAll(), LocalTime.MIN, LocalTime.MAX,2000);
            request.setAttribute("mealsTo", mealsTo);

            request.getRequestDispatcher("/meals.jsp").forward(request, response);

        } else if (action.equalsIgnoreCase("add")) {
            request.setAttribute("action", "Add");

            request.getRequestDispatcher("/mealsEdit.jsp").forward(request, response);

        } else if (action.equalsIgnoreCase("update")) {
            long id = Long.parseLong(request.getParameter("id"));
            Meal meal = mealStorage.getById(id);
            request.setAttribute("action", "Edit");
            request.setAttribute("id", id);
            request.setAttribute("datetime", meal.getDateTime());
            request.setAttribute("description", meal.getDescription());
            request.setAttribute("calories", meal.getCalories());

            request.getRequestDispatcher("/mealsEdit.jsp").forward(request, response);

        } else if (action.equalsIgnoreCase("delete")) {
            long id = Long.parseLong(request.getParameter("id"));
            mealStorage.deleteById(id);

            response.sendRedirect("meals");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        log.debug("forward to meals by POST");

        request.setCharacterEncoding("UTF-8");
        String action = request.getParameter("action");
        String ldtString = request.getParameter("datetime");
        LocalDateTime localDateTime = LocalDateTime.now().withSecond(0).withNano(0);
        if (ldtString != null && !(ldtString.isEmpty())) localDateTime = LocalDateTime.parse(ldtString, dtf);
        String description = request.getParameter("description");
        description = description == null ? "" : description;
        String caloriesStr = request.getParameter("calories");
        int calories = 0;
        if (caloriesStr != null && !(caloriesStr.isEmpty())) calories  = Integer.parseInt(caloriesStr);

        if (action.equalsIgnoreCase("add")) {
            mealStorage.add(localDateTime, description, calories);
        } else if (action.equalsIgnoreCase("Edit")) {
            long id = Long.parseLong(request.getParameter("id"));
            mealStorage.updateById(id, localDateTime, description, calories);
        }

        response.sendRedirect("meals");
    }
}

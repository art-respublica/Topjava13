package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.InMemoryMealRepositoryImpl;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.MealsUtil;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Objects;

import static org.slf4j.LoggerFactory.getLogger;

public class MealServlet extends HttpServlet {

    private static final Logger log = getLogger(MealServlet.class);

    private MealRepository repository;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        repository = new InMemoryMealRepositoryImpl();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String id = request.getParameter("id");

        Meal meal = new Meal(id.isEmpty() ? null : Long.valueOf(id),
                LocalDateTime.parse(request.getParameter("dateTime")),
                request.getParameter("description"),
                Integer.valueOf(request.getParameter("calories")));

        log.info(meal.isNew() ? "Create {}" : "Update {}", meal);
        repository.save(meal);
        response.sendRedirect("meals");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String action = request.getParameter("action");

        if (action == null) {
            log.info("getAll");
            request.setAttribute("meals",
                    MealsUtil.getWithExceeded(repository.getAll(), MealsUtil.DEFAULT_CALORIES_PER_DAY));
            request.getRequestDispatcher("/views/meals.jsp").forward(request, response);

        } else if ("delete".equals(action)) {
            Long id = getId(request);
            log.info("Delete {}", id);
            repository.delete(id);
            response.sendRedirect("meals");

        } else if ("create".equals(action) || "update".equals(action)) {
            final Meal meal = action.equals("create") ?
                    new Meal(LocalDateTime.now().withNano(0).withSecond(0), "", 1000) :
                    repository.get(getId(request));
            request.setAttribute("meal", meal);
            request.getRequestDispatcher("/views/meal.jsp").forward(request, response);
        }
    }

    private Long getId(HttpServletRequest request) {
        String paramId = Objects.requireNonNull(request.getParameter("id"));
        return Long.valueOf(paramId);
    }
}

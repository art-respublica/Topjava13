package ru.javawebinar.topjava.service.datajpa;

import org.junit.Test;
import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.UserTestData;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealServiceTest;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import static ru.javawebinar.topjava.MealTestData.*;
import static ru.javawebinar.topjava.UserTestData.ADMIN_ID;
import static ru.javawebinar.topjava.Profiles.DATAJPA;

@ActiveProfiles(DATAJPA)
public class DataJpaMealServiceTest extends MealServiceTest {

    @Test
    public void getWithUser() throws Exception {
        Meal adminMeal = service.getWithUser(ADMIN_MEAL_ID, ADMIN_ID);
        assertMatch(ADMIN_MEAL1, adminMeal);
        UserTestData.assertMatch(UserTestData.ADMIN, adminMeal.getUser());
    }

    @Test
    public void getWithUserNotFound() throws Exception {
        thrown.expect(NotFoundException.class);
        service.getWithUser(MEAL1_ID, ADMIN_ID);
    }
}

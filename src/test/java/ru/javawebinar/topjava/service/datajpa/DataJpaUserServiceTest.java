package ru.javawebinar.topjava.service.datajpa;

import org.junit.Test;
import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.MealTestData;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.service.UserServiceTest;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import static ru.javawebinar.topjava.Profiles.DATAJPA;
import static ru.javawebinar.topjava.UserTestData.*;

@ActiveProfiles(DATAJPA)
public class DataJpaUserServiceTest extends UserServiceTest {

    @Test
    public void getWithMeals() throws Exception {
        User user = service.getWithMeals(USER_ID);
        assertMatch(USER, user);
        MealTestData.assertMatch(MealTestData.MEALS, user.getMeals());
    }

    @Test
    public void getWithMealsNotFound() throws Exception {
        thrown.expect(NotFoundException.class);
        service.getWithMeals(1);
    }
}

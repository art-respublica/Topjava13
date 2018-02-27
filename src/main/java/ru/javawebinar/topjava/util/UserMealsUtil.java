package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.model.UserMealWithExceed;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.*;
import java.util.stream.Collectors;

/**
 * GKislin
 * 31.05.2015.
 */
public class UserMealsUtil {
    public static void main(String[] args) {
        List<UserMeal> mealList = Arrays.asList(
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 30,10,0), "Завтрак", 500),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 30,13,0), "Обед", 1000),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 30,20,0), "Ужин", 500),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 31,10,0), "Завтрак", 1000),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 31,13,0), "Обед", 500),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 31,20,0), "Ужин", 510)
        );
        List<UserMealWithExceed> filteredUserMealsWithExceeded =
                getFilteredWithExceeded(mealList, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000);
        System.out.println(filteredUserMealsWithExceeded);
    }

    public static List<UserMealWithExceed> getFilteredWithExceeded(List<UserMeal> mealList, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        return getFilteredWithExceededByCycle(mealList, startTime, endTime, caloriesPerDay);
        //return getFilteredWithExceededByStream(mealList, startTime, endTime, caloriesPerDay);
    }

    private static List<UserMealWithExceed> getFilteredWithExceededByCycle(List<UserMeal> mealList, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        
        final Map<LocalDate, Integer> caloriesSumByDate = new HashMap<>();
        mealList.forEach(meal -> caloriesSumByDate.merge(meal.getDate(), meal.getCalories(), Integer::sum));
        
        final List<UserMealWithExceed> userMealWithExceeds = new ArrayList<>();
        mealList.forEach(meal -> {
            if (TimeUtil.isBetween(meal.getTime(), startTime, endTime)) {
                userMealWithExceeds.add(createUserMealWithExceed(meal, caloriesSumByDate.get(meal.getDate()) > caloriesPerDay));
            }
        });
        return userMealWithExceeds;
    }

    private static UserMealWithExceed createUserMealWithExceed(UserMeal meal, Boolean exceed) {
        return new UserMealWithExceed(meal.getDateTime(), meal.getDescription(), meal.getCalories(), exceed);
    }

    private static List<UserMealWithExceed> getFilteredWithExceededByStream(List<UserMeal> mealList, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {

        Map<LocalDate, Integer> caloriesSumByDate = mealList.stream()
                .collect(Collectors.groupingBy(UserMeal::getDate, Collectors.summingInt(UserMeal::getCalories))
                        //                      Collectors.toMap(Meal::getDate, Meal::getCalories, Integer::sum)
                );

        return mealList.stream()
                .filter(meal -> TimeUtil.isBetween(meal.getTime(), startTime, endTime))
                .map(meal -> createUserMealWithExceed(meal, caloriesSumByDate.get(meal.getDate()) > caloriesPerDay))
                .collect(Collectors.toList());
    }
}

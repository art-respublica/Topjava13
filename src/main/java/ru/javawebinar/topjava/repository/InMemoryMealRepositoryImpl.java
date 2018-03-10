package ru.javawebinar.topjava.repository;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.MealsUtil;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public class InMemoryMealRepositoryImpl implements MealRepository {

    private Map<Long, Meal> repository = new ConcurrentHashMap<>();
    private AtomicLong counter = new AtomicLong(0);

    {
        MealsUtil.MEALS.forEach(this::save);
    }

    @Override
    public Meal save(Meal meal) {
        if (meal.isNew()) {
            meal.setId(counter.incrementAndGet());
        }
        repository.put(meal.getId(), meal);
        return meal;
    }

    @Override
    public void delete(Long id) {
        repository.remove(id);
    }

    @Override
    public Meal get(Long id) {
        return repository.get(id);
    }

    @Override
    public Collection<Meal> getAll() {
        return repository.values();
    }
}
  
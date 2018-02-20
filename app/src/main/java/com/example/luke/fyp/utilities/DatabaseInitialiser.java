package com.example.luke.fyp.utilities;

import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.Log;

import com.example.luke.fyp.data.AppDatabase;
import com.example.luke.fyp.data.Ingredient;
import com.example.luke.fyp.data.Meal;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by Luke on 04/02/2018.
 *
 * Test data to check if Room implementation works
 */

public class DatabaseInitialiser {

    private static final int DELAY_MILLIS = 500;

    public static void populateSync(@NonNull final AppDatabase db){ initialiseTestData(db);}

    public static void initialiseTestData(AppDatabase db) {

        Date today = getTodayPlusDays(0);
        Date yesterday = getTodayPlusDays(-1);
        db.ingredientModel().deleteAll();
        db.mealModel().deleteAll();

        Meal currentMeal = makeMeal(Long.parseLong("0"), "B", 0.0,0.0,0.0,0.0,0.0, 0.0,0.0);
        long currentId = addMeal(db, yesterday, currentMeal);

        Ingredient ingredient1 = addIngredient(db, Long.parseLong("0"), currentId, "apples", 10, "01001", 50.0, 0.0, 0.0, 10.0, 8.0, 0.0, 0.0);
        Ingredient ingredient2 = addIngredient(db, Long.parseLong("0"), currentId, "banana", Integer.parseInt("20"), "01001", Double.parseDouble("80"), Double.parseDouble("3"), Double.parseDouble("0.1"), Double.parseDouble("17"), Double.parseDouble("10"), Double.parseDouble("0"), Double.parseDouble("0"));
        Ingredient ingredient3 = addIngredient(db, Long.parseLong("0"), currentId, "oranges", Integer.parseInt("50"), "01001", 70.0, 12.0, 5.0, 12.0, 0.0, 26.0, 10.0);

        List<Ingredient> mealIngredients = new ArrayList<>();
        mealIngredients.add(ingredient1);
        mealIngredients.add(ingredient2);
        mealIngredients.add(ingredient3);

        Meal meal = ingredientsToMeal(mealIngredients, "B", currentId);
        addMeal(db, yesterday, meal);

        currentMeal = makeMeal(Long.parseLong("0"), "B", 0.0,0.0,0.0,0.0,0.0, 0.0,0.0);
        currentId = addMeal(db, today, currentMeal);

        Ingredient ingredient5 = addIngredient(db, Long.parseLong("0"), currentId, "beef", Integer.parseInt("50"), "01001", 120.0, 12.0, 5.0, 12.0, 0.0, 26.0, 10.0);
        Ingredient ingredient4 = addIngredient(db, Long.parseLong("0"), currentId, "cheese", 15, "01001", 100.0, 9.5, 4.0, 0.5, 0.5, 5.0, 5.0);

        mealIngredients.clear();

        mealIngredients.add(ingredient5);
        mealIngredients.add(ingredient4);

        meal = ingredientsToMeal(mealIngredients, "B", currentId);
        addMeal(db, today, meal);


//        List<Ingredient> mealIngredients1 = new ArrayList<>();
//        mealIngredients.add(ingredient3);
//        mealIngredients.add(ingredient4);

//        try {


//            Meal meal = ingredientsToMeal(mealIngredients, "B");
//            Meal meal1 = ingredientsToMeal(mealIngredients1, "L");

//            addMeal(db, today, meal);
//            Thread.sleep(DELAY_MILLIS);
//            addMeal(db, today, meal1);


//            addMeal(db, "1", "B", today, totalCalories, totalFat, totalSats, totalCarbs, totalSugars, totalProtein, totalSodium, ingredientIds);
//            Thread.sleep(DELAY_MILLIS);
//            addMeal(db, "2", "L", today, totalCalories, totalFat, totalSats, totalCarbs, totalSugars, totalProtein, totalSodium, ingredientIds);

            Log.d("DB", "Added Meal");

//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }

    }

    private static Meal ingredientsToMeal(List<Ingredient> mealIngredients, String mealType, long mealId) {

        Double totalCalories = 0.0;
        Double totalCarbs = 0.0;
        Double totalSugars = 0.0;
        Double totalFat = 0.0;
        Double totalSats = 0.0;
        Double totalProtein = 0.0;
        Double totalSodium = 0.0;

        for (int i = 0; i < mealIngredients.size(); i++) {
            Ingredient ingredient = mealIngredients.get(i);
            totalCalories = totalCalories + ingredient.calories;
            totalCarbs = totalCarbs + ingredient.carbs;
            totalSugars = totalSugars + ingredient.sugar;
            totalFat = totalFat + ingredient.fat;
            totalSats = totalSats + ingredient.saturates;
            totalProtein = totalProtein + ingredient.protein;
            totalSodium = totalSodium + ingredient.sodium;
        }
        Meal meal = makeMeal(mealId, mealType, totalCalories, totalFat, totalSats, totalCarbs, totalSugars, totalProtein, totalSodium);
        return meal;
    }



    private static Date getTodayPlusDays(int daysAgo) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, daysAgo);
        return calendar.getTime();
    }

    private static Ingredient addIngredient(final AppDatabase db, final long id, final long mealId, final String name, final int weight, final String ndbno, final Double calories,
                                            final Double fat, final Double sats, final Double carbs, final Double sugars, final Double protein, final Double sodium) {

    Ingredient ingredient = new Ingredient();
    ingredient.id = id;
    ingredient.meal_id = mealId;
    ingredient.name = name;
    ingredient.ndbno = ndbno;
    ingredient.weight = weight;
    ingredient.calories = calories;
    ingredient.fat = fat;
    ingredient.saturates = sats;
    ingredient.carbs = carbs;
    ingredient.sugar = sugars;
    ingredient.protein = protein;
    ingredient.sodium = sodium;
    db.ingredientModel().insertIngredient(ingredient);
    return ingredient;
    }

    private static Meal makeMeal(final long id, final String mealType, final Double calories,
                                 final Double fat, final Double sats, final Double carbs, final Double sugars, final Double protein, final Double sodium) {

        Meal meal = new Meal();
        meal.id = id;
        meal.mealType = mealType;
        meal.totalCalories = calories;
        meal.totalFat = fat;
        meal.totalSats = sats;
        meal.totalCarbs = carbs;
        meal.totalSugars = sugars;
        meal.totalProtein = protein;
        meal.totalSodium = sodium;
        return meal;
    }

//    private static Meal addMeal(final AppDatabase db, final Long id, final String mealType, Date mealTime, final Double calories,
//                                final Double fat, final Double sats, final Double carbs, final Double sugars, final Double protein, final Double sodium, final String[] ingredients){
//
//        Meal meal = new Meal();
//        meal.id = id;
//        meal.mealType = mealType;
//        meal.mealTime = mealTime;
//        meal.totalCalories = calories;
//        meal.totalFat = fat;
//        meal.totalSats = sats;
//        meal.totalCarbs = carbs;
//        meal.totalSugars = sugars;
//        meal.totalProtein = protein;
//        meal.totalSodium = sodium;
//        meal.ingredientIds = ingredients;
//        db.mealModel().insertMeal(meal);
//        return meal;
//    }
private static Long addMeal(final AppDatabase db, Date mealTime, final Meal meal){

    meal.mealTime = mealTime;
    long newId = db.mealModel().insertMeal(meal);
    return newId;
}


    private static class PopulateDbAsync extends AsyncTask<Void, Void, Void> {

        private final AppDatabase mDb;

        PopulateDbAsync(AppDatabase db) {
            mDb = db;
        }

        @Override
        protected Void doInBackground(final Void... params) {
            initialiseTestData(mDb);
            return null;
        }

    }
}


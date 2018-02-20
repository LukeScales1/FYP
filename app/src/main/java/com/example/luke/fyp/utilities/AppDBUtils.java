package com.example.luke.fyp.utilities;

import com.example.luke.fyp.data.AppDatabase;
import com.example.luke.fyp.data.Ingredient;
import com.example.luke.fyp.data.Meal;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by Luke on 13/02/2018.
 *
 * Work in Progress. Utilities for database/Room persistence.
 */

public class AppDBUtils {

    private static AppDatabase mDb;



    public static List<Ingredient> fetchIngredientsOfMeal(Date start, Date end) {

        List<Ingredient> mealIngredients = mDb.mealModel().findMealIngredientsByDayandType("B", start, end);
        return mealIngredients;
//        if (mealIngredients.size() > 0) {
//            for (Ingredient mealIngredient : mealIngredients) {
//                testTV.append("ID: " + mealIngredient.id + "\nMeal ID:" + mealIngredient.meal_id + "\nWeight:" + mealIngredient.weight + "g\nTotal Calories: " + mealIngredient.calories.toString() + "\n\n");
//            }
//        } else {
//            testTV.setText(getString(R.string.daily_view_no_meals));
//        }
    }



    public static Meal ingredientsToMeal(List<Ingredient> mealIngredients, String mealType, Date mealTime, long mealId) {

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
        Meal meal = makeMeal(mealId, mealType, mealTime, totalCalories, totalFat, totalSats, totalCarbs, totalSugars, totalProtein, totalSodium);
        return meal;
    }

    public static Date makeTimestamp(int year, int month, int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, day);
        return calendar.getTime();
    }


    public static Date getTodayPlusDays(int daysAgo) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, daysAgo);
        return calendar.getTime();
    }

    public static Ingredient addIngredient(final AppDatabase db, final long id, final long mealId, final String name, final int weight, final String ndbno, final Double calories,
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

    public static Meal makeMeal(final long id, final String mealType, final Date mealTime, final Double calories,
                                final Double fat, final Double sats, final Double carbs, final Double sugars, final Double protein, final Double sodium) {

        Meal meal = new Meal();
        meal.id = id;
        meal.mealType = mealType;
        meal.mealTime = mealTime;
        meal.totalCalories = calories;
        meal.totalFat = fat;
        meal.totalSats = sats;
        meal.totalCarbs = carbs;
        meal.totalSugars = sugars;
        meal.totalProtein = protein;
        meal.totalSodium = sodium;
        return meal;
    }

    public static Long addMeal(final AppDatabase db, final Meal meal){

        long newId = db.mealModel().insertMeal(meal);
        return newId;
    }

}

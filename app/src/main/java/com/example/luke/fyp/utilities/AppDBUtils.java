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



//    public static List<Ingredient> fetchIngredientsOfMeal(Date start, Date end) {
//
//        return mDb.mealModel().findMealIngredientsByDayandType("B", start, end);
////        if (mealIngredients.size() > 0) {
////            for (Ingredient mealIngredient : mealIngredients) {
////                testTV.append("ID: " + mealIngredient.id + "\nMeal ID:" + mealIngredient.meal_id + "\nWeight:" + mealIngredient.weight + "g\nTotal Calories: " + mealIngredient.calories.toString() + "\n\n");
////            }
////        } else {
////            testTV.setText(getString(R.string.daily_view_no_meals));
////        }
//    }


    public static Meal ingredientsToMeal(List<Ingredient> mealIngredients, long mealId, int mealType, Date mealTime) {

        Double totalCalories = 0.0;
        Double totalCarbs = 0.0;
        Double totalSugars = 0.0;
        Double totalFat = 0.0;
        Double totalSats = 0.0;
        Double totalProtein = 0.0;
        Double totalSodium = 0.0;

        for (int i = 0; i < mealIngredients.size(); i++) {
            Ingredient ingredient = mealIngredients.get(i);
            Double ingMultiplier = ingredient.weight/100.0;
            totalCalories = totalCalories + (ingredient.calories * ingMultiplier);
            totalCarbs = totalCarbs + (ingredient.carbs * ingMultiplier);
            totalSugars = totalSugars + (ingredient.sugar * ingMultiplier);
            totalFat = totalFat + (ingredient.fat * ingMultiplier);
            totalSats = totalSats + (ingredient.saturates * ingMultiplier);
            totalProtein = totalProtein + (ingredient.protein * ingMultiplier);
            totalSodium = totalSodium + (ingredient.sodium * ingMultiplier);
        }
        return makeMeal(mealId, mealType, mealTime, totalCalories, totalFat, totalSats, totalCarbs, totalSugars, totalProtein, totalSodium);
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

    public static long makeBlankIngredient(AppDatabase db){
        Ingredient ingredient = makeIngredient(0,0,null,null,null,null,null,null,null,null,null,null);
        return addIngredient(db, ingredient);
    }
//
    public static Ingredient makeIngredient(final long id, final long mealId, final String name, final Double weight, final String ndbno, final Double calories,
                                            final Double fat, final Double sats, final Double carbs, final Double sugars, final Double protein, final Double sodium){
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
        return ingredient;
    }

    public static Long addIngredient(final AppDatabase db, final Ingredient ingredient) {

        return db.ingredientModel().insertIngredient(ingredient);
    }


    public static Long makeBlankMeal(AppDatabase db, int mealType, Date mealTime) {

        Meal meal = makeMeal(0, mealType, mealTime,null,null,null,null,null,null, null);
        return addMeal(db, meal);
    }

    private static Meal makeMeal(final long id, final int mealType, Date mealTime, final Double calories,
                                 final Double fat, final Double sats, final Double carbs, final Double sugars, final Double protein, final Double sodium) {

        String mealTitle = getTitleFromInt(mealType);

        Meal meal = new Meal();
        meal.id = id;
        meal.mealType = mealType;
        meal.mealTime = mealTime;
        meal.mealTitle = mealTitle;
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

        return db.mealModel().insertMeal(meal);
    }

//    public static Long addMeal(final AppDatabase db,  Date mealTime, final Meal meal){
//
//        meal.mealTime = mealTime;
//        long newId = db.mealModel().insertMeal(meal);
//        return newId;
//    }


    private static String getTitleFromInt(int mealType) {
        String mealTitle = "";
        if(mealType == 1){
            mealTitle = "Breakfast";
        } else if(mealType == 2){
            mealTitle = "Lunch";
        } else if(mealType == 3){
            mealTitle = "Dinner";
        } else if(mealType == 4){
            mealTitle = "Snacks";
        }
        return mealTitle;
    }


}

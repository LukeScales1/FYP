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

import static com.example.luke.fyp.utilities.AppDBUtils.addIngredient;
import static com.example.luke.fyp.utilities.AppDBUtils.addMeal;
import static com.example.luke.fyp.utilities.AppDBUtils.ingredientsToMeal;
import static com.example.luke.fyp.utilities.AppDBUtils.makeBlankIngredient;
import static com.example.luke.fyp.utilities.AppDBUtils.makeBlankMeal;
import static com.example.luke.fyp.utilities.AppDBUtils.makeIngredient;

/**
 * Created by Luke on 04/02/2018.
 *
 * Test data to check if Room implementation works
 */

public class DatabaseInitialiser {

    private static final int DELAY_MILLIS = 500;

    public static void populateSync(@NonNull final AppDatabase db){ initialiseTestData(db);}

    static void initialiseTestData(AppDatabase db) {

        Date today = getTodayPlusDays(0);
        Date yesterday = getTodayPlusDays(-1);
        db.ingredientModel().deleteAll();
        db.mealModel().deleteAll();

        long currentId = makeBlankMeal(db, 2, today);


        long ing1Id = makeBlankIngredient(db);
        Ingredient ingredient1 = makeIngredient(ing1Id, currentId, "apples", 10.0, "01001", 50.0, 0.0, 0.0, 10.0, 8.0, 0.0, 0.0);
        addIngredient(db, ingredient1);


        long ing2Id = makeBlankIngredient(db);
        Ingredient ingredient2 = makeIngredient(ing2Id, currentId, "banana", 21.0, "01001", 80.0, 3.0, 0.1, 17.0, 10.0, 0.0, 0.0);
        addIngredient(db,ingredient2);

        long ing3Id = makeBlankIngredient(db);
        Ingredient ingredient3 = makeIngredient(ing3Id, currentId, "oranges", 50.0, "01001", 70.0, 12.0, 5.0, 12.0, 0.0, 26.0, 10.0);
        addIngredient(db, ingredient3);

        List<Ingredient> mealIngredients = new ArrayList<>();
        mealIngredients.add(ingredient1);
        mealIngredients.add(ingredient2);
        mealIngredients.add(ingredient3);

        int mealType = db.mealModel().retrieveMealType(currentId);
        Date mealTime = db.mealModel().retrieveMealTime(currentId);

        Meal meal = ingredientsToMeal(mealIngredients, currentId, mealType, mealTime);
        addMeal(db, meal);

////        currentMeal = makeMeal(Long.parseLong("0"), 3, 0.0,0.0,0.0,0.0,0.0, 0.0,0.0);
////        currentId = addMeal(db, today, currentMeal);
////
////        Ingredient ingredient5 = addIngredient(db, Long.parseLong("0"), currentId, "beef", Integer.parseInt("50"), "01001", 120.0, 12.0, 5.0, 12.0, 0.0, 26.0, 10.0);
////        Ingredient ingredient4 = addIngredient(db, Long.parseLong("0"), currentId, "cheese", 15, "01001", 100.0, 9.5, 4.0, 0.5, 0.5, 5.0, 5.0);
////
////        mealIngredients.clear();
////
////        mealIngredients.add(ingredient5);
////        mealIngredients.add(ingredient4);
////
////        meal = ingredientsToMeal(mealIngredients, currentMeal.mealType, currentId);
////        addMeal(db, today, meal);
//
//
//        currentMeal = makeMeal(Long.parseLong("0"), 4, 0.0,0.0,0.0,0.0,0.0, 0.0,0.0);
//        currentId = addMeal(db, today, currentMeal);
//
//        Ingredient ingredient6 = addIngredient(db, Long.parseLong("0"), currentId, "ham", Integer.parseInt("100"), "01001", 190.0, 40.0, 12.0, 12.0, 0.0, 32.0, 10.0);
//        Ingredient ingredient7 = addIngredient(db, Long.parseLong("0"), currentId, "cabbage", 100, "01001", 80.0, 1.5, 0.0, 80.5, 0.5, 5.0, 5.0);
//
//        mealIngredients.clear();
//
//        mealIngredients.add(ingredient6);
//        mealIngredients.add(ingredient7);
//
//        meal = ingredientsToMeal(mealIngredients, currentMeal.mealType, currentId);
//        addMeal(db, today, meal);
//
//        currentMeal = makeMeal(Long.parseLong("0"), 1, 0.0,0.0,0.0,0.0,0.0, 0.0,0.0);
//        currentId = addMeal(db, today, currentMeal);
//
//        Ingredient ingredient8 = addIngredient(db, Long.parseLong("0"), currentId, "chocolate", Integer.parseInt("50"), "01001", 120.0, 12.0, 5.0, 12.0, 0.0, 26.0, 10.0);
//
//        mealIngredients.clear();
//
//        mealIngredients.add(ingredient8);
//
//        meal = ingredientsToMeal(mealIngredients, currentMeal.mealType, currentId);
//        addMeal(db, today, meal);
//
////        List<Ingredient> mealIngredients1 = new ArrayList<>();
////        mealIngredients.add(ingredient3);
////        mealIngredients.add(ingredient4);
//
////        try {
//
//
////            Meal meal = ingredientsToMeal(mealIngredients, "B");
////            Meal meal1 = ingredientsToMeal(mealIngredients1, "L");
//
////            addMeal(db, today, meal);
////            Thread.sleep(DELAY_MILLIS);
////            addMeal(db, today, meal1);
//
//
////            addMeal(db, "1", "B", today, totalCalories, totalFat, totalSats, totalCarbs, totalSugars, totalProtein, totalSodium, ingredientIds);
////            Thread.sleep(DELAY_MILLIS);
////            addMeal(db, "2", "L", today, totalCalories, totalFat, totalSats, totalCarbs, totalSugars, totalProtein, totalSodium, ingredientIds);

            Log.d("DB", "Added Meal");

//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }

    }

//    private static Meal ingredientsToMeal(List<Ingredient> mealIngredients, int mealType, long mealId) {
//
//        Double totalCalories = 0.0;
//        Double totalCarbs = 0.0;
//        Double totalSugars = 0.0;
//        Double totalFat = 0.0;
//        Double totalSats = 0.0;
//        Double totalProtein = 0.0;
//        Double totalSodium = 0.0;
//
//        for (int i = 0; i < mealIngredients.size(); i++) {
//            Ingredient ingredient = mealIngredients.get(i);
//            totalCalories = totalCalories + ingredient.calories;
//            totalCarbs = totalCarbs + ingredient.carbs;
//            totalSugars = totalSugars + ingredient.sugar;
//            totalFat = totalFat + ingredient.fat;
//            totalSats = totalSats + ingredient.saturates;
//            totalProtein = totalProtein + ingredient.protein;
//            totalSodium = totalSodium + ingredient.sodium;
//        }
//        Meal meal = makeMeal(mealId, mealType, totalCalories, totalFat, totalSats, totalCarbs, totalSugars, totalProtein, totalSodium);
//        return meal;
//    }



    private static Date getTodayPlusDays(int daysAgo) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, daysAgo);
        return calendar.getTime();
    }

//    private static Ingredient addIngredient(final AppDatabase db, final long id, final long mealId, final String name, final int weight, final String ndbno, final Double calories,
//                                            final Double fat, final Double sats, final Double carbs, final Double sugars, final Double protein, final Double sodium) {
//
//    Ingredient ingredient = new Ingredient();
//    ingredient.id = id;
//    ingredient.meal_id = mealId;
//    ingredient.name = name;
//    ingredient.ndbno = ndbno;
//    ingredient.weight = weight;
//    ingredient.calories = calories;
//    ingredient.fat = fat;
//    ingredient.saturates = sats;
//    ingredient.carbs = carbs;
//    ingredient.sugar = sugars;
//    ingredient.protein = protein;
//    ingredient.sodium = sodium;
//    db.ingredientModel().insertIngredient(ingredient);
//    return ingredient;
//    }

//    private static Meal makeMeal(final long id, final int mealType, final Double calories,
//                                 final Double fat, final Double sats, final Double carbs, final Double sugars, final Double protein, final Double sodium) {
//
//        String mealTitle = getTitleFromInt(mealType);
//
//        Meal meal = new Meal();
//        meal.id = id;
//        meal.mealType = mealType;
//        meal.mealTitle = mealTitle;
//        meal.totalCalories = calories;
//        meal.totalFat = fat;
//        meal.totalSats = sats;
//        meal.totalCarbs = carbs;
//        meal.totalSugars = sugars;
//        meal.totalProtein = protein;
//        meal.totalSodium = sodium;
//        return meal;
//    }
//
//    public static String getTitleFromInt(int mealType) {
//        String mealTitle = "";
//        if(mealType == 1){
//            mealTitle = "Breakfast";
//        } else if(mealType == 2){
//            mealTitle = "Lunch";
//        } else if(mealType == 3){
//            mealTitle = "Dinner";
//        } else if(mealType == 4){
//            mealTitle = "Snacks";
//        }
//        return mealTitle;
//    }
//
//private static Long addMeal(final AppDatabase db, Date mealTime, final Meal meal){
//
//    meal.mealTime = mealTime;
//    long newId = db.mealModel().insertMeal(meal);
//    return newId;
//}


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


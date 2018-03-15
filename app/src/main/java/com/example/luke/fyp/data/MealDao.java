package com.example.luke.fyp.data;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Transaction;
import android.arch.persistence.room.TypeConverters;

import java.util.Date;
import java.util.List;

/**
 * Created by Luke on 03/02/2018.
 *
 * Interface for accessing Meal data & MealWithIngredients Data. Work in progress
 */
@Dao
public interface MealDao {
//    @Query("SELECT * From Meal")
//    LiveData<List<Meal>> findAllMeals();

    @Query("SELECT * From Meal")
    List<Meal> findAllMeals();

    @Query("SELECT * FROM Meal")
    List<MealWithIngredients> loadMealsWithIngredients();

    @Query("SELECT * FROM Meal WHERE Meal.id = :mealId")
    Meal findMealById(long mealId);


    @TypeConverters(DateConverter.class)
    @Query("SELECT * From Meal " +
            "WHERE Meal.mealTime > :dayStart " +
            "AND Meal.mealTime < :dayEnd")
    List<Meal> findAllMealsByDay(Date dayStart, Date dayEnd);


//    @TypeConverters(DateConverter.class)
//    @Transaction @Query("SELECT * From Meal " +
//            "INNER JOIN Ingredient ON Meal.id = Ingredient.meal_id " +
//            "WHERE Meal.mealType LIKE :mealType ")
//    List<Ingredient> findAllIngredientsFromMeal(String mealType);

    @Transaction @Query("SELECT * From Meal " +
            "INNER JOIN Ingredient ON Meal.id = Ingredient.meal_id " +
            "WHERE Meal.id = :mealId ")
    List<Ingredient> findAllIngredientsFromMeal(long mealId);

    @Query("SELECT Meal.mealType From Meal " +
            "WHERE Meal.id = :mealId ")
    int retrieveMealType(long mealId);

    @TypeConverters(DateConverter.class)
    @Query("SELECT Meal.mealTime From Meal " +
            "WHERE Meal.id = :mealId ")
    Date retrieveMealTime(long mealId);

    @TypeConverters(DateConverter.class)
    @Query("SELECT * From Meal " +
            "INNER JOIN Ingredient ON Meal.id = Ingredient.meal_id " +
            "WHERE Meal.mealType LIKE :mealType " +
            "AND Meal.mealTime > :dayStart " +
            "AND Meal.mealTime < :dayEnd")
    List<Ingredient> findMealIngredientsByDayandType(String mealType, Date dayStart, Date dayEnd);

//    @Query("SELECT Meal.id, Meal.mealType as type, Meal.mealTime " +
//            "FROM Meal " +
//            "WHERE Meal.mealType LIKE :mealType " +
//            "AND Meal" +
//            ".mealTime = :day "
//    )
//    LiveData<List<Meal>> findMealByDay(String mealType, Date day);

//    @Query("SELECT * FROM Meal WHERE mealTime = :day")
//    List<Meal> findMealsByDay(Date day);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertMeal(Meal meal);

    // Insert multiple items
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long[] insert(Meal... meals);

    @Delete
    void deleteMeal(Meal meal);

    @Query("DELETE FROM Meal")
    void deleteAll();

    @Query("DELETE FROM Meal "  +
            "WHERE Meal.id = :mealId ")
    void deleteMealById(long mealId);
}

package com.example.luke.fyp.data;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.TypeConverters;

import java.util.List;

/**
 * Created by Luke on 04/02/2018.
 *
 * DAO for ingredients. Work in progress
 */

@Dao
@TypeConverters(DateConverter.class)
public interface IngredientDao {

    @Query("select * from Ingredient where id = :id")
    Ingredient loadIngredientById(long id);

    @Query("SELECT * FROM Ingredient WHERE Ingredient.meal_id LIKE :mealId")
    List<Ingredient> findIngredientsOfMeal(long mealId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertIngredient(Ingredient ingredient);


    @Query("DELETE FROM Ingredient")
    void deleteAll();

    @Query("DELETE FROM Ingredient " +
            "WHERE Ingredient.id = :ingredientId")
    void deleteIngredientById(long ingredientId);

    @Query("DELETE FROM Ingredient " +
            "WHERE Ingredient.meal_id = :mealId")
    void deleteIngredientByMealId(long mealId);
}

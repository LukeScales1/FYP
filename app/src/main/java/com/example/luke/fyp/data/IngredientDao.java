package com.example.luke.fyp.data;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.TypeConverters;
import android.arch.persistence.room.Update;

import java.util.List;

import static android.arch.persistence.room.OnConflictStrategy.IGNORE;
import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

/**
 * Created by Luke on 04/02/2018.
 *
 * DAO for saved ingredients. Work in progress
 */

@Dao
@TypeConverters(DateConverter.class)
public interface IngredientDao {

    @Query("select * from Ingredient where id = :id")
    Ingredient loadIngredientById(int id);

    @Query("SELECT * FROM Ingredient WHERE Ingredient.meal_id LIKE :mealId")
    LiveData<List<Ingredient>> findIngredientsOfMeal(long mealId);


    @Query("SELECT * FROM Ingredient")
    LiveData<List<Ingredient>> findAllIngredients();


    @Query("SELECT * FROM Ingredient")
    List<Ingredient> findAllIngredientsSync();

    @Insert(onConflict = IGNORE)
    void insertIngredient(Ingredient ingredient);

    @Update(onConflict = REPLACE)
    void updateIngredient(Ingredient ingredient);

    @Query("DELETE FROM Ingredient")
    void deleteAll();
}

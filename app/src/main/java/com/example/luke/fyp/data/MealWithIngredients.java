package com.example.luke.fyp.data;

import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Relation;

import java.util.List;

/**
 * Created by Luke on 06/02/2018.
 *
 * Used for managing relationship between Meals and Ingredients in
 * database. Work in progress
 */

public class MealWithIngredients {
    @Embedded
    public Meal meal;

    @Relation(parentColumn = "id", entityColumn = "meal_id", entity = Ingredient.class)
    public List<Ingredient> ingredients;
}

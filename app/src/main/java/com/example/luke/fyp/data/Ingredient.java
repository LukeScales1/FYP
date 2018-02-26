package com.example.luke.fyp.data;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

/**
 * Created by Luke on 03/02/2018.
 *
 * Ingredient structure for database. Work in progress
 */

@Entity
public class Ingredient {
    @PrimaryKey(autoGenerate = true)
    public long id;

    public long meal_id;

    public String name;


    public Double weight;

    public String ndbno;

    public Double calories;
    public Double fat;
    public Double saturates;
    public Double carbs;
    public Double sugar;
    public Double protein;
    public Double sodium;

//    public Double calcium;
//    public Double iron;
//    public Doubl
}

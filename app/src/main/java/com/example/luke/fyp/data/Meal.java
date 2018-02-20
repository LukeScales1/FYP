package com.example.luke.fyp.data;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;

import java.util.Date;

/**
 * Created by Luke on 02/02/2018.
 *
 * Structure for Meals in database.
 */
@Entity
@TypeConverters(DateConverter.class)
public class Meal {
    @PrimaryKey(autoGenerate = true)
    public long id;

//    i.e. breakfast (B), lunch (L), dinner (D), snack (S).
    public String mealType;

    public Date mealTime;

    public Double totalCalories;
    public Double totalCarbs;
    public Double totalSugars;
    public Double totalFat;
    public Double totalSats;
    public Double totalProtein;
    public Double totalSodium;

}

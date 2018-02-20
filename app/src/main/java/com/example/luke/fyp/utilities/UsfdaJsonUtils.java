package com.example.luke.fyp.utilities;

import com.example.luke.fyp.Food;
import com.example.luke.fyp.Nutrient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Luke on 29/09/2017.
 *
 * Utility for processing the JSON retrieved from USDA servers.
 *
 */

public final class UsfdaJsonUtils {

    public static Nutrient[] getNutrientDataFromJson(String nutrientJsonString) throws JSONException{

        final String FDA_REPORT = "report";
        final String FDA_FOOD = "food";
        final String FDA_NUTRIENTS = "nutrients";
        final String FDA_NAME = "name";
        final String FDA_UNIT = "unit";
        final String FDA_VALUE = "value";

        JSONObject nutrientJson = new JSONObject(nutrientJsonString);
        JSONObject nutrientReport = nutrientJson.getJSONObject(FDA_REPORT);
        JSONObject nutrientFood = nutrientReport.getJSONObject(FDA_FOOD);
        JSONArray nutrientArray = nutrientFood.getJSONArray(FDA_NUTRIENTS);

        Nutrient[] nutrients = new Nutrient[nutrientArray.length()];

        for (int i = 0; i < nutrientArray.length(); i++) {

            nutrients[i] = new Nutrient();

            JSONObject results = nutrientArray.getJSONObject(i);

            nutrients[i].setName(results.getString(FDA_NAME));
            nutrients[i].setUnit(results.getString(FDA_UNIT));
            nutrients[i].setValue(results.getString(FDA_VALUE));
        }
        return nutrients;
    }

    public static Food[] getFoodDataFromJson(String foodJsonString) throws JSONException{

        final String FDA_LIST = "list";
        final String FDA_ITEM = "item";
        final String FDA_GROUP = "group";
        final String FDA_NAME = "name";
        final String FDA_NDBNO = "ndbno";

        JSONObject foodJson = new JSONObject(foodJsonString);
        JSONObject foodList = foodJson.getJSONObject(FDA_LIST);
        JSONArray foodArray = foodList.getJSONArray(FDA_ITEM);

        Food[] foods = new Food[foodArray.length()];

        for (int i = 0; i < foodArray.length(); i++){

            foods[i] = new Food();

            JSONObject results = foodArray.getJSONObject(i);

            foods[i].setGroup(results.getString(FDA_GROUP));
            foods[i].setName(results.getString(FDA_NAME));
            foods[i].setNdbno(results.getString(FDA_NDBNO));
        }

        return foods;
    }

}

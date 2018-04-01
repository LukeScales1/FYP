package com.example.luke.fyp.utilities;

import com.example.luke.fyp.models.Food;
import com.example.luke.fyp.models.Nutrient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Luke on 29/09/2017.
 *
 * Utility for processing the JSON retrieved from USDA servers.
 *
 */

public final class UsdaJsonUtils {

    public static Nutrient[] getNutrientDataFromJson(String nutrientJsonString) throws JSONException{

        final String USDA_REPORT = "report";
        final String USDA_FOOD = "food";
        final String USDA_NUTRIENTS = "nutrients";
        final String USDA_NAME = "name";
        final String USDA_UNIT = "unit";
        final String USDA_VALUE = "value";

        JSONObject nutrientJson = new JSONObject(nutrientJsonString);
        JSONObject nutrientReport = nutrientJson.getJSONObject(USDA_REPORT);
        JSONObject nutrientFood = nutrientReport.getJSONObject(USDA_FOOD);
        JSONArray nutrientArray = nutrientFood.getJSONArray(USDA_NUTRIENTS);

        Nutrient[] nutrients = new Nutrient[nutrientArray.length()];

        for (int i = 0; i < nutrientArray.length(); i++) {

            nutrients[i] = new Nutrient();

            JSONObject results = nutrientArray.getJSONObject(i);

            nutrients[i].setName(results.getString(USDA_NAME));
            nutrients[i].setUnit(results.getString(USDA_UNIT));
            nutrients[i].setValue(results.getString(USDA_VALUE));
        }
        return nutrients;
    }

    public static Food[] getFoodDataFromJson(String foodJsonString) throws JSONException{

        final String USDA_LIST = "list";
        final String USDA_ITEM = "item";
        final String USDA_GROUP = "group";
        final String USDA_NAME = "name";
        final String USDA_NDBNO = "ndbno";

        JSONObject foodJson = new JSONObject(foodJsonString);
        JSONObject foodList = foodJson.getJSONObject(USDA_LIST);
        JSONArray foodArray = foodList.getJSONArray(USDA_ITEM);

        Food[] foods = new Food[foodArray.length()];

        for (int i = 0; i < foodArray.length(); i++){

            foods[i] = new Food();

            JSONObject results = foodArray.getJSONObject(i);

            foods[i].setGroup(results.getString(USDA_GROUP));
            foods[i].setName(results.getString(USDA_NAME));
            foods[i].setNdbno(results.getString(USDA_NDBNO));
        }

        return foods;
    }

}

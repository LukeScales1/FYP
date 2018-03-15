package com.example.luke.fyp.activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.example.luke.fyp.Food;
import com.example.luke.fyp.R;
import com.example.luke.fyp.adapters.SearchResultsAdapter;
import com.example.luke.fyp.utilities.NetworkUtils;
import com.example.luke.fyp.utilities.UsdaJsonUtils;

import org.json.JSONException;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.example.luke.fyp.activities.MealBuilderActivity.EXTRA_INGREDIENT_ID;
import static com.example.luke.fyp.activities.MealBuilderActivity.EXTRA_WEIGHT_CASE;

public class SearchResultsActivity extends AppCompatActivity implements SearchResultsAdapter.ItemClickListener{

    public static final String EXTRA_FOOD_DATA = "com.example.luke.fyp.FOOD_DATA";
    public static final String EXTRA_NUTRITION_DATA = "com.example.luke.fyp.NUTRITION_DATA";
    public static final String EXTRA_FOOD_NAME = "com.example.luke.fyp.FOOD_NAME";

    SearchResultsAdapter itemAdapter;
    List<Food> foodList = new ArrayList<>();
    String resultName;
    long mealId;
    int mealType;
    long ingredientId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results);
        RecyclerView itemList = findViewById(R.id.rv_search_items);
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        itemList.setLayoutManager(layoutManager);


        Intent myIntent = getIntent();
        String foodResults = myIntent.getStringExtra(EXTRA_FOOD_DATA);
        mealId = myIntent.getLongExtra(MealTypeDialogFragment.EXTRA_MEAL_ID, 0);
            if(mealId == 0){
                throw new NullPointerException("mealId must be passed");
            }
        ingredientId = myIntent.getLongExtra(EXTRA_INGREDIENT_ID, 0);
            if(ingredientId == 0){
                throw new NullPointerException("mealId must be passed with Intent");
            }

        Food[] foodData = new Food[0];
        try {
            foodData = UsdaJsonUtils.getFoodDataFromJson(foodResults);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        foodList.addAll(Arrays.asList(foodData));

        itemAdapter = new SearchResultsAdapter(foodList);
        itemAdapter.setClickListener(this);

        itemList.setAdapter(itemAdapter);
    }

    @Override
    public void onItemClick(View v, int position) {
        Food food = foodList.get(position);
        resultName = food.getName();
        URL nutrientRequestUrl = NetworkUtils.makeNdbnoUrl(food.getNdbno());
        new NutrientQueryTask().execute(nutrientRequestUrl);

    }

    public class NutrientQueryTask extends AsyncTask<URL, Void, String> {

        @Override
        protected String doInBackground(URL... params) {
            URL newSearch = params[0];
            String searchResults = null;
            try {
                searchResults = NetworkUtils.getResponseFromHttpUrl(newSearch);

            } catch (Exception e) {
                e.printStackTrace();
            }
            return searchResults;
        }

        @Override
        protected void onPostExecute(String searchResults){
            if (searchResults != null && !searchResults.equals("")){
              Intent intent = new Intent(SearchResultsActivity.this, WeightActivity.class);
                intent.putExtra(EXTRA_WEIGHT_CASE, 1);
                intent.putExtra(EXTRA_NUTRITION_DATA, searchResults);
                intent.putExtra(EXTRA_FOOD_NAME, resultName);
                intent.putExtra(MealTypeDialogFragment.EXTRA_MEAL_ID, mealId);
                intent.putExtra(EXTRA_INGREDIENT_ID, ingredientId);
                startActivity(intent);
            }
        }
    }
}

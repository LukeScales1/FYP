package com.example.luke.fyp.activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.luke.fyp.R;
import com.example.luke.fyp.data.AppDatabase;
import com.example.luke.fyp.data.Ingredient;
import com.example.luke.fyp.utilities.DatabaseInitialiser;
import com.example.luke.fyp.utilities.NetworkUtils;

import java.net.URL;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity
{

    private AppDatabase mDb;
    private TextView testTV;

    private EditText searchInput;
    private Button searchBtn;
    String search = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        testTV = findViewById(R.id.tv_test);
        mDb = AppDatabase.getInMemoryDatabase(getApplicationContext());
        populateDb();
        Intent intent = new Intent(MainActivity.this, DailyViewActivity.class);
        startActivity(intent);
//        fetchData();

//        searchInput = (EditText) findViewById(R.id.et_search);
//        searchBtn = (Button) findViewById(R.id.btn_search);
//
//        searchBtn.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v){
//                searchFoodDatabase();
//            }
//        });
    }

    private void populateDb() {
        DatabaseInitialiser.populateSync(mDb);
    }

    private void fetchData() {
        //TODO: move to adapter/improve
//        List<Meal> todaysMeals = mDb.mealModel().findAllMeals();

//        for (Meal todaysMeal : todaysMeals) {
//                testTV.append("ID: " + todaysMeal.id + "\nMeal type:" + todaysMeal.mealType + "\nTotal Calories: " + todaysMeal.totalCalories.toString() + "\n\n");
//            }
//        List<Ingredient> mealIngredients = mDb.mealModel().findAllIngredientsFromMeal("B");
        Date startDate = getStartDateWithZeroEndDateWithOne(0);
        Date endDate = getStartDateWithZeroEndDateWithOne(1);
        List<Ingredient> mealIngredients = mDb.mealModel().findMealIngredientsByDayandType("B", startDate, endDate);
        if (mealIngredients.size() > 0) {
            for (Ingredient mealIngredient : mealIngredients) {
                testTV.append("ID: " + mealIngredient.id + "\nMeal ID:" + mealIngredient.meal_id + "\nWeight:" + mealIngredient.weight + "g\nTotal Calories: " + mealIngredient.calories.toString() + "\n\n");
            }
        } else{
            testTV.setText("Didn't work");
        }
    }

    //Use only for setting search period for returning database values i.e. start & end of day to query for data
    private static Date getStartDateWithZeroEndDateWithOne(int dayOffset) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, dayOffset);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    private void searchFoodDatabase(){
        search = searchInput.getText().toString();
        URL fdaSearchUrl = NetworkUtils.makeSearchUrl(search);
        new FoodSearchTask().execute(fdaSearchUrl);
    }

    private void retrieveNutrientData(){
        search = searchInput.getText().toString();
        URL fdaSearchUrl = NetworkUtils.makeNdbnoUrl(search);
        new RetrieveNutrientTask().execute(fdaSearchUrl);
    }

    public class FoodSearchTask extends AsyncTask<URL, Void, String> {

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
//                testResultsText.setText(searchResults);
                Intent intent = new Intent(MainActivity.this, SearchResultsActivity.class);
                intent.putExtra("Data", searchResults);
                startActivity(intent);
            }
        }
    }

    public class RetrieveNutrientTask extends AsyncTask<URL, Void, String> {

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
//                testResultsText.setText(searchResults);
                Intent intent = new Intent(MainActivity.this, WeightActivity.class);
                intent.putExtra("Nutrients", searchResults);
                startActivity(intent);
            }
        }
    }
}

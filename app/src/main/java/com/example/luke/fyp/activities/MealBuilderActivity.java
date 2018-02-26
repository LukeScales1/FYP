package com.example.luke.fyp.activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.luke.fyp.R;
import com.example.luke.fyp.adapters.IngredientAdapter;
import com.example.luke.fyp.data.AppDatabase;
import com.example.luke.fyp.data.Ingredient;
import com.example.luke.fyp.data.Meal;
import com.example.luke.fyp.utilities.NetworkUtils;

import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.example.luke.fyp.activities.MealTypeDialogFragment.EXTRA_MEAL_ID;
import static com.example.luke.fyp.utilities.AppDBUtils.makeBlankIngredient;

public class MealBuilderActivity extends AppCompatActivity implements IngredientAdapter.ItemClickListener{

    public static final String EXTRA_INGREDIENT_ID = "com.example.luke.fyp.INGREDIENT_ID";
    public static final String EXTRA_INGREDIENT_NAME = "com.example.luke.fyp.INGREDIENT_NAME";
    public static final String EXTRA_WEIGHT_CASE= "com.example.luke.fyp.WEIGHT_CASE";


    private AppDatabase mDb;
    private Meal currentMeal;
    private Long currentMealId;
    private Long currentIngId;
    private Date mealTime;
    private int mealType;

    IngredientAdapter ingredientAdapter;
    List<Ingredient> ingredientList = new ArrayList<>();


    private EditText searchInput;
    String search = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meal_builder);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        RecyclerView itemList = findViewById(R.id.rv_ingredient_items);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        itemList.setLayoutManager(layoutManager);

        mDb = AppDatabase.getInMemoryDatabase(getApplicationContext());

        Intent myIntent = getIntent();
        long mealId = myIntent.getLongExtra(MealTypeDialogFragment.EXTRA_MEAL_ID,0);
            if (mealId != 0){
                loadIngredients(mealId);
                ingredientAdapter = new IngredientAdapter(ingredientList);
                ingredientAdapter.setClickListener(this);
                itemList.setAdapter(ingredientAdapter);
            }
        currentMealId = mealId;
//        mealType = myIntent.getIntExtra(EXTRA_MEAL_TYPE, 1);
//        Toast.makeText(MealBuilderActivity.this, "Meal Type = " + mealType, Toast.LENGTH_SHORT).show();

//        Calendar c = Calendar.getInstance();
//        int year = myIntent.getIntExtra(MealTypeDialogFragment.EXTRA_MEAL_YEAR, c.get(Calendar.YEAR));
//        int month = myIntent.getIntExtra(MealTypeDialogFragment.EXTRA_MEAL_MONTH, c.get(Calendar.MONTH));
//        int day = myIntent.getIntExtra(MealTypeDialogFragment.EXTRA_MEAL_DAY, c.get(Calendar.DAY_OF_MONTH));
//
//
//        mealTime = makeTimestamp(year, month, day);

//        currentMeal = makeMeal(Long.parseLong("0"), mealType, mealTime, 0.0,0.0,0.0,0.0,0.0, 0.0,0.0);
//        new MealCreateTask().execute(currentMeal);

        searchInput = findViewById(R.id.et_search);
        Button searchBtn = findViewById(R.id.btn_search);

        searchBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v){
                currentIngId = makeBlankIngredient(mDb);
                searchFoodDatabase();
            }
        });

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void onItemClick(View v, int i){
        Intent intent = new Intent(MealBuilderActivity.this, WeightActivity.class);
        intent.putExtra(EXTRA_WEIGHT_CASE, 0);
        Ingredient ingredient = ingredientList.get(i);
        intent.putExtra(EXTRA_INGREDIENT_NAME, ingredient.name);
        intent.putExtra(EXTRA_INGREDIENT_ID, ingredient.id);
        intent.putExtra(EXTRA_MEAL_ID, currentMealId);
    }

    private void loadIngredients(long mealId) {
        ingredientList = mDb.mealModel().findAllIngredientsFromMeal(mealId);
    }


//    public class LoadIngredients extends AsyncTask<Void, Void, Void>{
//
//        @Override
//        protected Void doInBackground(Void... voids) {
//            return null;
//        }
//    }
//
//    public class MealCreateTask extends AsyncTask<Meal, Void, Long>{
//
//        @Override
//        protected Long doInBackground(Meal... meals) {
//
//            mDb = AppDatabase.getInMemoryDatabase(getApplicationContext());
//            currentMealId = null;
//            try {
//                currentMealId = AppDBUtils.addMeal(mDb, mealTime,currentMeal);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//            return currentMealId;
//        }
//
//        @Override
//        protected void onPostExecute(Long mealId){
//            if (mealId != null){
//                Intent intent = new Intent(MealBuilderActivity.this, IngredientAddFragment.class);
//                intent.putExtra(MealTypeDialogFragment.EXTRA_MEAL_ID, mealId);
//                startActivity(intent);
//            }
//        }
//    }

    private void searchFoodDatabase(){
        search = searchInput.getText().toString();
        URL fdaSearchUrl = NetworkUtils.makeSearchUrl(search);
        new FoodSearchTask().execute(fdaSearchUrl);
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
                Intent intent = new Intent(MealBuilderActivity.this, SearchResultsActivity.class);
                intent.putExtra(SearchResultsActivity.EXTRA_FOOD_DATA, searchResults);
                intent.putExtra(EXTRA_INGREDIENT_ID, currentIngId);
                intent.putExtra(MealTypeDialogFragment.EXTRA_MEAL_ID, currentMealId);
                startActivity(intent);
            }
        }
    }

}

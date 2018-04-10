package com.example.luke.fyp.activities;

import android.app.AlertDialog;
import android.arch.lifecycle.LifecycleObserver;
import android.content.Context;
import android.content.DialogInterface;
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
import android.widget.ImageButton;

import com.example.luke.fyp.classifier.CameraActivity;
import com.example.luke.fyp.R;
import com.example.luke.fyp.activities.fragments.MealTypeDialogFragment;
import com.example.luke.fyp.adapters.IngredientAdapter;
import com.example.luke.fyp.data.AppDatabase;
import com.example.luke.fyp.data.Ingredient;
import com.example.luke.fyp.data.Meal;
import com.example.luke.fyp.utilities.NetworkUtils;

import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import static com.example.luke.fyp.activities.fragments.MealTypeDialogFragment.EXTRA_MEAL_DAY;
import static com.example.luke.fyp.activities.fragments.MealTypeDialogFragment.EXTRA_MEAL_ID;
import static com.example.luke.fyp.activities.fragments.MealTypeDialogFragment.EXTRA_MEAL_MONTH;
import static com.example.luke.fyp.activities.fragments.MealTypeDialogFragment.EXTRA_MEAL_TYPE;
import static com.example.luke.fyp.activities.fragments.MealTypeDialogFragment.EXTRA_MEAL_YEAR;
import static com.example.luke.fyp.utilities.AppDBUtils.addMeal;
import static com.example.luke.fyp.utilities.AppDBUtils.deleteIngredientWithId;
import static com.example.luke.fyp.utilities.AppDBUtils.deleteIngredientWithMealId;
import static com.example.luke.fyp.utilities.AppDBUtils.deleteMealWithId;
import static com.example.luke.fyp.utilities.AppDBUtils.ingredientsToMeal;
import static com.example.luke.fyp.utilities.AppDBUtils.makeBlankIngredient;
import static com.example.luke.fyp.utilities.AppDBUtils.makeBlankMeal;
import static com.example.luke.fyp.utilities.AppDBUtils.makeTimestamp;
import static com.example.luke.fyp.utilities.AppDBUtils.returnIngredientsFromMealTypeAndDay;
import static com.example.luke.fyp.utilities.AppDBUtils.returnIngredientsWithMealId;
import static com.example.luke.fyp.utilities.AppDBUtils.returnMealIdFromTypeAndDay;
import static com.example.luke.fyp.utilities.AppDBUtils.returnTimeOfMealWithId;
import static com.example.luke.fyp.utilities.AppDBUtils.returnTypeOfMealWithId;

public class MealBuilderActivity extends AppCompatActivity implements IngredientAdapter.ItemClickListener, LifecycleObserver {

    public static final String EXTRA_INGREDIENT_ID = "com.example.luke.fyp.INGREDIENT_ID";
    public static final String EXTRA_INGREDIENT_NAME = "com.example.luke.fyp.INGREDIENT_NAME";
    public static final String EXTRA_WEIGHT_CASE= "com.example.luke.fyp.WEIGHT_CASE";


    private AppDatabase mDb;
    private Meal currentMeal;
    private static Long currentMealId;
    private static Long currentIngId;
    private Date mealTime;
    private int mealType;

    static Context currentContext;

    IngredientAdapter ingredientAdapter;
    List<Ingredient> ingredientList = new ArrayList<>();

    Ingredient mIngredient;


    private EditText searchInput;
    String search = "";


    //TODO: pass back date to Dailyview so it returns to the same date

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

            long mealId = myIntent.getLongExtra(MealTypeDialogFragment.EXTRA_MEAL_ID, 0);
            if (mealId != 0) {
                loadIngredients(mealId);
                ingredientAdapter = new IngredientAdapter(ingredientList);
                ingredientAdapter.setClickListener(this);
                itemList.setAdapter(ingredientAdapter);
                currentMealId = mealId;
            } else { //dates
                mealType = myIntent.getIntExtra(EXTRA_MEAL_TYPE, 0);
                if (mealType == 0) {
                    throw new NullPointerException("Houston, we have a problem: mealType must be set for new meal creation");
                }
                int year = myIntent.getIntExtra(EXTRA_MEAL_YEAR, 0);
                int month = myIntent.getIntExtra(EXTRA_MEAL_MONTH, 13); //Jan-Dec = 0 - 11; 12 reserved for lunar calendars
                int day = myIntent.getIntExtra(EXTRA_MEAL_DAY, 0);
                if (year == 0 | month == 13 | day == 0 ) { //weightactivity
                    throw new NullPointerException("Houston, we have a problem: date must be passed");
                }
                boolean overwrite = myIntent.getBooleanExtra(MealTypeDialogFragment.EXTRA_OVERWRITE_CASE,false);
                if(overwrite){
                    mealId = overwriteIngredients(mealType, day, month, year);
                    deleteIngredientWithMealId(mDb, mealId);
//                    loadIngredients(mealId);
//                    ingredientAdapter = new IngredientAdapter(ingredientList);
//                    ingredientAdapter.setClickListener(this);
//                    itemList.setAdapter(ingredientAdapter);
                    currentMealId = mealId;
                } else {

                Date mealTime = makeTimestamp(year, month, day);
                currentMealId = makeBlankMeal(mDb, mealType, mealTime);
            }
        }

//        new MealCreateTask().execute(currentMeal);

        searchInput = findViewById(R.id.et_search);
        ImageButton searchBtn = findViewById(R.id.btn_search);

        searchBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v){
                currentIngId = makeBlankIngredient(mDb);
                search = searchInput.getText().toString();
                Context context = MealBuilderActivity.this;
                searchFoodDatabase(context, search);
            }
        });

        Button imgSearch = findViewById(R.id.btn_img_search);
        imgSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentIngId = makeBlankIngredient(mDb);
                Intent intent = new Intent(MealBuilderActivity.this, CameraActivity.class);
                startActivity(intent);
            }
        });

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Would you like to save this meal?", Snackbar.LENGTH_LONG)
                        .setAction("Save", new MyMealSaver()).show();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }


    public void thisThing(View v, Ingredient ingredient){

        String ingredientName = ingredient.name;
        final long ingredientId = ingredient.id;
        Snackbar.make(v, ingredientName + ": would you like to edit this meal?", Snackbar.LENGTH_LONG)
                .setAction("Delete", new MyDeleteIngredientListener(ingredient.id)).show();
    }

    @Override
    public void onBackPressed() {
      if(ingredientList.size() < 1){
          deleteMealWithId(mDb, currentMealId);
            Intent intent = new Intent(MealBuilderActivity.this, DailyViewActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);

        MealBuilderActivity.super.onBackPressed();
        } else {
            finalDialogLaunch(ingredientList.size());
        }
    }

    private void finalDialogLaunch(int numberOfIngs) {
        new AlertDialog.Builder(MealBuilderActivity.this)
                .setTitle("You have added " + numberOfIngs + " ingredient(s) to this meal.")
                .setMessage("Would you like to save or delete this progress?")
                .setNeutralButton("Continue Editing", null)
                .setNegativeButton("Delete Meal", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface arg0, int arg1) {
                        mDb = AppDatabase.getInMemoryDatabase(getApplicationContext());
                        deleteIngredientWithMealId(mDb, currentMealId);
                        deleteMealWithId(mDb, currentMealId);
//                        mDb.ingredientModel().deleteIngredientByMealId(currentMealId);
//                        mDb.mealModel().deleteMealById(currentMealId);
                        Intent intent = new Intent(MealBuilderActivity.this, DailyViewActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);

                    }
                })
                .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent(MealBuilderActivity.this, DailyViewActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        MealBuilderActivity.super.onBackPressed();
                    }
                })
                .create().show();
    }

    @Override
    public void onItemClick(View v, int i){
        mIngredient = ingredientList.get(i);
        currentIngId = mIngredient.id;
        new AlertDialog.Builder(this)
                .setTitle("Edit/Delete Ingredient")
                .setMessage("Would you like to edit " + mIngredient.name + "?")
                .setNeutralButton("Edit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent(MealBuilderActivity.this, WeightActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        intent.putExtra(EXTRA_WEIGHT_CASE, 0);
                        intent.putExtra(EXTRA_INGREDIENT_ID, currentIngId);
                        intent.putExtra(EXTRA_MEAL_ID, currentMealId);
                        startActivity(intent);
                    }
                })
                .setNegativeButton(android.R.string.no, null)
                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {

                    //Deletes selected ingredient from DB, iterates through ingredientList to remove from UI
                    public void onClick(DialogInterface arg0, int arg1) {
                        mDb = AppDatabase.getInMemoryDatabase(getApplicationContext());
//                        mDb.ingredientModel().deleteIngredientById(currentIngId);
                        deleteIngredientWithId(mDb, currentIngId);
                        Iterator<Ingredient> i = ingredientList.iterator();
                        while (i.hasNext()) {
                            Ingredient thisIngredient = i.next();
                            if(thisIngredient.id == currentIngId) {
                                i.remove();
                            }
                        }
                        loadIngredients(currentMealId);
                        ingredientAdapter.notifyDataSetChanged();

                        //With ingredients of meal updated, retrieves type and time of meal to update meal totals
//                        int mealType = mDb.mealModel().retrieveMealType(currentMealId);
//                        Date mealTime = mDb.mealModel().retrieveMealTime(currentMealId);
                        int mealType = returnTypeOfMealWithId(mDb,currentMealId);
                        Date mealTime = returnTimeOfMealWithId(mDb, currentMealId);

                        Meal meal = ingredientsToMeal(ingredientList, currentMealId, mealType, mealTime);
                        addMeal(mDb, meal);
                    }
                }).create().show();
    }

    private long overwriteIngredients(int mealType, int day, int month, int year) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, day);
        Date dayStart = DailyViewActivity.setDateLimits(calendar, 0);
        Date dayEnd = DailyViewActivity.setDateLimits(calendar, 1);
//        ingredientList = mDb.mealModel().findMealIngredientsByDayandType(mealType, dayStart, dayEnd);
        ingredientList = returnIngredientsFromMealTypeAndDay(mDb, mealType, dayStart, dayEnd);
//        return mDb.mealModel().findMealIdByDayandType(mealType, dayStart, dayEnd);
        return returnMealIdFromTypeAndDay(mDb, mealType, dayStart, dayEnd);
    }

    private void loadIngredients(long mealId) {
//        ingredientList = mDb.mealModel().findAllIngredientsFromMeal(mealId);
//        ingredientList = mDb.ingredientModel().findIngredientsOfMeal(mealId);
        ingredientList = returnIngredientsWithMealId(mDb, mealId);

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

    public static void searchFoodDatabase(Context context, String search){
        currentContext = context;
        URL fdaSearchUrl = NetworkUtils.makeSearchUrl(search);
        new FoodSearchTask().execute(fdaSearchUrl);
    }

    public static class FoodSearchTask extends AsyncTask<URL, Void, String> {

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
                Intent intent = new Intent(currentContext, SearchResultsActivity.class);
                intent.putExtra(SearchResultsActivity.EXTRA_FOOD_DATA, searchResults);
                intent.putExtra(EXTRA_INGREDIENT_ID, currentIngId);
                intent.putExtra(MealTypeDialogFragment.EXTRA_MEAL_ID, currentMealId);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                currentContext.startActivity(intent);
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private class MyMealSaver implements View.OnClickListener {
        @Override
        public void onClick(View view) {

            updateMeal(mDb, currentMealId);
//            int mealType = mDb.mealModel().retrieveMealType(currentMealId);
//            Date mealTime = mDb.mealModel().retrieveMealTime(currentMealId);
//
//            Meal meal = ingredientsToMeal(ingredientList, currentMealId, mealType, mealTime);
//            addMeal(mDb, meal);

            Intent intent = new Intent(MealBuilderActivity.this, DailyViewActivity.class);
            startActivity(intent);
        }
    }

    public static void updateMeal(AppDatabase db, long mealId) {
//        int mealType = db.mealModel().retrieveMealType(mealId);
//        Date mealTime = db.mealModel().retrieveMealTime(mealId);
//        List<Ingredient> ingredients = db.ingredientModel().findIngredientsOfMeal(mealId);
        int mealType = returnTypeOfMealWithId(db, mealId);
        Date mealTime = returnTimeOfMealWithId(db, mealId);
        List<Ingredient> ingredients = returnIngredientsWithMealId(db, mealId);

        Meal meal = ingredientsToMeal(ingredients, mealId, mealType, mealTime);
        addMeal(db, meal);
    }

    private class MyDeleteIngredientListener implements View.OnClickListener {
        MyDeleteIngredientListener(long id) {
            currentIngId = id;
        }

        @Override
        public void onClick(View view) {
            mDb = AppDatabase.getInMemoryDatabase(getApplicationContext());
            deleteIngredientWithId(mDb, currentIngId);
//            mDb.ingredientModel().deleteIngredientById(currentIngId);
            ingredientAdapter.notifyDataSetChanged();
        }
    }
}

package com.example.luke.fyp.activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.example.luke.fyp.R;
import com.example.luke.fyp.data.AppDatabase;
import com.example.luke.fyp.data.Meal;
import com.example.luke.fyp.utilities.AppDBUtils;

import java.util.Calendar;
import java.util.Date;

import static com.example.luke.fyp.utilities.AppDBUtils.makeMeal;
import static com.example.luke.fyp.utilities.AppDBUtils.makeTimestamp;

public class MealBuilderActivity extends AppCompatActivity {

    //TODO: change package to reflect app name
    public static final String EXTRA_MEAL_ID = "com.example.luke.fyp.MEAL_ID";
    private AppDatabase mDb;
    private Meal currentMeal;
    private Long currentId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meal_builder);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent myIntent = getIntent();
        String mealType = myIntent.getStringExtra(MealTypeDialogFragment.EXTRA_MEAL_TYPE);
        Calendar c = Calendar.getInstance();
        int year = myIntent.getIntExtra(MealTypeDialogFragment.EXTRA_MEAL_YEAR, c.get(Calendar.YEAR));
        int month = myIntent.getIntExtra(MealTypeDialogFragment.EXTRA_MEAL_MONTH, c.get(Calendar.MONTH));
        int day = myIntent.getIntExtra(MealTypeDialogFragment.EXTRA_MEAL_DAY, c.get(Calendar.DAY_OF_MONTH));


        Date mealTime = makeTimestamp(year, month, day);

        currentMeal = makeMeal(Long.parseLong("0"), mealType, mealTime, 0.0,0.0,0.0,0.0,0.0, 0.0,0.0);
        new MealCreateTask().execute(currentMeal);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public class LoadIngredients extends AsyncTask<Void, Void, Void>{

        @Override
        protected Void doInBackground(Void... voids) {
            return null;
        }
    }

    public class MealCreateTask extends AsyncTask<Meal, Void, Long>{

        @Override
        protected Long doInBackground(Meal... meals) {

            mDb = AppDatabase.getInMemoryDatabase(getApplicationContext());
            currentId = null;
            try {
                currentId = AppDBUtils.addMeal(mDb, currentMeal);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return currentId;
        }

        @Override
        protected void onPostExecute(Long mealId){
            if (mealId != null){
                Intent intent = new Intent(MealBuilderActivity.this, IngredientAddFragment.class);
                intent.putExtra(EXTRA_MEAL_ID, mealId);
                startActivity(intent);
            }
        }
    }

}

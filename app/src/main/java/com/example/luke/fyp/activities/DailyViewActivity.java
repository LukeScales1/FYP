package com.example.luke.fyp.activities;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.luke.fyp.R;
import com.example.luke.fyp.data.AppDatabase;
import com.example.luke.fyp.data.Ingredient;
import com.example.luke.fyp.utilities.DatabaseInitialiser;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class DailyViewActivity extends AppCompatActivity implements MealTypeDialogFragment.Listener{

    TextView dayTitle;
    TextView testTV;
    int currentYear;
    int currentMonth;
    int currentDay;
    Date startDate;
    Date endDate;
    String dayOfWeek;
    String monthName;

    int changeCount;

    ImageButton previousDayBtn;
    ImageButton nextDayBtn;

    private AppDatabase mDb;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_view);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Calendar calendar = Calendar.getInstance();
        currentYear = calendar.get(Calendar.YEAR);
        currentMonth = calendar.get(Calendar.MONTH);
        currentDay = calendar.get(Calendar.DAY_OF_MONTH);
        int weekDay = calendar.get(Calendar.DAY_OF_WEEK);

        startDate = setDateLimits(calendar, 0);
        endDate = setDateLimits(calendar, 1);

//        final AppDatabase db;
        mDb = AppDatabase.getInMemoryDatabase(getApplicationContext());
//        new FetchMealsTask().execute();

        dayOfWeek = getDayName(weekDay);
        monthName = getMonthName(currentMonth);

        dayTitle = (TextView) findViewById(R.id.tv_day_title);
        //TODO: remove concatenation
        dayTitle.setText(dayOfWeek + ", " + currentDay + " " + monthName + " " + currentYear);

        testTV = (TextView) findViewById(R.id.tv_testicle);
        testTV.setText("");

        populateDb();
        fetchData(startDate, endDate);

        changeCount = 1;

        previousDayBtn = (ImageButton) findViewById(R.id.left_btn);
        nextDayBtn = (ImageButton) findViewById(R.id.right_btn);
        previousDayBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                changeDay(-1);
                changeCount--;
//                checkChangeCount(changeCount);
                }
            });
        nextDayBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeDay(1);
                changeCount++;
//                checkChangeCount(changeCount);
            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MealTypeDialogFragment.newInstance().show(getSupportFragmentManager(), "dialog");

            }
        });
    }

//    private class FetchMealsTask extends AsyncTask<Void, Void, Void>{
//
//        private final AppDatabase mDb = AppDatabase.getInMemoryDatabase(getApplicationContext());
//
////        FetchMealsTask(AppDatabase db) {
////            mDb = db;
////        }
//
//        @Override
//        protected Void doInBackground(Void... voids) {
////            mDb = AppDatabase.getInMemoryDatabase(getApplicationContext());
////            populateDb();
////            fetchData(startDate, endDate);
//            initialiseTestData(mDb);
//            fetchData(mDb, startDate, endDate);
//            return null;
//        }

//        @Override
//        protected void onPostExecute(String searchResults){
//            if (searchResults != null && !searchResults.equals("")){
////                testResultsText.setText(searchResults);
//                Intent intent = new Intent(MainActivity.this, WeightActivity.class);
//                intent.putExtra("Nutrients", searchResults);
//                startActivity(intent);
//            }
//        }
//    }
//    private void checkChangeCount(int i){
//        if(i < -4 | i > 4){
//            int x;
//            if(i < -4){
//                x = -1;
//            } else {
//                x = 1;
//            }
//            DialogFragment datePicker = new DatePickerFragment();
//            datePicker.show(getFragmentManager(),"Date Picker");
//            zeroChangeCount(x);
//        }
//    }

    public void zeroChangeCount(int diff){
        changeCount = diff;
    }

    public void changeDay(int offset){
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, currentYear);
        calendar.set(Calendar.MONTH, currentMonth);
        calendar.set(Calendar.DAY_OF_MONTH, currentDay);
        calendar.add(Calendar.DATE, offset);
        currentYear = calendar.get(Calendar.YEAR);
        currentMonth = calendar.get(Calendar.MONTH);
        currentDay = calendar.get(Calendar.DAY_OF_MONTH);
        int weekDay = calendar.get(Calendar.DAY_OF_WEEK);

        startDate = setDateLimits(calendar, 0);
        endDate = setDateLimits(calendar, 1);

        dayOfWeek = getDayName(weekDay);
        monthName = getMonthName(currentMonth);

        dayTitle = (TextView) findViewById(R.id.tv_day_title);
        //TODO: remove concatenation
        dayTitle.setText(dayOfWeek + ", " + currentDay + " " + monthName + " " + currentYear);

        fetchData(startDate, endDate);
    }

    private void populateDb() {
        DatabaseInitialiser.populateSync(mDb);
    }

    public void fetchData(Date start, Date end) {
        // remove from main thread
//        testTV.setText("this");
        List<Ingredient> mealIngredients = mDb.mealModel().findMealIngredientsByDayandType("B",start, end);
//        List<Ingredient> mealIngredients = fetchIngredientsOfMeal(start, end);
//        List<Ingredient> mealIngredients = mDb.mealModel().findMealIngredientsByDayandType("B", start, end);
        if (mealIngredients.size() > 0) {
            for (Ingredient mealIngredient : mealIngredients) {
                testTV.append("ID: " + mealIngredient.id + "\nMeal ID:" + mealIngredient.meal_id + "\nWeight:" + mealIngredient.weight + "g\nTotal Calories: " + mealIngredient.calories.toString() + "\n\n");

            }
        } else {
            testTV.setText(getString(R.string.daily_view_no_meals));
        }
    }

    //Use only for setting search period for returning database values i.e. start & end of day to query for data
    public Date setDateLimits(Calendar calendar, int dayOffset) {
//        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, dayOffset);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    public int getCurrentYear(){
        return currentYear;
    }

    public int getCurrentMonth(){
        return currentMonth;
    }

    public int getCurrentDay(){
        return currentDay;
    }

    public void setDayStorage(int year, int month, int day){
        currentYear = year;
        currentMonth = month;
        currentDay = day;
    }

    public String getDayName(int dayInt){
        String dayName = "";
        if(dayInt == 1){
            dayName = "Sun";
        } else if(dayInt == 2){
            dayName = "Mon";
        } else if(dayInt == 3){
            dayName = "Tues";
        }else if(dayInt == 4){
            dayName = "Wed";
        } else if(dayInt == 5){
            dayName = "Thurs";
        } else if(dayInt == 6){
            dayName = "Fri";
        }else if(dayInt == 7){
            dayName = "Sat";
        }
        return dayName;
    }

    public String getMonthName(int monthInt){
        String monthName = "";
        if(monthInt == 0){
            monthName = "Jan";
        } else if(monthInt == 1){
            monthName = "Feb";
        } else if(monthInt == 2){
            monthName = "Mar";
        } else if(monthInt == 3){
            monthName = "Apr";
        } else if(monthInt == 4){
            monthName = "May";
        } else if(monthInt == 5){
            monthName = "Jun";
        } else if(monthInt == 6){
            monthName = "Jul";
        } else if(monthInt == 7){
            monthName = "Aug";
        } else if(monthInt == 8){
            monthName = "Sep";
        } else if(monthInt == 9){
            monthName = "Oct";
        } else if(monthInt == 10){
            monthName = "Nov";
        } else if(monthInt == 11) {
            monthName = "Dec";
        }
        return monthName;
    }

    @Override
    public void onItemClicked(int position) {

    }
}

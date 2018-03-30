package com.example.luke.fyp.activities;

import android.app.AlertDialog;
import android.app.DialogFragment;
import android.arch.lifecycle.LifecycleOwner;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.example.luke.fyp.R;
import com.example.luke.fyp.activities.fragments.DatePickerFragment;
import com.example.luke.fyp.activities.fragments.MealTypeDialogFragment;
import com.example.luke.fyp.adapters.MealAdapter;
import com.example.luke.fyp.data.AppDatabase;
import com.example.luke.fyp.data.Meal;

import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static com.example.luke.fyp.activities.fragments.MealTypeDialogFragment.EXTRA_MEAL_ID;

public class DailyViewActivity extends AppCompatActivity implements MealTypeDialogFragment.Listener, LifecycleOwner{

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

    ListView mealList;

    static Boolean B = false;
    static Boolean L = false;
    static Boolean D = false;
    static Boolean S = false;

    @Override
    public void onResume() {
        super.onResume();
        loadMeals();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_view);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mealList = findViewById(R.id.meal_list);
        dayTitle = findViewById(R.id.tv_day_title);

        loadMeals();

        previousDayBtn = findViewById(R.id.btn_left);
        nextDayBtn = findViewById(R.id.btn_right);
        previousDayBtn.setOnClickListener(v -> {
            changeDay(-1);
            changeCount--;
            checkChangeCount(changeCount);
            });
        nextDayBtn.setOnClickListener(view -> {
            changeDay(1);
            changeCount++;
            checkChangeCount(changeCount);
        });

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> MealTypeDialogFragment.newInstance().show(getSupportFragmentManager(), "dialog"));
    }

    private void loadMeals() {
        Calendar calendar = Calendar.getInstance();
        currentYear = calendar.get(Calendar.YEAR);
        currentMonth = calendar.get(Calendar.MONTH);
        currentDay = calendar.get(Calendar.DAY_OF_MONTH);
        int weekDay = calendar.get(Calendar.DAY_OF_WEEK);

        startDate = setDateLimits(calendar, 0);
        endDate = setDateLimits(calendar, 1);

        //TODO: remove from main thread
        mDb = AppDatabase.getInMemoryDatabase(getApplicationContext());


        dayOfWeek = getDayName(weekDay);
        monthName = getMonthName(currentMonth);

        String thisString = dayOfWeek + ", " + currentDay + " " + monthName + " " + currentYear;
        dayTitle.setText(thisString);

        fetchData(startDate, endDate);

        changeCount = 1;
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
    private void checkChangeCount(int i){
        if(i < -4 | i > 4){
            int x;
            if(i < -4){
                x = -1;
            } else {
                x = 1;
            }
            DialogFragment datePicker = new DatePickerFragment();
            datePicker.show(getFragmentManager(),"Date Picker");
            zeroChangeCount(x);
        }
    }

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

        dayOfWeek = getDayName(weekDay);
        monthName = getMonthName(currentMonth);

        String thisString = dayOfWeek + ", " + currentDay + " " + monthName + " " + currentYear;
        dayTitle.setText(thisString);

        startDate = setDateLimits(calendar, 0);
        endDate = setDateLimits(calendar, 1);

        fetchData(startDate, endDate);
    }


    //TODO: remove from main thread
    public void fetchData(Date start, Date end) {
        final List<Meal> meals = mDb.mealModel().findAllMealsByDay(start, end);
        Collections.sort(meals);
        checkMeals(meals);

        MealAdapter mealAdapter = new MealAdapter(DailyViewActivity.this, meals);
        mealList.setAdapter(mealAdapter);
        mealList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Meal meal = meals.get(i);
                String mealTitle = meal.mealTitle;
                Snackbar.make(view, mealTitle + ": would you like to edit this meal?", Snackbar.LENGTH_LONG)
                        .setAction("Edit", new EditMealListener(meal.id)).show();
            }
        });
        mealList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                Meal meal = meals.get(i);
                String mealTitle = meal.mealTitle;
                Snackbar.make(view, mealTitle + ": would you like to delete this meal?", Snackbar.LENGTH_LONG)
                        .setAction("Delete", new DeleteMealListener(meal.id)).show();
                return true;
            }
        });
    }

    private class EditMealListener implements View.OnClickListener{

        long id;

        EditMealListener(long mealId) {
            this.id = mealId;
        }

        @Override
        public void onClick(View v) {
            int year = getCurrentYear();
            int month = getCurrentMonth();
            int day = getCurrentDay();
            Intent intent = new Intent(DailyViewActivity.this, MealBuilderActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.putExtra(EXTRA_MEAL_ID, id);
//            intent.putExtra(EXTRA_MEAL_TYPE, type);
//            intent.putExtra(EXTRA_MEAL_YEAR, year);
//            intent.putExtra(EXTRA_MEAL_MONTH, month);
//            intent.putExtra(EXTRA_MEAL_DAY, day);
            startActivity(intent);

        }
    }

    private class DeleteMealListener implements View.OnClickListener {

        long id;

        DeleteMealListener(long mealId) {
            this.id = mealId;
        }

        @Override
        public void onClick(View view) {
            new AlertDialog.Builder(DailyViewActivity.this)
                    .setTitle("Delete Meal")
                    .setMessage("Are you sure you want to delete this meal?")
                    .setNegativeButton("Cancel", null)
                    .setPositiveButton("Delete", new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface arg0, int arg1) {
                            mDb.mealModel().deleteMealById(id);
                            fetchData(startDate, endDate);

                        }
                    }).create().show();
        }
    }

    private void checkMeals(List<Meal> meals) {
        for (Meal meal : meals) {
            int type = meal.mealType;
            if (type == 1) {
                B = true;
            } else if (type == 2) {
                L = true;
            } else if (type == 3) {
                D = true;
            } else if (type == 4) {
                S = true;
            }
        }
    }

    //Boolean state checks
    public static Boolean checkB(){
        return B;
    }
    public static Boolean checkL(){
        return L;
    }
    public static Boolean checkD(){
        return D;
    }
    public static Boolean checkS(){
        return S;
    }

    //Use only for setting search period for returning database values i.e. start & end of day to query for data
    public static Date setDateLimits(Calendar calendar, int dayOffset) {
        calendar.add(Calendar.DATE, dayOffset);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
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

    //getters & setters
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


}

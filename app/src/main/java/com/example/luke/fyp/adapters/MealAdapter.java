package com.example.luke.fyp.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.luke.fyp.R;
import com.example.luke.fyp.data.Meal;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

/**
 * Created by Luke on 15/02/2018.
 *
 * Data adapter for dailyview activity
 */

public class MealAdapter extends ArrayAdapter<Meal> {

    public MealAdapter(Context context, List<Meal> meals){super(context, 0, meals);}



    @NonNull
    @Override
    public View getView(int position, View view, @NonNull ViewGroup parent) {
//        LayoutInflater inflater = context.getLayoutInflater();
//        View mealView = inflater.inflate(R.layout.meal_item, parent, true);
//        Meal meal = mealList.get(position);
        Meal meal = getItem(position);
        if(view == null){
            view = LayoutInflater.from(getContext()).inflate(R.layout.meal_item, parent, false);
        }


        TextView tvTitle = view.findViewById(R.id.meal_title);
        TextView tvCalTotal = view.findViewById(R.id.meal_cal_total);
        TextView tvFatTotal = view.findViewById(R.id.meal_fat_total);
        TextView tvSatTotal = view.findViewById(R.id.meal_sat_total);
        TextView tvProteinTotal = view.findViewById(R.id.meal_protein_total);
        TextView tvCarbTotal = view.findViewById(R.id.meal_carb_total);
        TextView tvSugarTotal = view.findViewById(R.id.meal_sugar_total);

        if(meal != null){

        tvTitle.setText(meal.mealTitle);
        String mString = roundToString(meal.totalCalories);
        tvCalTotal.setText(mString);
            mString = roundToString(meal.totalFat);
        tvFatTotal.setText(mString);
            mString = roundToString(meal.totalSats);
        tvSatTotal.setText(mString);
            mString = roundToString(meal.totalProtein);
        tvProteinTotal.setText(mString);
            mString = roundToString(meal.totalCarbs);
        tvCarbTotal.setText(mString);
            mString = roundToString(meal.totalSugars);
        tvSugarTotal.setText(mString);
        }
        return view;

    }

    private String roundToString(Double mDouble){
        Double thisDouble = 0.0;
        if(mDouble != null) {
            thisDouble = new BigDecimal(mDouble).setScale(1, RoundingMode.HALF_UP).doubleValue();
        }
        return thisDouble.toString();
    }
//    public String getTitleFromInt(int mealType) {
//        String mealTitle = "";
//        if(mealType == 1){
//            mealTitle = "Breakfast";
//        } else if(mealType == 2){
//            mealTitle = "Lunch";
//        } else if(mealType == 3){
//            mealTitle = "Dinner";
//        } else if(mealType == 4){
//            mealTitle = "Snacks";
//        }
//        return mealTitle;
//    }

//    public MealAdapter(List<Meal> meals){this.mealList = meals;}

//    public MealAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        View itemView = LayoutInflater.from(parent.getContext())
//                .inflate(R.layout.activity_daily_view, parent, false);
//        return new MealAdapter.ViewHolder(itemView);
//    }
//
////    @Override
//    public void onBindViewHolder(MealAdapter.ViewHolder holder, int position) {
//        Meal meal = mealList.get(position);
//        String mealType = meal.mealType;
//        Double mealTotal = meal.totalCalories;
//
//        holder.mealTypeTV.setText(mealType);
//        holder.mealTotalTV.setText(Double.toString(mealTotal));
//    }
//
//    @Override
//    public int getCount() {
//        return 0;
//    }
//
//    @Override
//    public Object getItem(int i) {
//        return null;
//    }
//
//    @Override
//    public long getItemId(int i) {
//        return 0;
//    }
//
//    @Override
//    public View getView(int i, View view, ViewGroup viewGroup) {
//        return null;
//    }
//
//    public class ViewHolder extends RecyclerView.ViewHolder {
//        public TextView mealTypeTV;
//        public TextView mealTotalTV;
//        public ViewHolder(View itemView) {
//            super(itemView);
//            mealTypeTV = (TextView) itemView.findViewById(R.id.tv_nutrient_value);
//            mealTotalTV = (TextView) itemView.findViewById(R.id.tv_nutrient_unit);
//        }
//    }

}

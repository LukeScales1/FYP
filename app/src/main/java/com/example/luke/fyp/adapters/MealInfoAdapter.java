package com.example.luke.fyp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.luke.fyp.R;
import com.example.luke.fyp.data.Meal;

import java.util.List;

/**
 * Created by Luke on 15/02/2018.
 *
 * Data adapter for dailyview activity
 */

public class MealInfoAdapter extends ArrayAdapter<Meal> {

    public MealInfoAdapter(Context context, List<Meal> meals){super(context, 0, meals);}



    @Override
    public View getView(int position, View view, ViewGroup parent) {
//        LayoutInflater inflater = context.getLayoutInflater();
//        View mealView = inflater.inflate(R.layout.meal_item, parent, true);
//        Meal meal = mealList.get(position);
        Meal meal = getItem(position);
        if(view == null){
            view = LayoutInflater.from(getContext()).inflate(R.layout.meal_item, parent, false);
        }
            String mealTitle = getTitleFromChar(meal.mealType);
        TextView tvTitle = (TextView) view.findViewById(R.id.meal_title);
        tvTitle.setText(mealTitle);

        TextView tvCalTotal = (TextView) view.findViewById(R.id.meal_cal_total);
        tvCalTotal.setText(meal.totalCalories.toString());

        TextView tvFatTotal = (TextView) view.findViewById(R.id.meal_fat_total);
        tvFatTotal.setText(meal.totalFat.toString());

        TextView tvSatTotal = (TextView) view.findViewById(R.id.meal_sat_total);
        tvSatTotal.setText(meal.totalSats.toString());

        TextView tvProteinTotal = (TextView) view.findViewById(R.id.meal_protein_total);
        tvProteinTotal.setText(meal.totalProtein.toString());

        TextView tvCarbTotal = (TextView) view.findViewById(R.id.meal_carb_total);
        tvCarbTotal.setText(meal.totalCarbs.toString());

        TextView tvSugarTotal = (TextView) view.findViewById(R.id.meal_sugar_total);
        tvSugarTotal.setText(meal.totalSugars.toString());
        return view;
    }

    private String getTitleFromChar(int mealType) {
        String mealTitle = "";
        if(mealType == 1){
            mealTitle = "Breakfast";
        } else if(mealType == 2){
            mealTitle = "Lunch";
        } else if(mealType == 3){
            mealTitle = "Dinner";
        } else if(mealType == 4){
            mealTitle = "Snacks";
        }
        return mealTitle;
    }

//    public MealInfoAdapter(List<Meal> meals){this.mealList = meals;}

//    public MealInfoAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        View itemView = LayoutInflater.from(parent.getContext())
//                .inflate(R.layout.activity_daily_view, parent, false);
//        return new MealInfoAdapter.ViewHolder(itemView);
//    }
//
////    @Override
//    public void onBindViewHolder(MealInfoAdapter.ViewHolder holder, int position) {
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

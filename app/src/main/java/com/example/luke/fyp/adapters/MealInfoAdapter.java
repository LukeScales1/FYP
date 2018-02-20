package com.example.luke.fyp.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.luke.fyp.R;
import com.example.luke.fyp.data.Meal;

import java.util.List;

/**
 * Created by Luke on 15/02/2018.
 *
 * Data adapter for dailyview activity
 */

public class MealInfoAdapter extends BaseAdapter{

    private List<Meal> mealList;


    public MealInfoAdapter(List<Meal> meals){this.mealList = meals;}

    public MealInfoAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activity_daily_view, parent, false);
        return new MealInfoAdapter.ViewHolder(itemView);
    }

//    @Override
    public void onBindViewHolder(MealInfoAdapter.ViewHolder holder, int position) {
        Meal meal = mealList.get(position);
        String mealType = meal.mealType;
        Double mealTotal = meal.totalCalories;

        holder.mealTypeTV.setText(mealType);
        holder.mealTotalTV.setText(Double.toString(mealTotal));
    }

    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        return null;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView mealTypeTV;
        public TextView mealTotalTV;
        public ViewHolder(View itemView) {
            super(itemView);
           //TODO: assign to correct xml
            mealTypeTV = (TextView) itemView.findViewById(R.id.tv_nutrient_value);
            mealTotalTV = (TextView) itemView.findViewById(R.id.tv_nutrient_unit);
        }
    }

}

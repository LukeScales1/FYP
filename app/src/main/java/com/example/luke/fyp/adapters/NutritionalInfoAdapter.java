package com.example.luke.fyp.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.luke.fyp.models.Nutrient;
import com.example.luke.fyp.R;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

/**
 * Created by Luke on 25/10/2017.
 *
 * Adapter for applying nutritonal info to textview of WeightActivity. Work in Progress
 */

public class NutritionalInfoAdapter extends RecyclerView.Adapter<NutritionalInfoAdapter.ViewHolder>{

    private List<Nutrient> nutrientList;
    private String nutrientName;
    private String nutrientVal;
    private String nutrientUnit;
    private Double foodWeight;
    private Double dynamicVal;

    public NutritionalInfoAdapter(List<Nutrient> nutrients, Double weight){this.nutrientList = nutrients; this.foodWeight = weight;}
//    public NutritionalInfoAdapter(Integer weight){this.foodWeight = weight;}


    public NutritionalInfoAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.nutrient_item, parent, false);
        return new NutritionalInfoAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Nutrient nutrient = nutrientList.get(position);
        Double multiplier = foodWeight/100;
        nutrientName = nutrient.getName();
        nutrientVal = nutrient.getValue();
        nutrientUnit = nutrient.getUnit();

        Double quantityPer100g = Double.parseDouble(nutrientVal);
        dynamicVal = quantityPer100g * multiplier;

        holder.nutrientNameTV.setText(nutrientName);
        double testvar = new BigDecimal(dynamicVal).setScale(1, RoundingMode.HALF_UP).doubleValue();
       String val =  Double.toString(testvar);
        holder.nutrientValTV.setText(val);
        holder.nutrientUnitTV.setText(nutrientUnit);
    }

    @Override
    public int getItemCount() {
        return nutrientList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView nutrientNameTV;
        TextView nutrientValTV;
        TextView nutrientUnitTV;
        ViewHolder(View itemView) {
            super(itemView);
            nutrientNameTV = itemView.findViewById(R.id.tv_nutrient_name);
            nutrientValTV = itemView.findViewById(R.id.tv_nutrient_value);
            nutrientUnitTV = itemView.findViewById(R.id.tv_nutrient_unit);
        }
    }
}

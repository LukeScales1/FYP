package com.example.luke.fyp.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.luke.fyp.Nutrient;
import com.example.luke.fyp.R;

import java.util.List;

/**
 * Created by Luke on 25/10/2017.
 */

public class NutritionalInfoAdapter extends RecyclerView.Adapter<NutritionalInfoAdapter.ViewHolder>{

    private List<Nutrient> nutrientList;
    private String nutrientName;
    private String nutrientVal;
    private String nutrientUnit;
    private Integer foodWeight;
    private Integer dynamicVal;

    public NutritionalInfoAdapter(List<Nutrient> nutrients){this.nutrientList = nutrients;}
    public NutritionalInfoAdapter(Integer weight){this.foodWeight = weight;}


    public NutritionalInfoAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.search_results_item, parent, false);
        return new NutritionalInfoAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Nutrient nutrient = nutrientList.get(position);
        Integer weight = foodWeight;
        nutrientName = nutrient.getName();
        nutrientVal = nutrient.getValue();
        nutrientUnit = nutrient.getUnit();

        Integer quantityPer100g = Integer.parseInt(nutrientVal);
        dynamicVal = quantityPer100g * weight;

        holder.nutrientNameTV.setText(nutrientName);
        holder.nutrientValTV.setText(Integer.toString(dynamicVal));
        holder.nutrientUnitTV.setText(nutrientUnit);
    }

    @Override
    public int getItemCount() {
        return nutrientList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView nutrientNameTV;
        public TextView nutrientValTV;
        public TextView nutrientUnitTV;
        public ViewHolder(View itemView) {
            super(itemView);
            nutrientNameTV = (TextView) itemView.findViewById(R.id.tv_nutrient_name);
            nutrientValTV = (TextView) itemView.findViewById(R.id.tv_nutrient_value);
            nutrientUnitTV = (TextView) itemView.findViewById(R.id.tv_nutrient_unit);
        }
    }
}

package com.example.luke.fyp.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import com.example.luke.fyp.R;
import com.example.luke.fyp.data.Ingredient;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

/**
 * Created by Luke on 24/02/2018.
 *
 * Used by MealBuilderAdapter to display current ingredients of meals
 */

public class IngredientAdapter extends RecyclerView.Adapter<IngredientAdapter.ViewHolder>  {

    private List<Ingredient> ingredientList;

    private ItemClickListener itemClickListener;
    private AdapterView.OnItemLongClickListener itemLongClickListener;

    public IngredientAdapter(List<Ingredient> ingredients){this.ingredientList = ingredients;}

    @Override
    public IngredientAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.ingredient_item, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(IngredientAdapter.ViewHolder holder, int position) {

        Ingredient ingredient = ingredientList.get(position);

        Double ingMultiplier = ingredient.weight/100.0;

        String name = ingredient.name;
        String grams = "g";
        String mgrams = "mg";
        String kcal = "kcal";

        String weight = String.valueOf(ingredient.weight) + grams;
        String cals = String.valueOf(roundThis(ingredient.calories, ingMultiplier)) + kcal;
        String fat = String.valueOf(roundThis(ingredient.fat, ingMultiplier)) + grams;
        String sats = String.valueOf(roundThis(ingredient.saturates, ingMultiplier)) + grams;
        String  protein = String.valueOf(roundThis(ingredient.protein, ingMultiplier)) + grams;
        String  carbs = String.valueOf(roundThis(ingredient.carbs, ingMultiplier)) + grams;
        String sugars = String.valueOf(roundThis(ingredient.sugar, ingMultiplier)) + grams;
        String sodium = "";

        Double thisDouble = roundThis(ingredient.sodium, ingMultiplier);
            if(thisDouble < 1000.0){
                sodium = String.valueOf(thisDouble) + mgrams;
            } else{
                thisDouble = thisDouble/1000.0;
                sodium = String.valueOf(thisDouble) + grams;
            }


        holder.nameTV.setText(name);
        holder.weightTV.setText(weight);
        holder.calsTV.setText(cals);
        holder.fatTV.setText(fat);
        holder.satTV.setText(sats);
        holder.proTV.setText(protein);
        holder.sodTV.setText(sodium);
        holder.carbTV.setText(carbs);
        holder.sugTV.setText(sugars);

    }

    private static Double roundThis(Double mDouble1, Double mDouble2){
        Double mDouble = mDouble1 * mDouble2;
        return new BigDecimal(mDouble).setScale(1, RoundingMode.HALF_UP).doubleValue();
    }

    @Override
    public int getItemCount() {
        return ingredientList.size();
    }

    public void setClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

//    @Override
//    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
//        return false;
//    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView nameTV;
        TextView weightTV;
        TextView calsTV;
        TextView fatTV;
        TextView satTV;
        TextView proTV;
        TextView carbTV;
        TextView sugTV;
        TextView sodTV;
        ViewHolder(View itemView) {
            super(itemView);
            nameTV = itemView.findViewById(R.id.tv_ingredient_name);
            weightTV = itemView.findViewById(R.id.tv_ingredient_weight);
            calsTV = itemView.findViewById(R.id.tv_ingredient_cals);
            fatTV = itemView.findViewById(R.id.ing_fat_total);
            satTV = itemView.findViewById(R.id.ing_sat_total);
            proTV = itemView.findViewById(R.id.ing_pro_total);
            sodTV = itemView.findViewById(R.id.ing_sod_total);
            carbTV = itemView.findViewById(R.id.ing_carb_total);
            sugTV = itemView.findViewById(R.id.ing_sug_total);

            itemView.setOnClickListener(this);
//            itemView.setOnLongClickListener(new View.OnLongClickListener()
//            {
//                @Override
//                public boolean onLongClick(View v) {
//
//                    int i = getAdapterPosition();
//                    Ingredient mIngredient = ingredientList.get(i);
////                    Toast.makeText(v.getContext(), "Long Click" + i,Toast.LENGTH_SHORT).show();
//                    MealBuilderActivity mealBuilderActivity = new MealBuilderActivity();
//                    mealBuilderActivity.thisThing(v, mIngredient);
//                    return true;
//
//                }
//            })
        }

        @Override
        public void onClick(View view) {
            if(itemClickListener != null) itemClickListener.onItemClick(view, getAdapterPosition());
        }
    }


    public interface ItemClickListener {
        void onItemClick(View view, int adapterPosition);
    }
}

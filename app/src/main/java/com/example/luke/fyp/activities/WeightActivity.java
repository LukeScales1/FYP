package com.example.luke.fyp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.luke.fyp.Nutrient;
import com.example.luke.fyp.R;
import com.example.luke.fyp.adapters.NutritionalInfoAdapter;
import com.example.luke.fyp.data.AppDatabase;
import com.example.luke.fyp.data.Ingredient;
import com.example.luke.fyp.utilities.UsdaJsonUtils;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.example.luke.fyp.activities.MealBuilderActivity.EXTRA_INGREDIENT_ID;
import static com.example.luke.fyp.activities.MealBuilderActivity.EXTRA_WEIGHT_CASE;
import static com.example.luke.fyp.activities.MealTypeDialogFragment.EXTRA_MEAL_ID;
import static com.example.luke.fyp.utilities.AppDBUtils.addIngredient;
import static com.example.luke.fyp.utilities.AppDBUtils.makeIngredient;

public class WeightActivity extends AppCompatActivity {

    NutritionalInfoAdapter infoAdapter;
    List<Nutrient> nutrientList = new ArrayList<>();

    Double weight;

    TextView nameTitle;
    TextView nameIn;
    TextView weightTitle;
    EditText weightIn;

    String Calories;
    String Protein;
    String Fat;
    String Sats;
    String Carbs;
    String Sugar;
    String Sodium;
    Double CalVal;
    Double ProVal;
    Double FatVal;
//    Double FibVal;
    Double SatVal;
    Double CarbVal;
    Double SugVal;
    Double SodVal;


    long mealId;
    long ingredientId;
    String itemName;

    private AppDatabase mDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weight);
        final RecyclerView itemList = findViewById(R.id.rv_nutrient_items);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        itemList.setLayoutManager(layoutManager);

//        nameTitle = (TextView) findViewById(R.id.tv_title);
        nameIn = findViewById(R.id.tv_item_name);
        weightTitle= findViewById(R.id.tv_weight);

        weightIn = findViewById(R.id.et_weight);

        mDb = AppDatabase.getInMemoryDatabase(getApplicationContext());

        Calories = getString(R.string.name_energy);
        Protein = getString(R.string.name_protein);
        Fat = getString(R.string.name_fat);
        Carbs = getString(R.string.name_carbs);
        Sugar = getString(R.string.name_sugar);
        Sodium = getString(R.string.name_sodium);
        Sats = getString(R.string.name_saturated_fat);

        Button confirmBtn = findViewById(R.id.btn_confirm);
        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                for(int i = 0; i < nutrientList.size(); i++){
                    Nutrient nutrient = nutrientList.get(i);
                    String nutrientName = nutrient.getName();
                    String nutrientValue = nutrient.getValue();
                    checkNutrient(nutrientName, nutrientValue);
                }

                //TODO: pass ndbno and save, or store all 26 nutrients in Database
                Ingredient ingredient1 = makeIngredient(ingredientId, mealId, itemName, weight, "01001", CalVal, FatVal, SatVal, CarbVal, SugVal, ProVal, SodVal);
                addIngredient(mDb, ingredient1);

                MealBuilderActivity.updateMeal(mDb, mealId);

                Intent intent = new Intent(WeightActivity.this, MealBuilderActivity.class);
                intent.putExtra(EXTRA_MEAL_ID, mealId);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });

        Intent myIntent = getIntent();
        int weightCase = myIntent.getIntExtra(EXTRA_WEIGHT_CASE,0);
        ingredientId = myIntent.getLongExtra(EXTRA_INGREDIENT_ID, 0);
        //TODO: possibly only pass mealId for case 1?
        mealId = myIntent.getLongExtra(EXTRA_MEAL_ID,0);
        if (mealId == 0) {
            throw new NullPointerException("mealId cannot be zero; ingredient must be assigned to a meal");
        } else if(ingredientId == 0){
            throw new NullPointerException("ingredientId cannot be zero; ingredient must be created before this stage");
        }
            if(weightCase == 0){
                Ingredient ingredient = mDb.ingredientModel().loadIngredientById(ingredientId);
                weightIn.setText(ingredient.weight.toString());

                itemName = ingredient.name;
                nameIn.setText(itemName);

                String grams = getString(R.string.unit_weight);
                String kcal = getString(R.string.unit_cals);
                String mgram = getString(R.string.unit_sodium);

                Nutrient[] nutrients = new Nutrient[7];

                nutrients[0] = new Nutrient(Calories, ingredient.calories.toString(), kcal);
                nutrients[1] = new Nutrient(Fat, ingredient.fat.toString(), grams);
                nutrients[2] = new Nutrient(Sats, ingredient.saturates.toString(), grams);
                nutrients[3] = new Nutrient(Protein, ingredient.protein.toString(), grams);
                nutrients[4] = new Nutrient(Sodium, ingredient.sodium.toString(), mgram);
                nutrients[5] = new Nutrient(Carbs, ingredient.carbs.toString(), grams);
                nutrients[6] = new Nutrient(Sugar, ingredient.sugar.toString(), grams);

                nutrientList.addAll(Arrays.asList(nutrients));

                //TODO: implement case of editing existing ingredient
            } else {
                String nutrientJson = myIntent.getStringExtra(SearchResultsActivity.EXTRA_NUTRITION_DATA);
                itemName = myIntent.getStringExtra(SearchResultsActivity.EXTRA_FOOD_NAME);

                nameIn.setText(itemName);

                String CalorieCheck = getString(R.string.case_energy);
                String ProteinCheck = getString(R.string.case_protein);
                String FatCheck = getString(R.string.case_fat);
                String FibreCheck = getString(R.string.case_fiber);
                String CarbCheck = getString(R.string.case_carbs);
                String SugarCheck = getString(R.string.case_sugar);
                String SodiumCheck = getString(R.string.case_sodium);
                String SatsCheck = getString(R.string.case_saturated_fat);

                Nutrient[] nutrientInfo = new Nutrient[0];
                try {
                    nutrientInfo = UsdaJsonUtils.getNutrientDataFromJson(nutrientJson);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                for (Nutrient aNutrientInfo : nutrientInfo) {

                    String Name = aNutrientInfo.getName();
//            String Value = nutrientInfo[i].getValue();
//            String Unit = nutrientInfo[i].getUnit();

                    if (Name.equals(CalorieCheck)) {
                        aNutrientInfo.setName(Calories);
                    }
                    if (Name.equals(ProteinCheck)) {
                        aNutrientInfo.setName(Protein);
                    }
                    if (Name.equals(FatCheck)) {
                        aNutrientInfo.setName(Fat);
                    }
                    if (Name.equals(FibreCheck)) {
                        aNutrientInfo.setName(getString(R.string.name_fiber));
                    }
                    if (Name.equals(CarbCheck)) {
                        aNutrientInfo.setName(Carbs);
                    }
                    if (Name.equals(SugarCheck)) {
                        aNutrientInfo.setName(Sugar);
                    }
                    if (Name.equals(SodiumCheck)) {
                        aNutrientInfo.setName(Sodium);
                    }
                    if (Name.equals(SatsCheck)) {
                        aNutrientInfo.setName(Sats);
                    }
                }


                nutrientList.addAll(Arrays.asList(nutrientInfo));
                Nutrient water = nutrientList.get(0);
                nutrientList.remove(0);
                nutrientList.add(water);
            }

        weight = Double.valueOf(weightIn.getText().toString());

        infoAdapter = new NutritionalInfoAdapter(nutrientList, weight);
        itemList.setAdapter(infoAdapter);



        weightIn.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String weightVal = weightIn.getText().toString();
                if(weightVal.length() == 0) {
                    weightVal = "1";
                }
                    weight = Double.valueOf(weightVal);
                    infoAdapter = new NutritionalInfoAdapter(nutrientList, weight);
                    itemList.setAdapter(infoAdapter);

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

//        }

//        Result nutrientResults = new Result();

//        Boolean loadCases = true;
//        for (int i = 0; i < nutrientInfo.length; i++) {
//
//            String Name = nutrientInfo[i].getName();
//            String Value = nutrientInfo[i].getValue();
//            String Unit = nutrientInfo[i].getUnit();
////
//////            if (loadCases) {
////            String CalorieCheck = getString(R.string.case_energy);
////            String ProteinCheck = getString(R.string.case_protein);
////            String FatCheck = getString(R.string.case_fat);
////            String FibreCheck = getString(R.string.case_fiber);
////            String CarbCheck = getString(R.string.case_carbs);
////            String SugarCheck = getString(R.string.case_sugar);
////            String SodiumCheck = getString(R.string.case_sodium);
////            String SatsCheck = getString(R.string.case_saturated_fat);
//////            }
////
//            if (Name == CalorieCheck){
//                CalVal = Integer.parseInt(Value);
//                CalUnit = Unit;
//            }
//            if (Name == ProteinCheck){
//                ProVal = Integer.parseInt(Value);
//                ProUnit = Unit;
//            }
//            if (Name == FatCheck){
//                FatVal = Integer.parseInt(Value);
//                FatUnit = Unit;
//            }
//            if (Name == FibreCheck){
//                FibVal = Integer.parseInt(Value);
//                FibUnit = Unit;
//            }
//            if (Name == CarbCheck){
//                CarbVal = Integer.parseInt(Value);
//                CarbUnit = Unit;
//            }
//            if (Name == SugarCheck){
//                SugVal = Integer.parseInt(Value);
//                SugUnit = Unit;
//            }
//            if (Name == SodiumCheck){
//                SodVal = Integer.parseInt(Value);
//                SugUnit = Unit;
//            }
//            if (Name == SatsCheck){
//                SatVal = Integer.parseInt(Value);
//                SugUnit = Unit;
//            }
//
////                loadCases = false;
//        }


//
    }

    public  void checkNutrient(String name, String val){

        if (name.equals(getString(R.string.name_energy))) {
            CalVal = Double.parseDouble(val);
        }
        if (name.equals(getString(R.string.name_protein))) {
            ProVal = Double.parseDouble(val);
        }
        if (name.equals(getString(R.string.name_fat))) {
            FatVal = Double.parseDouble(val);
        }
//        if (name.equals(FibreCheck)) {
//            nutrientInfo[i].setName(getString(R.string.name_fiber));
//        }
        if (name.equals(getString(R.string.name_carbs))) {
            CarbVal = Double.parseDouble(val);
        }
        if (name.equals(getString(R.string.name_sugar))) {
            SugVal = Double.parseDouble(val);
        }
        if (name.equals(getString(R.string.name_sodium))) {
            SodVal = Double.parseDouble(val);
        }
        if (name.equals(getString(R.string.name_saturated_fat))) {
            SatVal = Double.parseDouble(val);
        }
    }
}

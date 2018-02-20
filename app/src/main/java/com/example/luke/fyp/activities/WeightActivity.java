package com.example.luke.fyp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.TextView;

import com.example.luke.fyp.Nutrient;
import com.example.luke.fyp.R;
import com.example.luke.fyp.adapters.NutritionalInfoAdapter;
import com.example.luke.fyp.utilities.UsfdaJsonUtils;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class WeightActivity extends AppCompatActivity {

    NutritionalInfoAdapter infoAdapter;
    List<Nutrient> nutrientList = new ArrayList<>();

    Double weight;

    TextView nameTitle;
    TextView nameIn;
    TextView weightTitle;
    EditText weightIn;

//    TextView testText;
//    TextView test1Text;
//    TextView test2Text;

//    String Calories;
//    String Protein;
//    String Fat;
//    String Sats;
//    String Carbs;
//    String Sugar;
//    String Sodium;
//    Integer CalVal;
//    Integer ProVal;
//    Integer FatVal;
//    Integer FibVal;
//    Integer SatVal;
//    Integer CarbVal;
//    Integer SugVal;
//    Integer SodVal;
//    String CalUnit;
//    String ProUnit;
//    String FatUnit;
//    String FibUnit;
//    String SatUnit;
//    String CarbUnit;
//    String SugUnit;
//    String SodUnit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weight);
        final RecyclerView itemList = (RecyclerView) findViewById(R.id.rv_nutrient_items);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        itemList.setLayoutManager(layoutManager);

        nameTitle = (TextView) findViewById(R.id.tv_title);
        nameIn = (TextView) findViewById(R.id.tv_item_name);
        weightTitle= (TextView) findViewById(R.id.tv_weight);

        weightIn = (EditText) findViewById(R.id.et_weight);


//        testText = (TextView) findViewById(R.id.tv_test);
//        test1Text = (TextView) findViewById(R.id.tv_test1);
//        test2Text = (TextView) findViewById(R.id.tv_test2);

//        Calories = getString(R.string.name_energy);
//        Protein = getString(R.string.name_protein);
//        Fat = getString(R.string.name_fat);
//        Sats = getString(R.string.name_saturated_fat);
//        Carbs = getString(R.string.name_carbs);
//        Sugar = getString(R.string.name_sugar);
//        Sodium = getString(R.string.name_sodium);

        Intent myIntent = getIntent();
        String nutrientJson = myIntent.getStringExtra("Nutrients");
        String itemName = myIntent.getStringExtra("Name");
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
        try{
            nutrientInfo = UsfdaJsonUtils.getNutrientDataFromJson(nutrientJson);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        for (int i = 0; i < nutrientInfo.length; i++) {

            String Name = nutrientInfo[i].getName();
//            String Value = nutrientInfo[i].getValue();
//            String Unit = nutrientInfo[i].getUnit();

            if (Name.equals(CalorieCheck)) {
                nutrientInfo[i].setName(getString(R.string.name_energy));
            }
            if (Name.equals(ProteinCheck)) {
                nutrientInfo[i].setName(getString(R.string.name_protein));
            }
            if (Name.equals(FatCheck)) {
                nutrientInfo[i].setName(getString(R.string.name_fat));
            }
            if (Name.equals(FibreCheck)) {
                nutrientInfo[i].setName(getString(R.string.name_fiber));
            }
            if (Name.equals(CarbCheck)) {
                nutrientInfo[i].setName(getString(R.string.name_carbs));
            }
            if (Name.equals(SugarCheck)) {
                nutrientInfo[i].setName(getString(R.string.name_sugar));
            }
            if (Name.equals(SodiumCheck)) {
                nutrientInfo[i].setName(getString(R.string.name_sodium));
            }
            if (Name.equals(SatsCheck)) {
                nutrientInfo[i].setName(getString(R.string.name_saturated_fat));
            }
        }

        nutrientList.addAll(Arrays.asList(nutrientInfo));
        Nutrient water = nutrientList.get(0);
        nutrientList.remove(0);
        nutrientList.add(water);

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
}

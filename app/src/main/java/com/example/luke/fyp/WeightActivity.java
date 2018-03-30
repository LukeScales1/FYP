package com.example.luke.fyp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.example.luke.fyp.adapters.NutritionalInfoAdapter;
import com.example.luke.fyp.utilities.UsfdaJsonUtils;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class WeightActivity extends AppCompatActivity {

    NutritionalInfoAdapter infoAdapter;
    List<Nutrient> nutrientList = new ArrayList<>();

    TextView testText;
    TextView test1Text;
    TextView test2Text;

    String Calories;
    String Protein;
    String Fat;
    String Sats;
    String Carbs;
    String Sugar;
    String Sodium;
    Integer CalVal;
    Integer ProVal;
    Integer FatVal;
    Integer FibVal;
    Integer SatVal;
    Integer CarbVal;
    Integer SugVal;
    Integer SodVal;
    String CalUnit;
    String ProUnit;
    String FatUnit;
    String FibUnit;
    String SatUnit;
    String CarbUnit;
    String SugUnit;
    String SodUnit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weight);
        RecyclerView itemList = (RecyclerView) findViewById(R.id.rv_nutrient_items);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        itemList.setLayoutManager(layoutManager);

//        testText = (TextView) findViewById(R.id.tv_test);
//        test1Text = (TextView) findViewById(R.id.tv_test1);
//        test2Text = (TextView) findViewById(R.id.tv_test2);

        Calories = getString(R.string.name_energy);
        Protein = getString(R.string.name_protein);
        Fat = getString(R.string.name_fat);
        Sats = getString(R.string.name_saturated_fat);
        Carbs = getString(R.string.name_carbs);
        Sugar = getString(R.string.name_sugar);
        Sodium = getString(R.string.name_sodium);

        Intent myIntent = getIntent();
        String nutrientJson = myIntent.getStringExtra("Nutrients");

        Nutrient[] nutrientInfo = new Nutrient[0];
        try{
            nutrientInfo = UsfdaJsonUtils.getNutrientDataFromJson(nutrientJson);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        nutrientList.addAll(Arrays.asList(nutrientInfo));

        

        for (int i = 0; i < nutrientList.size(); i++) {

//            String Name = nutrientInfo[i].getName();
//            String Value = nutrientInfo[i].getValue();
//            String Unit = nutrientInfo[i].getUnit();
//
//            testText.append(Name);
//            test1Text.append(Value);
//            test2Text.append(Unit);
        }

//        Result nutrientResults = new Result();

//        Boolean loadCases = true;
//        for (int i = 0; i < nutrientInfo.length; i++) {
//
//            String Name = nutrientInfo[i].getName();
//            String Value = nutrientInfo[i].getValue();
//            String Unit = nutrientInfo[i].getUnit();
//
////            if (loadCases) {
//                String CalorieCheck = getString(R.string.case_energy);
//                String ProteinCheck = getString(R.string.case_protein);
//                String FatCheck = getString(R.string.case_fat);
//                String FibreCheck = getString(R.string.case_fiber);
//                String CarbCheck = getString(R.string.case_carbs);
//                String SugarCheck = getString(R.string.case_sugar);
//                String SodiumCheck = getString(R.string.case_sodium);
//                String SatsCheck = getString(R.string.case_saturated_fat);
////            }
//
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

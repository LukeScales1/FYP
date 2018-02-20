package com.example.luke.fyp.activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.example.luke.fyp.Food;
import com.example.luke.fyp.R;
import com.example.luke.fyp.adapters.SearchResultsAdapter;
import com.example.luke.fyp.utilities.NetworkUtils;
import com.example.luke.fyp.utilities.UsfdaJsonUtils;

import org.json.JSONException;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SearchResultsActivity extends AppCompatActivity implements SearchResultsAdapter.ItemClickListener{

    SearchResultsAdapter itemAdapter;
    List<Food> foodList = new ArrayList<>();
    String resultName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results);
        RecyclerView itemList = (RecyclerView) findViewById(R.id.rv_search_items);
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        itemList.setLayoutManager(layoutManager);


        Intent myIntent = getIntent();
        String foodResults = myIntent.getStringExtra("Data");

        Food[] foodData = new Food[0];
        try {
            foodData = UsfdaJsonUtils.getFoodDataFromJson(foodResults);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        foodList.addAll(Arrays.asList(foodData));

        itemAdapter = new SearchResultsAdapter(foodList);
        itemAdapter.setClickListener(this);

        itemList.setAdapter(itemAdapter);
    }

    @Override
    public void onItemClick(View v, int position) {
        Food food = foodList.get(position);
        resultName = food.getName();
        URL nutrientRequestUrl = NetworkUtils.makeNdbnoUrl(food.getNdbno());
        new NutrientQueryTask().execute(nutrientRequestUrl);

    }

    public class NutrientQueryTask extends AsyncTask<URL, Void, String> {

        @Override
        protected String doInBackground(URL... params) {
            URL newSearch = params[0];
            String searchResults = null;
            try {
                searchResults = NetworkUtils.getResponseFromHttpUrl(newSearch);

            } catch (Exception e) {
                e.printStackTrace();
            }
            return searchResults;
        }

        @Override
        protected void onPostExecute(String searchResults){
            if (searchResults != null && !searchResults.equals("")){
              Intent intent = new Intent(SearchResultsActivity.this, WeightActivity.class);
                intent.putExtra("Nutrients", searchResults);
                intent.putExtra("Name", resultName);
                startActivity(intent);
            }
        }
    }
}

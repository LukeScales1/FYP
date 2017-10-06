package com.example.luke.fyp;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.luke.fyp.utilities.NetworkUtils;

import java.io.IOException;
import java.net.URL;

public class MainActivity extends AppCompatActivity
{

    private EditText testSearchInput;

    private Button testBtn;

    private TextView testUrlText;

    private TextView testResultsText;

    String search = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        testSearchInput = (EditText) findViewById(R.id.et_test);
        testBtn = (Button) findViewById(R.id.btn_test);
        testUrlText = (TextView) findViewById(R.id.tv_test_url);
        testResultsText = (TextView) findViewById(R.id.tv_test_results);

        testBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v){
               searchFoodDatabase();
            }
        });
    }

    private void searchFoodDatabase(){
        search = testSearchInput.getText().toString();
        URL fdaSearchUrl = NetworkUtils.makeSearchUrl(search);
        testUrlText.setText(fdaSearchUrl.toString());
        new FoodSearchTask().execute(fdaSearchUrl);
    }

    private void retriveNutrientData(){
        search = testSearchInput.getText().toString();
        URL fdaSearchUrl = NetworkUtils.makeNdbnoUrl(search);
        testUrlText.setText(fdaSearchUrl.toString());
        new FoodSearchTask().execute(fdaSearchUrl);
    }

    public class FoodSearchTask extends AsyncTask<URL, Void, String> {

        @Override
        protected String doInBackground(URL... params) {
            URL newSearch = params[0];
            String searchResults = null;
            try {
                searchResults = NetworkUtils.getResponseFromHttpUrl(newSearch);

            } catch (IOException e) {
                e.printStackTrace();
            }
            return searchResults;
        }

        @Override
        protected void onPostExecute(String searchResults){
            if (searchResults != null && !searchResults.equals("")){
                testResultsText.setText(searchResults);
            }
        }
    }
}

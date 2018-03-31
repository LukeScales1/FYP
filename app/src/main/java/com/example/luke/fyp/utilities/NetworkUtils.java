package com.example.luke.fyp.utilities;

import android.net.Uri;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

/**
 * Created by Luke on 29/09/2017.
 */

public class NetworkUtils {

    private static final String apiKey = "xmdjedl4QAgkXR6zdQwAYWVuteyigeljVtwSxRTR" ;

    private final static String BASE_URL =
            "https://api.nal.usda.gov/ndb/";

    private final static String REPORTS = "reports/?";
    private final static String SEARCH = "search/?";

    private final static String SEARCH_QUERY = "q";
    private final static String BRAND_QUERY = "ds";
    private final static String API_QUERY = "api_key";
    private final static String TYPE_QUERY = "type";
    private final static String FORMAT_QUERY= "format";
    private final static String NDBNO_QUERY= "ndbno";

    private static final String type = "b";
    private static final String format = "json";
    private static final String brand = "Standard Reference";

    public static URL makeNdbnoUrl(String ndbno) {
        Uri builtUri = Uri.parse(BASE_URL + REPORTS).buildUpon()
                .appendQueryParameter(NDBNO_QUERY, ndbno)
                .appendQueryParameter(TYPE_QUERY, type)
                .appendQueryParameter(FORMAT_QUERY, format)
                .appendQueryParameter(API_QUERY, apiKey)
                .build();
        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }

    public static URL makeSearchUrl(String search) {
        Uri builtUri = Uri.parse(BASE_URL + SEARCH).buildUpon()
                .appendQueryParameter(SEARCH_QUERY, search)
                .appendQueryParameter(BRAND_QUERY, brand)
                .appendQueryParameter(FORMAT_QUERY, format)
                .appendQueryParameter(API_QUERY, apiKey)
                .build();
        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }


    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream inputStream = httpURLConnection.getInputStream();

            Scanner scanner = new Scanner(inputStream);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            httpURLConnection.disconnect();
        }
    }
}

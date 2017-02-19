package com.infoteck.timewall.Utilities;

import android.os.AsyncTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class jsonFromUrl extends AsyncTask<String, String, JSONObject> {

    HttpURLConnection urlConnection;

    @Override
    protected JSONObject doInBackground(String... args) {

        StringBuilder result = new StringBuilder();

        try {
            URL url = new URL(args[0]);
            urlConnection = (HttpURLConnection) url.openConnection();
            InputStream in = new BufferedInputStream(urlConnection.getInputStream());

            BufferedReader reader = new BufferedReader(new InputStreamReader(in));

            String line;
            while ((line = reader.readLine()) != null) {
                result.append(line);
            }

        }catch( Exception e) {
            e.printStackTrace();
        }
        finally {
            urlConnection.disconnect();
        }

        try {
            return new JSONObject(result.toString());
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }

    }


}
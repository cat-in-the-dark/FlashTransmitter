package com.catinthedark.task;

import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import com.catinthedark.activity.ReceiveActivity;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.TreeMap;

/**
 * Created by kirill on 01.10.14.
 */
public class SubmitDataTask extends AsyncTask<Object, String, Void> {
    @Override
    protected Void doInBackground(Object... params) {
        try {
            String transmissionResult = (String) params[0];

            String uri = "http://192.168.43.127:3000/graph";
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(uri);

            StringEntity se = new StringEntity(transmissionResult);
            httpPost.setEntity(se);

            httpPost.setHeader("accept", "*/*");
            httpPost.setHeader("content-type", "application/x-www-form-urlencoded");

            HttpResponse httpResponse = httpclient.execute(httpPost);

            InputStream inputStream = httpResponse.getEntity().getContent();

            String result;
            if(inputStream != null) {
                result = convertInputStreamToString(inputStream);
            }
            else {
                result = "Did not work!";
            }
            Log.d(ReceiveActivity.TAG, "Result: " + result);

        } catch (Exception e) {
            Log.d("InputStream", e.getLocalizedMessage());
        }
        return null;
    }

    private static String convertInputStreamToString(InputStream inputStream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while((line = bufferedReader.readLine()) != null)
            result += line;

        inputStream.close();
        return result;
    }
}


//// create the albums object
//JsonObject albums = new JsonObject();
//// add a property calle title to the albums object
//albums.addProperty("title", "album1");
//
//        // create an array called datasets
//        JsonArray datasets = new JsonArray();
//
//        // create a dataset
//        JsonObject dataset = new JsonObject();
//        // add the property album_id to the dataset
//        dataset.addProperty("album_id", 1);
//        // add the property album_year to the dataset
//        dataset.addProperty("album_year", 1996);
//
//        datasets.add(dataset);
//
//        albums.add("dataset", datasets);
//
//        // create the gson using the GsonBuilder. Set pretty printing on. Allow
//        // serializing null and set all fields to the Upper Camel Case
//        Gson gson = new GsonBuilder().setPrettyPrinting().serializeNulls().setFieldNamingPolicy(FieldNamingPolicy.UPPER_CAMEL_CASE).create();
//        System.out.println(gson.toJson(albums));
//        /* prints
//        {
//              "title": "album1",
//              "dataset": [
//                {
//                  "album_id": 1,
//                  "album_year": 1996
//                }
//              ]
//        }
//        */

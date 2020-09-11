package com.example.tiktokbupt.Network;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class RawDataFetcher {

    public void fetch(URL url, RawDataFetcherInterface callback) {
        String result = "";
        try {
            result = new RawDataFetchTask().execute(url).get();
        } catch (Exception ignored) {}


        if (result == null || result.isEmpty()) {
            callback.onFetchFailure();
        } else {
            Log.d("RawJson", result);
            callback.onFetchSuccess(result);
        }
    }

    private static class RawDataFetchTask extends AsyncTask<URL, Void, String> {
        @Override
        protected String doInBackground(URL... urls) {
            HttpURLConnection connection = null;
            BufferedReader reader = null;
            URL url = urls[0];
            try {
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                InputStream stream = connection.getInputStream();

                reader = new BufferedReader(new InputStreamReader(stream));
                StringBuffer buffer = new StringBuffer();
                String line = "";

                while ((line = reader.readLine()) != null) {
                    buffer.append(line);
                }
                return buffer.toString();

            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}

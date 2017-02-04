package de.reikodd.ddweki;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class URLConnection extends AsyncTask<String, String, String> {

    private URLInterface urlInterface;
    Activity mActivity;

    public URLConnection(URLInterface activityContext) {
        this.urlInterface = activityContext;
    }



    @Override
    protected String doInBackground(String... urls) {
        String contents = "";
        String line = "";

        try {
            URL url = new URL(urls[0]);
            HttpURLConnection connection = (HttpURLConnection) url
                    .openConnection();
            connection.setRequestProperty("connection", "close");

            InputStream inputStream = connection.getInputStream();

            BufferedReader buffer = new BufferedReader(new InputStreamReader(
                    inputStream));

            while ((line = buffer.readLine()) != null) {
                contents += line;
            }

            return contents;

        } catch (MalformedURLException rat) {
            rat.printStackTrace();
            String NotValue=" ";
            return NotValue;
        } catch (Exception rat) {
            rat.printStackTrace();
            String NotValue=" ";
            return NotValue;
        }
    }

    @Override
    protected void onPostExecute(String contents) {
        urlInterface.receivedContent(contents);
    }
}
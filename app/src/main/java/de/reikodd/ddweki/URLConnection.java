package de.reikodd.ddweki;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class URLConnection extends AsyncTask<String, String, String> {

    Context ctx;

    public URLConnection(Context c)
    {
        this.ctx = c;
    }

    @Override
    protected String doInBackground(String... urls) {

        try {
            URL url = new URL(urls[0]);
            String input = urls[1];
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            OutputStream os = conn.getOutputStream();
            os.write(input.getBytes());
            os.flush();
            if(conn.getResponseCode() == 200) {
                //Log.i("Reiko", "" + urls[0] + " - POST Success " + conn.getResponseCode());
            }
            else
            {
                //Log.i("Reiko", "Fehler: " + "" + conn.getResponseCode());
            }
            conn.disconnect();

            return String.valueOf(conn.getResponseCode());

        } catch (MalformedURLException rat) {
            rat.printStackTrace();
            //Log.i("Reiko", ""+rat);
            return "0";
        } catch (Exception rat) {
            //Log.i("Reiko", ""+rat);
            return "0";
        }
    }

    @Override
    protected void onPostExecute(String result) {
        TextView txtViewDesc = (TextView) ((Activity) ctx).findViewById(R.id.description);
        if(result.equals("200")) {
            txtViewDesc.setText("send to database successful");
        }
        else
        {
            txtViewDesc.setText("ERROR to send Data");
        }
    }


}
package com.example.pregunta10;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

public class MainActivity extends AppCompatActivity{

    private JSONObject data;
    private TextView textView;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        data = null;
        textView = findViewById(R.id.tiempo);
        new GetJson().execute("Madrid");
    }

    private class GetJson extends AsyncTask<String, Void, String> {
        protected String doInBackground(String... params) {
            try {
                String city = params[0];
                URL url = new URL("http://api.openweathermap.org/data/2.5/weather?q="+city+"&units=metric&APPID=a78fb6b76b035edf68179f24892bc13f");
                InputStream is = url.openStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(is));
                StringBuffer json = new StringBuffer(1024);
                String tmp = "";
                while((tmp = reader.readLine()) != null)
                    json.append(tmp).append("\n");
                reader.close();
                data = new JSONObject(json.toString());
                System.out.printf(json.toString());

                if(data.getInt("cod") != 200) {
                    System.out.println("Cancelled");
                    return null;
                }
            }
            catch (Exception e) {
                System.out.println("Exception "+ e.getMessage());
                return null;
            }

            return null;
        }
        protected void onPostExecute(String result) {
            try{
                textView.setText(data.toString());
                JSONObject jsonArray = data.getJSONObject("main");
                String strTemp = jsonArray.getString("temp");
                String cityStr = data.getString("name");
                String str = "Ubicación: "+cityStr+"\nTemperatura actual: "+strTemp;
                textView.setText(str);

            }catch (JSONException e) {
                System.out.println("Exception "+ e.getMessage());

            }

        }
        protected void onPreExecute() {}
        protected void onProgressUpdate(Void... values) {}
    }
}
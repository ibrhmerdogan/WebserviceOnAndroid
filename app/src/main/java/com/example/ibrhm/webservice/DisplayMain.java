package com.example.ibrhm.webservice;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * Created by ibrhm on 1.02.2017.
 */

public class DisplayMain extends Activity {
    private static final String url = "http://denemesitesidirdbyeni.co.nf/uyeler.php";
        ListView lvUyeler;
        ArrayList<String> uyeler;
        TextView textView;
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.diplay_main);
         textView=(TextView)findViewById(R.id.textView5);
            bilesenleri_yukle();
        }
        private void bilesenleri_yukle()
        {
            lvUyeler = (ListView) findViewById(R.id.listView);
            uyeler = new ArrayList<>();
            JSONObject jRegister = new JSONObject();
            try {
                jRegister.put("","");

            } catch (JSONException e) {
                e.printStackTrace();
            }

            HttpClient client = new HttpClient(url, jRegister);
            client.getJSONFromUrl();
        }


   class HttpClient extends AsyncTask<Void, Void, JSONObject> {
        private final String TAG = "HttpClient";
        private String URL;
        private JSONObject jsonObjSend;

       String  resultString;


        public HttpClient(String URL, JSONObject jsonObjSend) {
            this.URL = URL;
            this.jsonObjSend = jsonObjSend;
        }

        public void getJSONFromUrl() {
            this.execute();

        }
        @Override
        protected JSONObject doInBackground(Void... params) {

            try {
                DefaultHttpClient httpclient = new DefaultHttpClient();
                HttpPost httpPostRequest = new HttpPost(URL);

                StringEntity se = new StringEntity(jsonObjSend.toString());

                // Set HTTP parameters
                httpPostRequest.setEntity(se);
                httpPostRequest.setHeader("Accept", "application/json");
                httpPostRequest.setHeader("Content-type", "application/json");

                long t = System.currentTimeMillis();
                HttpResponse response = (HttpResponse) httpclient.execute(httpPostRequest);
                //Toast.makeText(getApplicationContext(),""+response,Toast.LENGTH_LONG).show();
                Log.i(TAG, "HTTPResponse received in [" + (System.currentTimeMillis() - t) + "ms]");

                HttpEntity entity = response.getEntity();

                if (entity != null) {
                    // Read the content stream
                    InputStream instream = entity.getContent();

                    // convert content stream to a String
                    resultString = convertStreamToString(instream);
                     instream.close();
                   // resultString = resultString.substring(1, resultString.length() - 1); // remove wrapping "[" and "]"
                     JSONObject object= new JSONObject(resultString);



                    //Raw DEBUG output of our received JSON object:
                   Log.i(TAG, "<JSONObject>\n" + object.toString() + "\n</JSONObject>");
                    return object;
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
       return null;
        }

        protected void onPostExecute(JSONObject object)  {
            JSONObject objectt=object;
            JSONArray result=null;
            int i;
            try {
                JSONArray array=new JSONArray();
            result=array.put(objectt);
            } catch (Exception e) {
                e.printStackTrace();

            }
            int x=objectt.length();
            textView.setText("a");
            try{
            for (i=0;i<x;i++){

                JSONObject jsonObject = result.getJSONObject(0).getJSONObject(String.valueOf(i));
             //   Toast.makeText(getApplicationContext(),"obje"+jsonObject,Toast.LENGTH_LONG).show();
                    uyeler.add(jsonObject.getInt("id") + " , " +jsonObject.getString("name") + " , " + jsonObject.getString("email") +" , " + jsonObject.getInt("no"));

            }
            }catch (JSONException e){e.printStackTrace();}
            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_list_item_1,uyeler);
            lvUyeler.setAdapter(arrayAdapter);
            Toast.makeText(getApplicationContext(), "listelendi", Toast.LENGTH_SHORT).show();
        }

        private String convertStreamToString(InputStream is) {

            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            StringBuilder sb = new StringBuilder();

            String line = null;
            try {
                int x=0;
                while ((line = reader.readLine()) != null) {
                    x++;
                    if(x>4)
                    sb.append(line + "\n");
                                   }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return sb.toString();
        }
    }
    }

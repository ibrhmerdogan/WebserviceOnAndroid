package com.example.ibrhm.webservice;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

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
    private static  final String url="http://denemesitesidirdbyeni.co.nf/uyeler.php";
    ListView listView;
    ArrayList<String> list;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.diplay_main);
        listView=(ListView)findViewById(R.id.listView);
        list=new ArrayList<>();
        JSONObject jRegister=new JSONObject();
        HttpClient client = new HttpClient(url, jRegister);
        client.getJSONFromUrl();

    }

    public class HttpClient extends AsyncTask<Void, Void, JSONArray> {
        private final String TAG = "HttpClient";
        private String URL;
        private JSONObject jsonObjSend;
        private JSONArray result = null;

        public HttpClient(String URL, JSONObject jsonObjSend) {
            this.URL = URL;
            this.jsonObjSend = jsonObjSend;
        }

        public void getJSONFromUrl() {
            this.execute();

        }

        @Override
        protected JSONArray doInBackground(Void... params) {

            try {
                DefaultHttpClient httpclient = new DefaultHttpClient();
                HttpPost httpPostRequest = new HttpPost(URL);

                StringEntity se;
                se = new StringEntity(jsonObjSend.toString());

                // Set HTTP parameters
                httpPostRequest.setEntity(se);
                httpPostRequest.setHeader("Accept", "application/json");
                httpPostRequest.setHeader("Content-type", "application/json");

                long t = System.currentTimeMillis();
                HttpResponse response = (HttpResponse) httpclient.execute(httpPostRequest);
                Log.i(TAG, "HTTPResponse received in [" + (System.currentTimeMillis() - t) + "ms]");

                HttpEntity entity = response.getEntity();

                if (entity != null) {
                    // Read the content stream
                    InputStream instream = entity.getContent();

                    // convert content stream to a String
                    String resultString = convertStreamToString(instream);
                    instream.close();
                    resultString = resultString.substring(1, resultString.length() - 1); // remove wrapping "[" and "]"

                    JSONArray jsonObjRecv = new JSONArray(resultString);

                    // Raw DEBUG output of our received JSON object:
                    Log.i(TAG, "<JSONObject>\n" + jsonObjRecv.toString() + "\n</JSONObject>");

                    return jsonObjRecv;
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(JSONArray jObject) {

            result = jObject;

            for(int i=0;i<result.length();i++){
                try{
                    JSONObject jsonObject=result.getJSONObject(i);
                    list.add(jsonObject.getString("name")+"="+jsonObject.getString("email")+"="+jsonObject.getString("no"));

                }catch (JSONException e){e.printStackTrace();}
            }
            ArrayAdapter<String> adapter=new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_list_item_1,list);
            listView.setAdapter(adapter);
        }

        private String convertStreamToString(InputStream is) {

            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            StringBuilder sb = new StringBuilder();

            String line = null;
            try {
                while ((line = reader.readLine()) != null) {
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

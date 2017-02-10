package com.example.ibrhm.webservice.base;

import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by ibrhm on 10.02.2017.
 */

public class DeleteClass {

    private static final String url_sil = "http://denemesitesidirdbyeni.co.nf/uyesil.php";
    private static final String url = "http://denemesitesidirdbyeni.co.nf/uyeler.php";
    public void  deleteFuns(int no){
        JSONObject jRegister = new JSONObject();
        try {
            jRegister.put("no", no);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        HttpClient client = new HttpClient(url_sil, jRegister);
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

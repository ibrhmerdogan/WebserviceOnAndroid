package com.example.ibrhm.webservice;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

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
 * Created by ibrhm on 1.02.2017.
 */

public class AddRecordMain extends Activity {
    private static final String url="http://denemesitesidirdbyeni.co.nf/uyeekle.php";
    Button add;
    EditText name,email,no;
    Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addrecord_main);
        add=(Button)findViewById(R.id.button);
        name=(EditText)findViewById(R.id.editText);
        email=(EditText)findViewById(R.id.editText2);
        no=(EditText)findViewById(R.id.editText3);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                JSONObject jRegister=new JSONObject();
                try {
                    jRegister.put("name",name.getText().toString());
                    jRegister.put("email",email.getText().toString());
                    jRegister.put("no",no.getText().toString());

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                HttpClient client = new HttpClient(url, jRegister);
               client.getJSONFromUrl();
            }
        });
    }


    public class HttpClient extends AsyncTask<Void, Void, JSONObject> {
        private final String TAG = "HttpClient";
        private String URL;
        private JSONObject jsonObjSend;
        private JSONObject result = null;

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

                    JSONObject jsonObjRecv = new JSONObject(resultString);

                    // Raw DEBUG output of our received JSON object:
                    Log.i(TAG, "<JSONObject>\n" + jsonObjRecv.toString() + "\n</JSONObject>");

                    return jsonObjRecv;
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(JSONObject jObject) {
            result = jObject;
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
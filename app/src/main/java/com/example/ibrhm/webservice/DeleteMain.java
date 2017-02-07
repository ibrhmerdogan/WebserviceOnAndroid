package com.example.ibrhm.webservice;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.ibrhm.webservice.base.Uye;
import com.example.ibrhm.webservice.base.UyeAdapter;

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
 * Created by ibrhm on 7.02.2017.
 */

public class DeleteMain extends Activity {
    private static final String url = "http://denemesitesidirdbyeni.co.nf/uyeler.php";
    private static final String url_sil = "http://denemesitesidirdbyeni.co.nf/sil.php";
    ListView lvUyeler;
    ArrayList<Uye> uyeler;
    UyeAdapter arrayAdapter;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.delete_main);

    }

    private AdapterView.OnItemClickListener lvUyelerListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
            new AlertDialog.Builder(DeleteMain.this)
                    .setTitle("Üyeyi Sil")
                    .setMessage("Üyeyi Silmek istediğinizden emin misiniz?")
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

                            JSONObject jRegister = new JSONObject();
                            try {
                                jRegister.put("id",uyeler.get(position).getUyeID());
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            HttpClient client = new HttpClient(url_sil, jRegister);
                            client.calistir();

                            uyeler.remove(position);
                            arrayAdapter.notifyDataSetChanged();

                        }
                    })
                    .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // do nothing
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        }
    };
    private void bilesenleri_yukle()
    {
        lvUyeler = (ListView) findViewById(R.id.listView);
        lvUyeler.setOnItemClickListener(lvUyelerListener);
        uyeler = new ArrayList<>();
        JSONObject jRegister = new JSONObject();
        try {
            jRegister.put("","");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        HttpClient client = new HttpClient(url, jRegister);
        client.calistir();
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

        public void calistir() {
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

                HttpEntity entity = response.getEntity();

                if (entity != null) {
                    // Read the content stream
                    InputStream instream = entity.getContent();

                    // convert content stream to a String
                    String resultString = convertStreamToString(instream);
                    instream.close();


                    JSONArray jsonObjRecv = new JSONArray(resultString);

                    // Raw DEBUG output of our received JSON object:
                    Log.d(TAG, "<JSONObject>\n" + jsonObjRecv.toString() + "\n</JSONObject>");

                    return jsonObjRecv;
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(JSONArray jObject) {
            try{
                result = jObject;
                for (int i=0;i<result.length();i++){
                    try {
                        JSONObject jsonObject = result.getJSONObject(i);
                        uyeler.add(new Uye(jsonObject.getString("AdSoyad"), jsonObject.getString("Email"),jsonObject.getString("UyeID")));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                arrayAdapter = new UyeAdapter(getApplicationContext(),uyeler);
                lvUyeler.setAdapter(arrayAdapter);
                Toast.makeText(getApplicationContext(), "listelendi", Toast.LENGTH_SHORT).show();
            }catch (Exception e){
                e.getMessage();
            }

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


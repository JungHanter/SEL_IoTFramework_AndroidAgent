package net.infidea.cma;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.app.Activity;
import android.os.Handler;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class LoginActivity extends Activity implements View.OnClickListener {
    private final int REQCODE = 777;
    private static final String TAG = "LoginActivity";

    private String mac = null;
    private SharedPreferences session = null;
    private SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
    private Handler mHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        findViewById(R.id.buttonLogin).setOnClickListener(this);

        WifiManager manager = (WifiManager) getSystemService(WIFI_SERVICE);
        WifiInfo info = manager.getConnectionInfo();
        mac = info.getMacAddress();

        ((TextView) findViewById(R.id.textViewMAC)).setText(mac);

        session = PreferenceManager.getDefaultSharedPreferences(this);
        Log.v(TAG, "Connection in the session: "+session.getString("connection", null));
        if (session.getString("connection", null) != null) {
            login();
        }
    }

    private void login() {
        startActivityForResult(new Intent(this, MainActivity.class), REQCODE);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.buttonLogin) {
            final String addr = ((EditText) findViewById(R.id.editTextFrameworkAddr)).getText().toString();
            final String id = ((EditText) findViewById(R.id.editTextID)).getText().toString();
            final String pw = ((EditText) findViewById(R.id.editTextPassword)).getText().toString();

            if (!id.equals("") && !pw.equals("")) {
                new Thread() {
                    @Override
                    public void run() {
                        Date timeFrom = new Date();
                        final String timeFromStr = "Start Time: "+LoginActivity.this.dateformat.format(timeFrom);
                        Log.d(TAG, timeFromStr);
                        try {
                            DefaultHttpClient client = new DefaultHttpClient();
                            HttpPost post = new HttpPost(addr+"/api/connect");
                            JSONObject json = new JSONObject();
                            json.put("device_item_address", mac);
                            json.put("user_id", id);
                            json.put("password", pw);
                            Log.v(TAG, json.toString());
                            StringEntity entity = new StringEntity(json.toString());
                            entity.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
                            post.setEntity(entity);
                            HttpResponse response = client.execute(post);
                            if(response != null) {
                                String responseStr = EntityUtils.toString(response.getEntity(), "UTF-8");
                                Log.v(TAG, "Server Response: "+responseStr);

                                JSONObject responseJson = new JSONObject(responseStr);
                                if (responseJson.getString("code").equals("SUCCESS")) {
                                    SharedPreferences.Editor editor = session.edit();
                                    editor.putString("connection", responseStr);
                                    editor.putString("serverAddr", addr);
                                    editor.apply();
                                    login();
                                } else {
                                    runOnUiThread(new Runnable() {
                                        public void run() {
                                            Toast.makeText(LoginActivity.this, "Failed to login. Enter right ID and PW.", Toast.LENGTH_LONG).show();
                                        }
                                    });
                                }
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                            runOnUiThread(new Runnable() {
                                public void run() {
                                    Toast.makeText(LoginActivity.this, "Cannot access to the address.", Toast.LENGTH_LONG).show();
                                }
                            });
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Date timeTo = new Date();
                        final String timeToStr = "End Time: "+LoginActivity.this.dateformat.format(timeTo);
                        Log.d(TAG, timeToStr);
                        final String elapsedTimeStr = "Elapsed Time: "+(timeTo.getTime()-timeFrom.getTime())+" ms";
                        Log.d(TAG, elapsedTimeStr);
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(LoginActivity.this, timeFromStr+'\n'+timeToStr+'\n'+elapsedTimeStr, Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                }.start();
            }

            Log.v(TAG, "Connect to "+addr+" with "+id+" / "+pw);
        }
    }
}

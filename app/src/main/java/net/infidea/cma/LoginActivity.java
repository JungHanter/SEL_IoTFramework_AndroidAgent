package net.infidea.cma;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.app.Activity;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

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

public class LoginActivity extends Activity implements View.OnClickListener {
    private final int REQCODE = 777;
    private static final String TAG = "LoginActivity";

    private String mac = null;
    private SharedPreferences session = null;

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
                                }
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }.start();
            }

            Log.v(TAG, "Connect to "+addr+" with "+id+" / "+pw);
        }
    }
}

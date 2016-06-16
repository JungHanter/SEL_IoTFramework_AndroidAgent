package net.infidea.cma.communicator;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import net.infidea.cma.ContextPack;
import net.infidea.cma.monitor.ContextMonitor;
import net.infidea.cma.setting.SettingActivity;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

public class ContextTransmitter {
	private static final String TAG = "ContextTransmitter";

	private ContextMonitor contextMonitor;
	private String url = "";
	private Timer transmissionTimer;
	private String deviceItemId;

	public ContextTransmitter(Context context, ContextMonitor contextMonitor) {
		// TODO Auto-generated constructor stub
		this.contextMonitor = contextMonitor;

		SharedPreferences session = PreferenceManager.getDefaultSharedPreferences(context);
		url = session.getString("serverAddr", "")+"/api/context";
		String connectionStr = session.getString("connection", "");
		try {
			deviceItemId = new JSONObject(connectionStr).getJSONObject("device_item").getString("item_id");
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	public void sendToServer() {
		ContextPack contextPack = contextMonitor.getContextPack();
		synchronized (contextPack) {
			try {
				Log.d("MKK", contextPack.toString());
				for (int i = 0; i < contextPack.length(); i++) {
					JSONObject req = new JSONObject();
					JSONObject context = contextPack.getJSONObject(i);
					req.put("device_item_id", deviceItemId);
					req.put("context", context);
					HttpClient client = new DefaultHttpClient();
					HttpPost post = new HttpPost(url);
					Log.v(TAG, req.toString());
					StringEntity entity = new StringEntity(req.toString());
					entity.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
					post.setEntity(entity);
					HttpResponse response = client.execute(post);
					if (response != null) response.getEntity().getContent();
				}
				for (int i = contextPack.length() - 1; i >= 0; i--) {
					contextPack.remove(i);
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}

	public void start() {
		if(!url.startsWith("http://")) {
			url = "http://"+url;
		}
		transmissionTimer = new Timer();
		long transferPeriod = (long) (SettingActivity.getTransferPeriod()*1000);
		transmissionTimer.schedule(new TimerTask() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				sendToServer();
			}
		}, transferPeriod, transferPeriod);
	}

	public void stop() {
		if(transmissionTimer != null) {
			transmissionTimer.cancel();
		}
	}
}

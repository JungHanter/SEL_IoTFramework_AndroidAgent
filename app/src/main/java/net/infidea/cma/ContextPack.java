package net.infidea.cma;

import net.infidea.cma.util.TimeConverter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.location.Location;

public class ContextPack extends JSONArray {
	public void put(SensorEvent event) {
		try {
			JSONObject context = new JSONObject();
			context.put("type", event.sensor.getName());
			context.put("time", TimeConverter.convertToMilliseconds(event.timestamp));
			try {
				if (event.values.length > 1) {
					JSONArray values;
					String[] subTypes;
					String[] units;
					switch (event.sensor.getType()) {
						case Sensor.TYPE_ACCELEROMETER:
						case Sensor.TYPE_GRAVITY:
						case Sensor.TYPE_LINEAR_ACCELERATION:
							subTypes = new String[]{"x", "y", "z"};
							units = new String[]{"m/s^2", "m/s^2", "m/s^2"};
							break;
						case Sensor.TYPE_GYROSCOPE:
							subTypes = new String[]{"x", "y", "z"};
							units = new String[]{"rad/s", "rad/s", "rad/s"};
							break;
						case Sensor.TYPE_GYROSCOPE_UNCALIBRATED:
							subTypes = new String[]{
									"rotation x", "rotation y", "rotation z",
									"drift x", "drift y", "drift z"
							};
							units = new String[]{
									"rad/s", "rad/s", "rad/s",
									"rad/s", "rad/s", "rad/s"
							};
							break;
						case Sensor.TYPE_ROTATION_VECTOR:
							subTypes = new String[]{"x", "y", "z", "scalar component"};
							units = new String[]{"number", "number", "number", "number"};
							break;
						case Sensor.TYPE_GAME_ROTATION_VECTOR:
						case Sensor.TYPE_GEOMAGNETIC_ROTATION_VECTOR:
							subTypes = new String[]{"x", "y", "z"};
							units = new String[]{"number", "number", "number"};
							break;
						case Sensor.TYPE_MAGNETIC_FIELD:
							subTypes = new String[]{"x", "y", "z"};
							units = new String[]{"microtesla", "microtesla", "microtesla"};
							break;
						case Sensor.TYPE_MAGNETIC_FIELD_UNCALIBRATED:
							subTypes = new String[]{
									"strength x", "strength y", "strength z",
									"bias x", "bias y", "bias z"
							};
							units = new String[]{
									"microtesla", "microtesla", "microtesla",
									"microtesla", "microtesla", "microtesla"
							};
							break;
						default:
							return;
					}
					values = new JSONArray();
					for (int i = 0; i < event.values.length; i++) {
						if (subTypes.length <= i) break;
						JSONObject value = new JSONObject();
						value.put("sub_type", subTypes[i]);
						value.put("value", event.values[i]);
						value.put("unit", units[i]);
						values.put(value);
					}
					context.put("data", values);
				} else {
					JSONObject value = new JSONObject();
					switch (event.sensor.getType()) {
						case Sensor.TYPE_STEP_COUNTER:
							value.put("value", event.values[0]);
							value.put("unit", "count");
							break;
						case Sensor.TYPE_PROXIMITY:
							value.put("value", event.values[0]);
							value.put("unit", "cm");
							break;
						case Sensor.TYPE_AMBIENT_TEMPERATURE:
							value.put("value", event.values[0]);
							value.put("unit", "degree Celsius");
							break;
						case Sensor.TYPE_LIGHT:
							value.put("value", event.values[0]);
							value.put("unit", "lux");
							break;
						case Sensor.TYPE_PRESSURE:
							value.put("value", event.values[0]);
							value.put("unit", "hPa");
							break;
						case Sensor.TYPE_RELATIVE_HUMIDITY:
							value.put("value", event.values[0]);
							value.put("unit", "percentage");
							break;
						default:
							return;
					}
					context.put("data", value);
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			// Append this context to the ContextPack
			put(context);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void put(Location location) {
		try {
			JSONObject context = new JSONObject();
			context.put("type", "Location");
			context.put("time", TimeConverter.convertToMilliseconds(location.getTime()));
			JSONArray values = new JSONArray();
			JSONObject lat = new JSONObject();
			JSONObject lng = new JSONObject();
			JSONObject alt = new JSONObject();
			lat.put("sub_type", "latitude");
			lat.put("value", location.getLatitude());
			lat.put("unit", "number");
			lng.put("sub_type", "longitude");
			lng.put("value", location.getLatitude());
			lng.put("unit", "number");
			alt.put("sub_type", "altitude");
			alt.put("value", location.getAltitude());
			alt.put("unit", "number");
			values.put(lat);
			values.put(lng);
			values.put(alt);
			context.put("data", values);
			put(context);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}

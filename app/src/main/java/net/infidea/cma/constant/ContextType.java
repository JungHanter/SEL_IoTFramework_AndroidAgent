package net.infidea.cma.constant;

public class ContextType {
    // Type values are based on Android.
    public static final int LOCATION = 100;
    public static final int ACCELEROMETER = 1;
    public static final int MAGNETIC_FIELD = 2;
    public static final int ORIENTATION = 3;
    public static final int GYROSCOPE = 4;
    public static final int LIGHT = 5;
    public static final int PRESSURE = 6;
    public static final int TEMPERATURE = 7;
    public static final int PROXIMITY = 8;
    public static final int GRAVITY = 9;
    public static final int LINEAR_ACCELERATION = 10;
    public static final int ROTATION_VECTOR = 11;
    public static final int RELATIVE_HUMIDITY = 12;
    public static final int AMBIENT_TEMPERATURE = 13;
    public static final int MAGNETIC_FIELD_UNCALIBRATED = 14;
    public static final int GAME_ROTATION_VECTOR = 15;
    public static final int GYROSCOPE_UNCALIBRATED = 16;
    public static final int SIGNIFICANT_MOTION = 17;
    public static final int STEP_DETECTOR = 18;
    public static final int STEP_COUNTER = 19;
    public static final int GEOMAGNETIC_ROTATION_VECTOR = 20;
    public static final int HEART_RATE = 21;

    public static String convertToString(int type) {
        // TODO Auto-generated method stub
        switch(type) {
            case LOCATION:                  return "Location";
            case ACCELEROMETER:             return "Acceleration";
            case MAGNETIC_FIELD:            return "Magnetic Field";
            case ORIENTATION:			  return "Orientation";
            case GYROSCOPE:				return "Gyroscope";
            case LIGHT:					return "Light";
            case PRESSURE:				return "Pressure";
            case TEMPERATURE:			return "Temperature";
            case PROXIMITY:				return "Proximity";
            case GRAVITY:				return "Gravity";
            case LINEAR_ACCELERATION:	return "Linear Acceleration";
            case ROTATION_VECTOR:		return "Rotation Vector";
            case RELATIVE_HUMIDITY:		return "Relative Humidity";
            case AMBIENT_TEMPERATURE:	return "Ambient Temperature";
            case MAGNETIC_FIELD_UNCALIBRATED:	return "Uncalibrated Magnetic Field";
            case GAME_ROTATION_VECTOR:	return "Game Rotation Vector";
            case GYROSCOPE_UNCALIBRATED:	return "Uncalibrated Gyroscope";
            case SIGNIFICANT_MOTION:	return "Significant Motion";
            case STEP_DETECTOR:	return "Step Detector";
            case STEP_COUNTER:	return "Step Counter";
            case GEOMAGNETIC_ROTATION_VECTOR:	return "Geomagnetic Rotation Vector";
            case HEART_RATE:	return "Heart Rate";
        }
        return "";
    }
}

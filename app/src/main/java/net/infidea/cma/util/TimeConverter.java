package net.infidea.cma.util;

import java.util.Calendar;
import java.util.Date;

public class TimeConverter {
	public static long convertToMilliseconds(long timestamp) {
//		return (new Date()).getTime()+(timestamp-System.nanoTime())/1000000L;
//		return timestamp / 1000000;
		return System.currentTimeMillis();
	}
}

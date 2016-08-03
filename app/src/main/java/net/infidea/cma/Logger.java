package net.infidea.cma;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.widget.TextView;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

/**
 * Created by Moon Kwon Kim on 2016-07-12.
 */
public class Logger {

    private Handler mHandler = new Handler();
    private TextView logView;
    private List<String> queue = new ArrayList<String>();

    public Logger(Context context) {
        logView = (TextView) ((Activity) context).findViewById(R.id.log);
    }

    public void addLog(String log) {
        if (queue.size() >= 100) {
            queue.remove(0);
        }
        queue.add(log);
    }

    public void display() {
        String logAll = "";
        for (int i = queue.size()-1; i >= 0; i--) {
            logAll += queue.get(i) + '\n';
        }
        final String finalLogAll = logAll;
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                logView.setText(finalLogAll.substring(0, finalLogAll.length()-1));
            }
        });
    }
}

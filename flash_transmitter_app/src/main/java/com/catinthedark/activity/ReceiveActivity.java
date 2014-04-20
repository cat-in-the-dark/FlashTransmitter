package com.catinthedark.activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.catinthedark.R;
import com.catinthedark.flash_transmitter.lib.algorithm.CompressedScheme;
import com.catinthedark.flash_transmitter.lib.algorithm.Converter;
import com.catinthedark.flash_transmitter.lib.algorithm.ManchesterLineCoder;
import com.catinthedark.flash_transmitter.lib.algorithm.RawDataTranslator;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GraphViewSeries;
import com.jjoe64.graphview.LineGraphView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Timer;
import java.util.TimerTask;

/**
 * User: Leyfer Cyril (kolbasisha@gmail.com)
 * Date: 01.03.14
 * Time: 18:13
 */
public class ReceiveActivity extends Activity implements SensorEventListener {

    private final int STATE_IDLE = 0;
    private final int STATE_SIGNAL = 2;
    private int currentState = STATE_IDLE;

    private static final String STATE_CHANGED_ACTION = "STATE_CHANGED";
    private static final String RESULT_RECEIVED_ACTION = "RECEIVED_RESULT";
    private static final String STATE_EXTRA_KEY = "STATE";
    private static final String RESULT_EXTRA_KEY = "RESULT";

    // global variables
    SensorManager mSensorManager;
    private boolean wasGrowth = false;
    private boolean wasFall = false;
    private float oldLux = -1.0f;
    private long oldTimestamp = 0;
    private long maxInterval = 0;
    private Timer mTimer;

    // statistical variables
    private float minLux = 10000.0f;
    private float maxLux = 0.0f;

    private ArrayList<Long> signals = new ArrayList<Long>();

    public final String TAG = "FlashTransmitter";

    public static GraphViewSeries exampleSeries;

    private BroadcastReceiver updateViewReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d(TAG, "State changed");
            String result;
            if ((result = intent.getExtras().getString(RESULT_EXTRA_KEY)) != null) {
                updateResult(result);
            }
            String status;
            if ((status = intent.getExtras().getString(STATE_EXTRA_KEY)) != null) {
                updateSensorStatus(status);
            }
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.receive);

        exampleSeries = new GraphViewSeries(new GraphView.GraphViewData[] {
        });

        registerLightSensorListener();

        GraphView graphView = new LineGraphView(
                this // context
                , "GraphViewDemo" // heading
        );
        graphView.setViewPort(0, 20);
        graphView.addSeries(exampleSeries);
        graphView.setScalable(true);
        graphView.setScrollable(true);

        LinearLayout layout = (LinearLayout) findViewById(R.id.graphView);
        layout.addView(graphView);
    }

    private void registerLightSensorListener() {
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        Sensor mLight = mSensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        if (mLight != null) {
            mSensorManager.registerListener(this, mLight, SensorManager.SENSOR_DELAY_FASTEST);
        }
        //TODO: show error message if light sensor is not found and switch to startActivity
    }

    private void unregisterLightSensorListener() {
        mSensorManager.unregisterListener(this);
    }

    private int getCurrentState() {
        return currentState;
    }

    private void setCurrentState(int newState) {
        currentState = newState;
    }

    private void collectEnvironmentParameters(float lux) {
        if (lux >= maxLux) {
            maxLux = lux;
        } else if (lux <= minLux) {
            minLux = lux;
        }
    }

    private void collectSignalParameters(long curTimestamp) {
        long interval = curTimestamp - oldTimestamp;
        if (interval != curTimestamp) {
            if (maxInterval < interval) {
                maxInterval = interval;
            }
        }
        oldTimestamp = curTimestamp;
    }

    private void registerSignalFront(long timestamp) {
        signals.add(timestamp);
    }

    private boolean notReceivingSignalForALongTime(long curTimestamp) {
        long interval = curTimestamp - oldTimestamp;
        Log.d(TAG, "MAX_INTERVAL: " + maxInterval);
        return maxInterval > 0 && interval > maxInterval * 3;
    }

    private boolean flashStateChanged(float lux) {
        if (oldLux >= 0) {
            float threshold = 4.0f;
            if (lux > oldLux && !wasGrowth) {
                if (lux / oldLux >= threshold) {
                    wasGrowth = true;
                    wasFall = false;
                    return true;
                } else {
                    return false;
                }
            } else if (!wasFall && wasGrowth) {
                if (oldLux / lux >= threshold) {
                    wasFall = true;
                    wasGrowth = false;
                    return true;
                } else {
                    return false;
                }
            }
        }
        return false;
    }

    private long[] toPrimitive(Long[] values) {
        long[] result = new long[values.length];
        for (int i = 0; i < values.length; i++) {
            result[i] = values[i];
        }
        return result;
    }

    private void broadcastUpdate(String action, String key, String extra) {
        Intent intent = new Intent(action);
        intent.putExtra(key, extra);
        sendBroadcast(intent);
    }

    private void timerMethod() {
        long timestamp = System.currentTimeMillis();
        if (notReceivingSignalForALongTime(timestamp)) {
            unregisterReceivingTimer();

            Log.d(TAG, "Size of signals: " + String.valueOf(signals.size()));
            Log.d(TAG, "Signals: " + Arrays.toString(toPrimitive(signals.toArray(new Long[signals.size()]))));
            //Log.d(TAG, Engine.decodeSequence(signals).toString());

            // #TODO construct classes in the activity constructor or elsewhere....
            // #TODO make dependency injection instead hardcode!!!
            CompressedScheme scheme = new CompressedScheme();
            ManchesterLineCoder coder = new ManchesterLineCoder();
            Converter converter = new Converter(scheme, coder);
            String result = converter.makeString(new RawDataTranslator().translate(signals));

            broadcastUpdate(STATE_CHANGED_ACTION, STATE_EXTRA_KEY, "Transmission of signal finished");
            broadcastUpdate(RESULT_RECEIVED_ACTION, RESULT_EXTRA_KEY, result);

            unregisterLightSensorListener();
            //Log.d(TAG, Engine.parseData(Engine.decodeSequence(signals)));
        }
    }

    private void registerReceivingTimer() {
        mTimer = new Timer();
        long timerInterval = 10;
        mTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                timerMethod();
            }
        }, 0, timerInterval);
    }

    private void unregisterReceivingTimer() {
        if (mTimer != null) {
            mTimer.cancel();
            mTimer = null;
        }
    }

    private void updateSensorStatus(String notification) {
        TextView sensorStateTextView = (TextView) findViewById(R.id.stateTextView);
        sensorStateTextView.setText(notification);
    }

    private void updateSensorInfo(String sensorInfo) {
        TextView sensorInfoTextView = (TextView) findViewById(R.id.sensorInfoTextView);
        sensorInfoTextView.setText(sensorInfo);
    }

    private void updateResult(String result) {
        TextView resultTextView = (TextView) findViewById(R.id.resultTextView);
        resultTextView.setText(result);
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        float lux = sensorEvent.values[0];
        long timestamp = System.currentTimeMillis();

        Log.d(TAG, "Brightness: " + String.valueOf(lux));
        updateSensorInfo("Brightness: " + String.valueOf(lux));

//        ReceiveActivity.exampleSeries.appendData(new GraphView.GraphViewData(timestamp/1000, lux), true, 1000);

        switch (getCurrentState()) {
            case STATE_IDLE:
                collectEnvironmentParameters(lux);
                if (flashStateChanged(lux)) {
                    registerSignalFront(timestamp);

                    setCurrentState(STATE_SIGNAL);
                    broadcastUpdate(STATE_CHANGED_ACTION, STATE_EXTRA_KEY, "Receiving signal...");

                    registerReceivingTimer();
                }
                break;
            case STATE_SIGNAL:
                if (flashStateChanged(lux)) {
                    collectSignalParameters(timestamp);
                    registerSignalFront(timestamp);
                }
                break;
        }
        oldLux = lux;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {
        // do nothing
    }

    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(STATE_CHANGED_ACTION);
        intentFilter.addCategory(Intent.CATEGORY_DEFAULT);
        intentFilter.addAction(RESULT_RECEIVED_ACTION);
        intentFilter.addCategory(Intent.CATEGORY_DEFAULT);
        registerReceiver(updateViewReceiver, intentFilter);
        registerLightSensorListener();
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterLightSensorListener();
        unregisterReceivingTimer();
    }
}

package com.catinthedark.activity;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.catinthedark.R;
import com.catinthedark.flash_transmitter.lib.algorithm.*;
import com.catinthedark.flash_transmitter.lib.factories.EncodingSchemeFactory;
import com.catinthedark.flash_transmitter.lib.factories.ErrorCorrectionFactory;
import com.catinthedark.flash_transmitter.lib.factories.LineCoderFactory;
import com.catinthedark.flash_transmitter.lib.factories.LogicalCodeFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Map;
import java.util.TreeMap;

/**
 * User: Leyfer Cyril (kolbasisha@gmail.com)
 * Date: 01.03.14
 * Time: 18:13
 */
public class ReceiveActivity extends Activity implements SensorEventListener {

    TextView sensorInfoTextView;
    Button endTransmissionButton;
    TextView resultTextView;

    SensorManager mSensorManager;
    private long firstTimestamp;
    private TreeMap<Long, Float> graph;
    private Converter converter;

    public final String TAG = "FlashTransmitter";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.receive);

        sensorInfoTextView = (TextView) findViewById(R.id.sensorInfoTextView);
        endTransmissionButton = (Button) findViewById(R.id.endTransmissionButton);
        resultTextView = (TextView) findViewById(R.id.resultTextView);

        firstTimestamp = System.currentTimeMillis();

        graph = new TreeMap<Long, Float>();

        Bundle extras = getIntent().getExtras();
        String encodingSchemeName = EncodingSchemeFactory.defaultScheme;
        String lineCoderName = LineCoderFactory.defaultCoder;
        String errorCorrectionName = ErrorCorrectionFactory.defaultErrorCorrection;
        String logicalCodeName = LogicalCodeFactory.defaultLogicalCode;
        if (extras != null) {
            encodingSchemeName = extras.getString("encoding_scheme_name");
            lineCoderName = extras.getString("line_coder_name");
            logicalCodeName = extras.getString("logical_code_name");
            errorCorrectionName = extras.getString("error_correction_name");
        }

        final EncodingScheme scheme = EncodingSchemeFactory.build(encodingSchemeName);
        final LineCoder coder = LineCoderFactory.build(lineCoderName);
        final ErrorCorrectionLayer correction = ErrorCorrectionFactory.build(errorCorrectionName);
        final LogicalCodeLayer logical = LogicalCodeFactory.build(logicalCodeName);

        this.converter = new Converter(scheme, coder, correction, logical);
        registerLightSensorListener();

        //TODO: interrupt transmission automatically!
        endTransmissionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                unregisterLightSensorListener();

                Log.d(TAG, drawGraph(graph));

                DateFormat dateFormat = new SimpleDateFormat("dd_MM_HH_mm_ss");
                Date date = new Date();

                String fileName = String.format("graph-%s.csv", dateFormat.format(date));
                saveToFile(fileName, drawGraph(graph));

                TreeMap<Long, Float> filteredGraph = new TreeMap<Long, Float>();

                if (graph.size() > 1) {
                    filteredGraph = Filter.filter(graph);
                }

                ArrayList<Long> signals = new ArrayList<Long>(filteredGraph.keySet());
                Log.d(TAG, "New way: " + Arrays.toString(new RawDataTranslator().translate(signals)));
                String result = converter.makeString(new RawDataTranslator().translate(signals));

                resultTextView.setText(String.format("\"%s\"", result));

                Log.d(TAG, drawGraph(filteredGraph));
            }
        });
    }

    private void registerLightSensorListener() {
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        Sensor mLight = mSensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        if (mLight != null) {
            mSensorManager.registerListener(this, mLight, SensorManager.SENSOR_DELAY_FASTEST);
        } else {
            Toast.makeText(getApplicationContext(),
                    "Your phone does not have light sensor", Toast.LENGTH_LONG).show();
        }
    }

    private void unregisterLightSensorListener() {
        mSensorManager.unregisterListener(this);
    }

    private String drawGraph(Map<Long, Float> graph) {
        StringBuilder builder = new StringBuilder();
        for (Map.Entry<Long, Float> point: graph.entrySet()) {
            builder.append(String.format("%d,%d\n", point.getKey(), point.getValue().intValue()));
        }

        return builder.toString();
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        float lux = sensorEvent.values[0];
        long timestamp = System.currentTimeMillis();

        graph.put(timestamp - firstTimestamp, lux);
        sensorInfoTextView.setText(String.format("Brightness: %f", lux));
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {
        // do nothing
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerLightSensorListener();
    }

    @Override
    protected void onPause() {
        unregisterLightSensorListener();
        super.onPause();
    }

    private void saveToFile(String filename, String data) {
        File cacheDir = getExternalCacheDir();
        File dstFile = new File(cacheDir, filename);

        try {
            OutputStream outputStream = new FileOutputStream(dstFile);
            outputStream.write(data.getBytes());
            outputStream.close();
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
    }
}

package com.catinthedark.activity;

import android.app.Activity;
import android.hardware.Camera;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.catinthedark.R;
import com.catinthedark.flash_transmitter.lib.algorithm.Converter;
import com.catinthedark.flash_transmitter.lib.algorithm.EncodingScheme;
import com.catinthedark.flash_transmitter.lib.algorithm.LineCoder;
import com.catinthedark.flash_transmitter.lib.factories.EncodingSchemeFactory;
import com.catinthedark.flash_transmitter.lib.factories.LineCoderFactory;

import java.util.Arrays;

import static java.lang.Thread.sleep;

public class TransmitActivity extends Activity{

    public final String TAG = "FlashTransmitter";
    private TextView transmitRawTextView;
    private Boolean shouldTransmissionStop = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        Bundle extras = getIntent().getExtras();
        String encodingSchemeName = EncodingSchemeFactory.defaultScheme;
        String lineCoderName = LineCoderFactory.defaultCoder;
        if (extras != null) {
            encodingSchemeName = extras.getString("encoding_scheme_name");
            lineCoderName = extras.getString("line_coder_name");
        }

        final EncodingScheme scheme = EncodingSchemeFactory.build(encodingSchemeName);
        final LineCoder coder = LineCoderFactory.build(lineCoderName);
        final Converter converter = new Converter(scheme, coder);

        Button transmitButton = (Button) findViewById(R.id.transmitButton);
        transmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText transmitEditText = (EditText) findViewById(R.id.transmitText);
                EditText frequencyEditText = (EditText) findViewById(R.id.frequencyText);
                transmitRawTextView = (TextView) findViewById(R.id.transmitRawTextView);

                String transmitString = transmitEditText.getText().toString();

                final Byte[] transmitBits = converter.makeBits(transmitString);

                final String transmitValue = Arrays.toString(transmitBits).replaceAll("[\\]\\[\\, ]", "");

                Log.e(TAG, transmitValue);
                final int frequency = Integer.valueOf(frequencyEditText.getText().toString());

                transmitRawTextView.setText(transmitValue + " will be transmitted.");

                new Thread() {
                    public void run() {
                        transmitData(transmitBits, frequency);
                    }
                }.start();
            }
        });
    }

    private void transmitData(Byte[] data, int frequency) {

        Camera camera = Camera.open();

        int millisInSecond = 1000;
        int period = millisInSecond / (frequency);
        Camera.Parameters parameters = camera.getParameters();
        for(byte bit: data) {
            synchronized (shouldTransmissionStop) {
                if (shouldTransmissionStop) {
                    break;
                }
            }

            try {
                if (bit == 1) {
                    parameters.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
                    camera.setParameters(parameters);
                    camera.startPreview();
                    sleep(period);
                } else {
                    parameters.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
                    camera.setParameters(parameters);
                    camera.stopPreview();
                    sleep(period);
                }
            } catch (InterruptedException e) {
                Log.w(TAG, "InterruptedException");
            }
        }
        parameters.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
        camera.setParameters(parameters);
        camera.stopPreview();

        camera.release();
    }

    @Override
    public void onResume() {
        super.onResume();
        synchronized (shouldTransmissionStop) {
            shouldTransmissionStop = false;
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        synchronized (shouldTransmissionStop) {
            shouldTransmissionStop = true;
        }
    }
}

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
import com.catinthedark.flash_transmitter.lib.algorithm.CompressedScheme;
import com.catinthedark.flash_transmitter.lib.algorithm.Converter;
import com.catinthedark.flash_transmitter.lib.algorithm.ManchesterLineCoder;

import static java.lang.Thread.sleep;

public class TransmitActivity extends Activity{

    public final String TAG = "FlashTransmitter";
    private TextView transmitRawTextView;
    private Boolean shouldTransmissionStop = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        Button transmitButton = (Button) findViewById(R.id.transmitButton);
        transmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText transmitEditText = (EditText) findViewById(R.id.transmitText);
                EditText frequencyEditText = (EditText) findViewById(R.id.frequencyText);
                transmitRawTextView = (TextView) findViewById(R.id.transmitRawTextView);

                String transmitString = transmitEditText.getText().toString();

                // #TODO make dependency injection instead hardcode!!!
                CompressedScheme scheme = new CompressedScheme();
                ManchesterLineCoder coder = new ManchesterLineCoder();
                Converter converter = new Converter(scheme, coder);
                Byte[] transmitBits = converter.makeBits(transmitString);

                final String transmitValue = "0101010101010101" + transmitBits.toString().replaceAll("[\\]\\[\\, ]", "");

                Log.e(TAG, transmitValue);
                final int frequency = Integer.valueOf(frequencyEditText.getText().toString());

                transmitRawTextView.setText(transmitValue + " will be transmitted.");

                new Thread() {
                    public void run() {
                        transmitData(transmitValue, frequency);
                    }
                }.start();
            }
        });
    }

    private void transmitData(String text, int frequency) {

        Camera camera = Camera.open();

        int millisInSecond = 1000;
        int period = millisInSecond / (frequency);
        Camera.Parameters parameters = camera.getParameters();
        for (int i = 0; i < text.length(); i++) {
            synchronized (shouldTransmissionStop) {
                if (shouldTransmissionStop) {
                    break;
                }
            }
            char currentSymbol = text.charAt(i);
            try {
                if (currentSymbol == '1') {
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

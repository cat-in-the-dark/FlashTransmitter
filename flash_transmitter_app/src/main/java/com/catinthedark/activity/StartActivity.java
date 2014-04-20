package com.catinthedark.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.catinthedark.R;

/**
 * User: kirill
 * Date: 01.03.14
 */

public class StartActivity extends Activity {

    public final String TAG = "FlashTransmitter";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.start);

        Spinner spinner = (Spinner) findViewById(R.id.encodingDropdown);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.encodings_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        Button transmitActivityButton = (Button) findViewById(R.id.transmitActivityButton);
        transmitActivityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(StartActivity.this, TransmitActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

        Button receiveActivityButton = (Button) findViewById(R.id.receiveActivityButton);
        receiveActivityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(StartActivity.this, ReceiveActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event){
        if(keyCode == KeyEvent.KEYCODE_BACK){
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }
}
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
import com.catinthedark.flash_transmitter.lib.factories.EncodingSchemeFactory;
import com.catinthedark.flash_transmitter.lib.factories.ErrorCorrectionFactory;
import com.catinthedark.flash_transmitter.lib.factories.LineCoderFactory;
import com.catinthedark.flash_transmitter.lib.factories.LogicalCodeFactory;

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

        final Spinner spinnerCoder = (Spinner) findViewById(R.id.encodingDropdown);
        ArrayAdapter<String> adapterEncoding = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, LineCoderFactory.getCodersNames());
        adapterEncoding.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCoder.setAdapter(adapterEncoding);

        final Spinner spinnerScheme = (Spinner) findViewById(R.id.schemeDropdown);
        ArrayAdapter<String> adapterScheme = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, EncodingSchemeFactory.getSchemesNames());
        adapterScheme.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerScheme.setAdapter(adapterScheme);

        final Spinner spinnerLogicalCode = (Spinner) findViewById(R.id.logicalDropdown);
        ArrayAdapter<String> adapterLogicalCode = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, LogicalCodeFactory.getLogicalCodesNames());
        adapterLogicalCode.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerLogicalCode.setAdapter(adapterLogicalCode);

        final Spinner spinnerErrorCorrection = (Spinner) findViewById(R.id.errorCorrectionDropdown);
        ArrayAdapter<String> adapterErrorCorrection = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, ErrorCorrectionFactory.getErrorCorrectionNames());
        adapterErrorCorrection.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerErrorCorrection.setAdapter(adapterErrorCorrection);

        Button transmitActivityButton = (Button) findViewById(R.id.transmitActivityButton);
        transmitActivityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(StartActivity.this, TransmitActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("encoding_scheme_name", spinnerScheme.getSelectedItem().toString());
                intent.putExtra("line_coder_name", spinnerCoder.getSelectedItem().toString());
                intent.putExtra("logical_code_name", spinnerLogicalCode.getSelectedItem().toString());
                intent.putExtra("error_correction_name", spinnerErrorCorrection.getSelectedItem().toString());
                startActivity(intent);
            }
        });

        Button receiveActivityButton = (Button) findViewById(R.id.receiveActivityButton);
        receiveActivityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(StartActivity.this, ReceiveActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("encoding_scheme_name", spinnerScheme.getSelectedItem().toString());
                intent.putExtra("line_coder_name", spinnerCoder.getSelectedItem().toString());
                intent.putExtra("logical_code_name", spinnerLogicalCode.getSelectedItem().toString());
                intent.putExtra("error_correction_name", spinnerErrorCorrection.getSelectedItem().toString());
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
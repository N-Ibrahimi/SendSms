package com.example.sendsms;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    public static final String ENVOIE_SMS ="com.example.android.apis.os.SMS_SEND_ACTION";
    public EditText smsText;
    public EditText phoneNumber;
    public Button sendBtn;
    // creation de l'activité
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        smsText = (EditText) findViewById(R.id.sms);
        phoneNumber = (EditText) findViewById(R.id.number);
        sendBtn = (Button) findViewById(R.id.send);
        sendBtn.setOnClickListener(sendMe);
    }


    //cliquer sur le button
    private View.OnClickListener sendMe = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            if (checkInput()) {
                createdDialog(0).show();
            } else {
                SmsManager sm = SmsManager.getDefault();
                sm.sendTextMessage(phoneNumber.getText().toString(), null,
                        smsText.getText().toString(), PendingIntent.getBroadcast(MainActivity.this, 0, new Intent(ENVOIE_SMS), 0), null);

                registerReceiver(new BroadcastReceiver() {
                    @Override
                    public void onReceive(Context context, Intent intent) {
                        smsText.setText("");
                        phoneNumber.setText("");
                        String message = null;
                        switch (getResultCode()) {
                            case Activity.RESULT_OK:
                                message = "SMS sent";
                                break;
                            case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                                message = "Error";
                                break;
                            case SmsManager.RESULT_ERROR_NO_SERVICE:
                                message = "Error: NO service";
                                break;
                            case SmsManager.RESULT_ERROR_NULL_PDU:
                                message = "Error: Null PDU";
                                break;
                            case SmsManager.RESULT_ERROR_RADIO_OFF:
                                message = "Error: Radio off";
                                break;
                        }
                        Toast.makeText(getBaseContext(), message, Toast.LENGTH_SHORT).show();
                    }
                }, new IntentFilter(ENVOIE_SMS));
            }
        }
    };
    // error si le numéro ou text n'existe pas
    protected Dialog createdDialog(int i){
        return new AlertDialog.Builder(MainActivity.this).setTitle("Error:Numéro ou text sms absent")
                .setPositiveButton("ok", new DialogInterface.OnClickListener(){
                    public  void onClick(DialogInterface dialog, int WhichButton){
                    }
                }).create();
    }
    // la validité d'entré
    protected  boolean checkInput(){
        if((phoneNumber.getText().toString()=="0") || (smsText.getText().length()==0) || (phoneNumber.getText().length()==0)) {
            return true;
        }else{
            return false;
        }
    }
}

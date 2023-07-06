package com.vsl.otp_brodcastreceiver;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.widget.EditText;

import com.google.android.gms.auth.api.phone.SmsRetriever;
import com.google.android.gms.auth.api.phone.SmsRetrieverClient;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {

    private static final int REQ_USER_CONSENT = 200;
    SmsBroadcastReceiver smsBroadcastReceiver;
    EditText etOTP;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etOTP= findViewById(R.id.enterOtp);
//        requestPermissions();
        startSmartUserConstant();
//        new OTPReceiver();
//        OTPReceiver.setEditText_otp(editText);
    }

    private void startSmartUserConstant() {
        SmsRetrieverClient client = SmsRetriever.getClient(this);
        client.startSmsUserConsent(null);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQ_USER_CONSENT){
            if ((resultCode == RESULT_OK) && (data != null)){
                String message = data.getStringExtra(SmsRetriever.EXTRA_SMS_MESSAGE);
                getOtpFromMessage(message);

            }
        }
    }

    private void getOtpFromMessage(String message) {
        Pattern otpPattern = Pattern.compile("(|^)\\d{6}");
        Matcher matcher = otpPattern.matcher(message);
        if (matcher.find()){
            etOTP.setText(matcher.group(0));
        }
    }

    private void registerBroadcastReceiver(){
        smsBroadcastReceiver = new SmsBroadcastReceiver();
        smsBroadcastReceiver.smsBroadcastReceiverListener = new SmsBroadcastReceiver.SmsBroadcastReceiverListener() {
            @Override
            public void onSuccess(Intent intent) {
                startActivityForResult(intent,REQ_USER_CONSENT);
            }
            @Override
            public void onFailure() {

            }
        };

        IntentFilter intentFilter = new IntentFilter(SmsRetriever.SMS_RETRIEVED_ACTION);
        registerReceiver(smsBroadcastReceiver,intentFilter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        registerBroadcastReceiver();

    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(smsBroadcastReceiver);
    }
}
//    private void requestPermissions() {
//
//        // when button click
////        if (ContextCompat.checkSelfPermission(MainActivity.this, android.Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED){
////
////        }else {
////            ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.SEND_SMS}, 100);
////        }
//
//        //For Auto popuo permission
//        if (ContextCompat.checkSelfPermission(MainActivity.this, android.Manifest.permission.RECEIVE_SMS) != PackageManager.PERMISSION_GRANTED){
//            ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.RECEIVE_SMS}, 100);
//        }
//
//    }
//
//
//}
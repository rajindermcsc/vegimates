package com.websoftquality.vegimate;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.sinch.android.rtc.calling.Call;

public class PlaceCallActivity extends BaseActivity {

    private Button mCallButton;
    private EditText mCallName;
    String username;
    Intent intent;

    @Override
    protected void onResume() {
        super.onResume();
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        intent=getIntent();
        username=intent.getStringExtra("username");
        Log.e("TAG", "onCreate: "+username);
        mCallName = (EditText) findViewById(R.id.callName);
        mCallButton = (Button) findViewById(R.id.callButton);
        mCallButton.setEnabled(false);
        mCallButton.setOnClickListener(buttonClickListener);
        mCallName.setText(username);
        Button stopButton = (Button) findViewById(R.id.stopButton);

        stopButton.setOnClickListener(buttonClickListener);
    }

    @Override
    protected void onServiceConnected() {
        TextView userName = (TextView) findViewById(R.id.loggedInName);
        userName.setText(getSinchServiceInterface().getUserName());
        mCallButton.setEnabled(true);
        Call call = getSinchServiceInterface().callUserVideo(username);
        String callId = call.getCallId();
        Log.e("TAG", "callButtonClicked: "+callId);

        Intent callScreen = new Intent(this, CallScreenActivity.class);
        callScreen.putExtra(SinchService.CALL_ID, callId);
        startActivity(callScreen);
    }



    private void stopButtonClicked() {
        if (getSinchServiceInterface() != null) {
            getSinchServiceInterface().stopClient();
        }
        finish();
    }

    private void callButtonClicked() {
        String userName = mCallName.getText().toString();
        if (userName.isEmpty()) {
            Toast.makeText(this, "Please enter a user to call", Toast.LENGTH_LONG).show();
            return;
        }

        Call call = getSinchServiceInterface().callUserVideo(username);
        String callId = call.getCallId();
        Log.e("TAG", "callButtonClicked: "+callId);

        Intent callScreen = new Intent(this, CallScreenActivity.class);
        callScreen.putExtra(SinchService.CALL_ID, callId);
        startActivity(callScreen);


    }

    private OnClickListener buttonClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.callButton:
                    callButtonClicked();
                    break;

                case R.id.stopButton:
                    stopButtonClicked();
                    break;

            }
        }
    };
}

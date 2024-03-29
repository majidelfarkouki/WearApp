package com.app.wearapp;


import android.widget.TextView;

import android.content.Intent;
import android.os.Bundle;
import android.support.wearable.activity.WearableActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextClock;

import com.app.wearapp.R;

public class MainActivity extends WearableActivity {

    private static final String TAG = MainActivity.class.getName();

    private Button mBTSendMessage;
    private Button mBTListMessages;
    private TextClock mClock;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initializeViews();

        // Enables Always-on
        setAmbientEnabled();
    }

    public void initializeViews()
    {
        mBTSendMessage =  findViewById(R.id.bt_send_message);
        mBTListMessages = findViewById(R.id.bt_message_list);


        mClock.setVisibility(View.INVISIBLE);

        mBTSendMessage.setOnClickListener(
                new View.OnClickListener(){
                    public void onClick(View v) {
                        Intent intent = new Intent(v.getContext(), SendMessage.class);
                        v.getContext().startActivity(intent);
                    }
                }
        );

        mBTListMessages.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View v) {
                        Intent intent = new Intent(v.getContext(), MessagesList.class);
                        v.getContext().startActivity(intent);
                    }

                }
        );
    }

    @Override
    public void onEnterAmbient(Bundle ambientDetails)
    {
        super.onEnterAmbient(ambientDetails);

        mClock.setFormat12Hour(null);
        //textClock.setFormat24Hour("dd/MM/yyyy hh:mm:ss a");
        mClock.setFormat24Hour("hh:mm:ss a EEE MMM d");

        mBTSendMessage.setVisibility(View.INVISIBLE);
        mBTListMessages.setVisibility(View.INVISIBLE);
        mClock.setVisibility(View.VISIBLE);
    }

    @Override
    public void onExitAmbient()
    {
        super.onExitAmbient();
        // Stop clock

        mClock.setVisibility(View.INVISIBLE);
        mBTSendMessage.setVisibility(View.VISIBLE);
        mBTListMessages.setVisibility(View.VISIBLE);
    }
}

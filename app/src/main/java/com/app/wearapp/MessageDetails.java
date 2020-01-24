package com.app.wearapp;

import android.os.Bundle;
import android.support.wearable.activity.WearableActivity;
import android.widget.TextView;

import com.app.wearapp.R;
import com.app.wearapp.Message;
import com.app.wearapp.EventChat;

public class MessageDetails extends WearableActivity{
    private Message mMessage;
    private TextView mTVMessageContent;
    private TextView mTVSender;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_details);

        mTVMessageContent = (TextView) findViewById(R.id.message_content);
        mTVSender = (TextView) findViewById(R.id.sender);

        EventChat app = (EventChat) getApplicationContext();
        mMessage = app.getMessage();

        mTVSender.setText(String.format("From %s", String.valueOf(mMessage.getStudent_id())));
        mTVMessageContent.setText(mMessage.getStudent_message());

        // Enables Always-on
        setAmbientEnabled();
    }
}

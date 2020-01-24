package com.app.wearapp;

import android.app.Application;

import com.app.wearapp.Message;

public class EventChat extends Application {
    private Message mMessage;

    public Message getMessage()
    {
        return mMessage;
    }

    public void setMessage(Message message)
    {
        mMessage = message;
    }
}

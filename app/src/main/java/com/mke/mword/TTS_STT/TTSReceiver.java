package com.mke.mword.TTS_STT;

import android.content.*;

public class TTSReceiver extends BroadcastReceiver {
    public static final String ACTION_TTS_READ_MESSAGE = "com.example.action.ACTION_TTS_READ_MESSAGE";
    @Override
    public void onReceive(Context context, Intent intent) {
        // get your SMS message
        // and then start the SMSService passing the message in the intent bundle
        Intent serviceIntent = new Intent(context, TTS.class);
        context.startService(serviceIntent);

        Intent serviceIntent1 = new Intent(context, ServiceClass.class);
        context.startService(serviceIntent1);

    }

}

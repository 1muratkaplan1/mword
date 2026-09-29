package com.mke.mword.Utils;

import android.app.*;
import android.widget.*;

public class ScreenCounterThread extends BASICTHREAD {
    public ScreenCounterThread(Activity activty, TextView textV) {
        tv = textV;
        pActivty = activty;
    }

    Activity pActivty = null;
    TextView tv = null;
    int i = 0;

    public void run() {
        running = true;
        if (pActivty!=null && tv !=null) {
            while (running) {
                pActivty.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (tv != null) {
                            tv.setText(Integer.toString(i));
                        }
                    }
                });
                i++;
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

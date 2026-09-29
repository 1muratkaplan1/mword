package com.mke.mword.Utils;

import static com.mke.mword.Utils.Utils.SENDMESSAGESCREENWINDOW;

import android.app.*;
import android.content.*;
import android.net.*;
import android.view.*;
import android.widget.*;

public class BASICTHREAD extends Thread {

    public boolean running = false;
    public static int nCloseThread=0;
    public boolean isRunning() {
        return running;
    }
    ScreenCounterThread T1=null;


    public  void SLEEP (int nTime) {
        if(!isRunning()){
            return;
        }
        try {
            sleep(nTime);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    public void STOPSCREENTALKCOUNTER(){
        if (T1!=null && T1.isRunning()){
            T1.stopRunning();
        }
    }
    public void stopRunning() {
//        Log.e("ERORRRRRRRRRRRRRRRERRRRRRRR", "stopRunning:"+ nCloseThread++);
        running = false;
        if (T1!= null && T1.isRunning()){T1.stopRunning();}
    }
}

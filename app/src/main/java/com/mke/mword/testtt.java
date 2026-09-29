package com.mke.mword;

import static com.mke.mword.Database.Constants.ABOUT_US_HTML;
import static com.mke.mword.Database.Constants.HELP_CENTER_WEB_VIEW_CURRENT_FILE;
import static com.mke.mword.Utils.Utils.SENDMESSAGE;

import androidx.appcompat.app.AppCompatActivity;

import android.os.*;
import android.webkit.*;
import android.widget.*;

public class testtt extends AppCompatActivity {
    private static final int STOPSPLASH = 0;
    //time in milliseconds
    private static final long SPLASHTIME = 20000;


    private ImageView splash;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_testtt);

        WebView view =  (WebView) findViewById(R.id.testt_webview);
        view.getSettings().setJavaScriptEnabled(true);
        view.loadUrl(HELP_CENTER_WEB_VIEW_CURRENT_FILE);
    }

    private void STOPSPLASH(){
        Message msg = new Message();
        msg.what = STOPSPLASH;
        splashHandler.sendMessageDelayed(msg, SPLASHTIME);
    }

    //handler for splash screen
    private Handler splashHandler = new Handler() {
        /* (non-Javadoc)
         * @see android.os.Handler#handleMessage(android.os.Message)
         */
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case STOPSPLASH:
                    //remove SplashScreen from view
//                    splash.setVisibility(View.GONE);
                    finish();
                    break;
            }
            super.handleMessage(msg);
        }
    };
    @Override
    protected void onStop() {
        STOPSPLASH();
        super.onStop();
    }
    @Override
    public void onBackPressed() {
        STOPSPLASH();
        super.onBackPressed();
    }
}
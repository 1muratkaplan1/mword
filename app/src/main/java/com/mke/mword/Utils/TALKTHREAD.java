package com.mke.mword.Utils;

import static com.mke.mword.Database.Constants.CURRIENTPAGETYPE;
import static com.mke.mword.Database.Constants.MESSAGE_SCREENVIEW_CHANGE_COLOR_SCREEN;
import static com.mke.mword.Database.Constants.MESSAGE_SCREENVIEW_PAGE_END;
import static com.mke.mword.Database.Constants.MESSAGE_SCREENVIEW_SETWORD_LEARNED;
import static com.mke.mword.Database.Constants.MESSAGE_SCREENVIEW_SHOW_SCREEN;
import static com.mke.mword.Database.Constants.MESSAGE_SCREENVIEW_VISIBLE_REMOVE_BUTTON;
import static com.mke.mword.Database.mkDB.nPageNumber;
import static com.mke.mword.Database.mkDB.nWordNumber;
import static com.mke.mword.MainActivity.g_Activity;
import static com.mke.mword.MainActivity.mkdb;
import static com.mke.mword.ScreenView.gSreencx;
import static com.mke.mword.ScreenView.szTalkString;
import static com.mke.mword.Utils.Utils.SENDMESSAGESCREENWINDOW;
import static com.mke.mword.Utils.Utils.SENDMESSAGEWRITESCREENWINDOW;

import android.app.*;
import android.graphics.drawable.*;
import android.speech.tts.*;
import android.widget.*;

import com.mke.mword.*;

import java.util.*;

public class TALKTHREAD extends BASICTHREAD {
    Activity threadActivty = null;
    TextView tvThreadScreeen = null;
    TextView tvThreadResults = null;
    TextView tvThreadCounter = null;
    boolean bIsPushRemove = false;
    TextToSpeech textToSpeech=null;
    public TALKTHREAD(Activity activity, TextView tvSC, TextView tvRes, TextView tvCount) {
        threadActivty = activity;
        tvThreadScreeen = tvSC;
        tvThreadResults = tvRes;
        tvThreadCounter = tvCount;
    }

    public void PushRemovoBtn() {
        bIsPushRemove = true;
    }
    public void SpeechText(String szText) {
        textToSpeech = new TextToSpeech(g_Activity.getApplicationContext(), new
                TextToSpeech.OnInitListener() {
                    @Override
                    public void onInit(int status) {
                        if (status != TextToSpeech.ERROR) {
                            textToSpeech.setLanguage(Locale.US);
                        }
                    }
                });
        textToSpeech.speak(szText, TextToSpeech.QUEUE_FLUSH, null, null);
    }
    public void run() {
        running = true;
        boolean bReadAllList=false;
        for (int n = mkdb.bContinueLastWord ? mkdb.
                GetPageLastWVSNumber(CURRIENTPAGETYPE, nPageNumber) : 0;
             n < mkdb.GetWVSSize(CURRIENTPAGETYPE, nPageNumber) && running; n++) {

            if (mkdb.GetWVSIsLearn(CURRIENTPAGETYPE, nPageNumber, n)) {
                continue;
            }
            SENDMESSAGESCREENWINDOW(MESSAGE_SCREENVIEW_VISIBLE_REMOVE_BUTTON);

            if (T1 != null && T1.isRunning()) {
                T1.stopRunning();
            }
            T1 = new ScreenCounterThread(threadActivty, tvThreadCounter);
            T1.start();
            tvThreadResults.setText("");
            SLEEP(500);
            szTalkString = mkdb.GetEngWVS(CURRIENTPAGETYPE, nPageNumber, n);
            bIsPushRemove = false;
            nWordNumber = n;
            mkdb.SetPageLastWVSNumber(CURRIENTPAGETYPE, nPageNumber, n);

            SENDMESSAGESCREENWINDOW(MESSAGE_SCREENVIEW_SHOW_SCREEN, nPageNumber, n);
            if (mkdb.bEleminateMode){
                SpeechText(mkdb.GetEngWVS(CURRIENTPAGETYPE, nPageNumber, n));
            }
            int nSleepTime = 1000;
            while (nSleepTime < (mkdb.nWaitTalkTime * 1000) && !bIsPushRemove) {
                if (szTalkString.toLowerCase().trim().contains("congrulations")||
                    (mkdb.bEleminateMode && szTalkString.toLowerCase().trim().contains("remove"))||
                        (mkdb.bEleminateMode && szTalkString.toLowerCase().trim().contains("next"))) {
                    STOPSCREENTALKCOUNTER();
                    SENDMESSAGESCREENWINDOW(MESSAGE_SCREENVIEW_SETWORD_LEARNED);
                    SENDMESSAGESCREENWINDOW(MESSAGE_SCREENVIEW_SHOW_SCREEN, nPageNumber, nWordNumber);

                } else {
                    SLEEP(500);
                    nSleepTime += 500;
                }
            }
            STOPSCREENTALKCOUNTER();
            if ((n+1) == mkdb.GetWVSSize(CURRIENTPAGETYPE, nPageNumber)){
                bReadAllList=true;
            }
        }
        if(bReadAllList) {
            SENDMESSAGESCREENWINDOW(MESSAGE_SCREENVIEW_PAGE_END);
        }

        running = false;
    }
}

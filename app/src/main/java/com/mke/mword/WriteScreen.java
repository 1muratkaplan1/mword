package com.mke.mword;

import static com.mke.mword.Database.Constants.CURRIENTPAGETYPE;
import static com.mke.mword.Database.Constants.DO_YOU_WANT_TO_CONTINUE_WRITE_EXAM;
import static com.mke.mword.Database.Constants.DO_YOU_WANT_TO_LISTEN_THIS_PAGE_ONEMORE;
import static com.mke.mword.Database.Constants.DO_YOU_WANT_TO_START_WRITE_EXAM;
import static com.mke.mword.Database.Constants.MESSAGE_SAVE_DOCUMENT;
import static com.mke.mword.Database.Constants.MESSAGE_SCREENVIEW_CHANGE_COLOR_SCREEN;
import static com.mke.mword.Database.Constants.MESSAGE_SCREENVIEW_FOCUS_WRITE_SCREEN;
import static com.mke.mword.Database.Constants.MESSAGE_SCREENVIEW_NEXT_WORD_BUTTON;
import static com.mke.mword.Database.Constants.MESSAGE_SCREENVIEW_PAGE_END;
import static com.mke.mword.Database.Constants.MESSAGE_SCREENVIEW_PAGE_START;
import static com.mke.mword.Database.Constants.MESSAGE_SCREENVIEW_PAUSE_BUTTON_CLICK;
import static com.mke.mword.Database.Constants.MESSAGE_SCREENVIEW_PLAY_BUTTON_CLICK;
import static com.mke.mword.Database.Constants.MESSAGE_SCREENVIEW_PREV_WORD_BUTTON;
import static com.mke.mword.Database.Constants.MESSAGE_SCREENVIEW_SETWORD_LEARNED;
import static com.mke.mword.Database.Constants.MESSAGE_SCREENVIEW_SHOW_SCREEN;
import static com.mke.mword.Database.Constants.MESSAGE_SCREENVIEW_UNVISIBLE_NEXT_BUTTON;
import static com.mke.mword.Database.Constants.MESSAGE_SCREENVIEW_UNVISIBLE_PREV_BUTTON;
import static com.mke.mword.Database.Constants.MESSAGE_SCREENVIEW_VISIBLE_NEXT_BUTTON;
import static com.mke.mword.Database.Constants.MESSAGE_SCREENVIEW_VISIBLE_PREV_BUTTON;
import static com.mke.mword.Database.Constants.MESSAGE_UPDATE_CURRENT_FRAGMENT_LISTVIEW;
import static com.mke.mword.Database.Constants.SCREENPAGESTATUS;
import static com.mke.mword.Database.Constants.SCREEN_PAGE_WRITE;
import static com.mke.mword.Database.mkDB.nPageNumber;
import static com.mke.mword.Database.mkDB.nWordNumber;
import static com.mke.mword.MainActivity.mkdb;
import static com.mke.mword.Utils.Utils.GetInfoScreen;
import static com.mke.mword.Utils.Utils.GetSpeakWord;
import static com.mke.mword.Utils.Utils.JUSTSLEEP;
import static com.mke.mword.Utils.Utils.SENDMESSAGE;
import static com.mke.mword.Utils.Utils.SENDMESSAGEEXAMSCREENWINDOW;
import static com.mke.mword.Utils.Utils.SENDMESSAGESCREENWINDOW;
import static com.mke.mword.Utils.Utils.SENDMESSAGEWRITESCREENWINDOW;

import static java.lang.Thread.sleep;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.*;

import android.app.*;
import android.content.*;
import android.graphics.drawable.*;
import android.os.*;
import android.view.*;
import android.view.inputmethod.*;
import android.widget.*;

import com.mke.mword.Utils.*;
import com.google.android.material.floatingactionbutton.*;

public class WriteScreen extends AppCompatActivity {
    private FloatingActionButton fab_next,fab_prev,fab_play;
    private TextView tvSCreen,tvInformation,tvCounter,tvResults;
    private EditText edSCreen;
    public static Activity gWriteScreenActivity;
    public static Context  gWriteScreencontext;
    boolean flag = true,bWriteWordLearn=false,
            bNextBtn=false,bPrevBtn=false;
    ScreenCounterThread T1=null;
    WRITETHREAD writethread=null;
    int nCounter=0;

    public static Handler mHandlerWriteScreenWindow    = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_screen);

        gWriteScreenActivity=this;
        gWriteScreencontext=this;

        tvInformation = (TextView) findViewById(R.id.textview_writescreen_information);
        tvInformation.setText(GetInfoScreen(CURRIENTPAGETYPE,nPageNumber,mkdb.
                GetPageLastWVSNumber(CURRIENTPAGETYPE,nPageNumber)));

        tvSCreen = (TextView) findViewById(R.id.textview_view_native_word);
        tvSCreen.setText(DO_YOU_WANT_TO_START_WRITE_EXAM);

        edSCreen = (EditText) findViewById(R.id.textview_write_eng_word);
        edSCreen.setText(DO_YOU_WANT_TO_START_WRITE_EXAM);

        tvCounter = (TextView) findViewById(R.id.textview_write_screen_counter);
        tvResults = (TextView) findViewById(R.id.textview_write_screen_resutls);
        tvCounter.setVisibility(View.GONE);
        tvResults.setVisibility(View.GONE);

        fab_next = (FloatingActionButton) findViewById(R.id.fab_write_screen_next);
        fab_next.setScaleType(ImageView.ScaleType.FIT_CENTER);
        fab_next.setVisibility(View.GONE);
        fab_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SENDMESSAGEWRITESCREENWINDOW(MESSAGE_SCREENVIEW_NEXT_WORD_BUTTON);
            }
        });
        fab_prev = (FloatingActionButton) findViewById(R.id.fab_write_screen_prev);
        fab_prev.setScaleType(ImageView.ScaleType.FIT_CENTER);
        fab_prev.setVisibility(View.GONE);
        fab_prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SENDMESSAGEWRITESCREENWINDOW(MESSAGE_SCREENVIEW_PREV_WORD_BUTTON);
            }
        });
        fab_play = (FloatingActionButton) findViewById(R.id.fab_write_screen_play);
        fab_play.setScaleType(ImageView.ScaleType.FIT_CENTER);
        fab_play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (flag) {
                    SENDMESSAGEWRITESCREENWINDOW(MESSAGE_SCREENVIEW_PLAY_BUTTON_CLICK);
                }else{
                    SENDMESSAGEWRITESCREENWINDOW(MESSAGE_SCREENVIEW_PAUSE_BUTTON_CLICK);
                }
            }
        });

        mHandlerWriteScreenWindow = new Handler() {
            @Override
            public void handleMessage(android.os.Message msg) {
                switch (msg.arg1) {
                    case MESSAGE_SCREENVIEW_PAGE_END:
                        mkdb.SetPageLastWVSNumber(CURRIENTPAGETYPE,nPageNumber,0);
                        mkdb.SetPageWatchCount(CURRIENTPAGETYPE,nPageNumber);
                        tvSCreen.setText(DO_YOU_WANT_TO_START_WRITE_EXAM);
                        STOP();
                        break;
                    case MESSAGE_SCREENVIEW_SHOW_SCREEN: //opens contacts application to browse contacts
                        tvInformation.setText(GetInfoScreen(CURRIENTPAGETYPE, msg.arg2, (int) msg.obj));
                       if (SCREENPAGESTATUS==SCREEN_PAGE_WRITE) {
                            tvSCreen.setText(GetSpeakWord(CURRIENTPAGETYPE, msg.arg2, (int) msg.obj,
                                                                                        bWriteWordLearn));
                        }
                        break;
                    case MESSAGE_SCREENVIEW_SETWORD_LEARNED:
                        mkdb.SetWVSLearn(CURRIENTPAGETYPE,nPageNumber,nWordNumber);
                        break;
                    case MESSAGE_SCREENVIEW_VISIBLE_PREV_BUTTON:
                        if (nCounter!=0 && fab_prev.getVisibility()==View.GONE){
                            fab_prev.setVisibility(View.VISIBLE);
                        }
                        break;
                    case MESSAGE_SCREENVIEW_UNVISIBLE_PREV_BUTTON:
                        if (fab_prev.getVisibility()!=View.GONE){
                            fab_prev.setVisibility(View.GONE);
                        }
                        break;
                    case MESSAGE_SCREENVIEW_VISIBLE_NEXT_BUTTON:
                        if (fab_next.getVisibility()!=View.VISIBLE){
                            fab_next.setVisibility(View.VISIBLE);
                        }
                        break;
                    case MESSAGE_SCREENVIEW_UNVISIBLE_NEXT_BUTTON:
                        if (fab_next.getVisibility()!=View.GONE){
                            fab_next.setVisibility(View.GONE);
                        }
                        break;
                    case MESSAGE_SCREENVIEW_PAGE_START:
                        break;
                    case MESSAGE_SCREENVIEW_PLAY_BUTTON_CLICK:
                        if (!mkdb.CheckThisPageWVSsIsLearn(CURRIENTPAGETYPE,nPageNumber)) {
                            PLAY();
                        }else{
                            AlertDialog.Builder builder = new AlertDialog.Builder(gWriteScreencontext);
                            builder.setTitle(DO_YOU_WANT_TO_LISTEN_THIS_PAGE_ONEMORE);
                            builder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    mkdb.SetPageMakeLearn(CURRIENTPAGETYPE,nPageNumber,false);
                                    PLAY();
                                }
                            });
                            builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    //TODO
                                    dialog.dismiss();
                                }
                            });
                            AlertDialog dialog1 = builder.create();
                            dialog1.show();
                        }
                        break;
                    case MESSAGE_SCREENVIEW_PAUSE_BUTTON_CLICK:
                        tvSCreen.setText(DO_YOU_WANT_TO_CONTINUE_WRITE_EXAM);
                        STOP();
                        break;
                    case MESSAGE_SCREENVIEW_CHANGE_COLOR_SCREEN:
                        edSCreen.setBackgroundColor((int)msg.obj);
                        break;
                    case MESSAGE_SCREENVIEW_FOCUS_WRITE_SCREEN:
                        edSCreen.setText("");
                        edSCreen.requestFocus();
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.showSoftInput(edSCreen, InputMethodManager.SHOW_IMPLICIT);
                        break;
                    case MESSAGE_SCREENVIEW_NEXT_WORD_BUTTON:
                        bNextBtn =true;
                        break;
                    case MESSAGE_SCREENVIEW_PREV_WORD_BUTTON:
                        bPrevBtn =true;
                        break;
                    default:
                        break;
                }
            }
        };
    }
    @Override
    public void onBackPressed() {
        stopRunningThreads();
        SENDMESSAGE(MESSAGE_UPDATE_CURRENT_FRAGMENT_LISTVIEW);
        SENDMESSAGE(MESSAGE_SAVE_DOCUMENT);
        super.onBackPressed();
    }
    private void PLAY(){
        fab_play.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),
                R.drawable.ic_pause_button_64));
        flag = false;
        fab_prev.setVisibility(View.GONE);
        fab_next.setVisibility(View.VISIBLE);
        tvCounter.setVisibility(View.VISIBLE);
        tvResults.setVisibility(View.GONE);
        if (SCREENPAGESTATUS.trim().equals(SCREEN_PAGE_WRITE)){
            writethread = new WRITETHREAD();
            writethread.start();
        }
    }
    private void STOP(){
        stopRunningThreads();
        fab_play.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),
                R.drawable.ic_play_button_64));
        flag = true;
        fab_prev.setVisibility(View.GONE);
        fab_next.setVisibility(View.GONE);
        tvCounter.setVisibility(View.GONE);
        tvResults.setVisibility(View.GONE);
    }
    private int IsHavePrevWords (){
        for (int k = nCounter-1; k >= 0; k--) {
            if (mkdb.GetWVSIsLearn(CURRIENTPAGETYPE, nPageNumber, k)) {
                continue;
            } else {
                return k;
            }
        }
        return -1;
    }
    private int IsHaveNextWords (){
        for (int k = nCounter +1; k < mkdb.GetWVSSize(CURRIENTPAGETYPE, nPageNumber); k++) {
            if (mkdb.GetWVSIsLearn(CURRIENTPAGETYPE, nPageNumber, k)) {
                continue;
            } else {
                return k;
            }
        }
        return -1;
    }
    private int GetCount () {
        int nLastword = -1;
        if (bNextBtn || bPrevBtn) {
            if (bNextBtn) {
                nLastword = IsHaveNextWords();
                if (nLastword != -1) {
                    bNextBtn = false;
                    nCounter = nLastword;
                    return nCounter;
                }
            } else if (bPrevBtn) {
                nLastword = IsHavePrevWords();
                if (nLastword != -1) {
                    bPrevBtn = false;
                    nCounter = nLastword;
                    return nCounter;
                }
            }
        }else {
            nCounter++;
        }
        return -1;
    }
    private int GetStartPoint (){
        int nLastword=0;
        if (mkdb.bContinueLastWord) {
            nLastword = mkdb.GetPageLastWVSNumber(CURRIENTPAGETYPE, nPageNumber);
            if (nLastword >= mkdb.GetWVSSize(CURRIENTPAGETYPE, nPageNumber)) {
                nLastword = 0;
            }
        }
        return nLastword;
    }
    class WRITETHREAD extends BASICTHREAD {
        public void run() {
            running = true;

            for (nCounter=GetStartPoint();
                        nCounter<mkdb.GetWVSSize(CURRIENTPAGETYPE,nPageNumber) &&running;GetCount ()) {

                if (nCounter== -1) {break;}
                if (mkdb.GetWVSIsLearn(CURRIENTPAGETYPE, nPageNumber, nCounter)) {
                    continue;
                }

                bWriteWordLearn=false;
                bNextBtn=false;bPrevBtn=false;
                nWordNumber=nCounter;

                SENDMESSAGEWRITESCREENWINDOW(MESSAGE_SCREENVIEW_SHOW_SCREEN,nPageNumber,nCounter);
                SENDMESSAGEWRITESCREENWINDOW(MESSAGE_SCREENVIEW_FOCUS_WRITE_SCREEN);

                if (T1!= null && T1.isRunning()){T1.stopRunning();JUSTSLEEP(250);}
                T1 = new ScreenCounterThread(gWriteScreenActivity, tvCounter);
                T1.start();JUSTSLEEP(250);

                mkdb.SetPageLastWVSNumber(CURRIENTPAGETYPE, nPageNumber, nCounter);

                if (IsHaveNextWords () != -1){
                    SENDMESSAGEWRITESCREENWINDOW(MESSAGE_SCREENVIEW_VISIBLE_NEXT_BUTTON);
                }else {
                    SENDMESSAGEWRITESCREENWINDOW(MESSAGE_SCREENVIEW_UNVISIBLE_NEXT_BUTTON);
                }

                if (IsHavePrevWords () != -1){
                    SENDMESSAGEWRITESCREENWINDOW(MESSAGE_SCREENVIEW_VISIBLE_PREV_BUTTON);
                }else {
                    SENDMESSAGEWRITESCREENWINDOW(MESSAGE_SCREENVIEW_UNVISIBLE_PREV_BUTTON);
                }

                WRITETHREADSLEEP(500);

                int nSleepTime=1000;
                while (nSleepTime<(mkdb.nWaitWriteTime*1000)
                            && !bWriteWordLearn && !bNextBtn && !bPrevBtn){
                    if (mkdb.GetEngWVS(CURRIENTPAGETYPE,nPageNumber,nWordNumber).trim().
                                            equals(edSCreen.getText().toString())){
                        bWriteWordLearn=true;
                        STOPSCREENWRITECOUNTER();

                        SENDMESSAGEWRITESCREENWINDOW(MESSAGE_SCREENVIEW_SETWORD_LEARNED);
                        SENDMESSAGEWRITESCREENWINDOW(MESSAGE_SCREENVIEW_SHOW_SCREEN,nPageNumber,nWordNumber);

                        JUSTSLEEP(500);

//                        Drawable dvTmp = tvSCreen.getBackground();
//                        SENDMESSAGESCREENWINDOW(MESSAGE_SCREENVIEW_CHANGE_COLOR_SCREEN,R.color.frg1Color);
//                        JUSTSLEEP(500);
//                        SENDMESSAGESCREENWINDOW(MESSAGE_SCREENVIEW_CHANGE_COLOR_SCREEN,R.color.frg2Color);
//                        JUSTSLEEP(500);
//                        SENDMESSAGESCREENWINDOW(MESSAGE_SCREENVIEW_CHANGE_COLOR_SCREEN,R.color.frg3Color);
//                        JUSTSLEEP(500);
//                        tvSCreen.setBackground(dvTmp);
                    }else {
                        WRITETHREADSLEEP(500);
                        nSleepTime += 500;
                    }
                }
                STOPSCREENWRITECOUNTER();
            }
            if(nCounter >= mkdb.GetWVSSize(CURRIENTPAGETYPE,nPageNumber)) {
                SENDMESSAGEWRITESCREENWINDOW(MESSAGE_SCREENVIEW_PAGE_END);
            }
        }
    }
    @Override
    protected void onStop() {
        stopRunningThreads();
        super.onStop();
    }
    private void stopRunningThreads(){
        STOPSCREENWRITECOUNTER();
        STOPEXAMTHREAD();
    }
    private void STOPSCREENWRITECOUNTER(){
        if (T1!=null && T1.isRunning()){
            T1.stopRunning();
        }
    }
    private void STOPEXAMTHREAD(){
        if (writethread!=null && writethread.isRunning()){
            writethread.stopRunning();
        }
    }
    private void WRITETHREADSLEEP (int nTime) {
        if (!bNextBtn && !bPrevBtn && writethread!=null
                && writethread.isRunning()) {
            try {
                sleep(nTime);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
package com.mke.mword.TTS_STT;

import static com.mke.mword.Database.Constants.CURRIENTPAGETYPE;
import static com.mke.mword.Database.Constants.DB_IDIOMS_PAGES;
import static com.mke.mword.Database.Constants.DB_VERBS_PAGES;
import static com.mke.mword.Database.Constants.MESSAGE_SCREENVIEW_PAGE_END;
import static com.mke.mword.Database.Constants.MESSAGE_SCREENVIEW_PAGE_LOAD_IMAGE;
import static com.mke.mword.Database.Constants.MESSAGE_SCREENVIEW_SHOW_SCREEN;
import static com.mke.mword.Database.Constants.MESSAGE_SCREENVIEW_UPDATE_MKDB;
import static com.mke.mword.Database.Constants.MESSAGE_SCREENVIEW_VISIBLE_REMOVE_BUTTON;
import static com.mke.mword.Database.mkDB.nPageNumber;
import static com.mke.mword.Database.mkDB.nWordNumber;
import static com.mke.mword.MainActivity.mkdb;
import static com.mke.mword.ScreenView.bIsPushRemove;
import static com.mke.mword.Utils.Utils.GetLanguageCode;
import static com.mke.mword.Utils.Utils.GetSpeakWord;
import static com.mke.mword.Utils.Utils.JUSTSLEEP;
import static com.mke.mword.Utils.Utils.SENDMESSAGE;
import static com.mke.mword.Utils.Utils.SENDMESSAGESCREENWINDOW;
import static java.lang.Thread.sleep;

import android.app.*;
import android.content.*;
import android.graphics.drawable.*;
import android.os.*;
import android.speech.tts.*;

import java.io.*;
import java.util.*;

public class TTS extends Service implements TextToSpeech.OnInitListener,
                                TextToSpeech.OnUtteranceCompletedListener {

    public static Locale localeTo = null;
    public static TextToSpeech mTTS;
    boolean running = false;

    private void TTSSLEEP(int nTime) {
        for (int n = 0; n < 4; n++) {
            if (isRunning()) {
                JUSTSLEEP((int) (nTime / 4.0f));
            } else {
                break;
            }
        }
    }

    public boolean isRunning() {
        return running;
    }

    public void stopRunning() {
        running = false;
    }

    @Override
    public void onCreate() {
        mTTS = new TextToSpeech(this, this);
        // This is a good place to set spokenText

    }

    public void SetSpeechRate(float fRate) {
        if (mTTS != null) {
            mTTS.setSpeechRate(fRate);
        }
    }

    private void speakSentence(String sentence) {
        if (mTTS != null && isRunning()) {
            mTTS.speak(sentence, TextToSpeech.QUEUE_FLUSH, null);
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

//        Toast.makeText(this, "Service Started", Toast.LENGTH_LONG).show();
        return Service.START_STICKY;
    }

    private int SetEngLanguage() {
        int result = mTTS.setLanguage(Locale.US);
        if (result != TextToSpeech.LANG_MISSING_DATA && result != TextToSpeech.LANG_NOT_SUPPORTED) {
//            Log.e(TAG, "Text to speech is normal");
        } else {
//            Log.e(TAG, "Text to speech not normal working ERRROR");
        }
        return result;
    }

    private int SetNatLanguage() {
        int result = TextToSpeech.LANG_MISSING_DATA;
        if (mkdb.bReadNativeLang && localeTo == null) {
            localeTo = new Locale(GetLanguageCode(), GetLanguageCode().toUpperCase(Locale.ROOT));
        }
        if (localeTo != null) {
            result = mTTS.setLanguage(localeTo);
            if (result != TextToSpeech.LANG_MISSING_DATA && result != TextToSpeech.LANG_NOT_SUPPORTED) {
//                Log.e(TAG, "Text to speech is normal");
            } else {
//                Log.e(TAG, "Text to speech not normal working ERRROR");
            }
        }
        return result;
    }

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {

            while (running) {
                long lTime = Long.valueOf(mkdb.GetPageTime(CURRIENTPAGETYPE, nPageNumber));
                long lTimeTmp = System.currentTimeMillis();
                running = true;
                for (int n = mkdb.bContinueLastWord ? mkdb.
                        GetPageLastWVSNumber(CURRIENTPAGETYPE, nPageNumber) : 0;
                     n < mkdb.GetWVSSize(CURRIENTPAGETYPE, nPageNumber) ; n++) {

                    if (!isRunning()) {return;}
                    if (mkdb.GetWVSIsLearn(CURRIENTPAGETYPE, nPageNumber, n)) {
                        continue;
                    }
                    if (!isRunning()) {return;}
                    int nSleepTime = mkdb.nWrdSndRepSlepTm;
                    float fVerbIncTime = 1.0f;
                    if (CURRIENTPAGETYPE.trim().equals(DB_VERBS_PAGES)) {
                        fVerbIncTime *= (int) ((mkdb.nVerbSlpTmIncPrcnt + 100) * 0.01f);
                    }

                    if (!isRunning()) {return;}
                    SENDMESSAGESCREENWINDOW(MESSAGE_SCREENVIEW_VISIBLE_REMOVE_BUTTON);

                    if (!isRunning()) {return;}
                    nWordNumber = n;
                    mkdb.SetPageLastWVSNumber(CURRIENTPAGETYPE, nPageNumber, n);
                    SENDMESSAGESCREENWINDOW(MESSAGE_SCREENVIEW_SHOW_SCREEN, nPageNumber, n);

                    SENDMESSAGESCREENWINDOW(MESSAGE_SCREENVIEW_PAGE_LOAD_IMAGE,nPageNumber,n);

                    if (!bIsPushRemove && isRunning()) {
                        float fRate = 1.0f;
                        int nRepeatTime = mkdb.nWordSoundRepeaTime + 1;
                        if (!mkdb.bWordSoundRepeat) {
                            nRepeatTime = 2;
                        }
                        for (int k = 0; k < nRepeatTime; k++) {
                            if (!isRunning()) {return;}
                            if (mkdb.bWordDecreaseSound) {
                                nSleepTime += nSleepTime * mkdb.nWrdSndRepSlepTmIncPrcnt * 0.01f;
                                fRate -= fRate * mkdb.nWordSoundDecreasePercent * 0.01f;
                                if (fRate < 0.0f) {
                                    fRate = 0.20f;
                                }
                                SetSpeechRate(fRate);
                                TTSSLEEP(500);
                            }
                            if (bIsPushRemove) {
                                bIsPushRemove = false;
                                break;
                            }
                            if (!bIsPushRemove) {
                                if (k != nRepeatTime - 1) {
                                    String szSpeak = mkdb.GetEngWVS(CURRIENTPAGETYPE, nPageNumber, n);
                                    if (CURRIENTPAGETYPE.trim().equals(DB_VERBS_PAGES)) {
                                        szSpeak += mkdb.GetVerbsString(nPageNumber, n);
                                    }
                                    SetEngLanguage();
                                    TTSSLEEP(500);
                                    speakSentence(szSpeak);
                                    TTSSLEEP((int) (nSleepTime * fVerbIncTime));
                                } else {
                                    if (mkdb.bReadNativeLang) {
                                        SetSpeechRate(1.0f);
                                        TTSSLEEP(500);
                                        if (!CURRIENTPAGETYPE.trim().equals(DB_IDIOMS_PAGES)) {
                                            SetNatLanguage();
                                        }
                                        TTSSLEEP(500);
                                        speakSentence(mkdb.GetOtherLanguageWVS(CURRIENTPAGETYPE, nPageNumber, n));
                                        TTSSLEEP((int) (nSleepTime * fVerbIncTime * (100 + mkdb.nNatLangSentIncTime) * 0.01f));
                                    }
                                }
                            }
                        }
                    } else {
                        bIsPushRemove = false;
                    }

                    if (lTimeTmp != 0) {
                        lTimeTmp = System.currentTimeMillis() - lTimeTmp;
                        lTime += lTimeTmp;
                        mkdb.SetPageTime(CURRIENTPAGETYPE, nPageNumber, Long.toString(lTime));
                    }
                    lTimeTmp = System.currentTimeMillis();

                    mkdb.SetWVSWatch(CURRIENTPAGETYPE, nPageNumber, n);
                }
                SENDMESSAGESCREENWINDOW(MESSAGE_SCREENVIEW_PAGE_END);
                running = false;
            }
        }
    };
    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {
            running = true;
            new Thread(runnable).start();
        }else{
//            Log.e(TAG, "Text to speech not normal working ERRROR");
        }
    }

    @Override
    public void onUtteranceCompleted(String uttId) {
//        stopSelf();
    }

    @Override
    public void onDestroy() {
        running = false;
        SENDMESSAGESCREENWINDOW(MESSAGE_SCREENVIEW_UPDATE_MKDB);
        if (mTTS != null) {
            mTTS.stop();
            mTTS.shutdown();
        }
    }

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }
}


package com.mke.mword;

import static com.mke.mword.Database.Constants.DO_YOU_WANT_TO_APPLY;
import static com.mke.mword.Database.Constants.DO_YOU_WANT_TO_CHANGE_SOURCE;
import static com.mke.mword.Database.Constants.DO_YOU_WANT_TO_RESET_THIS_WINDOW;
import static com.mke.mword.Database.Constants.DO_YOU_WANT_TO_SEND_MAIL;
import static com.mke.mword.Database.Constants.HELP_CENTER_WEB_VIEW_CURRENT_FILE;
import static com.mke.mword.Database.Constants.LEVEL_VIDEOS;
import static com.mke.mword.Database.Constants.MESSAGE_SETTING_UPDATE_WINDOW;
import static com.mke.mword.Database.Constants.NATIVE_LANGUAGES;
import static com.mke.mword.Database.Constants.UPDATE_PRO_VERSION_HTML;
import static com.mke.mword.MainActivity.mkdb;
import static com.mke.mword.Utils.Utils.SENDMESSAGE;


import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.*;

import android.app.*;
import android.content.*;
import android.database.*;
import android.net.*;


import android.os.*;
import android.provider.*;
import android.text.*;
import android.util.*;
import android.view.*;
import android.widget.*;

import com.mke.mword.Database.*;
import com.mke.mword.Utils.*;
import com.google.android.material.floatingactionbutton.*;

import java.io.*;


public class setLay extends AppCompatActivity {

    Button rGetFileBtn;
    BufferedReader  bfSelectInitFileName = null;
    String  szSelectInitFileName = null;
    Boolean bGetOldHistory = false;

    private Spinner  language_spinner;
    private Spinner  level_videos_Spinner;
    ArrayAdapter spinneradapter = null;

    CheckBox m_bContinueLastWord;
    CheckBox m_bWordDecreaseSound;
    CheckBox m_bWordSoundRepeat;
    CheckBox m_bReadNativeLang;
    CheckBox m_bEleminateMode;
    CheckBox m_bShowWVSImage;


    EditText m_szMail;
    EditText m_szMailPassword;

    EditText m_szPageWordSize;
    EditText m_szPageVerbSize;
    EditText m_szPageSentencesSize;

    private SeekBar seekBar_sound_repeat_time;
    private SeekBar seekBar_word_sound_repeat_sleep_time;
    private SeekBar seekBar_othlang_sentences_sleep_inc;
    private SeekBar seekBar_verb_inc_time;
    private SeekBar seekBar_sound_dec_per;
    private SeekBar seekBar_word_inc_time;

    private SeekBar seekBar_talk_wait_time;
    private SeekBar seekBar_exam_wait_time;
    private SeekBar seekBar_write_wait_time;

    int     n_sound_repeat_time=0;
    int     n_word_sound_repeat_sleep_time=0;
    int     n_othlang_sentences_sleep_inc=0;
    int     n_verb_inc_time=0;
    int     n_sound_dec_per=0;
    int     n_word_inc_time=0;

    int     n_talk_wait_time=0;
    int     n_exam_wait_time=0;
    int     n_write_wait_time=0;

    TextView m_nWordSoundRepeaTime;
    TextView m_nWordSoundDecreasePercent;
    TextView m_nWrdSndRepSlepTm;
    TextView m_nWrdSndRepSlepTmIncPrcnt;
    TextView m_nVerbSndRepSlepTmIncPrcnt;
    TextView m_szNatLangSentInc;
    TextView m_szTalkWaitTime;
    TextView m_szExamWaitTime;
    TextView m_szWriteWaitTime;

    Activity activitySetWin = null;
    Context cxSetWin = null;

    boolean bPartialUpdate=false;
    boolean bFullUpdate=false;

    public int m_nMode = Constants.IC_SETTING_WINDOW_MODE_NONE;
    FloatingActionButton fab;
    boolean flag = true; // true if first icon is visible, false if second one is visible.

    String szIsChangedData="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_lay);

        activitySetWin = this;
        cxSetWin = this;

        InitDialog();
        FillDialog();

        android.widget.Button resetBtn = findViewById(R.id.resetbtn);
        resetBtn.setOnClickListener(new android.view.View.OnClickListener() {
            @Override
            public void onClick(android.view.View v) {
                StartAlertDialog(1,DO_YOU_WANT_TO_RESET_THIS_WINDOW);
            }
        });

        android.widget.Button rApply = findViewById(R.id.SettingWindowApplyBtn);
        rApply.setOnClickListener(new android.view.View.OnClickListener() {
            @Override
            public void onClick(android.view.View v) {
                StartAlertDialog(2,DO_YOU_WANT_TO_APPLY);
            }
        });


        Button rSendMailBtn = findViewById(R.id.sendmailbtn);
        rSendMailBtn.setOnClickListener(new android.view.View.OnClickListener() {
            @Override
            public void onClick(android.view.View v) {
                HELP_CENTER_WEB_VIEW_CURRENT_FILE=UPDATE_PRO_VERSION_HTML;
                startActivity(new Intent(cxSetWin, testtt.class));
//                StartAlertDialog(3,DO_YOU_WANT_TO_SEND_MAIL);

            }
        });

        rGetFileBtn.setOnClickListener(new android.view.View.OnClickListener() {
            @Override
            public void onClick(android.view.View v) {
                HELP_CENTER_WEB_VIEW_CURRENT_FILE=UPDATE_PRO_VERSION_HTML;
                startActivity(new Intent(cxSetWin, testtt.class));
//                StartAlertDialog(4,DO_YOU_WANT_TO_CHANGE_SOURCE);
            }
        });

        fab =  findViewById(R.id.information_fab);
        fab.setScaleType(ImageView.ScaleType.FIT_CENTER);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(cxSetWin, HelpCenter.class));
                fab.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),
                        R.drawable.ic_help_outline_24));
                if (flag){ flag = false;} else{ flag = true;}
            }
        });


        seekBar_sound_repeat_time.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                m_nWordSoundRepeaTime.setText(Integer.toString(progress));
//                seekBar_exam_wait_time.REF_SMART_APPLICATION.writeSharedPreferences(Constants.KM, progress);
                //Get the thumb bound and get its left value
                int x = seekBar.getThumb().getBounds().left;
                n_sound_repeat_time=progress;
                //set the left value to textview x value
                m_nWordSoundRepeaTime.setX(x);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        seekBar_word_sound_repeat_sleep_time.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                m_nWrdSndRepSlepTm.setText(progress+"msn");
//                seekBar_exam_wait_time.REF_SMART_APPLICATION.writeSharedPreferences(Constants.KM, progress);
                //Get the thumb bound and get its left value
                int x = seekBar.getThumb().getBounds().left;
                n_word_sound_repeat_sleep_time=progress;

                //set the left value to textview x value
                m_nWrdSndRepSlepTm.setX(x);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        seekBar_othlang_sentences_sleep_inc.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                m_szNatLangSentInc.setText(progress+"%");
//                seekBar_exam_wait_time.REF_SMART_APPLICATION.writeSharedPreferences(Constants.KM, progress);
                //Get the thumb bound and get its left value
                int x = seekBar.getThumb().getBounds().left;
                n_othlang_sentences_sleep_inc=progress;

                //set the left value to textview x value
                m_szNatLangSentInc.setX(x);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        seekBar_verb_inc_time.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                m_nVerbSndRepSlepTmIncPrcnt.setText(progress+"%");
//                seekBar_exam_wait_time.REF_SMART_APPLICATION.writeSharedPreferences(Constants.KM, progress);
                //Get the thumb bound and get its left value
                int x = seekBar.getThumb().getBounds().left;
                n_verb_inc_time=progress;

                //set the left value to textview x value
                m_nVerbSndRepSlepTmIncPrcnt.setX(x);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        seekBar_sound_dec_per.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                m_nWordSoundDecreasePercent.setText(progress+"%");
//                seekBar_exam_wait_time.REF_SMART_APPLICATION.writeSharedPreferences(Constants.KM, progress);
                //Get the thumb bound and get its left value
                int x = seekBar.getThumb().getBounds().left;
                n_sound_dec_per=progress;

                //set the left value to textview x value
                m_nWordSoundDecreasePercent.setX(x);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        seekBar_word_inc_time.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                m_nWrdSndRepSlepTmIncPrcnt.setText(progress+"%");
//                seekBar_exam_wait_time.REF_SMART_APPLICATION.writeSharedPreferences(Constants.KM, progress);
                //Get the thumb bound and get its left value
                int x = seekBar.getThumb().getBounds().left;
                n_word_inc_time=progress;

                //set the left value to textview x value
                m_nWrdSndRepSlepTmIncPrcnt.setX(x);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        seekBar_exam_wait_time.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                m_szExamWaitTime.setText(progress+"sn");
//                seekBar_exam_wait_time.REF_SMART_APPLICATION.writeSharedPreferences(Constants.KM, progress);
                //Get the thumb bound and get its left value
                int x = seekBar.getThumb().getBounds().left;
                n_exam_wait_time=progress;

                //set the left value to textview x value
                m_szExamWaitTime.setX(x);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        seekBar_talk_wait_time.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                m_szTalkWaitTime.setText(progress+"sn");
//                seekBar_exam_wait_time.REF_SMART_APPLICATION.writeSharedPreferences(Constants.KM, progress);
                //Get the thumb bound and get its left value
                int x = seekBar.getThumb().getBounds().left;
                n_talk_wait_time=progress;

                //set the left value to textview x value
                m_szTalkWaitTime.setX(x);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        seekBar_write_wait_time.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                m_szWriteWaitTime.setText(progress+"sn");
//                seekBar_exam_wait_time.REF_SMART_APPLICATION.writeSharedPreferences(Constants.KM, progress);
                //Get the thumb bound and get its left value
                int x = seekBar.getThumb().getBounds().left;
                n_write_wait_time=progress;

                //set the left value to textview x value
                m_szWriteWaitTime.setX(x);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    private boolean IsChangedData() {
        if (mkdb.nVerbsVectorSize != Integer.valueOf(m_szPageVerbSize.getText().toString())) {
            szIsChangedData = "mkdb.nVerbsVectorSize" + mkdb.nVerbsVectorSize+Integer.valueOf(m_szPageVerbSize.getText().toString());
//            Log.e("IsChangedData",szIsChangedData+"\n");
            bFullUpdate=true;return bFullUpdate;
        }
        if (mkdb.nSentencesVectorSize != Integer.valueOf(m_szPageSentencesSize.getText().toString())) {
            szIsChangedData = "mkdb.nSentencesVectorSize" + mkdb.nSentencesVectorSize +Integer.valueOf(m_szPageSentencesSize.getText().toString());
//            Log.e("IsChangedData",szIsChangedData+"\n");
            bFullUpdate=true;return bFullUpdate;
        }
        if (!mkdb.szNativeLanguage.trim().equals(language_spinner.getSelectedItem().toString())) {
            szIsChangedData = "mkdb.szNativeLanguage" + mkdb.szNativeLanguage + language_spinner.getSelectedItem().toString();
//            Log.e("IsChangedData",szIsChangedData+"\n");
            bFullUpdate=true;return bFullUpdate;
        }
        if (mkdb.nWordsVectorSize != Integer.valueOf(m_szPageWordSize.getText().toString())) {
            szIsChangedData = "mkdb.nWordsVectorSize" + mkdb.nWordsVectorSize +Integer.valueOf(m_szPageWordSize.getText().toString());
//            Log.e("IsChangedData",szIsChangedData+"\n");
            bFullUpdate=true;return bFullUpdate;
        }

        if (!mkdb.stMailPassword.trim().equals(m_szMailPassword.getText().toString())) {
            szIsChangedData = "mkdb.stMailPassword" + mkdb.stMailPassword +m_szMailPassword.getText().toString();
//            Log.e("IsChangedData",szIsChangedData+"\n");
            bPartialUpdate=true;return bPartialUpdate;
        }
        if (!mkdb.stMail.trim().equals(m_szMail.getText().toString())){
            szIsChangedData = "mkdb.stMail" + mkdb.nWordsVectorSize +m_szMail.getText().toString();
//            Log.e("IsChangedData",szIsChangedData+"\n");
            bPartialUpdate=true;return bPartialUpdate;
        }
        if (mkdb.nNatLangSentIncTime != n_othlang_sentences_sleep_inc) {
            szIsChangedData = "mkdb.nNatLangSentIncTime" + mkdb.nNatLangSentIncTime +n_othlang_sentences_sleep_inc;
//            Log.e("IsChangedData",szIsChangedData+"\n");
            bPartialUpdate=true;return bPartialUpdate;
        }
        if (mkdb.nWaitTalkTime != n_talk_wait_time) {
            szIsChangedData = "mkdb.nWaitTalkTime" + mkdb.nWaitTalkTime +n_talk_wait_time;
//            Log.e("IsChangedData",szIsChangedData+"\n");
            bPartialUpdate=true;return bPartialUpdate;
        }
        if (mkdb.nWaitExamTime != n_exam_wait_time) {
            szIsChangedData = "mkdb.nWaitExamTime" +n_exam_wait_time;
//            Log.e("IsChangedData",szIsChangedData+"\n");
            bPartialUpdate=true;return bPartialUpdate;
        }
        if (mkdb.nWaitWriteTime != n_write_wait_time) {
            szIsChangedData = "mkdb.nWaitWriteTime" + mkdb.nWaitWriteTime +n_write_wait_time;
//            Log.e("IsChangedData",szIsChangedData+"\n");
            bPartialUpdate=true;return bPartialUpdate;
        }
        if (mkdb.nWordSoundRepeaTime != n_sound_repeat_time) {
            szIsChangedData = "mkdb.nWordSoundRepeaTime" + mkdb.nWordSoundRepeaTime +n_sound_repeat_time;
//            Log.e("IsChangedData",szIsChangedData+"\n");
            bPartialUpdate=true;return bPartialUpdate;
        }
        if (mkdb.nWordSoundDecreasePercent != n_sound_dec_per) {
            szIsChangedData = "mkdb.nWordSoundDecreasePercent" + mkdb.nWordSoundDecreasePercent +n_sound_dec_per;
//            Log.e("IsChangedData",szIsChangedData+"\n");
            bPartialUpdate=true;return bPartialUpdate;
        }
        if (mkdb.nWrdSndRepSlepTm != n_word_sound_repeat_sleep_time) {
            szIsChangedData = "mkdb.nWrdSndRepSlepTm" + n_word_sound_repeat_sleep_time;
//            Log.e("IsChangedData",szIsChangedData+"\n");
            bPartialUpdate=true;return bPartialUpdate;
        }
        if (mkdb.nWrdSndRepSlepTmIncPrcnt != n_word_inc_time) {
            szIsChangedData = "mkdb.nWrdSndRepSlepTmIncPrcnt" + mkdb.nWrdSndRepSlepTmIncPrcnt +n_word_inc_time;
//            Log.e("IsChangedData",szIsChangedData+"\n");
            bPartialUpdate=true;return bPartialUpdate;
        }
        if (mkdb.nVerbSlpTmIncPrcnt != n_verb_inc_time) {
            szIsChangedData = "mkdb.nVerbSlpTmIncPrcnt" + mkdb.nVerbSlpTmIncPrcnt + n_verb_inc_time;
//            Log.e("IsChangedData",szIsChangedData+"\n");
            bPartialUpdate=true;return bPartialUpdate;
        }
        if (mkdb.bContinueLastWord != m_bContinueLastWord.isChecked()) {
            szIsChangedData = "mkdb.bContinueLastWord" + mkdb.bContinueLastWord +m_bContinueLastWord.isChecked();
//            Log.e("IsChangedData",szIsChangedData+"\n");
            bPartialUpdate=true;return bPartialUpdate;
        }
        if (mkdb.bWordDecreaseSound != m_bWordDecreaseSound.isChecked()) {
            szIsChangedData = "mkdb.bWordDecreaseSound" +m_bWordDecreaseSound.isChecked();
//            Log.e("IsChangedData",szIsChangedData+"\n");
            bPartialUpdate=true;return bPartialUpdate;
        }
        if(mkdb.bWordSoundRepeat != m_bWordSoundRepeat.isChecked()) {
            szIsChangedData = "mkdb.bWordSoundRepeat" + mkdb.bWordSoundRepeat +m_bWordSoundRepeat.isChecked();
//            Log.e("IsChangedData",szIsChangedData+"\n");
            bPartialUpdate=true;return bPartialUpdate;
        }

        if (mkdb.bReadNativeLang != m_bReadNativeLang.isChecked()) {
            szIsChangedData = "mkdb.bReadNativeLang" + mkdb.bReadNativeLang +m_bReadNativeLang.isChecked();
//            Log.e("IsChangedData",szIsChangedData+"\n");
            bPartialUpdate=true;return bPartialUpdate;
        }

        if (mkdb.bEleminateMode != m_bEleminateMode.isChecked()) {
            szIsChangedData = "mkdb.bEleminateMode" + mkdb.bEleminateMode +m_bEleminateMode.isChecked();
//            Log.e("IsChangedData",szIsChangedData+"\n");
            bPartialUpdate=true;return bPartialUpdate;
        }
        if (mkdb.bShowWVSImage != m_bShowWVSImage.isChecked()) {
            szIsChangedData = "mkdb.bShowWVSImage" + mkdb.bShowWVSImage +m_bShowWVSImage.isChecked();
//            Log.e("IsChangedData",szIsChangedData+"\n");
            bPartialUpdate=true;return bPartialUpdate;
        }

        return false;
    }
    private void EndDialog() {
        if (mkdb != null) {

            mkdb.stMail                     = m_szMail.getText().toString();
            mkdb.stMailPassword             = m_szMailPassword.getText().toString();

            mkdb.szNativeLanguage           = language_spinner.getSelectedItem().toString();

            mkdb.nWordsVectorSize           = Integer.valueOf(m_szPageWordSize.getText().toString());
            mkdb.nVerbsVectorSize           = Integer.valueOf(m_szPageVerbSize.getText().toString());
            mkdb.nSentencesVectorSize       = Integer.valueOf(m_szPageSentencesSize.getText().toString());



            mkdb.bContinueLastWord          = m_bContinueLastWord.isChecked();
            mkdb.bWordDecreaseSound         = m_bWordDecreaseSound.isChecked();
            mkdb.bWordSoundRepeat           = m_bWordSoundRepeat.isChecked();
            mkdb.bReadNativeLang            = m_bReadNativeLang.isChecked();
            mkdb.bEleminateMode             = m_bEleminateMode.isChecked();
            mkdb.bShowWVSImage              = m_bShowWVSImage.isChecked();


            mkdb.nWaitTalkTime              = n_talk_wait_time;
            mkdb.nWaitExamTime              = n_exam_wait_time;
            mkdb.nWaitWriteTime             = n_write_wait_time;
            mkdb.nWordSoundRepeaTime        = n_sound_repeat_time;
            mkdb.nWordSoundDecreasePercent  = n_sound_dec_per;
            mkdb.nWrdSndRepSlepTm           = n_word_sound_repeat_sleep_time;
            mkdb.nWrdSndRepSlepTmIncPrcnt   = n_word_inc_time;
            mkdb.nVerbSlpTmIncPrcnt         = n_verb_inc_time;
            mkdb.nNatLangSentIncTime        = n_othlang_sentences_sleep_inc;

        }
    }

    private void InitDialog() {
        if (mkdb != null) {
            language_spinner                        = findViewById(R.id.language_Spinner);
            level_videos_Spinner                    = findViewById(R.id.level_videos_Spinner);

            rGetFileBtn                             = findViewById(R.id.getFilebtn);

            m_szMail                                = findViewById(R.id.edit_mail);
            m_szMailPassword                        = findViewById(R.id.edit_mailpswd);

            m_bContinueLastWord                     = findViewById(R.id.checkBox_cntunuelastWord);
            m_bWordDecreaseSound                    = findViewById(R.id.checkBox_wdecsound);
            m_bWordSoundRepeat                      = findViewById(R.id.checkBox_wordsndrep);
            m_bReadNativeLang                       = findViewById(R.id.checkBox_read_native_lang);
            m_bEleminateMode                        = findViewById(R.id.checkBox_talk_page_eleminate);
            m_bShowWVSImage                         = findViewById(R.id.checkBox_showimage);

            m_szPageWordSize                        = findViewById(R.id.edit_page_word_size);
            m_szPageWordSize.setFilters(new InputFilter[]{ new InputFilterMinMax("1","5000")});

            m_szPageVerbSize                        = findViewById(R.id.edit_page_verb_size);
            m_szPageVerbSize.setFilters(new InputFilter[]{ new InputFilterMinMax("1","500")});

            m_szPageSentencesSize                   = findViewById(R.id.edit_page_sentences_size);
            m_szPageSentencesSize.setFilters(new InputFilter[]{ new InputFilterMinMax("1","500")});

            seekBar_word_sound_repeat_sleep_time    =  findViewById(R.id.seekBar_word_sound_repeat_sleep_time);
            seekBar_othlang_sentences_sleep_inc     =  findViewById(R.id.seekBar_othlang_sentences_sleep_inc);
            seekBar_verb_inc_time                   =  findViewById(R.id.seekBar_verb_inc_time);
            seekBar_sound_dec_per                   =  findViewById(R.id.seekBar_sound_dec_per);
            seekBar_word_inc_time                   =  findViewById(R.id.seekBar_word_inc_time);
            seekBar_exam_wait_time                  =  findViewById(R.id.seekBar_exam_wait_time);
            seekBar_talk_wait_time                  =  findViewById(R.id.seekBar_talk_wait_time);
            seekBar_write_wait_time                 =  findViewById(R.id.seekBar_write_wait_time);
            seekBar_sound_repeat_time               =  findViewById(R.id.seekBar_sound_repeat_time);

            m_nWrdSndRepSlepTm                      = findViewById(R.id.edit_word_sound_repeat_sleep_time);
            m_szNatLangSentInc                      = findViewById(R.id.edit_othlang_sentences_sleep_inc);
            m_nVerbSndRepSlepTmIncPrcnt             = findViewById(R.id.edit_verb_inc_time);
            m_nWordSoundDecreasePercent             = findViewById(R.id.edit_sound_dec_per);
            m_nWrdSndRepSlepTmIncPrcnt              = findViewById(R.id.edit_word_inc_time);
            m_szExamWaitTime                        = findViewById(R.id.edit_exam_wait_time);
            m_szTalkWaitTime                        = findViewById(R.id.edit_talk_wait_time);
            m_szWriteWaitTime                       = findViewById(R.id.edit_write_wait_time);
            m_nWordSoundRepeaTime                   =  findViewById(R.id.edit_sound_repeat_time);
        }
    }

    private int setTViewLikeSeekPlace(SeekBar sbar,TextView tView,int nDataBaseValue,String szUnit){
        sbar.setProgress(nDataBaseValue);
        tView.setText(Integer.toString(nDataBaseValue)+szUnit);
        int x = sbar.getThumb().getBounds().left;
        //set the left value to textview x value
        tView.setX(x);
        return nDataBaseValue;
    }

    private void FillDialog() {
        if (mkdb != null) {
            n_word_sound_repeat_sleep_time=setTViewLikeSeekPlace(seekBar_word_sound_repeat_sleep_time,m_nWrdSndRepSlepTm,mkdb.nWrdSndRepSlepTm,"msn");
            n_othlang_sentences_sleep_inc=setTViewLikeSeekPlace(seekBar_othlang_sentences_sleep_inc,m_szNatLangSentInc,mkdb.nNatLangSentIncTime,"%");
            n_verb_inc_time=setTViewLikeSeekPlace(seekBar_verb_inc_time,m_nVerbSndRepSlepTmIncPrcnt,mkdb.nVerbSlpTmIncPrcnt,"%");
            n_sound_dec_per=setTViewLikeSeekPlace(seekBar_sound_dec_per,m_nWordSoundDecreasePercent,mkdb.nWordSoundDecreasePercent,"%");
            n_word_inc_time=setTViewLikeSeekPlace(seekBar_word_inc_time,m_nWrdSndRepSlepTmIncPrcnt,mkdb.nWrdSndRepSlepTmIncPrcnt,"%");
            n_sound_repeat_time=setTViewLikeSeekPlace(seekBar_sound_repeat_time,m_nWordSoundRepeaTime,mkdb.nWordSoundRepeaTime,"");

            n_exam_wait_time=setTViewLikeSeekPlace(seekBar_exam_wait_time,m_szExamWaitTime,mkdb.nWaitExamTime,"sn");
            n_talk_wait_time=setTViewLikeSeekPlace(seekBar_talk_wait_time,m_szTalkWaitTime,mkdb.nWaitTalkTime,"sn");
            n_write_wait_time=setTViewLikeSeekPlace(seekBar_write_wait_time,m_szWriteWaitTime,mkdb.nWaitWriteTime,"sn");

            m_szPageWordSize.setText(Integer.toString(mkdb.nWordsVectorSize));
            m_szPageVerbSize.setText(Integer.toString(mkdb.nVerbsVectorSize));
            m_szPageSentencesSize.setText(Integer.toString(mkdb.nSentencesVectorSize));

            m_bContinueLastWord.setChecked(mkdb.bContinueLastWord);
            m_bWordDecreaseSound.setChecked(mkdb.bWordDecreaseSound);
            m_bWordSoundRepeat.setChecked(mkdb.bWordSoundRepeat);
            m_bReadNativeLang.setChecked(mkdb.bReadNativeLang);
            m_bEleminateMode.setChecked(mkdb.bEleminateMode);
            m_bShowWVSImage.setChecked(mkdb.bShowWVSImage);

            rGetFileBtn.setText(Constants.SELECT_INIT_FILE_FROM_PHONE);

            m_szMail.setText(mkdb.stMail);
            m_szMailPassword.setText(mkdb.stMailPassword);

            spinneradapter = new ArrayAdapter(this,
                    android.R.layout.simple_spinner_dropdown_item, NATIVE_LANGUAGES);
            spinneradapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            language_spinner.setAdapter(spinneradapter);
            language_spinner.setSelection(((ArrayAdapter<String>)language_spinner.
                    getAdapter()).getPosition(mkdb.szNativeLanguage));

            ArrayAdapter adapter1 = new ArrayAdapter(this,
                    android.R.layout.simple_spinner_dropdown_item, LEVEL_VIDEOS);
            adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            level_videos_Spinner.setAdapter(adapter1);
            level_videos_Spinner.setSelection(((ArrayAdapter<String>)level_videos_Spinner.
                    getAdapter()).getPosition(mkdb.szLevelVideos));

        }
    }

    private static final int FILE_SELECT_CODE = 0;

    public void showFileChooser() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);

        // Update with mime types
        intent.setType("*/*");
//        String [] mimeTypes = {"text/csv", "text/comma-separated-values"};
        // Update with additional mime types here using a String[].
//        intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);

        // Only pick openable and local files. Theoretically we could pull files from google drive
        // or other applications that have networked files, but that's unnecessary for this example.
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);

        // REQUEST_CODE = <some-integer>
        startActivityForResult(intent, FILE_SELECT_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // If the user doesn't pick a file just return
        if (requestCode != FILE_SELECT_CODE || resultCode != RESULT_OK) {
            return;
        }
        // Import the file
        importFile(data.getData());
    }

    public void importFile(Uri uri) {
        szSelectInitFileName = getFileName(uri);
//        Log.d(TAG, "File Path: " + szSelectInitFileName);
        PrintFile(uri);
        if (szSelectInitFileName == null || szSelectInitFileName.isEmpty()) {
            rGetFileBtn.setText(Constants.SELECT_INIT_FILE_FROM_PHONE);
        } else {
            rGetFileBtn.setText(szSelectInitFileName);
        }
        // Done!
    }

    /**
     * Obtains the file name for a URI using content resolvers. Taken from the following link
     * https://developer.android.com/training/secure-file-sharing/retrieve-info.html#RetrieveFileInfo
     *
     * @param uri a uri to query
     * @return the file name with no path
     * @throws IllegalArgumentException if the query is null, empty, or the column doesn't exist
     */
    private String getFileName(Uri uri) throws IllegalArgumentException {
        // Obtain a cursor with information regarding this uri
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);

        if (cursor.getCount() <= 0) {
            cursor.close();
            throw new IllegalArgumentException("Can't obtain file name, cursor is empty");
        }
        cursor.moveToFirst();
        String fileName = cursor.getString(cursor.getColumnIndexOrThrow(OpenableColumns.DISPLAY_NAME));

        cursor.close();

        return fileName;
    }

    private void PrintFile(Uri uri) {
        InputStream inputStream = null;
        String str = "";
        StringBuffer buf = new StringBuffer();
        try {
            inputStream = getContentResolver().openInputStream(uri);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        bfSelectInitFileName = new BufferedReader(new InputStreamReader(inputStream));
        if (inputStream!=null){
            try {
                while((str = bfSelectInitFileName.readLine())!=null){
                    buf.append(str+"\n");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
//            Log.e("PrintFile", buf.toString()+ "\n");
        }
    }
    void StartAlertDialog(int nBtnNumber,String szTitle){

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(
                setLay.this);
        // Setting Dialog Title
        alertDialog.setTitle(szTitle);

        // Setting Dialog Message
        alertDialog.setMessage(szTitle);

        // Setting Icon to Dialog
        alertDialog.setIcon(R.drawable.ic_help_outline_24);

        // Setting OK Button
        alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // Write your code here to execute after dialog closed
                switch (nBtnNumber){
                    case 1://Reset button
                        mkdb.InitFillAllMember();
                        FillDialog();
                        break;
                    case 2:
                        closeDialog();
                        break;
                    case 3:
                        mkdb.WriteFileInternalStorageJSON();
                        mkdb.SendMail();
                        break;
                    case 4:
                        showFileChooser();
                        break;
                    case 5:
                        closeDialog();

                        break;
                    default:
                        break;
                }
            }
        });
        // Setting Cancel Button
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // Write your code here to execute after dialog closed
                switch (nBtnNumber){
                    case 1://Reset button
                        break;
                    case 2:
                        break;
                    case 3:
                        break;
                    case 4:
                        break;
                    case 5:
                        finish();
                        break;
                    default:
                        break;
                }
            }
        });

        AlertDialog alertDialogMain = alertDialog.create();

        // Showing Alert Message
        alertDialogMain.show();

    }

    private void closeDialog(){
        if (bPartialUpdate || bFullUpdate || IsChangedData()) {
            EndDialog();
        }
        if (bFullUpdate){
            SENDMESSAGE(MESSAGE_SETTING_UPDATE_WINDOW, bGetOldHistory ? 1 : 0, bfSelectInitFileName);
            bFullUpdate=false;bPartialUpdate=false;
        } else if (!bFullUpdate && bPartialUpdate) {
            mkdb.WriteFileExternalStorageJSON();
            mkdb.WriteFileInternalStorageJSON();
            bPartialUpdate=false;
        }
        finish();
    }

    @Override
    public void onBackPressed() {
        if (IsChangedData()) {
            StartAlertDialog(5, DO_YOU_WANT_TO_APPLY);
        }else{
            super.onBackPressed();
        }
    }
}

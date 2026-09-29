package com.mke.mword;


import static com.mke.mword.Database.Constants.CURRIENTPAGETYPE;
import static com.mke.mword.Database.Constants.DO_YOU_WANT_TO_LISTEN_THIS_PAGE_ONEMORE;
import static com.mke.mword.Database.Constants.MESSAGE_RESET_SETTINGS_WINDOW;
import static com.mke.mword.Database.Constants.MESSAGE_SCREENVIEW_CHANGE_COLOR_SCREEN;
import static com.mke.mword.Database.Constants.MESSAGE_SCREENVIEW_PAGE_END;
import static com.mke.mword.Database.Constants.MESSAGE_SCREENVIEW_PAGE_LOAD_IMAGE;
import static com.mke.mword.Database.Constants.MESSAGE_SCREENVIEW_PAGE_START;
import static com.mke.mword.Database.Constants.MESSAGE_SCREENVIEW_PAUSE_BUTTON_CLICK;
import static com.mke.mword.Database.Constants.MESSAGE_SCREENVIEW_PLAY_BUTTON_CLICK;
import static com.mke.mword.Database.Constants.MESSAGE_SCREENVIEW_SETWORD_LEARNED;
import static com.mke.mword.Database.Constants.MESSAGE_SCREENVIEW_SHOW_SCREEN;
import static com.mke.mword.Database.Constants.MESSAGE_SCREENVIEW_UPDATE_MKDB;
import static com.mke.mword.Database.Constants.MESSAGE_SCREENVIEW_VISIBLE_REMOVE_BUTTON;
import static com.mke.mword.Database.Constants.MESSAGE_UPDATE_CURRENT_FRAGMENT_LISTVIEW;
import static com.mke.mword.Database.Constants.SCREENPAGESTATUS;
import static com.mke.mword.Database.Constants.SCREEN_ACTIVITY_DO_YOU_WANT_TO_LISTEN;
import static com.mke.mword.Database.Constants.SCREEN_PAGE_LISTEN;
import static com.mke.mword.Database.Constants.SCREEN_PAGE_TALK;
import static com.mke.mword.Database.Constants.TVSCREEN_MAXLINES;
import static com.mke.mword.Database.mkDB.nPageNumber;
import static com.mke.mword.Database.mkDB.nWordNumber;
import static com.mke.mword.MainActivity.intentSpeech;
import static com.mke.mword.MainActivity.intentTTS;
import static com.mke.mword.MainActivity.mkdb;
import static com.mke.mword.MainActivity.wordsFragment;
import static com.mke.mword.Utils.Utils.FileExistFromAssetFiles;
import static com.mke.mword.Utils.Utils.GetInfoScreen;
import static com.mke.mword.Utils.Utils.GetSpeakWord;
import static com.mke.mword.Utils.Utils.JUSTSLEEP;
import static com.mke.mword.Utils.Utils.SENDMESSAGE;
import static com.mke.mword.Utils.Utils.SENDMESSAGESCREENWINDOW;
import static com.mke.mword.Utils.Utils.getCurrentWVSName;


import static java.lang.Thread.sleep;

import android.*;
import android.app.*;
import android.app.AlertDialog;
import android.content.*;
import android.content.pm.*;
import android.content.res.*;
import android.graphics.drawable.*;
import android.media.*;
import android.os.*;
import android.provider.*;
import android.util.*;
import android.view.*;
import android.widget.*;

import androidx.annotation.*;
import androidx.appcompat.app.*;
import androidx.core.app.*;
import androidx.core.content.*;

import com.mke.mword.Utils.*;
import com.google.android.material.floatingactionbutton.*;
import com.mke.mword.Wake.*;

import java.io.*;
import java.util.*;


public class ScreenView extends AppCompatActivity {
    public static TextView tvInformation,tvCounter,tvResults;
    public static TextView tvSCreen;
    public static ImageView image_view;
    public FloatingActionButton fab_play,fab_remove;
    public static boolean bIsPushRemove=false;
    public static String szTalkString="";
    public static boolean bSilent=false;

    public Activity gActivity;
    public static Context gSreencx;
    public static Handler mHandlerScreenWindow    = null;

    public boolean bCheckDontDistrubPermissionOneTime=false;
    public boolean bCheckAudioPermissionOneTime=false;

    TALKTHREAD talkthread=null;

    boolean flag = true; // true if first icon is visible, false if second one is visible.
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screen);

        gActivity=this;
        gSreencx =this;

        tvSCreen = (TextView) findViewById(R.id.textview_screen_screen);
        tvSCreen.setText(SCREEN_ACTIVITY_DO_YOU_WANT_TO_LISTEN);
        tvSCreen.setMaxLines(TVSCREEN_MAXLINES);

        tvCounter = (TextView) findViewById(R.id.textview_counter);
        tvResults = (TextView) findViewById(R.id.textview_resutls);
        image_view  = (ImageView) findViewById(R.id.image_view);

        if (SCREENPAGESTATUS.trim().equals(SCREEN_PAGE_LISTEN)) {

            tvCounter.setVisibility(View.GONE);
            tvResults.setVisibility(View.GONE);
            image_view.setVisibility(View.VISIBLE);
//            tvResults.setText(Integer.toString(mkdb.GetLernWVSSize(CURRIENTPAGETYPE,nPageNumber)));
            tvResults.setTextSize(41);
        }else {
            image_view.setVisibility(View.GONE);
            tvCounter.setVisibility(View.VISIBLE);
            tvResults.setVisibility(View.VISIBLE);
            tvResults.setTextSize(25);
        }

        if (mkdb.bShowWVSImage){
            image_view.setVisibility(View.VISIBLE);
        }else{
            image_view.setVisibility(View.GONE);
        }

        tvInformation = (TextView) findViewById(R.id.textview_screen_information);
        tvInformation.setText(GetInfoScreen(CURRIENTPAGETYPE,nPageNumber,mkdb.
                GetPageLastWVSNumber(CURRIENTPAGETYPE,nPageNumber)));

        fab_play = (FloatingActionButton) findViewById(R.id.fab_screen_play);
        fab_play.setScaleType(ImageView.ScaleType.FIT_CENTER);
        fab_play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(flag){
                    SENDMESSAGESCREENWINDOW(MESSAGE_SCREENVIEW_PLAY_BUTTON_CLICK);
                }else if(!flag){
                    SENDMESSAGESCREENWINDOW(MESSAGE_SCREENVIEW_PAUSE_BUTTON_CLICK);
                }
            }
        });
        fab_remove = (FloatingActionButton) findViewById(R.id.fab_screen_remove);
        fab_remove.setScaleType(ImageView.ScaleType.FIT_CENTER);
        fab_remove.setVisibility(View.GONE);
        fab_remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SENDMESSAGESCREENWINDOW(MESSAGE_SCREENVIEW_SETWORD_LEARNED);
            }
        });
        mHandlerScreenWindow = new Handler() {
            @Override
            public void handleMessage(android.os.Message msg) {
                switch (msg.arg1) {
                    case MESSAGE_SCREENVIEW_PAGE_LOAD_IMAGE:
                        // load image
                        if (mkdb.bShowWVSImage) {
                            try {
                                String szWordImageFileName = mkdb.GetEngWVS(CURRIENTPAGETYPE, msg.arg2, (int) msg.obj) + ".jpg";
                                String szRoot = "WVS_Image/" + CURRIENTPAGETYPE + "/";
                                if (FileExistFromAssetFiles(szRoot, szWordImageFileName)) {
                                    if (image_view.getVisibility() != View.VISIBLE) {
                                        image_view.setVisibility(View.VISIBLE);
                                    }
                                    // get input stream
                                    String szFile = szRoot + szWordImageFileName;
                                    InputStream ims = getAssets().open(szFile);
                                    // load image as Drawable
                                    Drawable d = Drawable.createFromStream(ims, null);
                                    // set image to ImageView
                                    image_view.setBackground(d);
                                } else {
                                    if (image_view.getVisibility() != View.GONE) {
                                        image_view.setVisibility(View.GONE);
                                    }
                                }
                            } catch (IOException ex) {
                                ex.printStackTrace();
                            }
                        }
                        break;
                    case MESSAGE_SCREENVIEW_PAGE_END:
                        mkdb.SetPageLastWVSNumber(CURRIENTPAGETYPE,nPageNumber,0);
                        mkdb.SetPageWatchCount(CURRIENTPAGETYPE,nPageNumber);
                        tvSCreen.setText(DO_YOU_WANT_TO_LISTEN_THIS_PAGE_ONEMORE);
                        SENDMESSAGESCREENWINDOW(MESSAGE_SCREENVIEW_PAUSE_BUTTON_CLICK);
                        break;
                    case MESSAGE_SCREENVIEW_SHOW_SCREEN: //opens contacts application to browse contacts
                        tvInformation.setText(GetInfoScreen(CURRIENTPAGETYPE, msg.arg2, (int) msg.obj));
                        tvSCreen.setText(GetSpeakWord(CURRIENTPAGETYPE, msg.arg2, (int) msg.obj));
                        break;
                    case MESSAGE_SCREENVIEW_SETWORD_LEARNED:
                        if (fab_remove.getVisibility()!=View.GONE) {
                            fab_remove.setVisibility(View.GONE);
                            mkdb.SetWVSLearn(CURRIENTPAGETYPE,nPageNumber,nWordNumber);
                            tvResults.setText("");
                            tvSCreen.setText("");
                        }
                        bIsPushRemove = true;
                        if (talkthread!=null){talkthread.PushRemovoBtn();}

                        break;
                    case MESSAGE_SCREENVIEW_VISIBLE_REMOVE_BUTTON:
                        fab_remove.setVisibility(View.VISIBLE);
                        break;
                    case MESSAGE_SCREENVIEW_CHANGE_COLOR_SCREEN:
                        tvSCreen.setBackgroundColor((int)msg.obj);
                        break;
                    case MESSAGE_SCREENVIEW_PAGE_START:
                        break;
                    case MESSAGE_SCREENVIEW_PLAY_BUTTON_CLICK:
                        if (!mkdb.CheckThisPageWVSsIsLearn(CURRIENTPAGETYPE,nPageNumber)) {
                            PLAY();
                        }else{
                            AlertDialog.Builder builder = new AlertDialog.Builder(gSreencx);
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
                        PAUSE();
                        break;
                    case MESSAGE_SCREENVIEW_UPDATE_MKDB:
                        if (mkdb!= null){
                            mkdb.WriteFileInternalStorageJSON();
                            wordsFragment.UpdateList();
                        }
                        break;
                    default:
                        break;
                }
            }
        };
    }

    final public int APP_MODIFY_POLICY_ACCESS_SETTINGS = 55;
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 55) {
            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                NotificationManager mNotificationManager = (NotificationManager)
                                                                getSystemService(gSreencx.NOTIFICATION_SERVICE);
                assert mNotificationManager != null;
                if (mNotificationManager.isNotificationPolicyAccessGranted()) {
                    bCheckDontDistrubPermissionOneTime=true;
                }else{
                    //DND permission isn't granted
                    //request the permission again or do something else
                    Toast.makeText(getApplicationContext(), "Do Not Disturb ON for mWord app",
                            Toast.LENGTH_LONG).show();
                }
            }
        }
    }
    private void setSoundUmute(boolean bMute) {
        if (!bCheckDontDistrubPermissionOneTime) {
            NotificationManager mNotificationManager = (NotificationManager)
                                            getSystemService(gSreencx.NOTIFICATION_SERVICE);
            assert mNotificationManager != null;
            if (!mNotificationManager.isNotificationPolicyAccessGranted()) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                        && !mNotificationManager.isNotificationPolicyAccessGranted()) {
                    PAUSE();
                    startActivityForResult(new Intent(Settings.ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS),
                            APP_MODIFY_POLICY_ACCESS_SETTINGS);
                }
            }else{
                bCheckDontDistrubPermissionOneTime=true;
            }
        }

        if (bCheckDontDistrubPermissionOneTime){
            AudioManager am = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
            if (am!=null) {
                if (!bMute) {
                    am.setStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_UNMUTE, AudioManager.FLAG_SHOW_UI);
                } else {
                    am.setStreamVolume(AudioManager.STREAM_RING, AudioManager.ADJUST_MUTE, AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE);
                }
            }
        }
    }

    private void PLAY(){
        if (flag) {
            fab_play.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),
                    R.drawable.ic_pause_button_64));
            flag = false;
            fab_remove.setVisibility(View.VISIBLE);
            if (SCREENPAGESTATUS==SCREEN_PAGE_LISTEN) {
//                setSoundUmute(false);
                startService(intentTTS);
            }else if (SCREENPAGESTATUS==SCREEN_PAGE_TALK){
                checkPermissions();
                setSoundUmute(true);
                startService(intentSpeech);
                talkthread = new TALKTHREAD(gActivity,tvSCreen,tvResults,tvCounter);
                talkthread.start();
            }

        }
    }
    private void PAUSE(){
        if (!flag) {
            fab_play.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),
                    R.drawable.ic_play_button_64));
            flag = true;
            fab_remove.setVisibility(View.GONE);
            stopRunningThreads();
            if (SCREENPAGESTATUS.trim().equals(SCREEN_PAGE_LISTEN)) {
                stopService(intentTTS);
            } else if (SCREENPAGESTATUS.trim().equals(SCREEN_PAGE_TALK)) {
                stopService(intentSpeech);
                stopRunningThreads();
                setSoundUmute(false);
            }
        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        stopRunningThreads();
        SENDMESSAGE(MESSAGE_UPDATE_CURRENT_FRAGMENT_LISTVIEW);
        stopService(intentTTS);
        stopService(intentSpeech);
        if (SCREENPAGESTATUS.trim().equals(SCREEN_PAGE_TALK)) {
            setSoundUmute(false);
        }
    }
    @Override
    protected void onDestroy() {
        if (SCREENPAGESTATUS.trim().equals(SCREEN_PAGE_TALK)) {
            setSoundUmute(false);
        }
        super.onDestroy();
    }
    private void stopRunningThreads(){
        if (talkthread!=null && talkthread.isRunning()){
            talkthread.stopRunning();
        }
    }
    @Override
    protected void onStart() {
        super.onStart();
//        LocalBroadcastManager.getInstance(this).registerReceiver((broadcastReceiver),
//                new IntentFilter(TTS.SERVICE_RESULT));
    }

    @Override
    protected void onStop() {
        super.onStop();
        stopRunningThreads();
        PAUSE();
//        LocalBroadcastManager.getInstance(this).unregisterReceiver(broadcastReceiver);
    }
    @Override
    public void onConfigurationChanged(Configuration newConfig)
    {
//        Log.d("tag", "config changed");
        super.onConfigurationChanged(newConfig);
        SENDMESSAGESCREENWINDOW(MESSAGE_SCREENVIEW_PAUSE_BUTTON_CLICK);
        int orientation = newConfig.orientation;
        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
//            Log.d("tag", "Portrait");
            tvSCreen.setPadding(0,0,0,0);
        }
        else if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
//            Log.d("tag", "Landscape");
            tvSCreen.setPadding(0,0,0,100);
        }
        else {
//            Log.w("tag", "other: " + orientation);
        }
    }

    private void checkPermissions() {

        int hasRecordAudio = ContextCompat.checkSelfPermission(gActivity, Manifest.permission.RECORD_AUDIO);
        int hasAudioSettigns = ContextCompat.checkSelfPermission(gActivity, Manifest.permission.MODIFY_AUDIO_SETTINGS);
        int hasWakeLock = ContextCompat.checkSelfPermission(gActivity, Manifest.permission.WAKE_LOCK);

        List<String> permissions = new ArrayList<>();

        if (hasRecordAudio != PackageManager.PERMISSION_GRANTED) {
            permissions.add(Manifest.permission.RECORD_AUDIO);
        }
        if (hasAudioSettigns != PackageManager.PERMISSION_GRANTED) {
            permissions.add(Manifest.permission.MODIFY_AUDIO_SETTINGS);
        }
        if (hasWakeLock != PackageManager.PERMISSION_GRANTED) {
            permissions.add(Manifest.permission.WAKE_LOCK);
        }
        if (!permissions.isEmpty()) {
            PAUSE();
            ActivityCompat.requestPermissions(gActivity,
                permissions.toArray(new String[permissions.size()]),
                APP_MODIFY_AUDIO_SETTINGS_REQUEST_CODE);
        }else{
            bCheckAudioPermissionOneTime=true;
        }
    }

    final public  static int APP_MODIFY_AUDIO_SETTINGS_REQUEST_CODE = 502;
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case APP_MODIFY_AUDIO_SETTINGS_REQUEST_CODE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    bCheckAudioPermissionOneTime=true;
                } else {
                    Toast.makeText(getApplicationContext(), "Can you permit this permission?",
                            Toast.LENGTH_LONG).show();
                }
            }
        }
    }
}
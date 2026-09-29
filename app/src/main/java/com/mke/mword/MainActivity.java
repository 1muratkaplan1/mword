package com.mke.mword;

import static android.os.Build.VERSION.SDK_INT;
import static com.mke.mword.Database.Constants.CURRIENTPAGETYPE;
import static com.mke.mword.Database.Constants.DB_IDIOMS_PAGES;
import static com.mke.mword.Database.Constants.DB_MKDB_INIT_NATIVE_LANGUAGE;
import static com.mke.mword.Database.Constants.DB_SENTENCES_PAGES;
import static com.mke.mword.Database.Constants.DB_VERBS_PAGES;
import static com.mke.mword.Database.Constants.DB_VIDEOS_PAGES;
import static com.mke.mword.Database.Constants.DB_WORDS_PAGES;
import static com.mke.mword.Database.Constants.MESSAGE_RESET_SETTINGS_WINDOW;
import static com.mke.mword.Database.Constants.MESSAGE_SAVE_DOCUMENT;
import static com.mke.mword.Database.Constants.MESSAGE_SETTING_DIALOG_OPEN_WINDOW;
import static com.mke.mword.Database.Constants.MESSAGE_SETTING_UPDATE_WINDOW;
import static com.mke.mword.Database.Constants.MESSAGE_START_SPEECH_SERVICE;
import static com.mke.mword.Database.Constants.MESSAGE_STOP_SERVICE;
import static com.mke.mword.Database.Constants.MESSAGE_STOP_SPEECH_SERVICE;
import static com.mke.mword.Database.Constants.MESSAGE_THROW_EXCEPTION;
import static com.mke.mword.Database.Constants.MESSAGE_UPDATE_ALL_FRAGMENT_LISTVIEW;
import static com.mke.mword.Database.Constants.MESSAGE_UPDATE_CURRENT_FRAGMENT_LISTVIEW;
import static com.mke.mword.Database.Constants.NATIVE_LANGUAGES;
import static com.mke.mword.Database.Constants.TABLAYOUT_PAGE_SIZE;
import static com.mke.mword.Database.Constants.TVSCREEN_IDIOMS_PAGE_MAXLINES;
import static com.mke.mword.Database.Constants.TVSCREEN_SENTENCES_PAGE_MAXLINES;
import static com.mke.mword.Database.Constants.TVSCREEN_VERB_PAGE_MAXLINES;
import static com.mke.mword.Database.Constants.TVSCREEN_WORD_PAGE_MAXLINES;
import static com.mke.mword.Database.Constants.TVSCREEN_MAXLINES;


import android.*;
import android.app.*;
import android.app.AlertDialog;
import android.content.*;
import android.content.pm.*;
import android.content.res.*;
import android.graphics.*;
import android.net.*;
import android.os.*;
import android.provider.*;
import android.text.*;
import android.util.*;
import android.view.*;
import android.widget.*;

import androidx.annotation.*;
import androidx.appcompat.app.*;
import androidx.coordinatorlayout.widget.*;
import androidx.core.app.*;
import androidx.core.content.*;
import androidx.fragment.app.*;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.*;
import androidx.viewpager2.adapter.*;
import androidx.viewpager2.widget.*;

import com.google.android.material.snackbar.*;
import com.google.android.play.core.appupdate.*;
import com.google.android.play.core.install.*;
import com.google.android.play.core.install.model.*;
import com.google.android.play.core.tasks.*;
import com.mke.mword.*;
import com.mke.mword.Database.*;
import com.mke.mword.Fragment.*;
import com.mke.mword.TTS_STT.*;
import com.mke.mword.Wake.*;
import com.google.android.material.floatingactionbutton.*;
import com.google.android.material.tabs.*;

import static com.mke.mword.Utils.Utils.JUSTSLEEP;
import static com.mke.mword.Utils.Utils.SENDMESSAGE;
import static com.mke.mword.Utils.Utils.SENDMESSAGESCREENWINDOW;
import static com.mke.mword.Utils.Utils.dbDeleteDBFileFromIntStorage;
import static com.mke.mword.Utils.Utils.dbFileHasFromIntStorage;

import android.os.Bundle;

import java.io.*;
import java.util.*;

public class MainActivity extends AppCompatActivity {
    public static Intent intentSpeech = null;
    public static Intent intentTTS = null;
    public static Activity g_Activity= null;

    public static Context  g_Context= null;

    public static Handler mHandler    = null;

    public static mkDB mkdb =null;


    //The pager widget, which handles animation and allows swiping horizontally to access previous and next wizard steps.
    public static ViewPager2                viewPager=null;
    public static TabLayout                 tabLayout=null;
    public static WordsFragment             wordsFragment=null;
    public static SentencesFragment         sentencesFragment=null;
    public static IdiomsFragment            idiomsFragment=null;
    public static VideosFragment            videosFragment=null;
    public static VerbsFragment             verbsFragment=null;
    // The pager adapter, which provides the pages to the view pager widget.
    private FragmentStateAdapter            pagerAdapter;
    // Arrey of strings FOR TABS TITLES
    private String[] titles = new String[]{"Word","Verb","Sntnc","Idiom", "Video"};

    public static String TAG = "\nMK engineering";

    boolean flag = true; // true if first icon is visible, false if second one is visible.
    FloatingActionButton fab;
    int nApplicationSleepTime = 2000;

    private static final int REQ_CODE_VERSION_UPDATE = 530;
    private AppUpdateManager appUpdateManager;
    private InstallStateUpdatedListener installStateUpdatedListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        g_Context =this;
        g_Activity=this;

        PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);

        WakeLocker.acquire(this);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);


        checkForAppUpdate();
    }

    private void initDialog(){
        mkdb = new mkDB(this);
        mkdb.ReadJSON();

        CreateTTS_STT();

        viewPager       = findViewById(R.id.mypager);
        pagerAdapter    = new MyPagerAdapter(this);
        viewPager.setAdapter(pagerAdapter);

        wordsFragment       = (WordsFragment)       WordsFragment.newInstance("Fragment 1");
        verbsFragment       = (VerbsFragment)       VerbsFragment.newInstance("Fragment 2");
        sentencesFragment   = (SentencesFragment)   SentencesFragment.newInstance("Fragment 3");
        idiomsFragment      = (IdiomsFragment)      IdiomsFragment.newInstance("Fragment 4");
        videosFragment      = (VideosFragment)      VideosFragment.newInstance("Fragment 5");
//inflating tab layout
        tabLayout =findViewById(R.id.tab_layout);
        tabLayout.setTabTextColors(Color.WHITE,Color.rgb(212,175,55));
//displaying tabs
        new TabLayoutMediator(tabLayout, viewPager,
                (tab, position) -> tab.setText(titles[position])).attach();
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int c = tab.getPosition();
                switch (c) {
                    case 0:
                        wordsFragment.UpdateList();
                        CURRIENTPAGETYPE=DB_WORDS_PAGES;
                        TVSCREEN_MAXLINES=TVSCREEN_WORD_PAGE_MAXLINES;
                        break;
                    case 1:
                        verbsFragment.UpdateList();
                        CURRIENTPAGETYPE=DB_VERBS_PAGES;
                        TVSCREEN_MAXLINES=TVSCREEN_VERB_PAGE_MAXLINES;
                        break;
                    case 2:
                        sentencesFragment.UpdateList();
                        CURRIENTPAGETYPE=DB_SENTENCES_PAGES;
                        TVSCREEN_MAXLINES=TVSCREEN_SENTENCES_PAGE_MAXLINES;
                        break;
                    case 3:
                        idiomsFragment.UpdateList();
                        CURRIENTPAGETYPE=DB_IDIOMS_PAGES;
                        TVSCREEN_MAXLINES=TVSCREEN_IDIOMS_PAGE_MAXLINES;
                        break;
                    case 4:
                        CURRIENTPAGETYPE=DB_VIDEOS_PAGES;
                        break;
                    default:
                        CURRIENTPAGETYPE=DB_WORDS_PAGES;
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                int c = tab.getPosition();
                if (c== 3 ){
                    if (videosFragment.mRecyclerView!=null) {
                        videosFragment.mRecyclerView.pausePlayer();
                    }
                }
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        fab =   findViewById(R.id.fab);
        fab.setScaleType(ImageView.ScaleType.FIT_CENTER);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if (videosFragment.mRecyclerView!=null) {
//                    videosFragment.mRecyclerView.onPausePlayer();
//                }
                if(flag){
                    fab.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),
                            R.drawable.ic_settings_button_64));
                    startActivity(new Intent(MainActivity.this, setLay.class));
                    flag = false;
                }else if(!flag){
                    fab.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),
                            R.drawable.ic_settings_button_64));
                    startActivity(new Intent(MainActivity.this, setLay.class));
                    flag = true;
                }
            }
        });
        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.arg1) {

                    case MESSAGE_THROW_EXCEPTION: //opens contacts application to browse contacts
                        throw new RuntimeException();
                    case MESSAGE_STOP_SERVICE: //opens contacts application to browse contacts
                        onPause();
                        break;
                    case MESSAGE_SAVE_DOCUMENT:
                        saveDB();
                        break;
                    case MESSAGE_START_SPEECH_SERVICE:
//                        Log.i("\nMESSAGE_START_SPEECH_SERVICE",".............Messsage Sent");
                        break;
                    case MESSAGE_STOP_SPEECH_SERVICE:
//                        Log.i("\nMESSAGE_STOP_SPEECH_SERVICE",".............Messsage Sent");
                        break;
                    case MESSAGE_RESET_SETTINGS_WINDOW:
                        mkdb.InitFillAllMember();
                        saveDB();
                        mkdb.TxtToJson(null,false);
                        SENDMESSAGE(MESSAGE_UPDATE_ALL_FRAGMENT_LISTVIEW);
                        CreateTTS_STT();
                        break;
                    case MESSAGE_SETTING_UPDATE_WINDOW:
                        saveDB();
                        mkdb.TxtToJson((BufferedReader)msg.obj,msg.arg2 == 1 ? true:false);
                        SENDMESSAGE(MESSAGE_UPDATE_ALL_FRAGMENT_LISTVIEW);
                        CreateTTS_STT();
                        break;
                    case MESSAGE_UPDATE_ALL_FRAGMENT_LISTVIEW:
                        wordsFragment.SetUpdate();
                        wordsFragment.UpdateList();
                        verbsFragment.SetUpdate();
                        verbsFragment.UpdateList();
                        sentencesFragment.SetUpdate();
                        sentencesFragment.UpdateList();
                        idiomsFragment.SetUpdate();
                        idiomsFragment.UpdateList();

                        break;
                    case MESSAGE_UPDATE_CURRENT_FRAGMENT_LISTVIEW:
                        if (CURRIENTPAGETYPE.trim().equals(DB_WORDS_PAGES)) {
                            wordsFragment.SetUpdate();
                            wordsFragment.UpdateList();
                        }else if (CURRIENTPAGETYPE.trim().equals(DB_VERBS_PAGES)) {
                            verbsFragment.SetUpdate();
                            verbsFragment.UpdateList();
                        }else if (CURRIENTPAGETYPE.trim().equals(DB_SENTENCES_PAGES)) {
                            sentencesFragment.SetUpdate();
                            sentencesFragment.UpdateList();
                        }else if (CURRIENTPAGETYPE.trim().equals(DB_IDIOMS_PAGES)) {
                            idiomsFragment.SetUpdate();
                            idiomsFragment.UpdateList();
                        }
                        break;
                    default:
                        break;
                }
            }
        };
    }


    @Override
    public void onConfigurationChanged(Configuration newConfig)
    {
//        Log.d("tag", "config changed");
        super.onConfigurationChanged(newConfig);
//        SENDMESSAGESCREENWINDOW(MESSAGE_SCREENVIEW_PAUSE_BUTTON_CLICK);
//        int orientation = newConfig.orientation;
//        if (orientation == Configuration.ORIENTATION_PORTRAIT)
//            Log.d("tag", "Portrait");
//        else if (orientation == Configuration.ORIENTATION_LANDSCAPE)
//            Log.d("tag", "Landscape");
//        else
//            Log.w("tag", "other: " + orientation);
    }

    private void showCustomDialog(String message, DialogInterface.OnClickListener listener) {
        new androidx.appcompat.app.AlertDialog.Builder(g_Activity)
                .setMessage(message)
                .setPositiveButton("Ok", listener)
                .setCancelable(false)
                .create()
                .show();
    }

    private class MyPagerAdapter extends FragmentStateAdapter {
        public MyPagerAdapter(FragmentActivity fa) {
            super(fa);
        }
        @Override
        public Fragment createFragment(int pos) {
            switch (pos) {
                case 0: {
                    return wordsFragment;
//                    return WordsFragment.newInstance("fragment 1");
                }
                case 1: {
                    return verbsFragment;
//                    return SentencesFragment.newInstance("fragment 2");
                }
                case 2: {
                    return sentencesFragment;
//                    return VideosFragment.newInstance("fragment 3");
                }
                case 3: {
                    return idiomsFragment;
//                    return VideosFragment.newInstance("fragment 3");
                }
                case 4: {
                    return videosFragment;
//                    return VideosFragment.newInstance("fragment 3");
                }
                default:
                    return wordsFragment;
//                    return WordsFragment.newInstance("fragment 1, Default");
            }
        }

        @Override
        public int getItemCount() {
            return TABLAYOUT_PAGE_SIZE;
        }
    }


    @Override
    public void onBackPressed() {
        if (videosFragment.mRecyclerView!=null) {
            videosFragment.mRecyclerView.pausePlayer();
        }

        if (viewPager.getCurrentItem() == 0) {
// If the user is currently looking at the first step, allow the system to handle the
            // Back button. This calls finish() on this activity and pops the back stack.d
            super.onBackPressed();
        } else {
// Otherwise, select the previous step.
            viewPager.setCurrentItem(viewPager.getCurrentItem() - 1);
        }
    }
    protected void CreateTTS_STT(){
        intentSpeech    = new Intent(this,ServiceClass.class);
        intentTTS       = new Intent(this,TTS.class);
    }
    protected void  saveDB(){
        if (mkdb!=null) {
            mkdb.WriteFileInternalStorageJSON();
        }
    }

    @Override
    protected void onStop() {
        if (videosFragment.mRecyclerView!=null) {
            videosFragment.mRecyclerView.pausePlayer();
        }
        if (intentTTS!=null) {
            stopService(intentTTS);
        }
        if (intentSpeech!=null) {
            stopService(intentSpeech);
        }

        super.onStop();
    }

    public boolean checkLanguage(boolean bFullUpdate){
        if (bFullUpdate){
            dbDeleteDBFileFromIntStorage();
        }

        if (dbFileHasFromIntStorage()){
            initDialog();
            return false;
        }

        AlertDialog.Builder alert = new AlertDialog.Builder(g_Context);
        alert.setTitle("Choose your native language");
        alert.setSingleChoiceItems(NATIVE_LANGUAGES, DB_MKDB_INIT_NATIVE_LANGUAGE, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // user checked an item
                DB_MKDB_INIT_NATIVE_LANGUAGE = which;
            }
        });
        alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // user clicked OK
                initDialog();
            }
        });

        alert.show();

        return true;
    }

    @Override
    protected void onDestroy() {
        WakeLocker.release();
        unregisterInstallStateUpdListener();
        super.onDestroy();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkNewAppVersionState();
    }

    @Override
    public void onActivityResult(int requestCode, final int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        switch (requestCode) {
            case REQ_CODE_VERSION_UPDATE:
                if (resultCode == RESULT_OK) { //RESULT_OK / RESULT_CANCELED / RESULT_IN_APP_UPDATE_FAILED
                    checkLanguage(true);
                }else{
                    Log.d("Update flow failed! Result code: " , "Code:"+ resultCode);
                    // If the update is cancelled or fails,
                    // you can request to start the update again.
                    checkLanguage(false);
                }
                unregisterInstallStateUpdListener();
                break;
        }
    }

    private void checkForAppUpdate() {
        // Creates instance of the manager.
        appUpdateManager = AppUpdateManagerFactory.create(this);

        // Returns an intent object that you use to check for an update.
        Task<AppUpdateInfo> appUpdateInfoTask = appUpdateManager.getAppUpdateInfo();

        // Create a listener to track request state updates.
        installStateUpdatedListener = new InstallStateUpdatedListener() {
            @Override
            public void onStateUpdate(InstallState installState) {
                // Show module progress, log state, or install the update.
                if (installState.installStatus() == InstallStatus.DOWNLOADED) {
                    // After the update is downloaded, show a notification
                    // and request user confirmation to restart the app.
                    popupSnackbarForCompleteUpdateAndUnregister();
                }
            }
        };

        // Checks that the platform will allow the specified type of update.
        appUpdateInfoTask.addOnSuccessListener(appUpdateInfo -> {
            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE) {
                // Request the update.
                if (appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.FLEXIBLE)) {

                    // Before starting an update, register a listener for updates.
                    appUpdateManager.registerListener(installStateUpdatedListener);
                    // Start an update.
                    startAppUpdateFlexible(appUpdateInfo);
                } else if (appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE) ) {
                    // Start an update.
                    startAppUpdateImmediate(appUpdateInfo);
                }
            }else{
                checkLanguage(false);
            }
        });
    }

    private void startAppUpdateImmediate(AppUpdateInfo appUpdateInfo) {
        try {
            appUpdateManager.startUpdateFlowForResult(
                    appUpdateInfo,
                    AppUpdateType.IMMEDIATE,
                    // The current activity making the update request.
                    this,
                    // Include a request code to later monitor this update request.
                    MainActivity.REQ_CODE_VERSION_UPDATE);
        } catch (IntentSender.SendIntentException e) {
            e.printStackTrace();
        }
    }

    private void startAppUpdateFlexible(AppUpdateInfo appUpdateInfo) {
        try {
            appUpdateManager.startUpdateFlowForResult(
                    appUpdateInfo,
                    AppUpdateType.FLEXIBLE,
                    // The current activity making the update request.
                    this,
                    // Include a request code to later monitor this update request.
                    MainActivity.REQ_CODE_VERSION_UPDATE);
        } catch (IntentSender.SendIntentException e) {
            e.printStackTrace();
            unregisterInstallStateUpdListener();
            checkLanguage(false);
        }
    }

    /**
     * Displays the snackbar notification and call to action.
     * Needed only for Flexible app update
     */
    private void popupSnackbarForCompleteUpdateAndUnregister() {
        CoordinatorLayout drawerLayout = (CoordinatorLayout) findViewById(R.id.coordinatorLayout);
        Snackbar snackbar =
                Snackbar.make(drawerLayout, "ACTION NEED!", Snackbar.LENGTH_INDEFINITE);
        snackbar.setAction("UPDATE AVAIBLE", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                appUpdateManager.completeUpdate();
            }
        });
        snackbar.setTextColor(ContextCompat.getColor(this,R.color.white));
        snackbar.setActionTextColor(ContextCompat.getColor(this,R.color.golden_metalic));
        snackbar.setBackgroundTint(ContextCompat.getColor(this,R.color.black));
        snackbar.show();

        unregisterInstallStateUpdListener();
    }

    /**
     * Checks that the update is not stalled during 'onResume()'.
     * However, you should execute this check at all app entry points.
     */
    private void checkNewAppVersionState() {
        appUpdateManager
            .getAppUpdateInfo()
            .addOnSuccessListener(
                appUpdateInfo -> {
                    //FLEXIBLE:
                    // If the update is downloaded but not installed,
                    // notify the user to complete the update.
                    if (appUpdateInfo.installStatus() == InstallStatus.DOWNLOADED) {
                        popupSnackbarForCompleteUpdateAndUnregister();
                    }

                    //IMMEDIATE:
                    if (appUpdateInfo.updateAvailability()
                            == UpdateAvailability.DEVELOPER_TRIGGERED_UPDATE_IN_PROGRESS) {
                        // If an in-app update is already running, resume the update.
                        startAppUpdateImmediate(appUpdateInfo);
                    }
                });
    }

    /**
     * Needed only for FLEXIBLE update
     */
    private void unregisterInstallStateUpdListener() {
        if (appUpdateManager != null && installStateUpdatedListener != null)
            appUpdateManager.unregisterListener(installStateUpdatedListener);
    }
}

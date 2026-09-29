package com.mke.mword.Utils;




import static com.mke.mword.Database.Constants.CURRIENTPAGETYPE;
import static com.mke.mword.Database.Constants.DB_IDIOMS;
import static com.mke.mword.Database.Constants.DB_IDIOMS_PAGES;
import static com.mke.mword.Database.Constants.DB_MKDB_INIT_NATIVE_LANGUAGE;
import static com.mke.mword.Database.Constants.DB_SAVE_DATA_JSON_FILENAME;
import static com.mke.mword.Database.Constants.DB_SENTENCES;
import static com.mke.mword.Database.Constants.DB_SENTENCES_PAGES;
import static com.mke.mword.Database.Constants.DB_VERBS;
import static com.mke.mword.Database.Constants.DB_VERBS_PAGES;
import static com.mke.mword.Database.Constants.DB_WORDS;
import static com.mke.mword.Database.Constants.DB_WORDS_PAGES;
import static com.mke.mword.Database.Constants.MAINWINDOW_GREEN_COLOR;
import static com.mke.mword.Database.Constants.MAINWINDOW_RED_COLOR;
import static com.mke.mword.Database.Constants.NATIVE_LANGUAGES;
import static com.mke.mword.Database.Constants.PAGE_BUTTON_TOP_SPACE_FROM_LAYOUT;
import static com.mke.mword.Database.Constants.SCREENPAGESTATUS;
import static com.mke.mword.Database.Constants.SCREEN_PAGE_EXAM;
import static com.mke.mword.Database.Constants.SCREEN_PAGE_LISTEN;
import static com.mke.mword.Database.Constants.SCREEN_PAGE_TALK;
import static com.mke.mword.Database.Constants.SCREEN_PAGE_WRITE;
import static com.mke.mword.Database.Constants.THIS_EXAM_IS_OVER;
import static com.mke.mword.Database.Constants.TVSCREEN_ENGWORD_TEXTHEIGHT_COEFF;
import static com.mke.mword.Database.mkDB.nPageNumber;
import static com.mke.mword.MainActivity.g_Activity;
import static com.mke.mword.MainActivity.g_Context;
import static com.mke.mword.MainActivity.mkdb;
import static com.mke.mword.ScreenView.mHandlerScreenWindow;
import static com.mke.mword.ScreenView.szTalkString;
import static com.mke.mword.WriteScreen.mHandlerWriteScreenWindow;
import static com.mke.mword.quizScreen.mHandlerQuizScreenWindow;
import static java.lang.Thread.sleep;
import static javax.mail.Session.getDefaultInstance;

import android.app.*;
import android.content.*;
import android.content.res.*;
import android.graphics.*;
import android.net.*;
import android.os.*;
import android.text.*;
import android.text.style.*;
import android.util.*;
import android.view.*;
import android.widget.*;

import com.mke.mword.*;

import org.json.*;

import java.io.*;
import java.security.*;
import java.text.*;
import java.util.*;
import java.util.concurrent.*;

public class Utils
{

    public static boolean FileExistFromAssetFiles(String path,String szFileFileName) {
        String [] list;
        try {
            list = g_Context.getAssets().list(path);
            if (list.length > 0) {
                // This is a folder
                for (String file : list) {
                    if (file.trim().equals(szFileFileName)) {
                        return true;
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return false;
    }
    /* Re size the font so the specified text fits in the text box
     * assuming the text box is the specified width.
     */
    public static String getCurrentWVSName(){
        String szWVS="";
        if (CURRIENTPAGETYPE == DB_WORDS_PAGES) {
            szWVS = "_Words ";
        }else if (CURRIENTPAGETYPE == DB_VERBS_PAGES) {
            szWVS = "_Verbs ";
        }else if (CURRIENTPAGETYPE == DB_SENTENCES_PAGES) {
            szWVS = "_Sentences ";
        }

        return szWVS;
    }

    public static android.text.SpannableString  QuestionExamResults(String szPage, int nPageN) {
        String szInfo1 = "", szInfo2 = "";
        if (SCREENPAGESTATUS == SCREEN_PAGE_EXAM) {
            szInfo1 = CURRIENTPAGETYPE+ "Number:" + Integer.toString(nPageN + 1)+"\n";

            szInfo2 ="Lerned" + getCurrentWVSName() +"Size:"     + Integer.toString(mkdb.getQuestionLernSize(szPage,nPageN)) + "   " +"\n"+
                    "Page Questions Size:"                      + Integer.toString(mkdb.getQuestionSize()) + "   " +"\n"+
                    "Last Question No:"                         + Integer.toString(mkdb.getPageLastQuestionNumber()) + "   " +"\n"+
                    "Page Watch Size:"                          + Integer.toString(mkdb.GetPageWatchCount(szPage, nPageN) + 1) + "   " +"\n"+
                    "Exam Finish Time:"                         + LongToStringDate(Long.valueOf(mkdb.getQuestionTime()));
        }

        szInfo1= THIS_EXAM_IS_OVER + "\n" +  szInfo1;

        if (mkdb.CheckThisQPageIsLearn(CURRIENTPAGETYPE,nPageNumber)) {
            szInfo1 = "CONGRULATIONS! YOU PASSED!"+ "\n" + szInfo1;
            return GetSpanText(szInfo1,szInfo2,1.1f,0.9f,
                    MAINWINDOW_GREEN_COLOR, MAINWINDOW_GREEN_COLOR);
        }
        return GetSpanText(szInfo1,szInfo2,1.1f,0.9f,
                MAINWINDOW_GREEN_COLOR, MAINWINDOW_RED_COLOR);
    }

    public static android.text.SpannableString GetInfoScreen(String szPage, int nPageN, int nWordN){
        String szInfo1="",szInfo2="";
        if (SCREENPAGESTATUS==SCREEN_PAGE_EXAM) {
            szInfo1="Question:"+Integer.toString(nWordN+1)+ "    "+
                    "Page:"+Integer.toString(nPageN+1);

            szInfo2 = "Lrn.:" + Integer.toString(mkdb.getQuestionLernSize(szPage,nPageN)+ 1) + "   " +
                      "Qsts.:" + Integer.toString(mkdb.getQuestionSize()) + "   " +
                      "LQst.:" + Integer.toString(mkdb.getPageLastQuestionNumber()) + "   " +
                      "WW:" + Integer.toString(mkdb.getQuestionWWatch(nWordN) + 1) + "   " +
                      "PW:" + Integer.toString(mkdb.GetPageWatchCount(szPage, nPageN) + 1) + "   " +
                      "Tm:"    + LongToStringDate(Long.valueOf(mkdb.getQuestionTime()));
        }else {
            String szWVS="";
            String szWVSSize="";
            if (szPage.trim().equals(DB_VERBS_PAGES)){
                szWVS="Verb:";
                szWVSSize="VSize:";
            }else if (szPage.trim().equals(DB_WORDS_PAGES)){
                szWVS="Word:";
                szWVSSize="WSize:";
            }else{
                szWVS="Sentence:";
                szWVSSize="SSize:";
            }

            szInfo1=szWVS+Integer.toString(nWordN+1)+ "    "+
                    "Page:"+Integer.toString(nPageN+1);

            szInfo2 = "Lrn:" + Integer.toString(mkdb.GetLernWVSSize(szPage, nPageN)) + "   " +
                    "LWVS.:" + Integer.toString(mkdb.GetPageLastWVSNumber(szPage, nPageN)) + "   " +
                    szWVSSize + Integer.toString(mkdb.GetWVSSize(szPage, nPageN)) + "   " +
                    "WW:" + Integer.toString(mkdb.GetWVSWatchCount(szPage, nPageN, nWordN) + 1) + "   " +
                    "PW:" + Integer.toString(mkdb.GetPageWatchCount(szPage, nPageN) + 1) + "   " +
                    "Time:" + LongToStringDate(Long.valueOf(mkdb.GetPageTime(szPage, nPageN)));
        }
        return GetSpanText(szInfo1,szInfo2,1.1f,0.6f,
                                                R.color.black, R.color.black);
//                Color.rgb(212,175,55), Color.rgb(212,175,55));
    }
    public static android.text.SpannableString GetPageButon(String szPage,int nPageN){
        String szWVS="";
        if (szPage.trim().equals(DB_VERBS_PAGES)){
            szWVS="Verb Size:";
        }else if (szPage.trim().equals(DB_WORDS_PAGES)){
            szWVS="Word Size:";
        }else if (szPage.trim().equals(DB_IDIOMS_PAGES)){
            szWVS="Idiom Size:";
        }else{
            szWVS="Sntnce Size:";
        }

        String szInfo1="Page:"+Integer.toString(nPageN+1) + "    " +
                szWVS+Integer.toString(mkdb.GetWVSSize(szPage,nPageN));

        String szInfo2=
                "LTWLS:"+Integer.toString(mkdb.GetLernWVSSize(szPage,nPageN))              +"   "+
                "ELS:"+Integer.toString(mkdb.getQuestionLernSize(szPage,nPageN))              +"   "+
                "LWVS:"+Integer.toString(mkdb.GetPageLastWVSNumber(szPage,nPageN)+1)        +"   "+
                "PW:"+Integer.toString(mkdb.GetPageWatchCount(szPage,nPageN)+1)           +"   "+
                "Tm:"+ LongToStringDate (Long.valueOf(mkdb.GetPageTime(szPage,nPageN)));

        return GetSpanText(szInfo1,szInfo2,1.3f,0.7f,
                Color.rgb(212,175,55), Color.rgb(212,175,55));
    }

    public static android.text.SpannableString GetSpanText(String firstRow,String secondRow,
                                       float firstRowScale,  float secondRowScale,
                                       int firstRowColor,int secondRowColor){
        String szInfo = firstRow + "\n" + secondRow;
        android.text.SpannableString ss1 = null;
        ss1=  new android.text.SpannableString(szInfo);
        ss1.setSpan(new android.text.style.RelativeSizeSpan(firstRowScale), 0,firstRow.length(), 0); // set size
        ss1.setSpan(new android.text.style.RelativeSizeSpan(secondRowScale), firstRow.length(),szInfo.length(), 0); // set size
        ss1.setSpan(new android.text.style.ForegroundColorSpan(firstRowColor),0,firstRow.length(), 0);// se
        ss1.setSpan(new android.text.style.ForegroundColorSpan(secondRowColor), firstRow.length(),szInfo.length(), 0);// se
        return ss1;
    }

    public static String LongToStringDate(long millis) {
        return String.format("%02d:%02d:%02d",
                TimeUnit.MILLISECONDS.toHours(millis),
                TimeUnit.MILLISECONDS.toMinutes(millis) -
                TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)),
                TimeUnit.MILLISECONDS.toSeconds(millis) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)));
    }

    public static int GetButtonHeight(int LayoutHeight,int nButtons) {
        int nHeight = 180;
        if (LayoutHeight>nHeight){
           nHeight= ((LayoutHeight - ((nButtons+1)*PAGE_BUTTON_TOP_SPACE_FROM_LAYOUT))/nButtons)-3;
        }
        return nHeight;
    }

    public static String GetButtonCaption(String szPage,int nPageN) {
        String szCap="";
        String szWVS="";
        if (szPage.trim().equals(DB_VERBS_PAGES)){
            szWVS="Verb:";
        }else if (szPage.trim().equals(DB_WORDS_PAGES)){
            szWVS="Word:";
        }else{
            szWVS="Sntnce:";
        }

        if (mkdb!=null) {
            szCap = "Page:" + (nPageN+1) + "....." +
                    szWVS + mkdb.GetWVSSize(szPage,nPageN) + "....." +
                        "Lerned :" + mkdb.GetLernWVSSize(szPage,nPageN);
        }
        return szCap;
    }

    public static android.text.SpannableString GetTextViewWord(String szText){
        android.text.SpannableString ss1 = null;

        ss1=  new android.text.SpannableString(szText);
//        ss1.setSpan(new android.text.style.RelativeSizeSpan(2f), 0,1, 0); // set size
//        ss1.setSpan(new android.text.style.ForegroundColorSpan(android.graphics.Color.RED), 0, 5, 0);// set color
        return ss1;
    }
    public static android.text.SpannableString GetSpeakWord(String szPage,int nPNo,int nWNo){
        return GetSpeakWord(szPage,nPNo,nWNo,false);
    }
    public static android.text.SpannableString GetSpeakWord(String szPage,int nPNo,int nWNo,boolean bLearn){
        android.text.SpannableString ss1 = null;

        if (mkdb!=null){
            String szAdd="";
            if (szPage.trim().equals(DB_VERBS_PAGES)){
                szAdd +=mkdb.GetVerbsString(nPNo,nWNo);
            }else if (szPage.trim().equals(DB_WORDS_PAGES)){
                szAdd="";
            }else{
                szAdd="";
            }

            if (SCREENPAGESTATUS==SCREEN_PAGE_LISTEN) {
                String szEngWord= mkdb.GetEngWVS(szPage,nPNo,nWNo)+szAdd;
                String szOlang  = mkdb.GetOtherLanguageWVS(szPage,nPNo,nWNo);
                szEngWord.replaceAll("\\s", " ");

                return GetSpeakWord(szEngWord,szOlang);
            }else if (SCREENPAGESTATUS==SCREEN_PAGE_TALK) {
                if ((!szTalkString.toLowerCase().contains("congrulations") && !mkdb.bEleminateMode)) {
                    String szSpeak = mkdb.GetOtherLanguageWVS(szPage, nPNo, nWNo);
                    ss1 = new android.text.SpannableString(szSpeak);
                    ss1.setSpan(new android.text.style.RelativeSizeSpan(TVSCREEN_ENGWORD_TEXTHEIGHT_COEFF),
                            0, szSpeak.length(), 0); // set size
                }else{
                    String szEngWord= mkdb.GetEngWVS(szPage,nPNo,nWNo);
                    String szOlang  = mkdb.GetOtherLanguageWVS(szPage,nPNo,nWNo);
                    return GetSpeakWord(szEngWord+szAdd,szOlang);
                }
            }else if (SCREENPAGESTATUS==SCREEN_PAGE_WRITE) {
                if (bLearn){
                    String szEngWord= mkdb.GetEngWVS(szPage+szAdd,nPNo,nWNo);
                    String szOlang  = mkdb.GetOtherLanguageWVS(szPage,nPNo,nWNo);
                    return GetSpeakWord(szEngWord,szOlang);
                }else {
                    String szSpeak = mkdb.GetOtherLanguageWVS(szPage, nPNo, nWNo);
                    ss1 = new android.text.SpannableString(szSpeak);
                    ss1.setSpan(new android.text.style.RelativeSizeSpan(TVSCREEN_ENGWORD_TEXTHEIGHT_COEFF),
                            0, szSpeak.length(), 0); // set size
                }
            }else if (SCREENPAGESTATUS==SCREEN_PAGE_EXAM) {
            }
        }
        return ss1;
    }

    public static ArrayList<String> mixList(ArrayList<String> szList){
        ArrayList<String>   szListRet =new ArrayList<String>();
        ArrayList<String>   szListTmp =new ArrayList<String>();
        szListTmp= szList;

        int n = GetRandom(0,4);
        szListRet.add(szListTmp.get(n));
        szListTmp.remove(n);

        n = GetRandom(0,3);
        szListRet.add(szListTmp.get(n));
        szListTmp.remove(n);

        n = GetRandom(0,2);
        szListRet.add(szListTmp.get(n));
        szListTmp.remove(n);

        szListRet.add(szListTmp.get(0));
        szListTmp.remove(0);

        return szListRet;

    }

    public static android.text.SpannableString GetSpeakWord(String szEngWord,String szOlang){
        android.text.SpannableString ss1 = null;
        if (mkdb!=null){
            if (!szEngWord.isEmpty() && !szOlang.isEmpty()){
                String szSpeak = szEngWord + "\n" + szOlang;
                ss1=  new android.text.SpannableString(szSpeak);
                ss1.setSpan(new android.text.style.RelativeSizeSpan(TVSCREEN_ENGWORD_TEXTHEIGHT_COEFF),
                        0,szEngWord.length(), 0); // set size
                ss1.setSpan(new android.text.style.
                            ForegroundColorSpan(Color.rgb(212,175,55)),
                            0,szEngWord.length(), 0);// set color
            }
        }
        return ss1;
    }
    public static final boolean[] answer = new boolean[1];
    public static boolean createAndShowAlertDialog(android.app.Activity activity,
                                                String szMessage,int ifYesMsgID,int ifNoMsgID) {

        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(activity);
                        builder.setTitle(szMessage);


        //            builder.setTitle("Do you want to listen this page?");
        builder.setPositiveButton(android.R.string.yes, new android.content.DialogInterface.OnClickListener() {
            public void onClick(android.content.DialogInterface dialog, int id) {
                //TODO
                if (ifYesMsgID>0){
                    SENDMESSAGE(ifYesMsgID);
                }
                answer[0] = true;
//                SENDMESSAGE(MESSAGE_LISTEN_SAME_PAGE);
                dialog.dismiss();
            }
        });
        builder.setNegativeButton(android.R.string.cancel, new android.content.DialogInterface.OnClickListener() {
            public void onClick(android.content.DialogInterface dialog, int id) {
                //TODO
//                SENDMESSAGE(MESSAGE_NEW_PAGE_BUTTONS_MAKE_VISIBLE);
                if (ifYesMsgID>0){
                    SENDMESSAGE(ifNoMsgID);
                }
                answer[0] = false;
                dialog.dismiss();
            }
        });
        androidx.appcompat.app.AlertDialog dialog = builder.create();
        dialog.show();
        return answer[0];
    }

    public static boolean SENDMESSAGEEXAMSCREENWINDOW(int arg1, int arg2, Object obj,
                                                       int nUId, int what, android.os.Messenger msn){
        if (mHandlerQuizScreenWindow!= null) {
            android.os.Message msg = android.os.Message.obtain();
            msg.arg1        = arg1;
            msg.arg2        = arg2;
            msg.obj         = obj;
            msg.sendingUid  = nUId;
            msg.what        = what;
            msg.replyTo     = msn;
            mHandlerQuizScreenWindow.sendMessage(msg);
            return true;
        }
        return false;
    }
    public static boolean SENDMESSAGEEXAMSCREENWINDOW(int arg1) {
        return SENDMESSAGEEXAMSCREENWINDOW(arg1, 0, null,0, 0, null);
    }
    public static boolean SENDMESSAGEEXAMSCREENWINDOW(int arg1,int arg2,Object obj) {
        return SENDMESSAGEEXAMSCREENWINDOW(arg1, arg2, obj,0, 0, null);
    }
    public static boolean SENDMESSAGEEXAMSCREENWINDOW(int arg1,int arg2,Object obj,int id) {
        return SENDMESSAGEEXAMSCREENWINDOW(arg1, arg2, obj,id, 0, null);
    }
    public static boolean SENDMESSAGEEXAMSCREENWINDOW(int arg1,Object obj) {
        return SENDMESSAGEEXAMSCREENWINDOW(arg1, 0, obj,0, 0, null);
    }
    public static boolean SENDMESSAGEEXAMSCREENWINDOW(int arg1,int arg2) {
        return SENDMESSAGEEXAMSCREENWINDOW(arg1, arg2, null,0, 0, null);
    }
    public static boolean SENDMESSAGEWRITESCREENWINDOW(int arg1, int arg2, Object obj,
                                                  int nUId, int what, android.os.Messenger msn){
        if (mHandlerWriteScreenWindow!= null) {
            android.os.Message msg = android.os.Message.obtain();
            msg.arg1        = arg1;
            msg.arg2        = arg2;
            msg.obj         = obj;
            msg.sendingUid  = nUId;
            msg.what        = what;
            msg.replyTo     = msn;
            mHandlerWriteScreenWindow.sendMessage(msg);
            return true;
        }
        return false;
    }
    public static boolean SENDMESSAGEWRITESCREENWINDOW(int arg1) {
        return SENDMESSAGEWRITESCREENWINDOW(arg1, 0, null,0, 0, null);
    }
    public static boolean SENDMESSAGEWRITESCREENWINDOW(int arg1,int arg2,Object obj) {
        return SENDMESSAGEWRITESCREENWINDOW(arg1, arg2, obj,0, 0, null);
    }
    public static boolean SENDMESSAGEWRITESCREENWINDOW(int arg1,int arg2,Object obj,int id) {
        return SENDMESSAGEWRITESCREENWINDOW(arg1, arg2, obj,id, 0, null);
    }

    public static boolean SENDMESSAGESCREENWINDOW(int arg1, int arg2, Object obj,
                                      int nUId, int what, android.os.Messenger msn){
        if (mHandlerScreenWindow!= null) {
            android.os.Message msg = android.os.Message.obtain();
            msg.arg1        = arg1;
            msg.arg2        = arg2;
            msg.obj         = obj;
            msg.sendingUid  = nUId;
            msg.what        = what;
            msg.replyTo     = msn;
            mHandlerScreenWindow.sendMessage(msg);
            return true;
        }
        return false;
    }
    public static boolean SENDMESSAGESCREENWINDOW(int arg1) {
        return SENDMESSAGESCREENWINDOW(arg1, 0, null,0, 0, null);
    }
    public static boolean SENDMESSAGESCREENWINDOW(int arg1,Object obj) {
        return SENDMESSAGESCREENWINDOW(arg1, 0, obj,0, 0, null);
    }
    public static boolean SENDMESSAGESCREENWINDOW(int arg1,int arg2,Object obj) {
        return SENDMESSAGESCREENWINDOW(arg1, arg2, obj,0, 0, null);
    }
    public static boolean SENDMESSAGE(int arg1, int arg2, Object obj,
                                      int nUId, int what, android.os.Messenger msn){
        if (MainActivity.mHandler!= null) {
            android.os.Message msg = android.os.Message.obtain();
            msg.arg1        = arg1;
            msg.arg2        = arg2;
            msg.obj         = obj;
            msg.sendingUid  = nUId;
            msg.what        = what;
            msg.replyTo     = msn;
            MainActivity.mHandler.sendMessage(msg);
            return true;
        }
        return false;
    }
    public static boolean SENDMESSAGE(int arg1) {
        return SENDMESSAGE(arg1, 0, null,0, 0, null);
    }
    public static boolean SENDMESSAGE(int arg1,Object obj) {
        return SENDMESSAGE(arg1, 0, obj,0, 0, null);
    }
    public static boolean SENDMESSAGE(int arg1,int arg2) {
        return SENDMESSAGE(arg1, arg2, null,0, 0, null);
    }
    public static boolean SENDMESSAGE(int arg1,int arg2,Object obj) {
        return SENDMESSAGE(arg1, arg2, obj,0, 0, null);
    }
    public static void SLEEP(int nTime) {
        try {
            Thread.sleep(nTime);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    public static boolean Text2Boolean(String input){
        String s = input.toLowerCase();
        Boolean b=false ;
        if ("true".trim().equals(s) || "false".trim().equals(s)) {
            b= Boolean.parseBoolean(s);
        }
        return b;
    }
    public static void MK_TAG(String message) {
        String fullClassName = Thread.currentThread().getStackTrace()[2].getClassName();
        String className = fullClassName.substring(fullClassName.lastIndexOf(".") + 1);
        String methodName = Thread.currentThread().getStackTrace()[2].getMethodName();
        int lineNumber = Thread.currentThread().getStackTrace()[2].getLineNumber();

//        android.util.Logd("MK_Engineering" +className + "." + methodName + "():" + lineNumber, message);
    }

    public static boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) g_Activity.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().trim().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    public static String GetFlagUrlSite(String countryCode) {
        return new String("https://www.countryflags.io/" + countryCode + "/shiny/64.png");
    }

    public static String GetCurrentDate() {
        DateFormat df = new SimpleDateFormat("dd.MM.yy");
        String date = df.format(Calendar.getInstance().getTime());
        return date;
    }

    public static double StringToDouble(String stVal) {
        if (!stVal.isEmpty()) {
            return Double.valueOf(stVal);
        }
        return 0;
    }


    public static SpannableStringBuilder SpannableStringBuilder(String text) {
        RelativeSizeSpan smallSizeText = new RelativeSizeSpan(.5f);
        SpannableStringBuilder ssBuilder = new SpannableStringBuilder(text);
        ssBuilder.setSpan(
                smallSizeText,
                text.indexOf(" "),
                text.length(),
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        );
        return ssBuilder;
    }
    public static int getIntToPixel(float realSize,Context context) {
        if (context!=null) {
            DisplayMetrics metrics = context.getResources().getDisplayMetrics();
            float fpixels = metrics.density * realSize;
            return ((int) (fpixels + 0.5f));
        }
        return 1;
    }

    public  static boolean SearchFolderFromPath(String szPath)
    {
        File folder = new File(szPath);
        boolean success = true;
        if (!folder.exists()) {
            success = folder.mkdirs();
        }
        return success;
    }

    public  static ArrayList<String> ReadSDCard(String szFolderName,String szFileExtension)
    {
        ArrayList<String> tFileList=null;
        //It have to be matched with the directory in SDCard
        try
        {
            tFileList= new ArrayList<String>();
            File f = new File(szFolderName);
            File[] files=f.listFiles();
            for(int i=0; i<files.length; i++)
            {
                File file = files[i];
                /*It's assumed that all file in the path are in supported type*/
                String filePath = file.getPath();
                if(filePath.endsWith(szFileExtension)) // Condition to check .jpg file extension
                    tFileList.add(filePath);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        return tFileList;
    }

    public static boolean copyFolderAssetToLocalFolder(String szLocalFolerName,String szFileName) {
        // "Name" is the name of your folder!
        AssetManager assetManager = g_Activity.getAssets();

        String state = Environment.getExternalStorageState();

        if (Environment.MEDIA_MOUNTED.trim().equals(state)) {
            // We can read and write the media
            // Checking file on assets subfolder
            InputStream in = null;
            OutputStream out = null;

            if (SearchFolderFromPath(Environment.getExternalStorageDirectory() +
                                        szLocalFolerName)) {
                // Moving all the files on external SD
                try {
                    in = assetManager.open(szFileName);
                    out = new FileOutputStream(Environment.getExternalStorageDirectory() + szLocalFolerName + szFileName );
//                    Log.i("WEBVIEW", Environment.getExternalStorageDirectory() + szLocalFolerName + szFileName);
                    copyFile(in, out);
                    in.close();
                    in = null;
                    out.flush();
                    out.close();
                    out = null;
                    return true;
                } catch(IOException e) {
//                    Log.e("ERROR", "Failed to copy asset file: " + szFileName, e);
                    return false;
                }
            }
            else {
                // Do something else on failure
            }
        } else if (Environment.MEDIA_MOUNTED_READ_ONLY.trim().equals(state)) {
            // We can only read the media
        } else {
            // Something else is wrong. It may be one of many other states, but all we need
            // is to know is we can neither read nor write
        }
        return false;
    }

    public static int FindNumberofLanguage(String szLang){
        for (int n=0;n< NATIVE_LANGUAGES.length;n++){
            if (NATIVE_LANGUAGES[n].trim().equals(szLang)){
                return n;
            }
        }
        return DB_MKDB_INIT_NATIVE_LANGUAGE;
    }
    public static String GetDBPageString(String szPageFirstChar){
        if (szPageFirstChar.trim().equals("W")){
            return DB_WORDS;
        }else if (szPageFirstChar.trim().equals("V")){
            return DB_VERBS;
        }else if (szPageFirstChar.trim().equals("S")){
            return DB_SENTENCES;
        }else if (szPageFirstChar.trim().equals("I")){
            return DB_IDIOMS;
        }

        return "";
    }
    public static int GetDBPageSize(String szPageFirstChar){
        if (szPageFirstChar.trim().equals("W")){
            return mkdb.nWordsVectorSize;
        }else if (szPageFirstChar.trim().equals("V")){
            return mkdb.nVerbsVectorSize;
        }else if (szPageFirstChar.trim().equals("I")){
            return mkdb.nIdiomsVectorSize;
        }else if (szPageFirstChar.trim().equals("S")){
            return mkdb.nSentencesVectorSize;
        }
        return 0;
    }

    // Method used by copyAssets() on purpose to copy a file.
    public static String GetLanguageCode() {
        String[] RowData = mkdb.szNativeLanguage.split(" ");
        return RowData[1];
    }
    // Method used by copyAssets() on purpose to copy a file.
    public static void copyFile(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[1024];
        int read;
        while((read = in.read(buffer)) != -1) {
            out.write(buffer, 0, read);
        }
    }

    public static SpannableString[] GetListViewList(String szPageType){
        SpannableString[] values = null;
        if (mkdb !=null && (szPageType.trim().equals(DB_WORDS_PAGES) ||
                                szPageType.trim().equals(DB_VERBS_PAGES)) ||
                                    szPageType.trim().equals(DB_SENTENCES_PAGES) ||
                                        szPageType.trim().equals(DB_IDIOMS_PAGES)) {
            values = new SpannableString[mkdb.GetPageSize(szPageType)];
            if (mkdb != null && mkdb.GetPageSize(szPageType) > 0) {
                for (int i = 0; i < mkdb.GetPageSize(szPageType); i++) {
                    values[i] = GetPageButon(szPageType, i);
                }
            }
        }
        return values;
    }

    public static void JUSTSLEEP (int nTime) {
        try {
            sleep(nTime);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    private static Random rand = new SecureRandom();

    public static int GetRandom (int min, int max){
        // Random rand = new Random();
        if (min!=max) {
            int randomNum = rand.nextInt((max - min) + min) + min;
            return randomNum;
        }
        return min;
    }
    public boolean create_questions(){
        if (mkdb == null){return false;}

        ArrayList<QuestionS> questionList   = new ArrayList<QuestionS>();

        JSONArray jsonPage= new JSONArray();
        jsonPage = mkdb.GetPageWVSJArray(CURRIENTPAGETYPE,nPageNumber);

        if (jsonPage!=null) {
            int n=0;
            while(jsonPage.length()>3) {
                int nRandomLimit = jsonPage.length();
                int i1 = GetRandom(0,nRandomLimit);
//                Log.e("ERROR", "n:"+ (n++) + "\n");
                if (!mkdb.GetWVSIsLearn(jsonPage, nPageNumber, i1)) {
                    QuestionS ques = new QuestionS();
                    ques.setQuestion(mkdb.GetOtherLanguageWVS(jsonPage, nPageNumber, i1));
                    ques.setAnswer(mkdb.GetEngWVS(jsonPage, nPageNumber, i1));
                    ques.addStringToList(mkdb.GetEngWVS(jsonPage, nPageNumber, i1));
                    jsonPage.remove(i1);

                    nRandomLimit = jsonPage.length() - 1;
                    i1 = GetRandom(0, nRandomLimit);
//                    Log.e("ERROR", "i1:"+ i1 + "\n");
                    ques.addStringToList(mkdb.GetEngWVS(jsonPage, nPageNumber, i1));
                    jsonPage.remove(i1);

                    nRandomLimit = jsonPage.length() - 1;
                    i1 = GetRandom(0, nRandomLimit);
//                    Log.e("ERROR", "i1:"+ i1 + "\n");
                    ques.addStringToList(mkdb.GetEngWVS(jsonPage, nPageNumber, i1));
                    jsonPage.remove(i1);

                    nRandomLimit = jsonPage.length() - 1;
                    i1 = GetRandom(0, nRandomLimit);
//                    Log.e("ERROR", "i1:"+ i1 + "\n");
                    ques.addStringToList(mkdb.GetEngWVS(jsonPage, nPageNumber, i1));
                    jsonPage.remove(i1);
                    ques.fillOpt();

                    questionList.add(ques);

                }else{
                    jsonPage.remove(i1);
                }
            }
        }
        return true;
    }

//    private static final boolean FINAL_CONSTANT_IS_LOCAL = true;
//    private static final String TAG = FooProvider.class.getSimpleName();

    public static String getLogTagWithMethod() {
//        if (FINAL_CONSTANT_IS_LOCAL) {
            Throwable stack = new Throwable().fillInStackTrace();
            StackTraceElement[] trace = stack.getStackTrace();
            return trace[0].getClassName() + "." + trace[0].getMethodName() + ":" + trace[0].getLineNumber();
//        }
//        else {
//            return TAG;
//        }
    }

    public static boolean dbFileHasFromIntStorage() {
        java.io.File dir = g_Context.getFilesDir();
        java.io.File file = new java.io.File(dir, DB_SAVE_DATA_JSON_FILENAME);
        if (file.exists()) {
            return true;
        }
        return false;
    }
    public static boolean dbDeleteDBFileFromIntStorage() {
        java.io.File dir = g_Context.getFilesDir();
        java.io.File file = new java.io.File(dir, DB_SAVE_DATA_JSON_FILENAME);
        if (file.exists()) {
            file.delete();
            return true;
        }
        return false;
    }
}





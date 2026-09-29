package com.mke.mword.Database;



import static com.mke.mword.Database.Constants.*;
import static com.mke.mword.MainActivity.g_Activity;
import static com.mke.mword.MainActivity.g_Context;
import static com.mke.mword.Utils.Utils.FindNumberofLanguage;
import static com.mke.mword.Utils.Utils.GetDBPageSize;
import static com.mke.mword.Utils.Utils.GetDBPageString;
import static com.mke.mword.Utils.Utils.GetRandom;
import static com.mke.mword.Utils.Utils.ReadSDCard;
import static com.mke.mword.Utils.Utils.SearchFolderFromPath;
import static com.mke.mword.Utils.Utils.copyFolderAssetToLocalFolder;
import static com.mke.mword.Utils.Utils.mixList;


import android.content.Context;
import android.content.res.*;
import android.os.*;

import androidx.annotation.*;

import com.mke.mword.*;
import com.mke.mword.PDF.*;
import com.mke.mword.Utils.*;
//import com.itextpdf.text.DocumentException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.*;
import java.net.*;
import java.text.DecimalFormat;
import java.util.*;

public class mkDB {

    public static double nVersion;
    public static String stAppName;
    public static String stMail;
    public static String stMailPassword;
    public static int nWordsVectorSize;
    public static int nVerbsVectorSize;
    public static int nIdiomsVectorSize;
    public static int nSentencesVectorSize;

    public static String szNativeLanguage;
    public static String szLevelVideos;
    public static int nSleepTime;

    public static String stPdfPassword;

    public static int nWaitTalkTime;
    public static int nWaitExamTime;
    public static int nWaitWriteTime;


    public static int nNatLangSentIncTime;
    public static int nVerbSlpTmIncPrcnt;
    public static int nPageNumber;
    public static int nWordNumber;
    public static boolean bContinueLastWord;
    public static boolean bWordDecreaseSound;
    public static int nWordSoundRepeaTime;
    public static boolean bWordSoundRepeat;
    public static boolean bReadNativeLang;
    public static boolean bEleminateMode;
    public static boolean bShowWVSImage;
    public static int nWordSoundDecreasePercent;
    public static int nWrdSndRepSlepTm;
    public static int nWrdSndRepSlepTmIncPrcnt;

    public static JSONArray wordsPagesAr                    = new JSONArray();
    public static JSONArray verbsPagesAr                    = new JSONArray();
    public static JSONArray sentencesPagesAr                = new JSONArray();
    public static JSONArray idiomsPagesAr                   = new JSONArray();
    public static JSONArray videosPagesAr                   = new JSONArray();

    public static boolean m_bUpdate                         = true;
    Context context                                         = null;


    //    public static CurrencyList currencyList = null;
    public ArrayList<Integer> mvtAlarmList                  = new ArrayList<Integer>();

    public static DecimalFormat myFormatter                 = new DecimalFormat("#.##");
    public static DecimalFormat myFormatters                = new DecimalFormat("#.####");

    public String szExternalSaveFileName="";
    public String szInternalSaveFileName="";

    public mkDB(Context cx) {
        context = cx;
        InitFillAllMember();
    }
    mkDB() {
        context = g_Context;
        InitFillAllMember();
    }

    mkDB(Context cx, InputStream is) {
        InitFillAllMember();
        context = cx;ReadJSON();
    }

    public void InitFillAllMember(){

        nVersion                    = DB_MKDB_INIT_VERSION;
        stAppName                   = DB_MKDB_INIT_APPNAME;
        stMail                      = DB_MKDB_INIT_MAIL;//"murat_muh@yahoo.com";
        stMailPassword              = DB_MKDB_INIT_MAIL_PASSWORD;//"fdSHsaG92aTQ";//VCoSGPalTr5J
        nWordsVectorSize            = DB_MKDB_INIT_PAGE_WORDS_SIZE;
        nVerbsVectorSize            = DB_MKDB_INIT_PAGE_VERBS_SIZE;
        nIdiomsVectorSize           = DB_MKDB_INIT_PAGE_IDIOMS_SIZE;
        nSentencesVectorSize        = DB_MKDB_INIT_PAGE_SENTENCES_SIZE;
        szNativeLanguage            = NATIVE_LANGUAGES[DB_MKDB_INIT_NATIVE_LANGUAGE];
        szLevelVideos               = LEVEL_VIDEOS[0];

        nSleepTime                  = DB_MKDB_INIT_SLEEP;

        stPdfPassword               = DB_MKDB_INIT_PDF_PASSWORD;

        nNatLangSentIncTime         = DB_MKDB_NAT_LANG_SLEEP_INC_TIME;

        nWaitTalkTime               = DB_MKDB_WVS_WAIT_TALK_TIME;
        nWaitExamTime               = DB_MKDB_WVS_WAIT_EXAM_TIME;
        nWaitWriteTime              = DB_MKDB_WVS_WAIT_WRITE_TIME;

        nPageNumber                 = DB_MKDB_INIT_PAGE_NUMBER;
        nWordNumber                 = DB_MKDB_INIT_WORD_NUMBER;
        bContinueLastWord           = DB_MKDB_INIT_CONTINUE_LAST_WORD;
        bWordDecreaseSound          = DB_MKDB_INIT_WORD_DECREASE_SOUND;
        nWordSoundRepeaTime         = DB_MKDB_INIT_WORD_SOUND_REPEAT_TIME;
        bWordSoundRepeat            = DB_MKDB_INIT_WORD_SOUND_REPEAT;
        bReadNativeLang             = DB_MKDB_INIT_READ_NATIVE_LANGUAGE;
        bEleminateMode              = DB_MKDB_INIT_READ_ELEMINATE_MODE;
        bShowWVSImage               = DB_MKDB_INIT_SHOW_WVS_IMAGE;
        nWordSoundDecreasePercent   = DB_MKDB_INIT_WORD_SOUND_DECREASE_PERCENT;
        nWrdSndRepSlepTm            = DB_MKDB_INIT_WORD_SOUND_SLEEP_TIME;
        nWrdSndRepSlepTmIncPrcnt    = DB_MKDB_SOUND_SLEEP_INCREMENT_PERCENT;
        nVerbSlpTmIncPrcnt          = DB_MKDB_VERB_SLEEP_INCREASE_PERCENT;
   }

    public boolean JSON2PDF() {
        if (ReadJSON()) {
            WriteFileInternalStorageJSON();
            PDFGenerator json2pdf = new PDFGenerator(this);
            json2pdf.mkDBTopdf();
        } else {
            return false;
        }
        return true;
    }

    private boolean bCheckVectorIsFill=false;
    public boolean ReadJSON() {
        try {
            String stFile = readFile();
            if (stFile.isEmpty()) {
                TxtToJson(null,false);
                return ReadJSON();
            }
            if (!stFile.isEmpty()) {
                ClearAllVector();
                JSONTokener tokener = new JSONTokener(stFile);
                JSONObject object = null;
                object = new JSONObject(tokener);
                if (object != null) {
                    SetVersion(object);
                    SetAppName(object);
                    SetSetting(object);
                    wordsPagesAr        =FillVectorFromObj(object,DB_WORDS_PAGES);
                    verbsPagesAr        =FillVectorFromObj(object,DB_VERBS_PAGES);
                    sentencesPagesAr    =FillVectorFromObj(object,DB_SENTENCES_PAGES);
                    idiomsPagesAr       =FillVectorFromObj(object,DB_IDIOMS_PAGES);
                    videosPagesAr       =FillVectorFromObj(object,DB_VIDEOS_PAGES);
                    if ((wordsPagesAr.length()==0 || verbsPagesAr.length()==0 ||
                        sentencesPagesAr.length()==0 || idiomsPagesAr.length()==0)&& !bCheckVectorIsFill){
                        TxtToJson(null,false);
                        ReadJSON();
                        bCheckVectorIsFill=true;
                    }

                }
            } else {
                return false;
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
    JSONArray FillVectorFromObj(JSONObject object,String content){
        try {
            return object.getJSONArray(content);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    boolean SetVersion(JSONObject object) {
        try {
            nVersion = object.getDouble(DB_VERSION_NUMBER);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return true;
    }

    boolean SetAppName(JSONObject object) {
        try {
            stAppName = object.getString(DB_APPLICATION_NAME);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return true;
    }

    boolean SetSetting(JSONObject object) {
        try {
            JSONObject setting          = object.getJSONObject(DB_SETTING);
            stMail                      = setting.getString(DB_SITE_MAIL);
            stPdfPassword               = setting.getString(DB_SITE_PDF_PASSWORD);
            stMailPassword              = setting.getString(DB_SITE_MAIL_PASSWORD);
            szNativeLanguage            = setting.getString(DB_SETTING_NATIVE_LANGUAGE_STRING);
            szLevelVideos               = setting.getString(DB_SETTING_LEVEL_VIDEOS_STRING);

            nNatLangSentIncTime         = setting.getInt(DB_NAT_LANG_SENT_INC_TIME_STRING);
            nWaitTalkTime               = setting.getInt(DB_READ_WAIT_TALK_TIME);
            nWaitExamTime               = setting.getInt(DB_READ_WAIT_EXAM_TIME);
            nWaitWriteTime              = setting.getInt(DB_READ_WAIT_WRITE_TIME);
            nSleepTime                  = setting.getInt(DB_SITE_THREAD_SLEEP_TIME);
            nWordsVectorSize            = setting.getInt(DB_WORDS_VECTOR_SIZE_STRING);
            nVerbsVectorSize            = setting.getInt(DB_VERBS_VECTOR_SIZE_STRING);
            nIdiomsVectorSize           = setting.getInt(DB_IDIOMS_VECTOR_SIZE_STRING);
            nSentencesVectorSize        = setting.getInt(DB_SENTENCES_VECTOR_SIZE_STRING);
            nPageNumber                 = setting.getInt(DB_PAGE_NUMBER);
            bContinueLastWord           = setting.getBoolean(DB_CONTINUE_LAST_WORD);
            bWordDecreaseSound          = setting.getBoolean(DB_WORD_DECREASE_SOUND);
            nWordSoundRepeaTime         = setting.getInt(DB_WORD_REPEAT_SOUND_TIME);
            bWordSoundRepeat            = setting.getBoolean(DB_WORD_REPEAT_SOUND);
            nWordSoundDecreasePercent   = setting.getInt(DB_WORD_DECREASE_SPEED_PERCENT);
            nWrdSndRepSlepTm            = setting.getInt(DB_WORD_REPEAT_SLEEP_TIME);
            nWrdSndRepSlepTmIncPrcnt    = setting.getInt(DB_WORD_REPEAT_SLEEP_TIME_INCREASE_PERCENT);
            bReadNativeLang             = setting.getBoolean(DB_READ_NATIVE_LANGUAGE);
            bEleminateMode              = setting.getBoolean(DB_READ_ELAMINATE_MODE);
            bShowWVSImage               = setting.getBoolean(DB_READ_SHOW_WVS_IMAGE);
            nVerbSlpTmIncPrcnt          = setting.getInt(DB_VERB_REPEAT_SLEEP_TIME_INCREASE_PERCENT);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return true;
    }
    private String readFile() {
        String result = "";
        if (context!=null){
            try {
                result = onLoad();
                if (result.isEmpty() && context!=null) {
                    BufferedReader br = new BufferedReader(
                            new InputStreamReader(context.getResources().
                                    getAssets().open("/assets/"+DB_SAVE_DATA_JSON_FILENAME)));
                    StringBuilder sb = new StringBuilder();
                    String line = br.readLine();
                    while (line != null) {
                        sb.append(line);
                        line = br.readLine();
                    }
                    result = sb.toString();
                    br.close();
                }


            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    public void SendMail(){

//        JSON2PDF();
        if (szExternalSaveFileName.isEmpty()){
            WriteFileExternalStorageJSON();
        }
        if (szInternalSaveFileName.isEmpty()){
            WriteFileInternalStorageJSON();
        }

        String[] szFileName= {szExternalSaveFileName,szInternalSaveFileName};
        final SendEmailTask sendEmailTask = new SendEmailTask("Test",szFileName,
                "mk.Egineering@gmail.com",stMail,stMailPassword,"Test Messsage....... from mk-engineering");
        sendEmailTask.execute();

    }

    public JSONObject GetDBJsonObj(){
        JSONObject obj = new JSONObject();
        try {
            obj.put(DB_APPLICATION_NAME, stAppName);
            obj.put(DB_VERSION_NUMBER, nVersion);
            JSONObject set = new JSONObject();
            set.put(DB_SITE_MAIL, stMail);
            set.put(DB_SITE_MAIL_PASSWORD, stMailPassword);
            set.put(DB_SITE_PDF_PASSWORD, stPdfPassword);
            set.put(DB_SETTING_NATIVE_LANGUAGE_STRING, szNativeLanguage);
            set.put(DB_SETTING_LEVEL_VIDEOS_STRING, szLevelVideos);
            set.put(DB_WORDS_VECTOR_SIZE_STRING,nWordsVectorSize);
            set.put(DB_VERBS_VECTOR_SIZE_STRING,nVerbsVectorSize);
            set.put(DB_IDIOMS_VECTOR_SIZE_STRING,nIdiomsVectorSize);
            set.put(DB_SENTENCES_VECTOR_SIZE_STRING,nSentencesVectorSize);
            set.put(DB_SITE_THREAD_SLEEP_TIME, nSleepTime);
            set.put(DB_NAT_LANG_SENT_INC_TIME_STRING, nNatLangSentIncTime);
            set.put(DB_READ_WAIT_TALK_TIME, nWaitTalkTime);
            set.put(DB_READ_WAIT_EXAM_TIME, nWaitExamTime);
            set.put(DB_READ_WAIT_WRITE_TIME, nWaitWriteTime);
            set.put(DB_READ_NATIVE_LANGUAGE, bReadNativeLang);
            set.put(DB_READ_ELAMINATE_MODE,bEleminateMode);
            set.put(DB_READ_SHOW_WVS_IMAGE,bShowWVSImage);
            set.put(DB_PAGE_NUMBER, nPageNumber);
            set.put(DB_CONTINUE_LAST_WORD, bContinueLastWord);
            set.put(DB_WORD_DECREASE_SOUND, bWordDecreaseSound);
            set.put(DB_WORD_REPEAT_SOUND_TIME,nWordSoundRepeaTime);
            set.put(DB_WORD_REPEAT_SOUND, bWordSoundRepeat);
            set.put(DB_WORD_DECREASE_SPEED_PERCENT,nWordSoundDecreasePercent);
            set.put(DB_WORD_REPEAT_SLEEP_TIME, nWrdSndRepSlepTm);
            set.put(DB_WORD_REPEAT_SLEEP_TIME_INCREASE_PERCENT,nWrdSndRepSlepTmIncPrcnt);
            set.put(DB_VERB_REPEAT_SLEEP_TIME_INCREASE_PERCENT,nVerbSlpTmIncPrcnt);

            obj.put(DB_SETTING,set);
            obj.put(DB_WORDS_PAGES,wordsPagesAr);
            obj.put(DB_VERBS_PAGES,verbsPagesAr);
            obj.put(DB_SENTENCES_PAGES,sentencesPagesAr);
            obj.put(DB_IDIOMS_PAGES,idiomsPagesAr);
            obj.put(DB_VIDEOS_PAGES,videosPagesAr);

        } catch (org.json.JSONException e) {
            e.printStackTrace();
        }
        return obj;
    }
    public String GetInternalFileName(){
        return szInternalSaveFileName;
    }
    public String GetExternalFileName(){
        return szExternalSaveFileName;
    }
    public void WriteFileInternalStorageJSON()
    {
        //Write JSON file
        try{
            FileOutputStream outputStream = null;
            java.io.File dir = context.getFilesDir();
            java.io.File file = new java.io.File(dir, DB_SAVE_DATA_JSON_FILENAME);
            if (file.exists()) {
                file.delete();
            }
            szInternalSaveFileName = file.getPath();
            outputStream = context.openFileOutput(DB_SAVE_DATA_JSON_FILENAME, Context.MODE_PRIVATE);
            if (outputStream != null) {
                outputStream.write(GetDBJsonObj().toString().getBytes());

                outputStream.flush();
                outputStream.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean UpdateDB(){
        if (!ReadJSON() || wordsPagesAr.length()==0 ){
            TxtToJson(null,false);
            return false;
        }
        return true;
    }

    private String onLoad() {
        String result = "";
        try {
            if (context!=null) {
                String path=context.getFilesDir().getAbsolutePath()+"/"+DB_SAVE_DATA_JSON_FILENAME;
                java.io.File file = new java.io.File( path );
                if ( !file.exists() ) {
                    TxtToJson(null,false);
                }
                szInternalSaveFileName=file.getPath();
                FileInputStream fis = context.openFileInput(DB_SAVE_DATA_JSON_FILENAME);
                InputStreamReader isr = new InputStreamReader(fis, "UTF-8");
                BufferedReader br = new BufferedReader(isr);
                StringBuilder sb = new StringBuilder();
                String line = br.readLine();
                while (line != null) {
                    sb.append(line);
                    line = br.readLine();
                }
                result = sb.toString();
                br.close();
                return result;
            }
        } catch (FileNotFoundException e) {
            return "";
        } catch (UnsupportedEncodingException e) {
            return "";
        } catch (IOException e) {
            return "";
        }
        return "";
    }

    public JSONObject createVideoObj(String szVideoFileName) throws JSONException {
        JSONObject obj = new JSONObject();
        obj.put(DB_ENG_WVS, szVideoFileName);
        obj.put(DB_OTHERLANGUAGE_WVS, szVideoFileName);
        obj.put(DB_LEARNED_WVS, false);
        obj.put(DB_WVS_VIEW_COUNT, 0);
        obj.put(DB_WVS_SENTENCE, "");
        return obj;
    }

    public void fillVideosAr()  {
        ArrayList<String> files = null;
        String path = Environment.getExternalStorageDirectory().toString()+
                "/Download/mwords_videos/";
        boolean isFolderCreate = SearchFolderFromPath (path);
//        if (isFolderCreate){
////            Log.d("Files", "Path: " + path);
//                files = ReadSDCard(path , ".mp4");
//                if (files != null || files.size()==0){
//                    if (copyFolderAssetToLocalFolder("/Download/mwords_videos/","Learn English.mp4")){
//                        files = ReadSDCard(path, ".mp4");
//                    }
//                }
//        }
        JSONArray videosAr = new JSONArray();
        if (files != null && files.size()>0) {
            for (int i = 0; i < files.size(); i++) {
                try {
                    videosAr.put(createVideoObj(files.get(i)));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
//                Log.d("Files", "FileName:" + files.get(i));
            }
        }else{
            for (int i = 0; i < VIDEO_ID_RESOURCE_INIT_FILES.length; i++) {
                try {
                    videosAr.put(createVideoObj(VIDEO_ID_RESOURCE_INIT_FILES[i]));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        AddInitPageObj(videosPagesAr,videosAr,null,
                DB_VIDEOS,0,0,"0",0);

    }

    public void ClearAllVector(){
        if (wordsPagesAr.length()>0){
            wordsPagesAr = new org.json.JSONArray();
        }
        if (verbsPagesAr.length()>0){
            verbsPagesAr = new org.json.JSONArray();
        }
        if (sentencesPagesAr.length()>0){
            sentencesPagesAr = new org.json.JSONArray();
        }
        if (idiomsPagesAr.length()>0){
            idiomsPagesAr = new org.json.JSONArray();
        }
        if (videosPagesAr.length()>0){
            videosPagesAr = new org.json.JSONArray();
        }
    }

    public void ReadXlSXFile(JSONArray jPageAr,String szObjType,int nResourceId){
//        try {
//            InputStream stream = context.getResources().openRawResource(nResourceId);
//            XSSFWorkbook workbook = new XSSFWorkbook(stream);
//            XSSFSheet sheet = workbook.getSheetAt(0);
//            DataFormatter formatter = new DataFormatter();
//
//            JSONArray jAr = new JSONArray();
//            for (int row=1; row <= sheet.getLastRowNum(); row++) {
//                String szEng = formatter.formatCellValue(sheet.getRow(row).getCell(0));
//
//                JSONObject obj = new JSONObject();
//                int nStep=0;
//
//                if (szEng != null) {
//                    obj.put(DB_ENG_WVS, szEng);
//                } else {continue;}
//
//                if (szObjType.trim().equals("V")) {
//                    nStep=4;
//                    String vForm = formatter.formatCellValue(sheet.getRow(row).getCell(1));
//                    if (vForm != null) {
//                        obj.put(DB_VERB_BAS_FORM, vForm);
//                    }
//                    vForm = formatter.formatCellValue(sheet.getRow(row).getCell(2));
//                    if (vForm != null) {
//                        obj.put(DB_VERB_PAST_FORM, vForm);
//                    }
//                    vForm = formatter.formatCellValue(sheet.getRow(row).getCell(3));
//                    if (vForm != null) {
//                        obj.put(DB_VERB_PAST_PARTICIPLE_FORM, vForm);
//                    }
//                    vForm = formatter.formatCellValue(sheet.getRow(row).getCell(4));
//                    if (vForm != null) {
//                        obj.put(DB_VERB_S_ES_IES_ING_FORM, vForm);
//                    }
//                }
//                int nCol = FindNumberofLanguage(szNativeLanguage) +nStep +1;
//                String szNatLang = formatter.formatCellValue(sheet.getRow(row).getCell(nCol));
//                if (szNatLang != null) {
//                    obj.put(DB_OTHERLANGUAGE_WVS, szNatLang);
//                } else {obj.put(DB_OTHERLANGUAGE_WVS,NATIVE_LANGUAGES[0]);}
//
//                obj.put(DB_LEARNED_WVS, false);
//                obj.put(DB_WVS_VIEW_COUNT, 0);
//                obj.put(DB_WVS_SENTENCE, "");
//
//                jAr.put(obj);
//                if (jAr.length()==GetDBPageSize(szObjType)){
//
//                    AddInitPageObj(jPageAr,jAr,CreateQuestions(jAr),GetDBPageString(szObjType),
//                            0,0,"0",0);
//                    jAr = new JSONArray();
//                }
//            }
//
//            if (jAr.length()!=0){
//                AddInitPageObj(jPageAr,jAr,CreateQuestions(jAr),GetDBPageString(szObjType),
//                        0,0,"0",0);
//            }
//            stream.close();
//        } catch (IOException | JSONException e) {
//            e.printStackTrace();
//        }
    }

    public void FillArFromAssetTxt(JSONArray jPageAr,String szObjType,String szAssetFileName) {
        try {
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(context.getResources().
                            getAssets().open(szAssetFileName)));

            String line=null;
            JSONArray jAr = new JSONArray();
            while ((line = reader.readLine()) != null) {
                if (line.isEmpty()) {
                    continue;
                }
                String[] RowData = line.toString().trim().split("\t");
                if (RowData == null || RowData.length == 1) {
                    continue;
                }
                JSONObject obj = new JSONObject();
                int nStep=0;

                if (RowData.length>1&& RowData[0] != null) {
                    obj.put(DB_ENG_WVS, RowData[0]);
                } else {continue;}

                if (szObjType.trim().equals("V")) {
                    nStep=4;
                    if (RowData[1] != null) {
                        obj.put(DB_VERB_BAS_FORM, RowData[1]);
                    }
                    if (RowData[2] != null) {
                        obj.put(DB_VERB_PAST_FORM, RowData[2]);
                    }
                    if (RowData[3] != null) {
                        obj.put(DB_VERB_PAST_PARTICIPLE_FORM, RowData[3]);
                    }
                    if (RowData[4] != null) {
                        obj.put(DB_VERB_S_ES_IES_ING_FORM, RowData[4]);
                    }
                }

                if (szObjType.trim().equals("I")) {
                    obj.put(DB_OTHERLANGUAGE_WVS, RowData[1]);
                }else{
                    if (RowData.length > (FindNumberofLanguage(szNativeLanguage) + nStep + 1) &&
                            RowData[FindNumberofLanguage(szNativeLanguage) + nStep + 1] != null) {
                        obj.put(DB_OTHERLANGUAGE_WVS, RowData[FindNumberofLanguage(szNativeLanguage) + nStep + 1]);
                    } else {
                        obj.put(DB_OTHERLANGUAGE_WVS, NATIVE_LANGUAGES[DB_MKDB_INIT_NATIVE_LANGUAGE]);
                    }
                }

                obj.put(DB_LEARNED_WVS, false);
                obj.put(DB_WVS_VIEW_COUNT, 0);
                obj.put(DB_WVS_SENTENCE, "");

                jAr.put(obj);
                if (jAr.length()==GetDBPageSize(szObjType)){

                    AddInitPageObj(jPageAr,jAr,CreateQuestions(jAr),GetDBPageString(szObjType),
                            0,0,"0",0);
                    jAr = new JSONArray();
                }
            }
            if (jAr.length()!=0){
                AddInitPageObj(jPageAr,jAr,CreateQuestions(jAr),GetDBPageString(szObjType),
                        0,0,"0",0);
            }
        } catch (java.io.IOException | org.json.JSONException e) {
            e.printStackTrace();
        }
    }
    private JSONArray GetCloneJSONAr (final JSONArray jAr){
        JSONArray jsonAR= new JSONArray();
        for(int n=0;n<jAr.length();n++){
            try {
                jsonAR.put(jAr.get(n));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return jsonAR;
    }
    public JSONArray CreateQuestions(JSONArray objAr){
        if (objAr == null || objAr.length()==0){return null;}

        JSONArray jsonQuestions= new JSONArray();
        JSONArray jsonPage= GetCloneJSONAr(objAr);

        while(jsonPage.length()>3) {
            int nRandomLimit = jsonPage.length();
            int i1 = GetRandom(0,nRandomLimit);

            try {
                if (jsonPage.get(i1)!=null) {
                    JSONObject obj = new JSONObject();
                    ArrayList<String> szList= new ArrayList<String>();

                    obj.put(DB_QUESTIONS_QUESTION,((JSONObject)jsonPage.get(i1)).getString(DB_OTHERLANGUAGE_WVS));
                    obj.put(DB_QUESTIONS_ANSWER,((JSONObject)jsonPage.get(i1)).getString(DB_ENG_WVS));
                    szList.add(((JSONObject)jsonPage.get(i1)).getString(DB_ENG_WVS));
                    jsonPage.remove(i1);

                    nRandomLimit = jsonPage.length() - 1;

                    i1 = GetRandom(0, nRandomLimit);
//                    Log.e("ERROR", "DB_ENG_WVS " + i1 + "  " + nRandomLimit );
                    szList.add(((JSONObject)jsonPage.get(i1)).getString(DB_ENG_WVS));
                    jsonPage.remove(i1);

                    nRandomLimit = jsonPage.length() - 1;
                    i1 = GetRandom(0, nRandomLimit);
//                    Log.e("ERROR", "DB_ENG_WVS " + i1 + "  " + nRandomLimit );
                    szList.add(((JSONObject)jsonPage.get(i1)).getString(DB_ENG_WVS));
                    jsonPage.remove(i1);

                    nRandomLimit = jsonPage.length() - 1;
                    i1 = GetRandom(0, nRandomLimit);
//                    Log.e("ERROR", "DB_ENG_WVS " + i1 + "  " + nRandomLimit );
                    szList.add(((JSONObject)jsonPage.get(i1)).getString(DB_ENG_WVS));
                    jsonPage.remove(i1);


                    ArrayList<String> mList= new ArrayList<String>();
                    mList= mixList(szList);
                    obj.put(DB_QUESTIONS_OPTA, mList.get(0));
                    obj.put(DB_QUESTIONS_OPTB, mList.get(1));
                    obj.put(DB_QUESTIONS_OPTC, mList.get(2));
                    obj.put(DB_QUESTIONS_OPTD, mList.get(3));
                    obj.put(DB_QUESTIONS_RESPOND, "");
                    obj.put(DB_LEARNED_WVS, false);
                    obj.put(DB_WVS_VIEW_COUNT, 0);
                    obj.put(DB_WVS_SENTENCE, "");

                    jsonQuestions.put(obj);
                }else{
                    jsonPage.remove(i1);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return jsonQuestions;
    }
    public boolean TxtToJson(BufferedReader bfObj, boolean bLoadSavedDetails){
        ClearAllVector();
        FillArFromAssetTxt(wordsPagesAr,"W",DB_ASSETS_WORDS_TXT_FILENAME);
        FillArFromAssetTxt(verbsPagesAr,"V",DB_ASSETS_VERBS_TXT_FILENAME);
        FillArFromAssetTxt(sentencesPagesAr,"S",DB_ASSETS_SENTENCES_TXT_FILENAME);
        FillArFromAssetTxt(idiomsPagesAr,"I",DB_ASSETS_IDIOMS_TXT_FILENAME);
        fillVideosAr();
        WriteFileInternalStorageJSON();
//        WriteFileExternalStorageJSON();

        return true;
    }

    public void AddInitPageObj(@NonNull JSONArray pagesAr, JSONArray objAr,JSONArray questionsAr,
                               String szContent, int nVCount, int nLSelect, String listenTime,int nLQNumber) {
        JSONObject page = new JSONObject();
        JSONObject pageprop = new JSONObject();
        try {
            pageprop.put(szContent,objAr);

            pageprop.put(DB_PAGE_QUESTIONS,questionsAr);
            pageprop.put(DB_PAGE_VIEW_COUNT,nVCount);
            pageprop.put(DB_PAGE_VIEW_LAST_QUESTION_NUMBER,nLQNumber);
            pageprop.put(DB_PAGE_LAST_WORD_NUMBER,nLSelect);
            pageprop.put(DB_PAGE_LISTEN_TIME,listenTime);
            page.put(DB_PAGE,pageprop);
            pagesAr.put(page);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void FindWordAndSetData (String szPageType,JSONArray jsnAry,JSONObject objWord){
        if (jsnAry !=null && jsnAry.length()>0) {
            try {
                int nPNumber=-1,nWnumber=-1;
                JSONObject oWordTmp=FindWordNumber(szPageType,jsnAry,objWord,nPNumber,nWnumber);
                if (oWordTmp!= null) {
                    if (nPNumber != -1 && nWnumber != -1) {
                        objWord.put(DB_LEARNED_WVS, oWordTmp.getBoolean(DB_LEARNED_WVS));
                        objWord.put(DB_WVS_VIEW_COUNT, oWordTmp.getInt(DB_WVS_VIEW_COUNT));
                        objWord.put(DB_WVS_SENTENCE, oWordTmp.getString(DB_WVS_SENTENCE));
                    }
                }else{
                    objWord.put(DB_LEARNED_WVS, false);
                    objWord.put(DB_WVS_VIEW_COUNT, 0);
                    objWord.put(DB_WVS_SENTENCE, "");
                }
            } catch (org.json.JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public JSONObject FindWordNumber (String szPageType,JSONArray jsnAry,JSONObject objWord,int nPNumber,int nWnumber) {
        JSONObject retObj=null;
        if (jsnAry !=null && jsnAry.length()>0) {
            if (jsnAry.length()>0){
                try {
                    JSONArray jar = GetPageWVSJArray(szPageType,nPNumber);
                    for (int i = 0; i < jar.length(); i++) {
                        if (((JSONObject)jar.get(i)).getString(DB_ENG_WVS).trim().equals(
                                                    objWord.getString(DB_ENG_WVS)))
                        retObj = new org.json.JSONObject();
                        retObj = ((JSONObject)jar.get(i));
                    }
                } catch (org.json.JSONException e) {
                    e.printStackTrace();
                    return null;
                }
            }else{
                return null;
            }
        }
        return retObj;
    }

    public String writeFileExternalStorage(String szFileName,String szFolderName,String szObj) {
        String szReturnFilename= "";
        //Checking the availability state of the External Storage.
        String state = Environment.getExternalStorageState();
        if (!Environment.MEDIA_MOUNTED.trim().equals(state)) {

            //If it isn't mounted - we can't write into it.
            return szReturnFilename;
        }
        //Create a new file that points to the root directory, with the given name:
        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS),
                        szFileName);

        if (file.exists()){
            file.delete();
        }
        //This point and below is responsible for the write operation
        FileOutputStream outputStream = null;
        try {
            file.createNewFile();
            szReturnFilename= file.getPath();
            //second argument of FileOutputStream constructor indicates whether
            //to append or create new file if one exists
            outputStream = new FileOutputStream(file, false);

            outputStream.write(szObj.getBytes());
            outputStream.flush();
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return szReturnFilename;
    }

    public void WriteFileExternalStorageJSON() {
        szExternalSaveFileName =writeFileExternalStorage(DB_SAVE_DATA_JSON_FILENAME,
                DB_SAVE_DATA_JSON_FILE_FOLDERNAME,GetDBJsonObj().toString());
    }

    public String GetEngWVS (String szPageType,int nPageNo,int nWords){
        try {
            JSONArray jar = GetPageWVSJArray(szPageType,nPageNo);;
            if (jar.length()>0){
                JSONObject obj = (JSONObject)jar.get(nWords);
                return obj.getString(DB_ENG_WVS);
            }

        } catch (org.json.JSONException e) {
            e.printStackTrace();
        }
        return "";
    }
    public String GetEngWVS (JSONArray jar,int nPageNo,int nWords){
        try {
            if (jar.length()>0){
                JSONObject obj = (JSONObject)jar.get(nWords);
                return obj.getString(DB_ENG_WVS);
            }
        } catch (org.json.JSONException e) {
            e.printStackTrace();
        }
        return "";
    }
    public String GetOtherLanguageWVS (String szPageType,int nPageNo,int nWords){
        try {
            JSONArray jar = GetPageWVSJArray(szPageType,nPageNo);
            if (jar.length()>0){
                JSONObject obj = (JSONObject)jar.get(nWords);
                return obj.getString(DB_OTHERLANGUAGE_WVS);
            }

        } catch (org.json.JSONException e) {
            e.printStackTrace();
        }
        return "";
    }
    public String GetOtherLanguageWVS (JSONArray jar,int nPageNo,int nWords){
        try {
            if (jar.length()>0){
                JSONObject obj = (JSONObject)jar.get(nWords);
                return obj.getString(DB_OTHERLANGUAGE_WVS);
            }

        } catch (org.json.JSONException e) {
            e.printStackTrace();
        }
        return "";
    }
    public String GetVerbsString (int nPageNo,int nWords){
        String szVerbs="";
        try {
            JSONArray jar = GetPageWVSJArray(DB_VERBS_PAGES,nPageNo);
            if (jar.length()>0){
                JSONObject obj = (JSONObject)jar.get(nWords);
                szVerbs=  "\n"+ obj.getString(DB_VERB_BAS_FORM)+"\n";
                szVerbs+= obj.getString(DB_VERB_PAST_FORM)+"\n";
                szVerbs+= obj.getString(DB_VERB_PAST_PARTICIPLE_FORM)+"\n";
                szVerbs+= obj.getString(DB_VERB_S_ES_IES_ING_FORM);
            }

        } catch (org.json.JSONException e) {
            e.printStackTrace();
        }
        return szVerbs;
    }


    public boolean CheckThisPageWVSsIsLearn(String szPageType,int nPageNo){
        try {
            JSONArray jar = GetPageWVSJArray(szPageType,nPageNo);;
            if (jar.length()>0){
                for (int n=0;n<jar.length();n++) {
                    JSONObject obj = (JSONObject) jar.get(n);
                    if (!obj.getBoolean(DB_LEARNED_WVS)){
                        return false;
                    }
                }
                return true;
            }
        } catch (org.json.JSONException e) {
            e.printStackTrace();
        }
        return false;
    }

    public int GetPageLastWVSNumber (String szPageType,int nPageNo){
        int nWords=0;
        try {
            nWords = GetPageWVSJObject(szPageType,nPageNo).getInt(DB_PAGE_LAST_WORD_NUMBER);
            if (nWords>=0 && nWords< GetWVSSize(szPageType,nPageNo)) {
                GetPageWVSJObject(szPageType,nPageNo).put(DB_PAGE_LAST_WORD_NUMBER, nWords);
                return nWords;
            }else{
                GetPageWVSJObject(szPageType,nPageNo).put(DB_PAGE_LAST_WORD_NUMBER, 0);
                return nWords;
            }
        } catch (org.json.JSONException e) {
            e.printStackTrace();
            return nWords;
        }
    }

    public boolean SetPageLastWVSNumber (String szPageType,int nPageNo,int nWords){
        try {
            if (nWords>=0 && nWords< GetWVSSize(szPageType,nPageNo)) {
                GetPageWVSJObject(szPageType,nPageNo).put(DB_PAGE_LAST_WORD_NUMBER, nWords);
            }else{
                GetPageWVSJObject(szPageType,nPageNo).put(DB_PAGE_LAST_WORD_NUMBER, 0);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public boolean SetPageMakeLearn (String szPageType,int nPageNo,boolean bLearnedOrNo){
        try {
            JSONArray jar = GetPageWVSJArray(szPageType,nPageNo);
            for (int i = 0; i < jar.length(); i++) {
                ((JSONObject)jar.get(i)).put(DB_LEARNED_WVS,bLearnedOrNo);;
            }
        } catch (org.json.JSONException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public boolean GetWVSIsLearn (String szPageType,int nPageNo,int nWords){
        try {
            JSONArray jar = GetPageWVSJArray(szPageType,nPageNo);
            if (jar.length()>0) {
                return ((JSONObject) jar.get(nWords)).getBoolean(DB_LEARNED_WVS);
            }
        } catch (org.json.JSONException e) {
            e.printStackTrace();
        }
        return false;
    }
    public boolean GetWVSIsLearn (JSONArray jar,int nPageNo,int nWords){
        try {
             if (jar.length()>0) {
                return ((JSONObject) jar.get(nWords)).getBoolean(DB_LEARNED_WVS);
            }
        } catch (org.json.JSONException e) {
            e.printStackTrace();
        }
        return false;
    }
    public int GetWVSWatchCount (String szPageType,int nPageNo,int nWords){
        try {
            JSONArray jar = GetPageWVSJArray(szPageType,nPageNo);
            if (jar.length()>0) {
                return ((JSONObject)jar.get(nWords)).getInt(DB_WVS_VIEW_COUNT);
            }
        } catch (org.json.JSONException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public void SetWVSWatch (String szPageType,int nPageNo,int nWords){
        try {
            JSONArray jar = GetPageWVSJArray(szPageType,nPageNo);
            if (jar.length()>0) {
                int nWatch = GetWVSWatchCount(szPageType,nPageNo,nWords);nWatch++;
                ((JSONObject)jar.get(nWords)).put(DB_WVS_VIEW_COUNT,nWatch);
            }
        } catch (org.json.JSONException e) {
            e.printStackTrace();
        }
    }

    public void SetWVSLearn (String szPageType,int nPageNo,int nWords){
        try {
            ((JSONObject) GetPageWVSJArray(szPageType,nPageNo).get(nWords)).put(DB_LEARNED_WVS,true);
        } catch (org.json.JSONException e) {
            e.printStackTrace();
        }
    }

    public int GetLernWVSSize(String szPageType,int nPageNo){
        int nSize=0;
        try {
            JSONArray jar = GetPageWVSJArray(szPageType,nPageNo);
            for(int n=0;n<jar.length();n++){
                JSONObject jObj= (JSONObject)jar.get(n);
                boolean blearn=jObj.getBoolean(DB_LEARNED_WVS);
                if(blearn){
                    nSize++;
                }
            }
        } catch (org.json.JSONException e) {
            e.printStackTrace();
        }
        return nSize;
    }

    public void SetPageTime (String szPageType,int nPage,String szTime){
        try {
            GetPageWVSJObject(szPageType,nPage).put(DB_PAGE_LISTEN_TIME,szTime);
        } catch (org.json.JSONException e) {
            e.printStackTrace();
        }
    }

    public String GetPageTime (String szPageType,int nPageNo){
        try {
            return GetPageWVSJObject(szPageType,nPageNo).getString(DB_PAGE_LISTEN_TIME);
        } catch (org.json.JSONException e) {
            e.printStackTrace();
        }
        return "0";
    }

    public void SetPageWatchCount (String szPageType,int nPageNo){
        try {
            int nWatch = GetPageWatchCount(szPageType,nPageNo);
            nWatch++;
            GetPageWVSJObject(szPageType,nPageNo).put(DB_PAGE_VIEW_COUNT,nWatch);
        } catch (org.json.JSONException e) {
            e.printStackTrace();
        }
    }

    public int GetPageWatchCount (String szPageType,int nPageNo){
        try {
            return GetPageWVSJObject(szPageType,nPageNo).getInt(DB_PAGE_VIEW_COUNT);
        } catch (org.json.JSONException e) {
            e.printStackTrace();
        }
        return 0;
    }
    public JSONArray GetPageWVSJArray (String szPageType,int nPage){
        if (szPageType.trim().equals(DB_WORDS_PAGES)) {
            try {return GetPageWVSJObject(szPageType,nPage).getJSONArray(DB_WORDS);
            } catch (JSONException e) {e.printStackTrace();return null;}
        }
        else if (szPageType.trim().equals(DB_VERBS_PAGES)) {
            try {return GetPageWVSJObject(szPageType,nPage).getJSONArray(DB_VERBS);
            } catch (JSONException e) {e.printStackTrace();return null;}
        }
        else if (szPageType.trim().equals(DB_SENTENCES_PAGES)) {
            try {return GetPageWVSJObject(szPageType,nPage).getJSONArray(DB_SENTENCES);
            } catch (JSONException e) {e.printStackTrace();return null;}
        }
        else if (szPageType.trim().equals(DB_IDIOMS_PAGES)) {
            try {return GetPageWVSJObject(szPageType,nPage).getJSONArray(DB_IDIOMS);
            } catch (JSONException e) {e.printStackTrace();return null;}
        }
        else if (szPageType.trim().equals(DB_VIDEOS_PAGES)) {
            try {return GetPageWVSJObject(szPageType,nPage).getJSONArray(DB_VIDEOS);
            } catch (JSONException e) {e.printStackTrace();return null;}
        }else{
            return null;
        }
    }

    public JSONObject GetPageWVSJObject (String szPageType,int nPage){
        if (szPageType.trim().equals(DB_WORDS_PAGES)) {
            if (wordsPagesAr.length() > 0) {
                try {return ((JSONObject)wordsPagesAr.get(nPage)).getJSONObject(DB_PAGE);
                } catch (JSONException e) {e.printStackTrace();return null;}
            }
        }
        else if (szPageType.trim().equals(DB_VERBS_PAGES)) {
            if (verbsPagesAr.length() > 0) {
                try {return ((JSONObject)verbsPagesAr.get(nPage)).getJSONObject(DB_PAGE);
                } catch (JSONException e) {e.printStackTrace();return null;}
            }
        }
        else if (szPageType.trim().equals(DB_SENTENCES_PAGES)) {
            if (sentencesPagesAr.length() > 0) {
                try {return ((JSONObject)sentencesPagesAr.get(nPage)).getJSONObject(DB_PAGE);
                } catch (JSONException e) {e.printStackTrace();return null;}
            }
        }
        else if (szPageType.trim().equals(DB_IDIOMS_PAGES)) {
            if (idiomsPagesAr.length() > 0) {
                try {return ((JSONObject)idiomsPagesAr.get(nPage)).getJSONObject(DB_PAGE);
                } catch (JSONException e) {e.printStackTrace();return null;}
            }
        }
        else if (szPageType.trim().equals(DB_VIDEOS_PAGES)) {
            if (videosPagesAr.length() > 0) {
                try {return ((JSONObject)videosPagesAr.get(nPage)).getJSONObject(DB_PAGE);
                } catch (JSONException e) {e.printStackTrace();return null;}
            }
        }else{
            return null;
        }
        return null;
    }

    public int GetPageSize (String szPageType){
        if (szPageType.trim().equals(DB_WORDS_PAGES)) {
            if (wordsPagesAr.length() > 0) {
                return wordsPagesAr.length();
            }
        }
        else if (szPageType.trim().equals(DB_VERBS_PAGES)) {
            if (verbsPagesAr.length() > 0) {
                return verbsPagesAr.length();
            }
        }
        else if (szPageType.trim().equals(DB_SENTENCES_PAGES)) {
            if (sentencesPagesAr.length() > 0) {
                return sentencesPagesAr.length();
            }
        }
        else if (szPageType.trim().equals(DB_IDIOMS_PAGES)) {
            if (idiomsPagesAr.length() > 0) {
                return idiomsPagesAr.length();
            }
        }
        else if (szPageType.trim().equals(DB_VIDEOS_PAGES)) {
            if (videosPagesAr.length() > 0) {
                return videosPagesAr.length();
            }
        }else{
            return 0;
        }
        return 0;
    }

    public int GetWVSSize (String szPageType,int nPageNo){
        return GetPageWVSJArray(szPageType,nPageNo).length();
    }

    public boolean SearchSameWordsInFileAndDelete(){
        try {
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(context.getResources().
                            getAssets().open(DB_SAVE_DATA_JSON_FILENAME)));
            String line=null;
            ArrayList<String> lines= new ArrayList<String>();
            ArrayList<String> linesTmp= new ArrayList<String>();

            int nPosition=0,gPosition=0;
            while (true) {
                if (!((line = reader.readLine()) != null)) break;
                lines.add(line);
                linesTmp.add(line);
            }
            for (int n=0;n<linesTmp.size();n++){
                String[] RowData = linesTmp.get(n).split("\t");
                boolean bFound=false;
                if (RowData!= null) {
                    for (int k = 0; k < lines.size(); k++) {
                        String[] ColumnData = lines.get(k).split("\t");
                        if (ColumnData != null) {
                            if (!RowData[0].isEmpty() && !ColumnData[0].isEmpty()) {
                                if (RowData[0].trim().equals(ColumnData[0])) {
                                    if (bFound) {
                                        lines.remove(k);
                                    }
                                    bFound = true;
                                }
                            }
                        }
                    }
                }
            }
            String szList = null;
            for (int k = 0; k < lines.size(); k++){
                szList +=lines.get(k)+"\n";
            }
            String[] szFileName = {writeFileExternalStorage(DB_SAVE_DATA_JSON_FILENAME,
                                    DB_SAVE_DATA_JSON_FILE_FOLDERNAME,szList)};

            final SendEmailTask sendEmailTask = new SendEmailTask("Test",szFileName,
                            "mk.Egineering@gmail.com",stMail,stMailPassword,
                    "Test SearchSameWordsInFileAndDelete");
            sendEmailTask.execute();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }


    public void FindWVSFromPageAndSetLearn(String szPageType,int nPageNo,String szEngWVS){
        Thread thread = new BASICTHREAD() {
            @Override
            public void run() {
                try {
                    for (int n=0;n<GetPageWVSJArray(szPageType,nPageNo).length();n++){
                        if (((JSONObject) GetPageWVSJArray(szPageType,nPageNo).get(n)).
                                                    getString(DB_ENG_WVS).trim().equals(szEngWVS)){
                            ((JSONObject) GetPageWVSJArray(szPageType,nPageNo).
                                    get(n)).put(DB_LEARNED_WVS,true);
                        }
                    }
                } catch (org.json.JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        thread.start();
    }

    public JSONArray GetPageQuestions (String szPageType,int nPage) {
        try {
            return GetPageWVSJObject(szPageType, nPage).getJSONArray(DB_PAGE_QUESTIONS);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Object getQuestionPageProp(String szCode,String szValueType){
        if (SCREENPAGESTATUS==SCREEN_PAGE_EXAM) {
            try {
                if (szValueType.trim().equals("Int")) {
                    return GetPageWVSJObject(CURRIENTPAGETYPE, nPageNumber).
                            getInt(szCode);
                } else if (szValueType.trim().equals("Boolean")) {
                    return GetPageWVSJObject(CURRIENTPAGETYPE, nPageNumber).
                            getBoolean(szCode);
                } else if (szValueType.trim().equals("String")) {
                    return GetPageWVSJObject(CURRIENTPAGETYPE, nPageNumber).
                            getString(szCode);
                } else if (szValueType.trim().equals("Long")) {
                    return GetPageWVSJObject(CURRIENTPAGETYPE, nPageNumber).
                            getLong(szCode);
                }
            } catch (JSONException e) {
                e.printStackTrace();
                return 0;
            }
        }
        return null;
    }
    public void setQuestionPageTime (String szTm) {
        try {
            GetPageWVSJObject(CURRIENTPAGETYPE, nPageNumber).put(DB_PAGE_LISTEN_TIME,szTm);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void setPageLastQuestionNumber (int nQuestionNo) {
        try {
            GetPageWVSJObject(CURRIENTPAGETYPE, nPageNumber).put(DB_PAGE_VIEW_LAST_QUESTION_NUMBER,nQuestionNo);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public int getPageLastQuestionNumber () {
        int nWords = (int)getQuestionPageProp(DB_PAGE_VIEW_LAST_QUESTION_NUMBER,"Int");
        if (nWords>=0 && nWords< getQuestionSize()) {
            setPageLastQuestionNumber(nWords);
            return nWords;
        }else{
            nWords=0;
            setPageLastQuestionNumber(nWords);
            return nWords;
        }
    }
    public int getQuestionSize(){
        return GetPageQuestions(CURRIENTPAGETYPE,nPageNumber).length();
    }

    public void setQuestionPageWatchCount (){
        try {
            int nWatch = getQuestionPWatch();
            nWatch++;
            GetPageWVSJObject(CURRIENTPAGETYPE, nPageNumber).put(DB_PAGE_VIEW_COUNT,nWatch);
        } catch (org.json.JSONException e) {
            e.printStackTrace();
        }
    }
    public int getQuestionPWatch(){
        return (int)getQuestionPageProp(DB_PAGE_VIEW_COUNT,"Int");
    }
    public String getQuestionTime(){
        return (String)getQuestionPageProp(DB_PAGE_LISTEN_TIME,"String");
    }

    public String getCurrentQustionProp(int nWord,String dataName){
        try {
            return ((JSONObject)GetPageQuestions(CURRIENTPAGETYPE,nPageNumber).
                    get(nWord)).getString(dataName);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return "";
    }

    public void setQuestionLearn(int nWord){
        try {
            ((JSONObject)GetPageQuestions(CURRIENTPAGETYPE,nPageNumber).
                    get(nWord)).put(DB_LEARNED_WVS,true);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public void setQuestionRespond(int nWord,String szRespond){
        try {
            ((JSONObject)GetPageQuestions(CURRIENTPAGETYPE,nPageNumber).
                    get(nWord)).put(DB_QUESTIONS_RESPOND,szRespond);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public String getQuestion (int nQuestionNo){
        return getCurrentQustionProp(nQuestionNo,DB_QUESTIONS_QUESTION);
    }
    public String getAnswer (int nQuestionNo){
        return getCurrentQustionProp(nQuestionNo,DB_QUESTIONS_ANSWER);
    }
    public String getOPTA (int nQuestionNo){
        return getCurrentQustionProp(nQuestionNo,DB_QUESTIONS_OPTA);
    }
    public String getOPTB (int nQuestionNo){
        return getCurrentQustionProp(nQuestionNo,DB_QUESTIONS_OPTB);
    }
    public String getOPTC (int nQuestionNo){
        return getCurrentQustionProp(nQuestionNo,DB_QUESTIONS_OPTC);
    }
    public String getOPTD (int nQuestionNo){
        return getCurrentQustionProp(nQuestionNo,DB_QUESTIONS_OPTD);
    }
    public String getRespond (int nQuestionNo){
        return getCurrentQustionProp(nQuestionNo,DB_QUESTIONS_RESPOND);
    }
    public int getQuestionWWatch (int nQuestionNo){
        try {
            return ((JSONObject)GetPageQuestions(CURRIENTPAGETYPE,nPageNumber).
                    get(nQuestionNo)).getInt(DB_WVS_VIEW_COUNT);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public boolean getQuestionLearn (int nQuestionNo){
        try {
            return ((JSONObject)GetPageQuestions(CURRIENTPAGETYPE,nPageNumber).
                    get(nQuestionNo)).getBoolean(DB_LEARNED_WVS);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return false;
    }
    public boolean CheckThisQPageIsLearn(String szPageType,int nPageNo){
        try {
            JSONArray jar = GetPageQuestions(szPageType,nPageNo);
            if (jar.length()>0){
                for (int n=0;n<jar.length();n++) {
                    JSONObject obj = (JSONObject) jar.get(n);
                    if (!obj.getBoolean(DB_LEARNED_WVS)){
                        return false;
                    }
                }
                return true;
            }
        } catch (org.json.JSONException e) {
            e.printStackTrace();
        }
        return false;
    }
    public boolean SetQPageMakeLearn (String szPageType,int nPageNo,boolean bLearnedOrNo){
        try {
            JSONArray jar = GetPageQuestions(szPageType,nPageNo);
            for (int i = 0; i < jar.length(); i++) {
                ((JSONObject)jar.get(i)).put(DB_LEARNED_WVS,bLearnedOrNo);;
            }
        } catch (org.json.JSONException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
    public int getQuestionLernSize(String szPageType,int nPageNo){
        int nSize=0;
        try {
            JSONArray jar = GetPageQuestions(szPageType,nPageNo);
            for(int n=0;n<jar.length();n++){
                JSONObject jObj= (JSONObject)jar.get(n);
                boolean blearn=jObj.getBoolean(DB_LEARNED_WVS);
                if(blearn){
                    nSize++;
                }
            }
        } catch (org.json.JSONException e) {
            e.printStackTrace();
        }
        return nSize;
    }
}


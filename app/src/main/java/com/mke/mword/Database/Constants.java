package com.mke.mword.Database;


import android.graphics.Color;

import java.util.Locale;

/**
 * Created by MK-E on 25/11/18.
 */

public class Constants {


    public static String DB_WORDS_VECTOR_SIZE_STRING                = "WordsSize";
    public static String DB_VERBS_VECTOR_SIZE_STRING                = "VerbsSize";
    public static String DB_IDIOMS_VECTOR_SIZE_STRING               = "IdiomsSize";
    public static String DB_SENTENCES_VECTOR_SIZE_STRING            = "SentencesSize";

    public static String DB_BUY_SELL_LIST_DATE                      = "Date";
    public static String DB_BUY_SELL_LIST_ALARMONOFF                = "AlarmOn/Off";
    public static String DB_BUY_SELL_LIST_ALARMTYPE                 = "AlrmType";
    public static String DB_BUY_SELL_LIST_REPEATTIME                = "RepeatTime";

    public static String DB_SITE_QUERY_TIME                         = "QUERY TIME";
    public static String DB_SITE_QUERY_TIME_NO_WIFI                 = "QUERY TIME NO WIFI";
    public static String DB_SITE_PDF_PASSWORD                       = "PDF PASSWORD";
    public static String DB_SITE_MAIL                               = "MAIL";
    public static String DB_SITE_MAIL_PASSWORD                      = "MAIL PASSWORD";
    public static String DB_SITE_MAIL_SUBJECT                       = "mkRates Operation Results!";
    public static String DB_SITE_THREAD_SLEEP_TIME                  = "THREAD SLEEP TIME";
    public static String DB_VERSION_NUMBER                          = "Version";
    public static String DB_APPLICATION_NAME                        = "App Name";
    public static String DB_SETTING_NATIVE_LANGUAGE_STRING          = "Native Language";

    public static String DB_SETTING_LEVEL_VIDEOS_STRING             = "Level Videos";
    public static String DB_NAT_LANG_SENT_INC_TIME_STRING           = "Native Language Wait Sleep Inc";
    public static String DB_READ_WAIT_TALK_TIME                     = "Read wait talk time";
    public static String DB_READ_WAIT_EXAM_TIME                     = "Read wait exam time";
    public static String DB_READ_WAIT_WRITE_TIME                    = "Read wait write time";


    public static String DB_READ_NATIVE_LANGUAGE                    = "Read Native Language";
    public static String DB_PAGE_NUMBER                             = "Last Saved page number";
    public static String DB_CONTINUE_LAST_WORD                      = "Continue Last Word";
    public static String DB_WORD_REPEAT_SOUND_TIME                  = "Word Repeat Sound Time";
    public static String DB_WORD_REPEAT_SOUND                       = "Word Repeat Sound";
    public static String DB_WORD_DECREASE_SOUND                     = "Word Decrease Sound";
    public static String DB_WORD_DECREASE_SPEED_PERCENT             = "Word Decrease Speedercent";
    public static String DB_WORD_REPEAT_SLEEP_TIME                  = "Word Repeat Sleep Time";
    public static String DB_WORD_REPEAT_SLEEP_TIME_INCREASE_PERCENT = "Word Repeat Sleep Time Increase Percent";
    public static String DB_VERB_REPEAT_SLEEP_TIME_INCREASE_PERCENT = "Verb Repeat Sleep Time Increase Percent";
    public static String DB_READ_ELAMINATE_MODE                     = "Read Eleminate Mode";
    public static String DB_READ_SHOW_WVS_IMAGE                     = "Show WVS Image";
    public static String DB_SETTING                                 = "Setting";
    public static String DB_SAVE_DATA_JSON_FILENAME                 = "mwords.JSON";
    public static String DB_SAVE_DATA_JSON_FILE_FOLDERNAME          = "/download";

    public static String DB_ENG_WVS                                 = "English WVS";
    public static String DB_OTHERLANGUAGE_WVS                       = "OtherLanguage WVS";

    public static String DB_QUESTIONS_QUESTION                      = "Question";
    public static String DB_QUESTIONS_ANSWER                        = "Answer";
    public static String DB_QUESTIONS_OPTA                          = "A";
    public static String DB_QUESTIONS_OPTB                          = "B";
    public static String DB_QUESTIONS_OPTC                          = "C";
    public static String DB_QUESTIONS_OPTD                          = "D";
    public static String DB_QUESTIONS_RESPOND                       = "Respond";



    public static String DB_PAGE                                    = "PAGE";
    public static String DB_PAGE_QUESTIONS                          = "Page Questions";
    public static String DB_PAGE_VIEW_COUNT                         = "Page View Count";
    public static String DB_PAGE_VIEW_LAST_QUESTION_NUMBER          = "Page View Last Question Number";
    public static String DB_PAGE_LAST_WORD_NUMBER                   = "Last Saved word number";
    public static String DB_PAGE_LISTEN_TIME                        = "Page Listen Time";

    public static String DB_WORDS_PAGES                             = "WORDS PAGES";
    public static String DB_VERBS_PAGES                             = "VERBS PAGES";
    public static String DB_SENTENCES_PAGES                         = "SENTENCES PAGES";
    public static String DB_IDIOMS_PAGES                            = "IDIOMS PAGES";
    public static String DB_VIDEOS_PAGES                            = "VIDEOS PAGES";

    public static String DB_WORDS                                   = "WORDS";
    public static String DB_VERBS                                   = "VERBS";
    public static String DB_SENTENCES                               = "SENTENCES";
    public static String DB_IDIOMS                                  = "IDIOMS";
    public static String DB_VIDEOS                                  = "VIDEOS";

    public static String DB_VERB_BAS_FORM                           = "Verb Base Form";
    public static String DB_VERB_PAST_FORM                          = "Verb Past Form";
    public static String DB_VERB_PAST_PARTICIPLE_FORM               = "Verb Past Participle Form";
    public static String DB_VERB_S_ES_IES_ING_FORM                  = "Verb s / es/ ies form";

    public static String DB_WVS_VIEW_COUNT                          = "WVS View Count";
    public static String DB_WVS_SENTENCE                            = "WVS Sentence";
    public static String DB_LEARNED_WVS                             = "Learned WVS";

    public static final int TABLAYOUT_PAGE_SIZE                     = 5;

    public static double DB_MKDB_INIT_VERSION                       = 1.01;
    public static String DB_MKDB_INIT_APPNAME                       = "mwords";
    public static String DB_MKDB_INIT_MAIL                          = "ttest@mail.com";//"1muratkaplan1@gmail.com";
    public static String DB_MKDB_INIT_MAIL_PASSWORD                 = "Tr345678";//"200976Mk";
    public static int DB_MKDB_INIT_PAGE_WORDS_SIZE                  = 571;
    public static int DB_MKDB_INIT_PAGE_VERBS_SIZE                  = 199;
    public static int DB_MKDB_INIT_PAGE_IDIOMS_SIZE                 = 199;
   public static int DB_MKDB_INIT_PAGE_SENTENCES_SIZE               = 199;

    public static int DB_MKDB_INIT_SLEEP                            = 5000;
    public static int DB_MKDB_INIT_QUERY_TIME                       = 1000;

    public static int DB_MKDB_INIT_QUERY_NOWIFI                     = 2000;
    public static String DB_MKDB_INIT_PDF_PASSWORD                  = "123";

    public static int DB_MKDB_NAT_LANG_SLEEP_INC_TIME               = 20;
    public static int DB_MKDB_WVS_WAIT_TALK_TIME                    = 10;
    public static int DB_MKDB_WVS_WAIT_EXAM_TIME                    = 10;
    public static int DB_MKDB_WVS_WAIT_WRITE_TIME                   = 10;
    public static int DB_MKDB_INIT_PAGE_NUMBER                      = 0;
    public static int DB_MKDB_INIT_WORD_NUMBER                      = 0;
    public static boolean DB_MKDB_INIT_CONTINUE_LAST_WORD           =true;
    public static boolean DB_MKDB_INIT_WORD_DECREASE_SOUND          = true;
    public static int DB_MKDB_INIT_WORD_SOUND_REPEAT_TIME           = 2;
    public static boolean DB_MKDB_INIT_WORD_SOUND_REPEAT            = true;
    public static boolean DB_MKDB_INIT_READ_NATIVE_LANGUAGE         = true;
    public static boolean DB_MKDB_INIT_READ_ELEMINATE_MODE          = false;
    public static boolean DB_MKDB_INIT_SHOW_WVS_IMAGE               = true;

    
    public static int DB_MKDB_INIT_WORD_SOUND_DECREASE_PERCENT      = 30;
    public static int DB_MKDB_INIT_WORD_SOUND_SLEEP_TIME            = 1000;
    public static int DB_MKDB_SOUND_SLEEP_INCREMENT_PERCENT         = 20;
    public static int DB_MKDB_VERB_SLEEP_INCREASE_PERCENT           = 50;
    public static int DB_MKDB_INIT_NATIVE_LANGUAGE                  = 56;


    public static String DB_ASSETS_WVS_TXT_FILENAME                 = "english_turkish_words.txt";

    public static String DB_ASSETS_WORDS_TXT_FILENAME               = "english_words_db.txt";
    public static String DB_ASSETS_VERBS_TXT_FILENAME               = "english_verbs_db.txt";
    public static String DB_ASSETS_SENTENCES_TXT_FILENAME           = "english_sentences_db.txt";
    public static String DB_ASSETS_IDIOMS_TXT_FILENAME              = "english_idioms_db.txt";

    public static boolean MONEY_STRUCTURE_THREAD_STATE              = true;

    public static String SAVE_DATA_CSV_FILENAME_BLANK               = "\"\";\"\";\"\";\"\";\"\";0.0;0.0;0.0;0.0;";
    public static String SAVE_DATA_CSV_FILENAME                     = "mwords_123123.csv";
    public static String SAVE_DATA_CSV_FILE_SPLIT_CHAR              = ";";
    public static String SELECT_INIT_FILE_FROM_PHONE                = "SELECT INIT FILE FROM PHONE";

    public static int MAINWINDOW_RED_COLOR                          = Color.rgb(239, 119,113);
    public static int MAINWINDOW_GREEN_COLOR                        = Color.rgb(141, 154, 91);

    public static String SCREEN_PAGE_LISTEN                         ="LISTEN";
    public static String SCREEN_PAGE_TALK                           ="TALK";
    public static String SCREEN_PAGE_WRITE                          ="WRITE";
    public static String SCREEN_PAGE_EXAM                           ="EXAM";
    public static String SCREENPAGESTATUS                           =SCREEN_PAGE_LISTEN;

    public static String CURRIENTPAGETYPE                           =DB_WORDS_PAGES;

    public static float TVSCREEN_ENGWORD_TEXTHEIGHT_COEFF           =1.9f;
    public static int   TVSCREEN_WORD_PAGE_MAXLINES                 =2;
    public static int   TVSCREEN_VERB_PAGE_MAXLINES                 =6;
    public static int   TVSCREEN_SENTENCES_PAGE_MAXLINES            =3;
    public static int   TVSCREEN_IDIOMS_PAGE_MAXLINES               =3;
    public static int   TVSCREEN_MAXLINES                           =TVSCREEN_WORD_PAGE_MAXLINES;

    public static int MAINWINDOW_LISTVIEW_ITEM_BUTTON_HEIGHT        = 20;
    public static int MAINWINDOW_LISTVIEW_ITEM_BUTTON_WIDTH         = 30;

    public static String SCREEN_ACTIVITY_DO_YOU_WANT_TO_LISTEN = "Do you want to listen?";
    public static String GOLD = "Do you want to listen more?";
    public static String MAIN_ACTIVITY_PLAY_BUTTON = "PLAY";
    public static String MAIN_ACTIVITY_PAGES_BUTTON = "PAGES";
    public static String MAIN_ACTIVITY_SETTINGS_BUTTON = "SETTGINS";

    public static String MAIN_ACTIVITY_STOP_BUTTON                  = "STOP";
    public static String MAIN_ACTIVITY_REMOVE_BUTTON                = "REMOVE";
    public static String MAIN_ACTIVITY_RE_START_BUTTON              = "RE-START";
    public static String MAIN_ACTIVITY_BACK_WORDS_PAGE              = "BACK WORDS PAGE";
    public static String MAIN_ACTIVITY_START_SENTENCE               ="...Hi you doing Murat....";

    public static final String SITE_FTP_SERVER                      = "www.mk-e.com.tr";
    public static final String SITE_FTP_USER                        = "mkec8085";
    public static final String SITE_FTP_PASSWORD                    = "Mke0112#";
    public static final String SAVE_FILE_NAME                       = "Data123.txt";
    public static final int SITE_FTP_PORT                           = 21;

    public static final int PAGE_BUTTON_LEFT_SPACE_FROM_LAYOUT = 1;
    public static final int PAGE_BUTTON_RIGHT_SPACE_FROM_LAYOUT = 1;
    public static final int PAGE_BUTTON_TOP_SPACE_FROM_LAYOUT = 15;
    public static final int PAGE_BUTTON_BOTTOM_SPACE_FROM_LAYOUT = 15;
    public static final int REQUEST_CODE_CSV= 123;
    /*
     * A user-agent string that's sent to the HTTP site. It includes information about the device
     * and the build that the device is running.
     */
    public static final String USER_AGENT                   = "Mozilla/5.0 (Linux; U; Android "
            + android.os.Build.VERSION.RELEASE + ";"
            + Locale.getDefault().toString() + "; " + android.os.Build.DEVICE
            + "/" + android.os.Build.ID + ")";

    public static long SITE_THREAD_SLEEP_TIME = 1000;

    public static final int MESSAGE_SCREENVIEW_PAGE_END                 = 1000;
    public static final int MESSAGE_SCREENVIEW_SHOW_SCREEN              = 1001;
    public static final int MESSAGE_SCREENVIEW_SETWORD_LEARNED          = 1002;
    public static final int MESSAGE_SCREENVIEW_PAGE_START               = 1003;
    public static final int MESSAGE_SCREENVIEW_UPDATE_MKDB              = 1004;
    public static final int MESSAGE_SCREENVIEW_VISIBLE_REMOVE_BUTTON    = 1005;
    public static final int MESSAGE_SCREENVIEW_PLAY_BUTTON_CLICK        = 1006;
    public static final int MESSAGE_SCREENVIEW_PAUSE_BUTTON_CLICK       = 1007;
    public static final int MESSAGE_SCREENVIEW_VISIBLE_PREV_BUTTON      = 1008;
    public static final int MESSAGE_SCREENVIEW_NEXT_WORD_BUTTON         = 1009;
    public static final int MESSAGE_SCREENVIEW_PREV_WORD_BUTTON         = 1010;
    public static final int MESSAGE_SCREENVIEW_FOCUS_WRITE_SCREEN       = 1011;
    public static final int MESSAGE_SCREENVIEW_CHANGE_COLOR_SCREEN      = 1012;
    public static final int MESSAGE_WRITESCREENVIEW_LEARN_WORD          = 1013;
    public static final int MESSAGE_UPDATE_CURRENT_FRAGMENT_LISTVIEW    = 1014;
    public static final int MESSAGE_SCREENVIEW_UNVISIBLE_PREV_BUTTON    = 1015;
    public static final int MESSAGE_SCREENVIEW_VISIBLE_NEXT_BUTTON      = 1016;
    public static final int MESSAGE_SCREENVIEW_UNVISIBLE_NEXT_BUTTON    = 1017;
    public static final int MESSAGE_EXAM_VIEW_CLEAR_QUESTION_DETAIL     = 1018;
    public static final int MESSAGE_EXAM_VIEW_FILL_QUESTION_DETAIL      = 1019;
    public static final int MESSAGE_EXAM_VIEW_SET_LAST_QUESTION         = 1020;
    public static final int MESSAGE_EXAM_VIEW_WRITE_SCREEN_TRUE_ANSWER  = 1021;
    public static final int MESSAGE_EXAM_VIEW_SET_ANSWER                = 1022;
    public static final int MESSAGE_SCREENVIEW_PAGE_LOAD_IMAGE          =1023;


    public static final int MESSAGE_LEARNED_WORD           = 0;
    public static final int MESSAGE_STOP_SERVICE         = 1;
    public static final int MESSAGE_LEARN_BUTTON_VISIBLE            = 2;
    public static final int MESSAGE_LEARN_BUTTON_INVISIBLE            = 221;
    public static final int MESSAGE_PLAY_BUTTON_CLICK             = 3;
    public static final int MESSAGE_NEW_PAGE_WORDS         = 4;
    public static final int MESSAGE_STOP_BUTTON_CLICK           = 5;
    public static final int MESSAGE_WRITE_TEXT_SCREEN        = 6;
    public static final int MESSAGE_INCREASE_PAGE_NUMBER       = 7;
    public static final int MESSAGE_SPEAK_WORD        = 8;
    public static final int MESSAGE_ALL_SCREEN_MAKE_INVISIBLE  = 9;
    public static final int MESSAGE_ALL_SCREEN_MAKE_VISIBLE = 10;
    public static final int MESSAGE_ALL_BUTTON_MAKE_VISIBLE= 11;
    public static final int MESSAGE_ALL_BUTTON_MAKE_INVISIBLE            = 12;
    public static final int MESSAGE_SHOW_TEXT_TO_TV           = 13;
    public static final int MESSAGE_END_OF_PAGE    = 14;
    public static final int MESSAGE_RE_START_LIST_LISTEN          = 15;
    public static final int MESSAGE_PLAY_BUTTON_MAKE_VISIBLE     = 16;
    public static final int MESSAGE_PLAY_BUTTON_MAKE_INVISIBLE     = 17;
    public static final int MESSAGE_NEW_PAGES_CREATE_BUTTONS= 18;
    public static final int MESSAGE_NEW_PAGE_BUTTONS_MAKE_INVISIBLE = 19;
    public static final int MESSAGE_NEW_PAGE_BUTTONS_MAKE_VISIBLE = 191;
    public static final int MESSAGE_SCREEN_TEXTVIEW_MAKE_VISIBLE = 20;
    public static final int MESSAGE_LISTEN_SAME_PAGE = 21;
    public static final int MESSAGE_MAIN_BUTTON_VISIBLE = 22;
    public static final int MESSAGE_MAIN_BUTTON_INVISIBLE = 23;
    public static final int MESSAGE_INFO_SCREEN_VISIBLE = 24;
    public static final int MESSAGE_INFO_SCREEN_INVISIBLE = 25;
    public static final int MESSAGE_PAGES_WINDOW= 26;
    public static final int MESSAGE_SAVE_DOCUMENT= 27;
    public static final int MESSAGE_RESET_SETTINGS_WINDOW= 28;
    public static final int MESSAGE_SETTING_WINDOW_LOAD_FILE= 29;
    public static final int MESSAGE_SETTING_DIALOG_OPEN_WINDOW= 30;
    public static final int MESSAGE_SETTING_UPDATE_WINDOW= 33;
    public static final int MESSAGE_START_SPEECH_SERVICE= 34;
    public static final int MESSAGE_STOP_SPEECH_SERVICE= 35;
    public static final int MESSAGE_UPDATE_ALL_FRAGMENT_LISTVIEW= 36;
    public static final int MESSAGE_THROW_EXCEPTION= 37;

    public static final float MAIN_WINDOW_PAGE_BUTTON_MAX_TEXT_HEIGHT =20.f;

    //Country Codes Dialog Kutusunu Main window çağırıyor.
    public static final int COUNTRY_CODES_MAIN_WINDOW_FROM_COUNTRY = 0;
    //Country Codes Dialog Kutusunu Setting window çağırıyor.
    public static final int COUNTRY_CODES_MAIN_WINDOW_TO_COUNTRY = 1; //

    public static final String DO_YOU_WANT_TO_CHANGE_SOURCE = "DO YOU WANT TO CHANGE SOURCE?";
    public static final String DO_YOU_WANT_TO_SEND_MAIL = "DO YOU WANT TO SEND EMAIL?";
    public static final String DO_YOU_WANT_TO_APPLY = "DO YOU WANT TO APPLY? (You can lose page history)";
    public static final String DO_YOU_WANT_TO_RESET_THIS_WINDOW = "DO YOU WANT TO RESET THIS WINDOW?";
    public static final String DO_YOU_WANT_TO_LISTEN_THIS_PAGE_ONEMORE = "DO YOU WANT TO LISTEN THIS PAGE ONEMORE?";
    public static final String SEND_MESSAGE_TO_MAIN_WINDOWS= "SEND MESSAGE TO MAIN WINDOWS";
    public static final String COUNTRY_CODES_LISTVIEW_CAPTION_TEXT = "Country Codes";
    public static final String CURRRENCY_LISTVIEW_CAPTION_TEXT = "Currency Sites";

    public static final String THIS_EXAM_IS_OVER = "THIS EXAM IS COMPLETED";
    public static final String DO_YOU_WANT_TO_START_EXAM = "DO YOU WANT TO START EXAM?";
    public static final String DO_YOU_WANT_TO_CONTINUE_THIS_EXAM = "DO YOU WANT TO CONTINUE THIS EXAM?";
    public static final String DO_YOU_WANT_TO_START_WRITE_EXAM = "DO YOU WANT TO START WRITE EXAM?";
    public static final String DO_YOU_WANT_TO_CONTINUE_WRITE_EXAM = "DO YOU WANT TO CONTINUE WRITE EXAM?";


    public static final int CURRRENCY_LISTVIEW_CAPTION_TEXT_COLOR = Color.BLACK;
    public static final int CURRRENCY_LISTVIEW_CAPTION_TEXT_HEIGHT = 30;
    public static final int CURRRENCY_LISTVIEW_CAPTION_WINDOW_HEIGHT = 150;
    public static final int CURRRENCY_LISTVIEW_CAPTION_BACKGROUND_COLOR = Color.GRAY;
    public static final int CURRRENCY_LISTVIEW_LIST_TEXT_COLOR = Color.GRAY;
    public static final int CURRRENCY_LISTVIEW_LIST_TEXT_SPLITLINE_COLOR = Color.GRAY;
    public static final int CURRRENCY_LISTVIEW_LIST_TEXT_SPLITLINE_HEIGHT = 2;
    public static final int CURRRENCY_LISTVIEW_LIST_SELECTION_COLOR = Color.BLUE;
    public static final int CURRRENCY_LISTVIEW_BACKGROUND_COLOR = Color.BLACK;
    public static final int CURRRENCY_LISTVIEW_TEXT_HEIGHT = 25;

    public static final int IC_SETTING_WINDOW_MODE_NONE = 0;
    public static final int IC_SETTING_WINDOW_MODE_LOAD_FILE = 1;

    public static final int PAGE_BUTTON_START_NUMBER =1231;

    public static final int COLORS_GOLD_COLOR =Color.rgb(255,167,38);

    public static final String[] VIDEO_RESOURCE_INIT_FILES = {
            "https://youtu.be/vd0yESrQMs0",
            "https://youtu.be/2_pZWdF7ujA",
            "https://youtu.be/8rgJRzz_zHo",
            "https://www.youtube.com/watch?v=juKd26qkNAw&t=12s&ab_channel=LearnEnglishwithEnglishClass101.com"//,
//            "https://www.youtube.com/watch?v=NNamZZsggM4&t=17s&ab_channel=LearnEnglishwithEnglishClass101.com",
//            "https://www.youtube.com/watch?v=kCMYfcjqlvI&t=254s&ab_channel=LearnEnglishwithEnglishClass101.com",
//            "https://www.youtube.com/watch?v=XzjQV5oRtOQ&t=95s&ab_channel=LearnEnglishwithEnglishClass101.com",
//            "https://www.youtube.com/watch?v=6MVxOPEu6HA&t=1s&ab_channel=LearnEnglishwithEnglishClass101.com",
//            "https://www.youtube.com/watch?v=HV6h7MRrRNA&ab_channel=LearnEnglishwithEnglishClass101.com",
//            "https://www.youtube.com/watch?v=QTJ02h7uiXs&t=10447s&ab_channel=LearnEnglishwithEnglishClass101.com",
//            "https://www.youtube.com/watch?v=G5dViczwTXo&ab_channel=LearnEnglishwithEnglishClass101.com",
//            "https://www.youtube.com/watch?v=bEB8-SWMYhI&t=1s&ab_channel=LearnEnglishwithEnglishClass101.com",
//            "https://www.youtube.com/watch?v=RiGvfKmpsCI&ab_channel=LearnEnglishwithEnglishClass101.com"
            };



    public static final String[] VIDEO_ID_RESOURCE_INIT_FILES = {
            "vd0yESrQMs0","2_pZWdF7ujA","juKd26qkNAw&t","8rgJRzz_zHo",
            "NNamZZsggM4&t","kCMYfcjqlvI&t","XzjQV5oRtOQ&t","6MVxOPEu6HA&t"};

    public static final String[] NATIVE_LANGUAGES = {
        "Afrikaans af", "Albanian sq",  "Arabic ar",    "Armenian hy",   "Azerbaijani az",      //1
        "Basque eu",    "Belarusian be","Bengali bn",   "Bulgarian bg",  "Catalan ca",          //2
        "Chinese zh-CN","Croatian hr",  "Czech cs",     "Danish da",     "Dutch nl",            //3
        "Esperanto eo", "Estonian et",  "Filipino tl",  "Finnish fi",    "French fr",           //4
        "Galician gl",  "German de",    "Georgian ka",  "Greek el",      "Haitian-Creole ht",   //5
        "Hebrew iw",    "Hindi hi",     "Hungarian hu", "Icelandic is",  "Indonesian id",       //6
        "Irish ga",     "Italian it",   "Japanese ja",  "Korean ko",     "Lao lo",              //7
        "Latin la",     "Latvian lv",   "Lithuanian lt","Macedonian mk", "Malay ms",            //8
        "Maltese mt",   "Norwegian no", "Persian fa",   "Polish pl",     "Portuguese pt",       //9
        "Romanian ro",  "Russian ru",   "Serbian sr",   "Slovak sk",     "Slovenian sl",        //10
        "Spanish es",   "Swahili sw",   "Swedish sv",   "Tamil ta",      "Telugu te",           //11
        "Thai th",      "Turkish tr",   "Ukrainian uk", "Urdu ur",       "Vietnamese vi",       //12
        "Welsh cy",     "Yiddish yi"};

    public static final String[] LEVEL_VIDEOS = {"Sentences","Grammar",	"Words","Sentences","Conversation"};

    public static String ABOUT_US_HTML              ="file:///android_asset/about_us.htm";
    public static String TUTORIOL_HTML              ="file:///android_asset/tutoriol.htm";
    public static String FAQS_HTML                  ="file:///android_asset/faqs.htm";
    public static String CONTACT_SUPPORT_HTML       ="file:///android_asset/contact_support.html";
    public static String TERMS_OF_SERVICE_HTML      ="file:///android_asset/terms_of_service.html";
    public static String PRIVACY_POLICY_HTML        ="file:///android_asset/privacy_policy.html";
    public static String UPDATE_PRO_VERSION_HTML    ="file:///android_asset/upgrade_to_pro.htm";

    public static String HELP_CENTER_WEB_VIEW_CURRENT_FILE=ABOUT_US_HTML;

}
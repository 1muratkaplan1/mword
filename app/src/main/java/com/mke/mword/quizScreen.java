package com.mke.mword;

import static com.mke.mword.Database.Constants.CURRIENTPAGETYPE;
import static com.mke.mword.Database.Constants.DO_YOU_WANT_TO_CONTINUE_THIS_EXAM;
import static com.mke.mword.Database.Constants.DO_YOU_WANT_TO_START_EXAM;
import static com.mke.mword.Database.Constants.MESSAGE_EXAM_VIEW_CLEAR_QUESTION_DETAIL;
import static com.mke.mword.Database.Constants.MESSAGE_EXAM_VIEW_FILL_QUESTION_DETAIL;
import static com.mke.mword.Database.Constants.MESSAGE_EXAM_VIEW_SET_ANSWER;
import static com.mke.mword.Database.Constants.MESSAGE_EXAM_VIEW_SET_LAST_QUESTION;
import static com.mke.mword.Database.Constants.MESSAGE_EXAM_VIEW_WRITE_SCREEN_TRUE_ANSWER;
import static com.mke.mword.Database.Constants.MESSAGE_SAVE_DOCUMENT;
import static com.mke.mword.Database.Constants.MESSAGE_SCREENVIEW_CHANGE_COLOR_SCREEN;
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
import static com.mke.mword.Database.Constants.SCREEN_PAGE_EXAM;
import static com.mke.mword.Database.mkDB.nPageNumber;
import static com.mke.mword.MainActivity.mkdb;
import static com.mke.mword.Utils.Utils.GetInfoScreen;
import static com.mke.mword.Utils.Utils.GetSpeakWord;
import static com.mke.mword.Utils.Utils.JUSTSLEEP;
import static com.mke.mword.Utils.Utils.QuestionExamResults;
import static com.mke.mword.Utils.Utils.SENDMESSAGE;
import static com.mke.mword.Utils.Utils.SENDMESSAGEEXAMSCREENWINDOW;
import static com.mke.mword.Utils.Utils.SENDMESSAGESCREENWINDOW;
import static com.mke.mword.Utils.Utils.SENDMESSAGEWRITESCREENWINDOW;

import static java.lang.Thread.sleep;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.*;
import androidx.recyclerview.widget.*;

import android.app.*;
import android.content.*;
import android.os.*;
import android.view.*;
import android.widget.*;

import com.mke.mword.Utils.*;
import com.mke.mword.quiz_recyclerview.*;
import com.google.android.material.floatingactionbutton.*;

import java.util.*;

public class quizScreen extends AppCompatActivity {
    private FloatingActionButton fab_next,fab_prev,fab_play;
    private TextView tvInformation,tvCounter;
    public static Activity gQuizScreenActivity;
    public static Context  gQuizScreencontext;
    boolean flag = true,bNextBtn=false,bPrevBtn=false;
//    ArrayList<QuestionS> questionList   =null;
    public static Handler mHandlerQuizScreenWindow    = null;
    ScreenCounterThread T1=null;
    EXAMTHREAD examthread=null;
    static int nExamCounter=0;
    int nAnswerNo=-1;

    //Exam Screen
    TextView questionView=null;//textview_exam_questionView
    RecyclerView recyclerView;
    private Recycler_View_Adapter quiz_adapter=null;

    int nNextBtn=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_screen);

        gQuizScreenActivity=this;
        gQuizScreencontext=this;

        tvInformation = (TextView) findViewById(R.id.textview_quizscreen_information);
        tvInformation.setText(GetInfoScreen(CURRIENTPAGETYPE,nPageNumber,mkdb.
                GetPageLastWVSNumber(CURRIENTPAGETYPE,nPageNumber)));

        tvCounter = (TextView) findViewById(R.id.textview_quiz_screen_counter);
        tvCounter.setVisibility(View.GONE);

        questionView            = findViewById(R.id.textview_exam_questionView);
        questionView.setText(DO_YOU_WANT_TO_START_EXAM);

        recyclerView = (RecyclerView) findViewById(R.id.quiz_recyclerview);
        recyclerView.setVisibility(View.GONE);

        fab_next = (FloatingActionButton) findViewById(R.id.fab_quiz_screen_next);
        fab_next.setScaleType(ImageView.ScaleType.FIT_CENTER);
        fab_next.setVisibility(View.GONE);
        fab_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nNextBtn++;
                SENDMESSAGEEXAMSCREENWINDOW(MESSAGE_SCREENVIEW_NEXT_WORD_BUTTON);
            }
        });
        fab_prev = (FloatingActionButton) findViewById(R.id.fab_quiz_screen_prev);
        fab_prev.setScaleType(ImageView.ScaleType.FIT_CENTER);
        fab_prev.setVisibility(View.GONE);
        fab_prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SENDMESSAGEEXAMSCREENWINDOW(MESSAGE_SCREENVIEW_PREV_WORD_BUTTON);
            }
        });
        fab_play = (FloatingActionButton) findViewById(R.id.fab_quiz_screen_play);
        fab_play.setScaleType(ImageView.ScaleType.FIT_CENTER);
        fab_play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (flag) {
                    SENDMESSAGEEXAMSCREENWINDOW(MESSAGE_SCREENVIEW_PLAY_BUTTON_CLICK);
                }else{
                    SENDMESSAGEEXAMSCREENWINDOW(MESSAGE_SCREENVIEW_PAUSE_BUTTON_CLICK);
                }
            }
        });

        mHandlerQuizScreenWindow = new Handler() {
            @Override
            public void handleMessage(android.os.Message msg) {
                switch (msg.arg1) {
                    case MESSAGE_SCREENVIEW_PAGE_END:
                        mkdb.setPageLastQuestionNumber(0);
                        mkdb.setQuestionPageWatchCount();
                        questionView.setText(QuestionExamResults(CURRIENTPAGETYPE,nPageNumber));
                        STOP();
                        break;
                    case MESSAGE_SCREENVIEW_SHOW_SCREEN: //opens contacts application to browse contacts
                        tvInformation.setText(GetInfoScreen(CURRIENTPAGETYPE, msg.arg2, (int) msg.obj));
                        break;
                    case MESSAGE_SCREENVIEW_SETWORD_LEARNED:
                        mkdb.setQuestionLearn(nExamCounter);
                        break;
                    case MESSAGE_SCREENVIEW_VISIBLE_PREV_BUTTON:
                        if (nExamCounter!=0 && fab_prev.getVisibility()==View.GONE){
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
                        if (!mkdb.CheckThisQPageIsLearn(CURRIENTPAGETYPE,nPageNumber)) {
                            PLAY();
                        }else{
                            AlertDialog.Builder builder = new AlertDialog.Builder(gQuizScreencontext);
                            builder.setTitle(DO_YOU_WANT_TO_START_EXAM);
                            builder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    mkdb.SetQPageMakeLearn(CURRIENTPAGETYPE,nPageNumber,false);
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
                        questionView.setText(DO_YOU_WANT_TO_CONTINUE_THIS_EXAM);
                        STOP();
                        break;
                    case MESSAGE_SCREENVIEW_CHANGE_COLOR_SCREEN:
//                        questionView.setBackgroundColor(R.color.frg1Color);
                        break;
                    case MESSAGE_EXAM_VIEW_SET_LAST_QUESTION:
                        mkdb.setPageLastQuestionNumber(nExamCounter);
                        break;
                    case MESSAGE_SCREENVIEW_NEXT_WORD_BUTTON:
                        bNextBtn =true;
                        break;
                    case MESSAGE_SCREENVIEW_PREV_WORD_BUTTON:
                        bPrevBtn =true;
                        break;
                    case MESSAGE_EXAM_VIEW_CLEAR_QUESTION_DETAIL:
                        clearAllData();
                        break;
                    case MESSAGE_EXAM_VIEW_SET_ANSWER:
                        nAnswerNo=msg.arg2;
                        mkdb.setQuestionRespond(nExamCounter,getAnswerText(nAnswerNo));
                        break;
                    case MESSAGE_EXAM_VIEW_FILL_QUESTION_DETAIL:
                        setQuestionsValue();
                        break;
                    case MESSAGE_EXAM_VIEW_WRITE_SCREEN_TRUE_ANSWER:
                        questionView.setText(GetSpeakWord(mkdb.getAnswer(nExamCounter),
                                mkdb.getQuestion(nExamCounter)));
                        break;
                    default:
                        break;
                }
            }
        };
    }

    private void PLAY(){
        fab_play.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),
                R.drawable.ic_pause_button_64));
        flag = false;
        fab_prev.setVisibility(View.GONE);
        fab_next.setVisibility(View.VISIBLE);
        tvCounter.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.VISIBLE);
//        ExamControlsSetVisibility(View.VISIBLE);
        if (SCREENPAGESTATUS.trim().equals(SCREEN_PAGE_EXAM)){
            examthread = new EXAMTHREAD();
            examthread.start();
        }
    }
    private void STOP(){
        stopRunningThreads();

        fab_play.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),
                R.drawable.ic_play_button_64));
        flag = true;
        fab_prev.setVisibility(View.GONE);
        fab_next.setVisibility(View.GONE);
        recyclerView.setVisibility(View.GONE);
        tvCounter.setVisibility(View.GONE);
    }

    @Override
    protected void onStop() {
        stopRunningThreads();
        super.onStop();
    }

    private int IsHavePrevWords (){
       for (int k = nExamCounter-1; k >= 0; k--) {
            if (mkdb.getQuestionLearn(k)) {
                continue;
            } else {
                return k;
            }
        }
        return -1;
    }

    private int IsHaveNextWords (){
        for (int k = nExamCounter +1; k < mkdb.GetPageQuestions(CURRIENTPAGETYPE,nPageNumber).length(); k++) {
            if (mkdb.getQuestionLearn(k)) {
                continue;
            } else {
                return k;
            }
        }
        return -1;
    }
    private int GetCount () {
        int nLastword = 0;
        if (bNextBtn || bPrevBtn) {
            if (bNextBtn) {
                nLastword = IsHaveNextWords();
                if (nLastword != -1) {
                    bNextBtn = false;
                    nExamCounter = nLastword;
                    return nExamCounter;
                }
            } else if (bPrevBtn) {
                nLastword = IsHavePrevWords();
                if (nLastword != -1) {
                    bPrevBtn = false;
                    nExamCounter = nLastword;
                    return nExamCounter;
                }
            }
        }else {
            nExamCounter++;
//            if (nExamCounter >= mkdb.GetPageQuestions(CURRIENTPAGETYPE,nPageNumber).length() ||
//                    nExamCounter <0 ) {
//                nExamCounter = 0;
//            }
        }
        return 0;
    }

    class EXAMTHREAD extends BASICTHREAD {
        EXAMTHREAD(){running=true;}
        public void run() {
            if (mkdb.GetPageQuestions(CURRIENTPAGETYPE, nPageNumber) != null &&
                    mkdb.GetPageQuestions(CURRIENTPAGETYPE, nPageNumber).length() != 0) {

                long lTime = Long.valueOf(mkdb.getQuestionTime());
                long lTimeTmp = System.currentTimeMillis();

                for (nExamCounter = mkdb.getPageLastQuestionNumber();
                     nExamCounter < mkdb.GetPageQuestions(CURRIENTPAGETYPE, nPageNumber).length()
                             && nExamCounter >= 0 && running; GetCount()) {

                    bNextBtn = false;
                    bPrevBtn = false;

                    if (nExamCounter == -1) {
                        break;
                    }
                    if (mkdb.getQuestionLearn(nExamCounter)) {
                        continue;
                    }

                    SENDMESSAGEEXAMSCREENWINDOW(MESSAGE_SCREENVIEW_SHOW_SCREEN, nPageNumber, nExamCounter);

                    SENDMESSAGEEXAMSCREENWINDOW(MESSAGE_EXAM_VIEW_CLEAR_QUESTION_DETAIL);
                    SENDMESSAGEEXAMSCREENWINDOW(MESSAGE_EXAM_VIEW_FILL_QUESTION_DETAIL);
                    SENDMESSAGEEXAMSCREENWINDOW(MESSAGE_EXAM_VIEW_SET_LAST_QUESTION);

                    if (T1 != null && T1.isRunning()) {
                        T1.stopRunning();
                        JUSTSLEEP(250);
                    }
                    T1 = new ScreenCounterThread(gQuizScreenActivity, tvCounter);
                    T1.start();
                    JUSTSLEEP(250);

                    if (IsHaveNextWords() != -1) {
                        SENDMESSAGEEXAMSCREENWINDOW(MESSAGE_SCREENVIEW_VISIBLE_NEXT_BUTTON);
                    } else {
                        SENDMESSAGEEXAMSCREENWINDOW(MESSAGE_SCREENVIEW_UNVISIBLE_NEXT_BUTTON);
                    }

                    if (IsHavePrevWords() != -1) {
                        SENDMESSAGEEXAMSCREENWINDOW(MESSAGE_SCREENVIEW_VISIBLE_PREV_BUTTON);
                    } else {
                        SENDMESSAGEEXAMSCREENWINDOW(MESSAGE_SCREENVIEW_UNVISIBLE_PREV_BUTTON);
                    }

                    EXAMTHREADSLEEP(500);

                    int nSleepTime = 1000;
                    while (nSleepTime < (mkdb.nWaitExamTime * 1000)
                            && !bNextBtn && !bPrevBtn) {
                        if (nAnswerNo != -1) {
                            STOPSCREENEXAMCOUNTER();
                            if (getAnswerNo() == nAnswerNo) {
                                SENDMESSAGEEXAMSCREENWINDOW(MESSAGE_SCREENVIEW_SETWORD_LEARNED);
                                SENDMESSAGEEXAMSCREENWINDOW(MESSAGE_EXAM_VIEW_WRITE_SCREEN_TRUE_ANSWER);
                            }
                            JUSTSLEEP(1000);
                            nSleepTime = mkdb.nWaitTalkTime * 1000;
                        } else {
                            EXAMTHREADSLEEP(500);
                            nSleepTime += 500;
                        }
                    }
                    STOPSCREENEXAMCOUNTER();
                    if (lTimeTmp != 0) {
                        lTimeTmp = System.currentTimeMillis() - lTimeTmp;
                        lTime += lTimeTmp;
                        mkdb.setQuestionPageTime(Long.toString(lTime));
                    }
                    lTimeTmp = System.currentTimeMillis();
                }
                if(nExamCounter >= mkdb.GetPageQuestions(CURRIENTPAGETYPE, nPageNumber).length()) {
                    SENDMESSAGEEXAMSCREENWINDOW(MESSAGE_SCREENVIEW_PAGE_END);
                }
            }
        }
    }

    public static String getAnswerText(int nSelect){
        switch(nSelect){
            case 0:
                return mkdb.getOPTA(nExamCounter);
            case 1:
                return mkdb.getOPTB(nExamCounter);
            case 2:
                return mkdb.getOPTC(nExamCounter);
            case 3:
                return mkdb.getOPTD(nExamCounter);
            default:
                return "";
        }
    }
    public static int getAnswerNo(){
        if (mkdb.getAnswer(nExamCounter).trim().equals(mkdb.getOPTA(nExamCounter))){
            return 0;
        }else if (mkdb.getAnswer(nExamCounter).trim().equals(mkdb.getOPTB(nExamCounter))){
            return 1;
        }else if (mkdb.getAnswer(nExamCounter).trim().equals(mkdb.getOPTC(nExamCounter))){
            return 2;
        }else if (mkdb.getAnswer(nExamCounter).trim().equals(mkdb.getOPTD(nExamCounter))){
            return 3;
        }
        return -1;
    }
    private void EXAMTHREADSLEEP (int nTime) {
        if (!bNextBtn && !bPrevBtn && examthread!=null
                && examthread.isRunning()) {
            try {
                sleep(nTime);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }else{
            STOPSCREENEXAMCOUNTER();
        }
    }

    private void clearAllData() {
        questionView.setText("");
    }
    //Create a list of Data objects
    public List<Data> fill_with_data() {
        List<Data> data = new ArrayList<>();

        data.add(new Data("A:)",mkdb.getOPTA(nExamCounter) ));
        data.add(new Data("B:)",mkdb.getOPTB(nExamCounter) ));
        data.add(new Data("C:)",mkdb.getOPTC(nExamCounter) ));
        data.add(new Data("D:)",mkdb.getOPTD(nExamCounter) ));

        return data;
    }
    private boolean setQuestionsValue() {
        List<Data> data = fill_with_data();

        quiz_adapter = new Recycler_View_Adapter(data, getApplication());
        recyclerView.setAdapter(quiz_adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        questionView.setText(mkdb.getQuestion(nExamCounter));
        nAnswerNo=-1;
        return true;
    }
    private void stopRunningThreads(){
        STOPSCREENEXAMCOUNTER();
        STOPEXAMTHREAD();
    }
    private void STOPSCREENEXAMCOUNTER(){
        if (T1!=null && T1.isRunning()){
            T1.stopRunning();
        }
    }
    private void STOPEXAMTHREAD(){
        if (examthread!=null && examthread.isRunning()){
            examthread.stopRunning();
        }
    }

    @Override
    public void onBackPressed() {
        stopRunningThreads();

        SENDMESSAGE(MESSAGE_UPDATE_CURRENT_FRAGMENT_LISTVIEW);
        SENDMESSAGE(MESSAGE_SAVE_DOCUMENT);

        super.onBackPressed();
    }
}
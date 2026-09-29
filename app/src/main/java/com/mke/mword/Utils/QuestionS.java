package com.mke.mword.Utils;

import static com.mke.mword.Utils.Utils.GetRandom;

import java.util.*;

public class QuestionS {
    String szQuestion           = "";
    String szAnswer             = "";
    String szOptA               = "";
    String szOptB               = "";
    String szOptC               = "";
    String szOptD               = "";
    ArrayList<String> szList    =null;
    boolean bAnswer=false;

    public boolean getRespond(){return bAnswer;}
    public void    setRespond(boolean bRes){bAnswer=bRes;}

    public void addStringToList(String szOpt) {
        if (szList== null){
            szList=new ArrayList<String>();
        }
        szList.add(szOpt);
    }




    public ArrayList<String> getSzList(){return szList;}

    public void fillOpt(){
        ArrayList<String> szListTmp    =new ArrayList<String>();
        szListTmp= getSzList();

        int n = GetRandom(0,4);
//        Log.e("ERORRRRRRRRRRRRRRRERRRRRRRR      fillOpt()", "n:"+ n);
        szOptA= szListTmp.get(n);
        szListTmp.remove(n);

        n = GetRandom(0,3);
//        Log.e("ERORRRRRRRRRRRRRRRERRRRRRRR      fillOpt()", "n:"+ n);
        szOptB= szListTmp.get(n);
        szListTmp.remove(n);

        n = GetRandom(0,2);
//        Log.e("ERORRRRRRRRRRRRRRRERRRRRRRR      fillOpt()", "n:"+ n);
        szOptC= szListTmp.get(n);
        szListTmp.remove(n);

        szOptD= szListTmp.get(0);
        szListTmp.remove(0);

    }

    public void setQuestion(String szQues) {
        szQuestion = szQues;
    }

    public String getQuestion() {
        return szQuestion;
    }

    public void setAnswer(String szAns) {
        szAnswer = szAns;
    }

    public String getAnswer() {
        return szAnswer;
    }

    public void setOptA(String szA) {
        szOptA = szA;
    }

    public String getOptA() {
        return szOptA;
    }

    public void setOptB(String szB) {
        szOptB = szB;
    }

    public String getOptB() {
        return szOptB;
    }

    public void setOptC(String szC) {
        szOptC = szC;
    }

    public String getOptC() {
        return szOptC;
    }

    public void setOptD(String szD) {
        szOptD = szD;
    }

    public String getOptD() {
        return szOptD;
    }

}

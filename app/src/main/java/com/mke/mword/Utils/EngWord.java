package com.mke.mword.Utils;


public class EngWord {
    String szEnglish;
    String szOtherLang;
    boolean bLearned;

    public EngWord() {
        szEnglish = "";
        szOtherLang = "";
        bLearned = false;
    }
    public void SetWord(EngWord eword){
        this.szEnglish=eword.szEnglish;
        this.szOtherLang=eword.szOtherLang ;
        this.bLearned=eword.bLearned;
    }
    public void SetWord(String eng,String olang){
        this.szEnglish=eng;
        this.szOtherLang=olang ;
        this.bLearned=false;
    }

    public EngWord(String szEng, String szOth) {
        szEnglish = szEng;
        szOtherLang = szOth;
        bLearned = false;
    }

    public void setEnglish(String szEng) {
        szEnglish = szEng;
    }

    public void setOtherLang(String szOthLng) {
        szOtherLang = szOthLng;
    }

    public void setLearned(boolean bLearn) {
        bLearned = bLearn;
    }

    public String getEnglish() {
        return szEnglish;
    }

    public String getOtherLang() {
        return szOtherLang;
    }

    public Boolean IsLearn() {
        return bLearned;
    }
}

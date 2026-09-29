package com.mke.mword.Utils;

public class InformationWVS {
    private int nPNumber;
    private int nWVSCount;
    private int nlearnedWVS;
    private int nLastWVS;
    private int nwatchTime;

    public InformationWVS() {
    }

    public InformationWVS(int nPNno, int nWVSCnt, int nLrnWVS, int nLWVS, int nWVSTm) {
        nPNumber    = nPNno;
        nWVSCount   = nWVSCnt;
        nlearnedWVS = nLrnWVS;
        nLastWVS    = nLWVS;
        nwatchTime  = nWVSTm;
    }

    public int GetPagenumber() {
        return nPNumber;
    }

    public String getPageNumberStrinig() {
        return "Page Number:" + nPNumber;
    }

    public String getWVSCountStrinig() {
        return "Words Size:" + nWVSCount;
    }

    public String getLearnedWVSStrinig() {
        return "Learn Words:" + nlearnedWVS;
    }

    public String getLastWVSStrinig() {
        return "Last Words:" + nLastWVS;
    }

    public String getWatchTimeStrinig() {
        return "Watch Time:" + nwatchTime;
    }

    public boolean IsWatchBefore() {
        return nWVSCount == nlearnedWVS ? true : false;
    }
}

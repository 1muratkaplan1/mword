package com.mke.mword.Utils;

import android.os.*;

import com.mke.mword.MAIL.*;

public class SendEmailTask extends AsyncTask<Void, Void, Void> {
    String szSubject = "";
    String[] szFilenameList = null;
    String szMailTo = "";
    String szContent = "";
    String szMailFrom = "";
    String szMailFromPassWord = "";

    public SendEmailTask(String szSubj, String[] szFilenameL, String szTo,
                         String szFrom, String szFromPWord, String szCont) {
        super();
        // Do something with these parameters
        szSubject = szSubj;
        szFilenameList = szFilenameL;
        szMailTo = szTo;
        szContent = szCont;
        szMailFrom = szFrom;
        szMailFromPassWord = szFromPWord;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
//            Log.i("Email sending", "sending start");
    }

    @Override
    protected Void doInBackground(Void... params) {
        try {
            GmailSender sender = new GmailSender(szMailFrom, szMailFromPassWord);
            sender.sendMail(szSubject, szFilenameList, szMailFrom, szMailTo, szContent);
//                for(String name : szFilenameList)
//                    Log.i("Email sending", "send" + name);

        } catch (Exception e) {
            for (String name : szFilenameList)
//                    Log.i("Email sending", "cannot send"+name);

                e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void result) {
        super.onPostExecute(result);
    }
}

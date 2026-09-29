package com.mke.mword;

import static com.mke.mword.Database.Constants.ABOUT_US_HTML;
import static com.mke.mword.Database.Constants.FAQS_HTML;
import static com.mke.mword.Database.Constants.PRIVACY_POLICY_HTML;
import static com.mke.mword.Database.Constants.TERMS_OF_SERVICE_HTML;
import static com.mke.mword.Database.Constants.TUTORIOL_HTML;
import static com.mke.mword.Database.Constants.HELP_CENTER_WEB_VIEW_CURRENT_FILE;
import static com.mke.mword.Database.Constants.UPDATE_PRO_VERSION_HTML;
import static com.mke.mword.Utils.Utils.SENDMESSAGEWRITESCREENWINDOW;

import androidx.appcompat.app.AppCompatActivity;

import android.content.*;
import android.net.*;
import android.os.Bundle;
import android.util.*;
import android.view.*;
import android.widget.*;

import com.mke.mword.Utils.*;

public class HelpCenter extends AppCompatActivity {
    Context cxHelpCenter=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help_center);
        cxHelpCenter=this;

        Button btnAboutUs =  findViewById(R.id.help_center_about_us);
        btnAboutUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HELP_CENTER_WEB_VIEW_CURRENT_FILE=ABOUT_US_HTML;
                startActivity(new Intent(cxHelpCenter, testtt.class));
            }
        });
        Button btnTutoriol =  findViewById(R.id.help_center_tutoriol);
        btnTutoriol.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HELP_CENTER_WEB_VIEW_CURRENT_FILE=TUTORIOL_HTML;
                startActivity(new Intent(cxHelpCenter, testtt.class));
            }
        });
        Button btnFaqs =  findViewById(R.id.help_center_faqs);
        btnFaqs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HELP_CENTER_WEB_VIEW_CURRENT_FILE=FAQS_HTML;
                startActivity(new Intent(cxHelpCenter, testtt.class));
            }
        });
        Button btnContactSupport =  findViewById(R.id.help_center_contact_support);
        btnContactSupport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(cxHelpCenter, contact_support.class));
            }
        });
        Button btnTermsOfService =  findViewById(R.id.help_center_termsofservice);
        btnTermsOfService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HELP_CENTER_WEB_VIEW_CURRENT_FILE=TERMS_OF_SERVICE_HTML;
                startActivity(new Intent(cxHelpCenter, testtt.class));
            }
        });
        Button btnPrivacyPolicy =  findViewById(R.id.help_center_privacy_policy);
        btnPrivacyPolicy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HELP_CENTER_WEB_VIEW_CURRENT_FILE=PRIVACY_POLICY_HTML;
                startActivity(new Intent(cxHelpCenter, testtt.class));
            }
        });
        Button btnRate =  findViewById(R.id.help_center_rate);
        btnRate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cxHelpCenter.startActivity(new Intent(Intent.ACTION_VIEW,
                        Uri.parse("market://details?id=" + "com.mke.mword")));

//                mWordRate.app_launched(cxHelpCenter);
//                Log.e("ERROR","under construction");
            }
        });
        Button btnProVersion =  findViewById(R.id.help_center_getproversion);
        btnProVersion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    HELP_CENTER_WEB_VIEW_CURRENT_FILE=UPDATE_PRO_VERSION_HTML;
                    startActivity(new Intent(cxHelpCenter, testtt.class));

            }
        });

    }
}
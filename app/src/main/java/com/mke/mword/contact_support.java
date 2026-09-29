package com.mke.mword;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.*;
import android.widget.*;

import com.mke.mword.Utils.*;

public class contact_support extends AppCompatActivity {

    EditText szFirstLastName=null;
    EditText szMailAdress=null;
    EditText szSubject=null;
    EditText szContent=null;

    String szMyMailAdress       = "easylearnenglishword@gmail.com";
    String szMyMailAdressPWord  = "200976Mk";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_support);

        szFirstLastName     = findViewById(R.id.contact_support_firstlast_name);
        szMailAdress        = findViewById(R.id.contact_support_email_adress);
        szSubject           = findViewById(R.id.contact_support_subject);
        szContent           = findViewById(R.id.contact_support_content);

//        CheckEmailAdress();

        android.widget.Button summitBtn = findViewById(R.id.contact_support_summit);
        summitBtn.setOnClickListener(new android.view.View.OnClickListener() {
            @Override
            public void onClick(android.view.View v) {
                if (!szFirstLastName.getText().toString().isEmpty() &&
                        !szMailAdress.getText().toString().isEmpty() &&
                            !szSubject.getText().toString().isEmpty() &&
                                !szContent.getText().toString().isEmpty()) {
                    final SendEmailTask sendEmailTask = new SendEmailTask(szSubject.getText().toString(), null,
                            szMyMailAdress, szMyMailAdress, szMyMailAdressPWord,
                            szFirstLastName.getText().toString() + "\n" +
                                    szMailAdress.getText().toString() + "\n" +
                                    szContent.getText().toString());
                    sendEmailTask.execute();
                    finish();
                }else{
                    Toast.makeText(getApplicationContext(),"invalid Subject or Content?",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void CheckEmailAdress(){
        String email = szMailAdress.getText().toString().trim();
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        szMailAdress .addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                if (email.matches(emailPattern) && s.length() > 0){
                    Toast.makeText(getApplicationContext(),"valid email address",Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(getApplicationContext(),"Invalid email address",Toast.LENGTH_SHORT).show();
                }
            }
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // other stuffs
            }
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // other stuffs
            }
        });
    }
}
package com.vaibhav.loginsignuplogoutdemo;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ResetPasswordActivity extends AppCompatActivity {

    private EditText reemailtext;
    private Button resetpwdbutton;
    private TextView errorlbl;
    private ProgressDialog progressDialog;
    private static final Pattern VALID_EMAIL_ADDRESS_REGEX =Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
        reemailtext=(EditText)findViewById(R.id.ReEmailTb);
        resetpwdbutton=(Button)findViewById(R.id.ResetPwdBtn);
        errorlbl=(TextView)findViewById(R.id.errorl2);
        progressDialog=new ProgressDialog(this);
        reemailtext.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                errorlbl.setText("");
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        resetpwdbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String email=reemailtext.getText().toString().trim();
                if(TextUtils.isEmpty(email))
                {
                    errorlbl.setText("Please enter email address");
                }else{
                    if(validate(email))
                    {
                        progressDialog.setMessage("Please Wait..");
                        progressDialog.show();
                        FirebaseAuth.getInstance().sendPasswordResetEmail(email).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                progressDialog.dismiss();
                                    AlertDialog alertDialog = new AlertDialog.Builder(ResetPasswordActivity.this).create();
                                    alertDialog.setTitle("Email sent");
                                    alertDialog.setMessage("Link for reset password is been sent to "+email);
                                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                                            new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int which) {
                                                    dialog.dismiss();
                                                }
                                            });
                                    alertDialog.show();
                                    try {
                                        Thread.sleep(5000);
                                    }catch (Exception e){}
                                    Intent i = new Intent(getApplicationContext(),MainActivity.class);
                                    startActivity(i);
                                }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                progressDialog.dismiss();
                                errorlbl.setText("Account does'nt exists");
                            }
                        });
                    }
                    else{errorlbl.setText("Invalid email address");}
                }
            }
        });
    }
    private boolean validate(String emailStr) {
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX .matcher(emailStr);
        return matcher.find();
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(getApplicationContext(),MainActivity.class);
        startActivity(i);
        overridePendingTransition(R.anim.slide_out_left,android.R.anim.slide_out_right);
        finish();
    }
}

package com.vaibhav.loginsignuplogoutdemo;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {
    private EditText emailtextbox;
    private EditText passwordtextbox;
    private Button loginbutton;
    private TextView signuplinktextview;
    private TextView resetpwdtv;
    private TextView errortextview;
    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;
    private static final Pattern VALID_EMAIL_ADDRESS_REGEX =Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    private static int SP=5000;
    @Override
    protected void onStart() {
        super.onStart();
        firebaseAuth=FirebaseAuth.getInstance();
        if(firebaseAuth.getCurrentUser()!=null)
        {
            Intent i = new Intent(getApplicationContext(),MainPageActivity.class);
            startActivity(i);
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        progressDialog=new ProgressDialog(this);
        resetpwdtv=(TextView)findViewById(R.id.ResetPwdLink);
        emailtextbox=(EditText)findViewById(R.id.EmailTb2);
        passwordtextbox=(EditText)findViewById(R.id.PasswordTb2);
        loginbutton=(Button)findViewById(R.id.LoginBtn);
        signuplinktextview=(TextView)findViewById(R.id.SignUpLink);
        errortextview=(TextView)findViewById(R.id.ErrorLabel2);
        loginbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userLogin();
            }
        });
        signuplinktextview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(),SignUpPageActivity.class);
                startActivity(i);
                overridePendingTransition(R.anim.slide_in_from_bottom_right,android.R.anim.slide_out_right);
            }
        });
        resetpwdtv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(),ResetPasswordActivity.class);
                startActivity(i);
                overridePendingTransition(android.R.anim.slide_in_left,android.R.anim.slide_out_right);
            }
        });
        keyLiss(emailtextbox);
        keyLiss(passwordtextbox);
    }
    private boolean validate(String emailStr) {
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX .matcher(emailStr);
        return matcher.find();
    }
    private void userLogin()
    {
        String email=emailtextbox.getText().toString().trim();
        String password=passwordtextbox.getText().toString().trim();
        if(TextUtils.isEmpty(email))
        {
            errortextview.setText("Please enter email address");
        }
        else
        {

            if(TextUtils.isEmpty(password))
            {
                errortextview.setText("Please enter password");
            }
            else
            {
                if(validate(email))
                {
                progressDialog.setMessage("Authenticating Please Wait..");
                progressDialog.show();
                firebaseAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if(task.isSuccessful())
                        {
                            progressDialog.dismiss();
                            Intent i = new Intent(getApplicationContext(),MainPageActivity.class);
                            startActivity(i);
                            overridePendingTransition(android.R.anim.slide_in_left,R.anim.nav_to_message);
                        }
                        else
                        {
                            progressDialog.dismiss();
                            try
                            {
                                throw task.getException();
                            }
                            catch (FirebaseAuthInvalidUserException invalidEmail)
                            {
                                errortextview.setText("Account doesn't exist");
                            }
                            catch (FirebaseAuthInvalidCredentialsException wrongPassword)
                            {
                                errortextview.setText("Invalid password");
                            }
                            catch (Exception e)
                            {
                                Toast.makeText(MainActivity.this,"Couldn't able to login please try again",Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                });
            }
            else{
                    errortextview.setText("Invalid email address");
                }
        }
       }
    }
    private void keyLiss(EditText e)
    {

        e.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                errortextview.setText("");
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }
    @Override
    public void onBackPressed() {
        finishAffinity();
    }
}

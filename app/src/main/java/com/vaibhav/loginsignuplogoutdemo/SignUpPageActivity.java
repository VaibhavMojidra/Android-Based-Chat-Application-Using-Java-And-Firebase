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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignUpPageActivity extends AppCompatActivity {
    EditText emailtext,passwordtext,usernametext;
    TextView errortv,logintv;
    Button registerb;
    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;
    private static final Pattern VALID_EMAIL_ADDRESS_REGEX =Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
    private DatabaseReference reference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_page);
        firebaseAuth=FirebaseAuth.getInstance();
        emailtext=(EditText)findViewById(R.id.EmailTb2);
        passwordtext=(EditText)findViewById(R.id.PasswordTb2);
        usernametext=(EditText)findViewById(R.id.UserNameTB);
        errortv=(TextView)findViewById(R.id.ErrorLabel2);
        logintv=(TextView)findViewById(R.id.LoginLink);
        registerb=(Button)findViewById(R.id.RegisterBtn);
        registerb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();
            }
        });
        logintv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(i);
                overridePendingTransition(R.anim.slide_in_from_top_right,R.anim.slide_in_from_right);
            }
        });
        progressDialog=new ProgressDialog(this);
        keyLiss(emailtext);
        keyLiss(passwordtext);
    }
    private void registerUser()
    {
        errortv.setText("");
        String email=emailtext.getText().toString().trim();
        String password=passwordtext.getText().toString().trim();
        final String username=usernametext.getText().toString().trim();
        if(TextUtils.isEmpty(email))
        {
            errortv.setText("Please enter email");
        }
        else
        {
            if(validate(email)) {
                if (TextUtils.isEmpty(password)) {
                    errortv.setText("Please enter password");
                }else {
                    if (TextUtils.isEmpty(username)) {
                        errortv.setText("Please enter password");
                    }
                    else{
                    progressDialog.setMessage("Register Please Wait..");
                    progressDialog.show();
                    firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                firebaseAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                                            String userid = firebaseUser.getUid();
                                            reference = FirebaseDatabase.getInstance().getReference("Users").child(userid);
                                            HashMap<String, String> hashMap = new HashMap<>();
                                            hashMap.put("id", userid);
                                            hashMap.put("username",username);
                                            hashMap.put("imageURL","default");
                                            hashMap.put("status","offline");
                                            reference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    progressDialog.dismiss();
                                                    AlertDialog alertDialog = new AlertDialog.Builder(SignUpPageActivity.this).create();
                                                    alertDialog.setTitle("Verify by Email");
                                                    alertDialog.setMessage("Registered successfully. Please check email for verification.");
                                                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                                                            new DialogInterface.OnClickListener() {
                                                                public void onClick(DialogInterface dialog, int which) {
                                                                    dialog.dismiss();
                                                                    FirebaseAuth.getInstance().signOut();
                                                                    Intent i = new Intent(getApplicationContext(), MainActivity.class);
                                                                    startActivity(i);
                                                                }
                                                            });
                                                    alertDialog.show();
                                                    emailtext.setText("");
                                                    passwordtext.setText("");
                                                    errortv.setText("");
                                                }
                                            }).addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    progressDialog.dismiss();
                                                    errortv.setText("Error in registering,Please try again later");
                                                }
                                            });

                                        } else {
                                            progressDialog.dismiss();
                                            Toast.makeText(SignUpPageActivity.this, "Could'nt register make you are connected to internet", Toast.LENGTH_LONG).show();
                                        }
                                    }
                                });
                            } else {
                                try {
                                    throw task.getException();
                                } catch (FirebaseAuthWeakPasswordException weakPassword) {
                                    progressDialog.dismiss();
                                    errortv.setText("Weak Password");
                                } catch (FirebaseAuthUserCollisionException existEmail) {
                                    progressDialog.dismiss();
                                    errortv.setText("This email is already registered");
                                } catch (Exception e) {
                                    progressDialog.dismiss();
                                    Toast.makeText(SignUpPageActivity.this, "Could'nt register make you are connected to internet", Toast.LENGTH_LONG).show();
                                }
                            }
                        }
                    });
                 }
                }
            }else{errortv.setText("Invalid email address");}
        }
    }
    private boolean validate(String emailStr) {
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX .matcher(emailStr);
        return matcher.find();
    }

    private void keyLiss(EditText e)
    {

        e.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                errortv.setText("");
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(getApplicationContext(),MainActivity.class);
        startActivity(i);
        overridePendingTransition(R.anim.slide_in_from_top_right,R.anim.slide_in_from_right);
        finish();
    }
}

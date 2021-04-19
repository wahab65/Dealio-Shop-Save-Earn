package com.example.deelio;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegisterActivity extends AppCompatActivity {
    //Declare all elements that will need probable coding
    TextView tvGoToLoginActivity;
    Button registerButton;
    ProgressBar progressBar;
    FirebaseAuth fAuth;
    EditText fullName, registerEmail, registerPassword;

    public static final String TAG = "RegisterActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        this.getSupportActionBar().hide();

        //list all element bindings here
        tvGoToLoginActivity= (TextView)findViewById(R.id.loginNregisterSwitcher);
        registerButton = findViewById(R.id.registerButton);
        progressBar = findViewById(R.id.progressBar);
        fullName = findViewById(R.id.fullName);
        registerEmail = findViewById(R.id.registerEmail);
        registerPassword = findViewById(R.id.registerPassword);

        fAuth = FirebaseAuth.getInstance();
        //if USer is already logged in
        if(fAuth.getCurrentUser() != null){
            final Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
        // When you press the already registered text
        tvGoToLoginActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = registerEmail.getText().toString().trim();
                String password = registerPassword.getText().toString().trim();
                //to check if user email is empty
                if(TextUtils.isEmpty(email)){
                    registerEmail.setError("Email is Required!");
                    return;
                }
                //to check if password is empty
                if(TextUtils.isEmpty(password)){
                    registerPassword.setError("Password is Required!");
                    return;
                }
                //to check if password has less than 6 characters
                if(password.length() < 6){
                    registerPassword.setError("Password must have 6 or more characters");
                    return;
                }

                progressBar.setVisibility(View.VISIBLE);

                //register the User
                fAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(RegisterActivity.this, "User Successfully Created", Toast.LENGTH_SHORT).show();
                            Log.i(TAG, " User has been succesfully registerd");
                            final Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        }else{
                            Toast.makeText(RegisterActivity.this, "Error: !", Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);
                            Log.e(TAG, "Failed to Register User", task.getException());

                        }
                    }
                });

            }
        });
    }
}
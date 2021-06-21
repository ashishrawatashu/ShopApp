package com.example.shopapp.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.shopapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {
    EditText emailET, passET;
    Button login_button;
    FirebaseAuth firebaseAuth;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor login_editor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        firebaseAuth = FirebaseAuth.getInstance();

        emailET = findViewById(R.id.emailET);
        passET = findViewById(R.id.passET);
        login_button = findViewById(R.id.login_button);

        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = emailET.getText().toString().trim();
                String password = passET.getText().toString().trim();

                if (TextUtils.isEmpty(email)) {
                    emailET.setError("email empty");
                    return;
                }
                if (TextUtils.isEmpty(password)) {
                    passET.setError("pass empty");
                    return;
                }
                if (password.length() < 6) {
                    passET.setError("pass atleast 6 character");
                    return;
                }

                firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getApplicationContext(), "login succesfully", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            sharedPreferences = getSharedPreferences("userDetails", 0);
                            login_editor = sharedPreferences.edit();
                            login_editor.putString("userId", task.getResult().getUser().getUid());
                            login_editor.putString("userMail", task.getResult().getUser().getEmail());
                            login_editor.putString("userPhone", task.getResult().getUser().getPhoneNumber());
                            login_editor.putString("userName", task.getResult().getUser().getDisplayName());

                            login_editor.apply();
                            startActivity(intent);
                        } else {
                            Toast.makeText(LoginActivity.this, "Login Failed !", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }
}

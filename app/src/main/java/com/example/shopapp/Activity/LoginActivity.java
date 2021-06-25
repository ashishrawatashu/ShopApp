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
import android.widget.TextView;
import android.widget.Toast;

import com.example.shopapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity implements  View.OnClickListener{
    TextInputEditText email_TIET, password_TIET;
    Button login_BT;
    FirebaseAuth firebaseAuth;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor login_editor;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    TextView signUp_TV;
    MaterialCardView login_back_arrow_CV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        firebaseAuth = FirebaseAuth.getInstance();

        email_TIET = findViewById(R.id.email_TIET);
        password_TIET = findViewById(R.id.password_TIET);
        login_BT = findViewById(R.id.login_BT);
        signUp_TV = findViewById(R.id.signUp_TV);
        login_back_arrow_CV=findViewById(R.id.login_back_arrow_CV);
        login_back_arrow_CV.setOnClickListener(this);
        signUp_TV.setOnClickListener(this);
        login_BT.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.signUp_TV:
                startActivity(new Intent(LoginActivity.this,SignUpActivity.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
                break;

            case R.id.login_back_arrow_CV:
                finishAffinity();
                break;

            case R.id.login_BT:
                String email = email_TIET.getText().toString().trim();
                String password = password_TIET.getText().toString().trim();

                if (TextUtils.isEmpty(email)) {
                    email_TIET.setError(""+R.string.emailEmpty);
                    password_TIET.setError(null);
                }else if (!email.isEmpty() && !email.matches(emailPattern)) {
                    email_TIET.setError(""+R.string.ivaildEmail);
                    password_TIET.setError(null);

                }
                else if (TextUtils.isEmpty(password)) {
                    password_TIET.setError(""+R.string.passEmpty);
                    email_TIET.setError(null);
                }
                else if (password.length() < 6) {
                    password_TIET.setError(""+R.string.passLimit);
                    email_TIET.setError(null);
                }

                firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getApplicationContext(), R.string.loginDone, Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(LoginActivity.this, ShopListActivity.class);
                            sharedPreferences = getSharedPreferences("userDetails", 0);
                            login_editor = sharedPreferences.edit();
                            login_editor.putString("userId", task.getResult().getUser().getUid());
                            login_editor.putString("userMail", task.getResult().getUser().getEmail());
                            login_editor.putString("userPhone", task.getResult().getUser().getPhoneNumber());
                            login_editor.putString("userName", task.getResult().getUser().getDisplayName());
                            login_editor.putString("loggedIn", "loggedIn");
                            login_editor.apply();
                            startActivity(intent);
                            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
                        } else {
                            Toast.makeText(LoginActivity.this, R.string.loginFail, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                break;
        }
    }
}

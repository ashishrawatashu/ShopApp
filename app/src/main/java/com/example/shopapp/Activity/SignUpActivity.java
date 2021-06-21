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

import com.example.shopapp.Models.UserPojo;
import com.example.shopapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {
    EditText nameET, emialET, passET, phoneET;
    Button register_button, login_button;
    private FirebaseAuth mAuth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    UserPojo userPojo;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor login_editor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        mAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();

        databaseReference = firebaseDatabase.getReference().child("ShopApp").child("Users");

        initView();





    }

    private void initView() {


        if (mAuth.getCurrentUser() != null) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
        nameET = findViewById(R.id.nameET);
        emialET = findViewById(R.id.emailET);
        passET = findViewById(R.id.passET);
        phoneET = findViewById(R.id.phonenoET);

        register_button = findViewById(R.id.register_button);
        login_button = findViewById(R.id.login_button);

        register_button.setOnClickListener(this);
        login_button.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.register_button:

                String email = emialET.getText().toString().trim();
                String password = passET.getText().toString().trim();

                if (TextUtils.isEmpty(email)) {
                    emialET.setError("email empty");
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

                mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {
                            sharedPreferences = getSharedPreferences("userDetails", 0);
                            login_editor = sharedPreferences.edit();
                            login_editor.putString("userId", task.getResult().getUser().getUid());
                            login_editor.putString("userMail", task.getResult().getUser().getEmail());
                            login_editor.putString("userPhone", task.getResult().getUser().getPhoneNumber());
                            login_editor.putString("userName", task.getResult().getUser().getDisplayName());

                            login_editor.apply();
                            userPojo = new UserPojo();
                            userPojo.setUserId(task.getResult().getUser().getUid());
                            userPojo.setUserName(nameET.getText().toString());
                            userPojo.setUserPhone(phoneET.getText().toString());
                            userPojo.setUserMail(task.getResult().getUser().getEmail());
                            databaseReference.child(task.getResult().getUser().getUid()).setValue(userPojo);
                            Toast.makeText(getApplicationContext(), "register succesfully", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
                            startActivity(intent);
                        } else {

                            Toast.makeText(SignUpActivity.this, "Sorry", Toast.LENGTH_SHORT).show();

                        }
                    }
                });
                break;

            case R.id.login_button:

                startActivity(new Intent(getApplicationContext(), LoginActivity.class));

                break;

            default:


                break;
        }
    }
}
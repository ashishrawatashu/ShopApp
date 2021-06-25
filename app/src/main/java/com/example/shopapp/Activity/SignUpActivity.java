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

import com.example.shopapp.Models.UserPojo;
import com.example.shopapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {
    TextInputEditText full_name_TIET, phone_TIET, email_TIET, password_TIET;
    Button signUp_BT;
    TextView signIn_TV;
    private FirebaseAuth mAuth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    UserPojo userPojo;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor login_editor;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    MaterialCardView back_arrow_CV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        mAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();

        databaseReference = firebaseDatabase.getReference().child("ShopApp").child("Users");

        initView();
        back_arrow_CV=findViewById(R.id.back_arrow_CV);
        back_arrow_CV.setOnClickListener(this);


    }

    private void initView() {
//
//        if (mAuth.getCurrentUser() != null) {
//            Intent intent = new Intent(this, MainActivity.class);
//            startActivity(intent);
//        }
        full_name_TIET = findViewById(R.id.full_name_TIET);
        email_TIET = findViewById(R.id.email_TIET);
        password_TIET = findViewById(R.id.password_TIET);
        phone_TIET = findViewById(R.id.phone_TIET);

        signUp_BT = findViewById(R.id.signUp_BT);
        signIn_TV = findViewById(R.id.signIn_TV);

        signUp_BT.setOnClickListener(this);
        signIn_TV.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.back_arrow_CV:
                onBackPressed();
                break;

            case R.id.signUp_BT:

                String email = email_TIET.getText().toString().trim();
                String password = password_TIET.getText().toString().trim();
                String fullName = full_name_TIET.getText().toString().trim();
                String phone = phone_TIET.getText().toString().trim();

                if (TextUtils.isEmpty(fullName)) {
                    full_name_TIET.setError(""+R.string.enterName);
                    email_TIET.setError(null);
                    phone_TIET.setError(null);
                    password_TIET.setError(null);
                } else if (TextUtils.isEmpty(email)) {
                    email_TIET.setError(""+R.string.emailEmpty);
                    full_name_TIET.setError(null);
                    phone_TIET.setError(null);
                    password_TIET.setError(null);
                } else if (!email.isEmpty() && !email.matches(emailPattern)) {
                    email_TIET.setError(""+R.string.ivaildEmail);
                    full_name_TIET.setError(null);
                    phone_TIET.setError(null);
                    password_TIET.setError(null);
                } else if (TextUtils.isEmpty(password)) {
                    password_TIET.setError(""+R.string.passEmpty);
                    full_name_TIET.setError(null);
                    phone_TIET.setError(null);
                    email_TIET.setError(null);
                } else if (password.length() < 6) {
                    password_TIET.setError(""+R.string.passLimit);
                    full_name_TIET.setError(null);
                    phone_TIET.setError(null);
                    email_TIET.setError(null);
                }else {
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
                                login_editor.putString("loggedIn", "loggedIn");
                                login_editor.apply();
                                userPojo = new UserPojo();
                                userPojo.setUserId(task.getResult().getUser().getUid());
                                userPojo.setUserName(full_name_TIET.getText().toString());
                                userPojo.setUserPhone(phone_TIET.getText().toString());
                                userPojo.setUserMail(task.getResult().getUser().getEmail());
                                userPojo.setShopStatus("false");
                                databaseReference.child(task.getResult().getUser().getUid()).setValue(userPojo);
                                Toast.makeText(getApplicationContext(), ""+R.string.signUpDone, Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(SignUpActivity.this, ShopListActivity.class);
                                startActivity(intent);
                                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
                            } else {

                                Toast.makeText(SignUpActivity.this, R.string.truagain, Toast.LENGTH_SHORT).show();

                            }
                        }
                    });

                }

                break;

            case R.id.signIn_TV:
                onBackPressed();
                break;
        }
    }
}
package com.example.shopapp.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.shopapp.Models.ShopsPojo;
import com.example.shopapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ShopActivity extends AppCompatActivity implements View.OnClickListener {

    EditText shopName_ET;
    ShopsPojo shopsPojo;
    Button addShop_BT, addProduct_BT,myOrders_BT;
    FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    FirebaseAuth.AuthStateListener authStateListener;
    long maxIdFood = 0;
    String u_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop);
        shopName_ET = findViewById(R.id.shopName_ET);
        addShop_BT = findViewById(R.id.addShop_BT);
        addProduct_BT = findViewById(R.id.addProduct_BT);
        myOrders_BT = findViewById(R.id.myOrders_BT);
        myOrders_BT.setOnClickListener(this);
        addProduct_BT.setOnClickListener(this);
        addShop_BT.setOnClickListener(this);

        shopsPojo = new ShopsPojo();
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        u_id = firebaseUser.getUid();

        databaseReference = firebaseDatabase.getReference().child("ShopApp").child("Shops").child(shopName_ET.getText().toString()).child(u_id);
        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser currentUser = firebaseAuth.getCurrentUser();

                if (currentUser != null) {
                    Toast.makeText(getApplicationContext(), "successfully", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "sorry", Toast.LENGTH_SHORT).show();
                }
            }
        };

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    maxIdFood = (dataSnapshot.getChildrenCount());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                Log.e("Failed to read value.", databaseError.toException().toString());

            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        firebaseAuth.addAuthStateListener(authStateListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (authStateListener != null) {
            firebaseAuth.removeAuthStateListener(authStateListener);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.addShop_BT:
                shopsPojo.setShopName(shopName_ET.getText().toString());
                shopsPojo.setLat("22.449249");
                shopsPojo.setLog("88.371628");
                shopsPojo.setId(u_id);
                databaseReference.setValue(shopsPojo);
                Toast.makeText(ShopActivity.this, "inserted succesfully", Toast.LENGTH_SHORT).show();
                break;

            case R.id.addProduct_BT:
                startActivity(new Intent(ShopActivity.this, AddUpdateProduct.class));

                break;

            case R.id.myOrders_BT:
                startActivity(new Intent(ShopActivity.this, MyOrdersActivity.class));

                break;
        }

    }
}
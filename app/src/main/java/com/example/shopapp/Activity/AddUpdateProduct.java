package com.example.shopapp.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.shopapp.Models.ProductPojo;
import com.example.shopapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AddUpdateProduct extends AppCompatActivity implements View.OnClickListener {
    Button addProduct_BT;
    EditText addProductName_ET, addProductPrice_ET;
    ProductPojo productPojo;
    DatabaseReference databaseReference;
    FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;
    long maxIdFood = 0;
    FirebaseAuth.AuthStateListener authStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_update_product);

        addProduct_BT = findViewById(R.id.addProduct_BT);
        addProductName_ET = findViewById(R.id.addProductName_ET);
        addProductPrice_ET = findViewById(R.id.addProductPrice_ET);

        addProduct_BT.setOnClickListener(this);
        productPojo = new ProductPojo();
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();

        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        String u_id = firebaseUser.getUid();

        databaseReference = firebaseDatabase.getReference().child("ShopApp").child("Shops").child(u_id).child("Products");
        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser currentUser = firebaseAuth.getCurrentUser();

                if (currentUser != null) {
                    Toast.makeText(getApplicationContext(), "succesfully", Toast.LENGTH_SHORT).show();
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
        switch (v.getId()){
            case R.id.addProduct_BT:
                productPojo.setPrice(addProductPrice_ET.getText().toString());
                productPojo.setProductName(addProductName_ET.getText().toString());
                productPojo.setId(String.valueOf(maxIdFood+1));
                productPojo.setImageUrl("");


                databaseReference.child(String.valueOf(maxIdFood+1)).setValue(productPojo);
                // databaseReference.push().setValue(member);
                //databaseReference.child("mem").setValue(member);


                Toast.makeText(AddUpdateProduct.this, "inserted succesfully", Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
package com.example.shopapp.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.example.shopapp.Adapters.MyCartAdapter;
import com.example.shopapp.Models.OrdersPojo;
import com.example.shopapp.R;
import com.example.shopapp.Utils.Config;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MyCartActivity extends AppCompatActivity {
    RecyclerView recycler_data;
    DatabaseReference databaseReference;
    ArrayList<OrdersPojo> userCartPojoArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_cart);
        recycler_data = findViewById(R.id.recycler_data);
        userCartPojoArrayList = new ArrayList<>();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("ShopApp").child("Users").child(Config.user_id).child("MyOrders");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    OrdersPojo pojo = dataSnapshot1.getValue(OrdersPojo.class);
                    Log.e("datafound", pojo.toString());
                    userCartPojoArrayList.add(pojo);
                }

                LinearLayoutManager linearLayoutManager3 = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
                recycler_data.setLayoutManager(linearLayoutManager3);
                MyCartAdapter shopListAdapter = new MyCartAdapter(getApplicationContext(), userCartPojoArrayList);
                recycler_data.setAdapter(shopListAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("errmsg", databaseError.getMessage());
            }
        });
    }
}
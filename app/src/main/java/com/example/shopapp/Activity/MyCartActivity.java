package com.example.shopapp.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.shopapp.AdapterClicks.ClickOnPosition;
import com.example.shopapp.Adapters.MyCartAdapter;
import com.example.shopapp.Models.OrdersPojo;
import com.example.shopapp.R;
import com.example.shopapp.Utils.Config;
import com.google.android.material.card.MaterialCardView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MyCartActivity extends AppCompatActivity implements ClickOnPosition {
    RecyclerView recycler_data;
    DatabaseReference databaseReference,databaseReferenceShopOrder;
    ArrayList<OrdersPojo> userCartPojoArrayList;
    MaterialCardView back_arrow_CV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_cart);
        recycler_data = findViewById(R.id.recycler_data);
        back_arrow_CV = findViewById(R.id.back_arrow_CV);
        back_arrow_CV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        databaseReference = FirebaseDatabase.getInstance().getReference().child("ShopApp").child("Users").child(Config.user_id).child("MyOrders");
        databaseReferenceShopOrder = FirebaseDatabase.getInstance().getReference().child("ShopApp").child("Shops");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userCartPojoArrayList = new ArrayList<>();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    OrdersPojo pojo = dataSnapshot1.getValue(OrdersPojo.class);
                    Log.e("datafound", pojo.toString());
                    userCartPojoArrayList.add(pojo);
                }

                LinearLayoutManager linearLayoutManager3 = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
                recycler_data.setLayoutManager(linearLayoutManager3);
                MyCartAdapter shopListAdapter = new MyCartAdapter(getApplicationContext(), userCartPojoArrayList,MyCartActivity.this);
                recycler_data.setAdapter(shopListAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("errmsg", databaseError.getMessage());
            }
        });
    }

    @Override
    public void onViewDetailsClick(int position) {
        databaseReference.child(userCartPojoArrayList.get(position).getOrderID()).removeValue();
        databaseReferenceShopOrder.child(userCartPojoArrayList.get(position).getShopId()).child("Orders").child(userCartPojoArrayList.get(position).getUserId()).child(userCartPojoArrayList.get(position).getOrderID()).removeValue();
        Toast.makeText(this, R.string.orderCancel, Toast.LENGTH_SHORT).show();
    }
}
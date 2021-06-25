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
import com.example.shopapp.Adapters.MyOrdersAdapter;
import com.example.shopapp.Adapters.ShopListAdapter;
import com.example.shopapp.Models.OrdersPojo;
import com.example.shopapp.Models.ShopsOrdersPojo;
import com.example.shopapp.Models.ShopsPojo;
import com.example.shopapp.R;
import com.example.shopapp.Utils.Config;
import com.google.android.material.card.MaterialCardView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MyOrdersActivity extends AppCompatActivity implements ClickOnPosition , View.OnClickListener {
    RecyclerView recycler_data;

    DatabaseReference databaseReference,databaseReferenceForOrders,databaseReferenceForUserUpdateOrder;
    ArrayList<OrdersPojo> ordersPojoArrayList;
    MaterialCardView back_arrow_CV;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_orders);
        back_arrow_CV = findViewById(R.id.back_arrow_CV);
        recycler_data=findViewById(R.id.recycler_data);
        back_arrow_CV.setOnClickListener(this);
        databaseReferenceForUserUpdateOrder= FirebaseDatabase.getInstance().getReference().child("ShopApp").child("Users");

        databaseReference= FirebaseDatabase.getInstance().getReference().child("ShopApp").child("Shops").child(Config.user_id).child("Orders");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ordersPojoArrayList=new ArrayList<OrdersPojo>();
                for (DataSnapshot dataSnapshot1:dataSnapshot.getChildren()) {
                    Log.e("id", dataSnapshot1.getKey().toString());
                    databaseReferenceForOrders= FirebaseDatabase.getInstance().getReference().child("ShopApp").child("Shops").child(Config.user_id).child("Orders").child(dataSnapshot1.getKey());
                    databaseReferenceForOrders.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {

                            for (DataSnapshot dataSnapshot2:snapshot.getChildren()) {
                                OrdersPojo pojo=dataSnapshot2.getValue(OrdersPojo.class);
                                Log.e("datafound", pojo.toString());
                                ordersPojoArrayList.add(pojo);
                            }
                            Log.e("datafound", ordersPojoArrayList.get(0).getCustomerName());
                            LinearLayoutManager linearLayoutManager3 = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
                            recycler_data.setLayoutManager(linearLayoutManager3);
                            MyOrdersAdapter myOrdersAdapter = new MyOrdersAdapter(getApplicationContext(),ordersPojoArrayList,MyOrdersActivity.this);
                            recycler_data.setAdapter(myOrdersAdapter);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("errmsg", databaseError.getMessage());
            }
        });


    }

    @Override
    public void onViewDetailsClick(int position) {
        databaseReferenceForUserUpdateOrder.child(ordersPojoArrayList.get(position).getUserId()).child("MyOrders").child(ordersPojoArrayList.get(position).getOrderID()).child("status").setValue("true");
        databaseReference.child(ordersPojoArrayList.get(position).getUserId()).child(ordersPojoArrayList.get(position).getOrderID()).child("status").setValue("true");
        Toast.makeText(this, R.string.orderDoneDone, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back_arrow_CV:
                onBackPressed();
                break;
        }
    }
}
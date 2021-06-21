package com.example.shopapp.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.example.shopapp.AdapterClicks.ClickOnPosition;
import com.example.shopapp.Adapters.ShopListAdapter;
import com.example.shopapp.Models.ShopsPojo;
import com.example.shopapp.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ShopListActivity extends AppCompatActivity implements ClickOnPosition {

    RecyclerView recycler_data;

    DatabaseReference databaseReference;
    ArrayList<ShopsPojo> shopsPojoArrayList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_list);
        shopsPojoArrayList=new ArrayList<ShopsPojo>();
        recycler_data=findViewById(R.id.recycler_data);

        databaseReference= FirebaseDatabase.getInstance().getReference().child("ShopApp").child("Shops");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1:dataSnapshot.getChildren())
                {
                    ShopsPojo pojo=dataSnapshot1.getValue(ShopsPojo.class);
                    Log.e("datafound", pojo.toString());
                    shopsPojoArrayList.add(pojo);


                }

                LinearLayoutManager linearLayoutManager3 = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
                recycler_data.setLayoutManager(linearLayoutManager3);
                ShopListAdapter shopListAdapter= new ShopListAdapter(getApplicationContext(),shopsPojoArrayList,ShopListActivity.this);
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
        Intent intent = new Intent(this,ProductListActivity.class);
        intent.putExtra("shopId", shopsPojoArrayList.get(position).getId());
        intent.putExtra("shopName", shopsPojoArrayList.get(position).getShopName());
        startActivity(intent);

        
    }
}
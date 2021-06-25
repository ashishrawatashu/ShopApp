package com.example.shopapp.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.shopapp.AdapterClicks.ClickOnPosition;
import com.example.shopapp.Adapters.ProductListAdapter;
import com.example.shopapp.Models.ProductPojo;
import com.example.shopapp.R;
import com.example.shopapp.Utils.Config;
import com.google.android.material.card.MaterialCardView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ShopProductListActivity extends AppCompatActivity implements View.OnClickListener, ClickOnPosition {
    MaterialCardView back_arrow_CV,add_CV;
    RecyclerView products_RV;
    ArrayList<ProductPojo> productPojoArrayList;
    DatabaseReference databaseReference;
    FirebaseDatabase firebaseDatabase;
    private final int ADD_PRODUCT = 200;

    String shopName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_product_list);
        Intent intent = getIntent();
        intent.putExtra("shopName",shopName);

        back_arrow_CV = findViewById(R.id.back_arrow_CV);
        products_RV=findViewById(R.id.products_RV);
        add_CV=findViewById(R.id.add_CV);
        add_CV.setOnClickListener(this);
        back_arrow_CV.setOnClickListener(this);
        firebaseDatabase = FirebaseDatabase.getInstance();

        allProductsReference();


    }

    private void allProductsReference() {

        databaseReference = firebaseDatabase.getReference().child("ShopApp").child("Shops").child(Config.user_id).child("Products");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                productPojoArrayList=new ArrayList<ProductPojo>();
                for (DataSnapshot dataSnapshot1:dataSnapshot.getChildren()) {
                    ProductPojo product=dataSnapshot1.getValue(ProductPojo.class);
                    Log.e("datafound", product.toString());
                    productPojoArrayList.add(product);
                }

                GridLayoutManager linearLayoutManager = new GridLayoutManager(ShopProductListActivity.this,2);
                products_RV.setLayoutManager(linearLayoutManager);
                ProductListAdapter productListAdapter = new ProductListAdapter(getApplicationContext(),productPojoArrayList,ShopProductListActivity.this);
                products_RV.setAdapter(productListAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("errmsg", databaseError.getMessage());
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back_arrow_CV:
                onBackPressed();
                break;
            case R.id.add_CV:
                Intent intent = new  Intent(ShopProductListActivity.this,AddUpdateProduct.class);
                intent.putExtra("type","ADD");
                intent.putExtra("shopName",shopName);
                startActivityForResult(intent, ADD_PRODUCT);

                break;
        }
    }
    @RequiresApi(api = Build.VERSION_CODES.FROYO)
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ADD_PRODUCT) {
            if (resultCode == RESULT_OK) {
              productPojoArrayList.clear();
              allProductsReference();
            }
        }
    }
    @Override
    public void onViewDetailsClick(int position) {
        Intent intent = new Intent(ShopProductListActivity.this,AddUpdateProduct.class);
        intent.putExtra("shopName",shopName);
        intent.putExtra("productName",productPojoArrayList.get(position).getProductName());
        intent.putExtra("price",productPojoArrayList.get(position).getPrice());
        intent.putExtra("imageUrl",productPojoArrayList.get(position).getImageUrl());
        intent.putExtra("id",productPojoArrayList.get(position).getId());
        intent.putExtra("type","UPDATE");
        startActivityForResult(intent, ADD_PRODUCT);

    }
}
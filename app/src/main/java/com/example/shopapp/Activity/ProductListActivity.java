package com.example.shopapp.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.shopapp.AdapterClicks.ClickOnPosition;
import com.example.shopapp.Adapters.ProductListAdapter;
import com.example.shopapp.Models.ProductPojo;
import com.example.shopapp.Models.OrdersPojo;
import com.example.shopapp.R;
import com.example.shopapp.Utils.Config;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ProductListActivity extends AppCompatActivity implements ClickOnPosition {
    RecyclerView recycler_data;

    DatabaseReference databaseReference, databaseReferenceOrders,databaseReferenceUserCart;
    ArrayList<ProductPojo> productPojoArrayList;
    FirebaseAuth.AuthStateListener authStateListener;
    FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;
    String u_id;
    Dialog placeOrder_dialog;
    OrdersPojo customerPojo;
    String shopId,shopName;
    long maxIdOrders = 0;
    long maxIdUserCart = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);
        recycler_data=findViewById(R.id.recycler_data);
        Intent intent = getIntent();
        shopId = intent.getStringExtra("shopId");
        shopName = intent.getStringExtra("shopName");

        Log.e("ShopId",shopId);
        productPojoArrayList=new ArrayList<ProductPojo>();

        firebaseAuth= FirebaseAuth.getInstance();
        firebaseDatabase=FirebaseDatabase.getInstance();
        FirebaseUser firebaseUser=firebaseAuth.getCurrentUser();
        u_id=firebaseUser.getUid();
        allProductsReference();
        allShopsOrdersReference();
        allUserCartReference();
    }

    private void allUserCartReference() {
        databaseReferenceUserCart= firebaseDatabase.getReference().child("ShopApp").child("Users").child(Config.user_id).child("MyOrders");
        databaseReferenceUserCart.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    maxIdUserCart = (dataSnapshot.getChildrenCount());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("Failed to read value.", databaseError.toException().toString());
            }
        });
    }

    private void allShopsOrdersReference() {
        databaseReferenceOrders= firebaseDatabase.getReference().child("ShopApp").child("Shops").child(shopId).child("Orders").child(Config.user_id);
        databaseReferenceOrders.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    maxIdOrders = (dataSnapshot.getChildrenCount());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("Failed to read value.", databaseError.toException().toString());
            }
        });
    }

    private void allProductsReference() {
        databaseReference= FirebaseDatabase.getInstance().getReference().child("ShopApp").child("Shops").child(u_id).child("Products");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1:dataSnapshot.getChildren()) {
                    ProductPojo product=dataSnapshot1.getValue(ProductPojo.class);
                    Log.e("datafound", product.toString());
                    productPojoArrayList.add(product);
                }

                LinearLayoutManager linearLayoutManager3 = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
                recycler_data.setLayoutManager(linearLayoutManager3);
                ProductListAdapter productListAdapter = new ProductListAdapter(getApplicationContext(),productPojoArrayList,ProductListActivity.this);
                recycler_data.setAdapter(productListAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("errmsg", databaseError.getMessage());
            }
        });
    }

    @Override
    public void onViewDetailsClick(int position) {
        Toast.makeText(this, productPojoArrayList.get(position).getProductName()+"=="+productPojoArrayList.get(position).getPrice(), Toast.LENGTH_SHORT).show();
        showPlaceOrderDialog(productPojoArrayList.get(position).getProductName(),productPojoArrayList.get(position).getPrice(),productPojoArrayList.get(position).getId());
    }

    private void showPlaceOrderDialog(String productName, String price, String id) {
        placeOrder_dialog = new Dialog(this);
        placeOrder_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        placeOrder_dialog.setContentView(R.layout.product_add_place_dialog);
        placeOrder_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        Button placeOrder_BT = placeOrder_dialog.findViewById(R.id.placeOrder_BT);
        EditText quantity_ET = placeOrder_dialog.findViewById(R.id.quantity_ET);

        placeOrder_BT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("CLICK","CLICK");
                customerPojo = new OrdersPojo();
                customerPojo.setCustomerName(Config.user_name);
                customerPojo.setPrice(price);
                customerPojo.setProductName(productName);
                customerPojo.setProductId(id);
                customerPojo.setStatus("false");
                customerPojo.setQuantity(quantity_ET.getText().toString().trim());
                customerPojo.setUserId(Config.user_id);
                databaseReferenceOrders.child(String.valueOf(maxIdOrders+1)).setValue(customerPojo);
                databaseReferenceUserCart.child(String.valueOf(maxIdUserCart+1)).setValue(customerPojo);

                Toast.makeText(ProductListActivity.this, "Added successfully ", Toast.LENGTH_SHORT).show();


            }
        });

        placeOrder_dialog.show();
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        Window window = placeOrder_dialog.getWindow();
        lp.copyFrom(window.getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        window.setAttributes(lp);

    }
}
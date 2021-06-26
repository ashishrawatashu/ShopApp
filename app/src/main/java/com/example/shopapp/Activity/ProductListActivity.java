package com.example.shopapp.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shopapp.AdapterClicks.ClickOnPosition;
import com.example.shopapp.Adapters.ProductListAdapter;
import com.example.shopapp.Models.ProductPojo;
import com.example.shopapp.Models.OrdersPojo;
import com.example.shopapp.R;
import com.example.shopapp.Utils.Config;
import com.example.shopapp.Utils.Methods;
import com.google.android.material.card.MaterialCardView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class ProductListActivity extends AppCompatActivity implements View.OnClickListener, ClickOnPosition {
    RecyclerView recycler_data;

    DatabaseReference databaseReference, databaseReferenceOrders, databaseReferenceUserCart;
    ArrayList<ProductPojo> productPojoArrayList;
    FirebaseAuth.AuthStateListener authStateListener;
    FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;
    String u_id;
    Dialog placeOrder_dialog;
    OrdersPojo customerPojo;
    String shopId, shopName;
    long maxIdOrders = 0;
    long maxIdUserCart = 0;
    MaterialCardView back_arrow_CV;
    TextView shop_name_TV;
    Methods methods;

    //dialog
    Button placeOrder_BT,cancel_BT;
    EditText quantity_ET, address_ET;
    String productId, productName,productPrice,productImageUrl;
    TextView date_TV,product_name_TV;
    DatePickerDialog picker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);
        recycler_data = findViewById(R.id.recycler_data);
        shop_name_TV = findViewById(R.id.shop_name_TV);
        back_arrow_CV = findViewById(R.id.back_arrow_CV);
        Intent intent = getIntent();
        methods = new Methods(this);
        back_arrow_CV.setOnClickListener(this);
        shopId = intent.getStringExtra("shopId");
        shopName = intent.getStringExtra("shopName");

        Log.e("ShopId", shopId);
        shop_name_TV.setText(shopName);
        productPojoArrayList = new ArrayList<ProductPojo>();

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        u_id = firebaseUser.getUid();
        allProductsReference();
        allShopsOrdersReference();
        allUserCartReference();
    }

    private void allUserCartReference() {
        databaseReferenceUserCart = firebaseDatabase.getReference().child("ShopApp").child("Users").child(Config.user_id).child("MyOrders");
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
        databaseReferenceOrders = firebaseDatabase.getReference().child("ShopApp").child("Shops").child(shopId).child("Orders").child(Config.user_id);
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
        databaseReference = FirebaseDatabase.getInstance().getReference().child("ShopApp").child("Shops").child(shopId).child("Products");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    ProductPojo product = dataSnapshot1.getValue(ProductPojo.class);
                    Log.e("datafound", product.toString());
                    productPojoArrayList.add(product);
                }

                GridLayoutManager linearLayoutManager = new GridLayoutManager(ProductListActivity.this, 2);
                recycler_data.setLayoutManager(linearLayoutManager);
                ProductListAdapter productListAdapter = new ProductListAdapter(getApplicationContext(), productPojoArrayList, ProductListActivity.this);
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
        productId = productPojoArrayList.get(position).getId();
        productImageUrl=productPojoArrayList.get(position).getImageUrl();
        productName = productPojoArrayList.get(position).getProductName();
        productPrice = productPojoArrayList.get(position).getPrice();
        showPlaceOrderDialog(productName,productPrice);

    }

    private void showPlaceOrderDialog(String productName, String productPrice) {
        placeOrder_dialog = new Dialog(this);
        placeOrder_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        placeOrder_dialog.setContentView(R.layout.product_add_place_dialog);
        placeOrder_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        placeOrder_BT = placeOrder_dialog.findViewById(R.id.placeOrder_BT);
        cancel_BT = placeOrder_dialog.findViewById(R.id.cancel_BT);
        product_name_TV = placeOrder_dialog.findViewById(R.id.product_name_TV);
        quantity_ET = placeOrder_dialog.findViewById(R.id.quantity_ET);
        address_ET = placeOrder_dialog.findViewById(R.id.address_ET);
        date_TV = placeOrder_dialog.findViewById(R.id.date_TV);
        date_TV.setOnClickListener(this);
        placeOrder_BT.setOnClickListener(this);
        cancel_BT.setOnClickListener(this);
        product_name_TV.setText(productName+getResources().getString(R.string.price)+productPrice);

        placeOrder_dialog.show();
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        Window window = placeOrder_dialog.getWindow();
        lp.copyFrom(window.getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        window.setAttributes(lp);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_arrow_CV:
                onBackPressed();
                break;

            case R.id.cancel_BT:
                placeOrder_dialog.dismiss();
                break;

            case R.id.date_TV:

                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);
                // date picker dialog
                picker = new DatePickerDialog(this,new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        date_TV.setText(Config.changeDateFormat(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year));
                    }
                }, year, month, day);
                picker.getDatePicker().setMinDate(System.currentTimeMillis());
                picker.show();

                break;

            case R.id.placeOrder_BT:

                if (TextUtils.isEmpty(quantity_ET.getText().toString())){
                    quantity_ET.setError(""+R.string.enterQuntity);
                    address_ET.setError(null);
                }else if (TextUtils.isEmpty(date_TV.getText().toString())){
                    Toast.makeText(this, ""+R.string.enterDate, Toast.LENGTH_SHORT).show();
                }else if (TextUtils.isEmpty(address_ET.getText().toString())){
                    address_ET.setError(""+R.string.enterAddress);
                    quantity_ET.setError(null);
                }else {
                    String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
                    String orderId = "Order_" + timeStamp + ":=>"+Config.user_id+":=>"+productId;
                    Log.e("CLICK", "CLICK");
                    customerPojo = new OrdersPojo();
                    customerPojo.setCustomerName(Config.user_name);
                    customerPojo.setPrice(productPrice);
                    customerPojo.setProductName(productName);
                    customerPojo.setProductId(productId);
                    customerPojo.setStatus("false");
                    customerPojo.setQuantity(quantity_ET.getText().toString().trim());
                    customerPojo.setUserId(Config.user_id);
                    customerPojo.setAddress(address_ET.getText().toString());
                    customerPojo.setOrderDate(date_TV.getText().toString());
                    customerPojo.setProductImageUrl(productImageUrl);
                    customerPojo.setShopName(shopName);
                    customerPojo.setShopId(shopId);
                    customerPojo.setOrderID(orderId);
                    databaseReferenceOrders.child(orderId).setValue(customerPojo);
                    databaseReferenceUserCart.child(orderId).setValue(customerPojo);

                    Toast.makeText(ProductListActivity.this, R.string.orderPlaced, Toast.LENGTH_SHORT).show();
                    placeOrder_dialog.dismiss();
                }

                break;
        }
    }
}
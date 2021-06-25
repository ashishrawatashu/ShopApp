package com.example.shopapp.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shopapp.AdapterClicks.ClickOnPosition;
import com.example.shopapp.Adapters.MyCartAdapter;
import com.example.shopapp.Adapters.ShopListAdapter;
import com.example.shopapp.Models.ShopsPojo;
import com.example.shopapp.Models.UserPojo;
import com.example.shopapp.R;
import com.example.shopapp.Utils.Config;
import com.example.shopapp.Utils.MediaUtils;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.util.ArrayList;

import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class ShopListActivity extends AppCompatActivity implements ClickOnPosition, View.OnClickListener {
    private final int MY_PERMISSIONS_REQUEST_READ_STORAGE = 300;

    RecyclerView recycler_data;
    Dialog addShopDialog;

    DatabaseReference databaseReferenceShopsList, databaseReferenceUsersData, databaseReferenceForShop;
    ArrayList<ShopsPojo> shopsPojoArrayList;
    MaterialCardView shop_CV, cart_CV;
    SharedPreferences.Editor login_editor;
    SharedPreferences sharedPreferences;
    TextInputEditText shopName_TIET, shopMail_TIET, shopPhone_TIET;
    Button cancel_BT;
    Button create_BT;
    FirebaseAuth firebaseAuth;
    FirebaseAuth.AuthStateListener authStateListener;
    ShopsPojo shopsPojo;
    UserPojo userPojo;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

    FirebaseDatabase firebaseDatabase;
    String u_id;
    boolean storageDialog = false;
    Dialog storagePermissionDialog;
    FloatingActionButton logout_FAB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_list);
        sharedPreferences = getSharedPreferences("userDetails", 0);
        Config.user_id = sharedPreferences.getString("userId", "");
        Config.user_email = sharedPreferences.getString("userMail", "");
        Config.user_phone = sharedPreferences.getString("userPhone", "");
        Config.user_name = sharedPreferences.getString("userName", "");
        Config.shopStatus = sharedPreferences.getString("shopStatus", "");
        checkStorageAndCameraPermission();

        recycler_data = findViewById(R.id.recycler_data);
        shop_CV = findViewById(R.id.shop_CV);
        logout_FAB = findViewById(R.id.logout_FAB);
        logout_FAB.setOnClickListener(this);
        cart_CV = findViewById(R.id.cart_CV);

        cart_CV.setOnClickListener(this);
        shop_CV.setOnClickListener(this);


        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        u_id = firebaseUser.getUid();
        userData();
        allShopsList();
        forAddShop();
        auth();


    }

    private void checkStorageAndCameraPermission() {
        if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, WRITE_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_READ_STORAGE);
            return;
        }
    }

    private void auth() {
        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser currentUser = firebaseAuth.getCurrentUser();

                if (currentUser != null) {
                    Log.e("DONE","DONE");
                } else {
                    Toast.makeText(getApplicationContext(), R.string.truagain, Toast.LENGTH_SHORT).show();
                }
            }
        };
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_STORAGE: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (storageDialog) {
                        storagePermissionDialog.dismiss();
                        storageDialog = false;
                        checkStorageAndCameraPermission();
                    }
                } else {
                    storageDialog = false;
                    showStoragePermissionDialog();
                }
                return;
            }

        }
    }

    private void showStoragePermissionDialog() {
        storageDialog = true;
        storagePermissionDialog = new Dialog(this);
        storagePermissionDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        storagePermissionDialog.setCancelable(false);
        storagePermissionDialog.setContentView(R.layout.storage_permission_dialog);
        storagePermissionDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        Button grant_permission_BT = storagePermissionDialog.findViewById(R.id.grant_permission_BT);
        storagePermissionDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        grant_permission_BT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkStorageAndCameraPermission();
            }
        });
        storagePermissionDialog.show();
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        Window window = storagePermissionDialog.getWindow();
        lp.copyFrom(window.getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        window.setAttributes(lp);
    }

    private void forAddShop() {
        databaseReferenceForShop = firebaseDatabase.getReference().child("ShopApp").child("Shops");
    }

    private void userData() {
        shop_CV.setEnabled(false);
        databaseReferenceUsersData = FirebaseDatabase.getInstance().getReference().child("ShopApp").child("Users").child(Config.user_id);
        databaseReferenceUsersData.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                UserPojo pojo = dataSnapshot.getValue(UserPojo.class);
                if (pojo == null) {
                    Toast.makeText(ShopListActivity.this, R.string.noData, Toast.LENGTH_SHORT).show();
                } else {
                    Log.e("datafound", pojo.toString());
                    login_editor = sharedPreferences.edit();
                    login_editor.putString("shopStatus", pojo.getShopStatus());
                    Config.shopStatus = pojo.getShopStatus();
                    Config.user_name = pojo.getUserName();
                    Config.user_email = pojo.getUserMail();
                    Config.user_phone = pojo.getUserPhone();
                    login_editor.apply();
                    shop_CV.setEnabled(true);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("errmsg", databaseError.getMessage());
            }
        });

    }

    private void allShopsList() {
        databaseReferenceShopsList = FirebaseDatabase.getInstance().getReference().child("ShopApp").child("Shops");
        databaseReferenceShopsList.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                shopsPojoArrayList = new ArrayList<ShopsPojo>();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {

                    ShopsPojo pojo = dataSnapshot1.getValue(ShopsPojo.class);
                    Log.e("datafound", pojo.toString());
                    shopsPojoArrayList.add(pojo);


                }

                GridLayoutManager linearLayoutManager = new GridLayoutManager(ShopListActivity.this, 2);
                recycler_data.setLayoutManager(linearLayoutManager);
                ShopListAdapter shopListAdapter = new ShopListAdapter(getApplicationContext(), shopsPojoArrayList, ShopListActivity.this);
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
        Intent intent = new Intent(this, ShopDetailsActivity.class);
        intent.putExtra("shopId", shopsPojoArrayList.get(position).getId());
        intent.putExtra("shopName", shopsPojoArrayList.get(position).getShopName());
        intent.putExtra("shopImageUrl", shopsPojoArrayList.get(position).getShopProfileImageUrl());
        intent.putExtra("shopMail", shopsPojoArrayList.get(position).getShopMail());
        intent.putExtra("shopPhone", shopsPojoArrayList.get(position).getShopPhone());
        intent.putExtra("lat", shopsPojoArrayList.get(position).getLat());
        intent.putExtra("log", shopsPojoArrayList.get(position).getLog());

        startActivity(intent);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.logout_FAB:
                SharedPreferences preferences = getSharedPreferences("userDetails", 0);
                SharedPreferences.Editor editor = preferences.edit();
                editor.clear();
                editor.apply();
                editor.commit();
                startActivity(new Intent(this, LoginActivity.class));
                finish();
                break;
            case R.id.shop_CV:
                if (Config.shopStatus.equals("true")) {
                    startActivity(new Intent(ShopListActivity.this, ShopActivity.class));
                } else {
                    androidx.appcompat.app.AlertDialog.Builder confirmationDialog = new androidx.appcompat.app.AlertDialog.Builder(this);
                    confirmationDialog.setTitle(R.string.hello + Config.user_name);
                    confirmationDialog.setMessage(R.string.haveAShop);
                    confirmationDialog.setCancelable(false);

                    confirmationDialog.setPositiveButton(
                            "Yes",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.dismiss();
                                    addShopDialogBox();
                                }
                            });
                    confirmationDialog.setNegativeButton(
                            "No",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });

                    androidx.appcompat.app.AlertDialog alert11 = confirmationDialog.create();
                    alert11.show();
                }
                break;

            case R.id.cart_CV:
                startActivity(new Intent(ShopListActivity.this, MyCartActivity.class));
                break;

            case R.id.create_BT:
                String shopName = shopName_TIET.getText().toString();
                String shopMail = shopMail_TIET.getText().toString();
                String shopPhone = shopPhone_TIET.getText().toString();

                shopsPojo = new ShopsPojo();
                if (TextUtils.isEmpty(shopName)) {
                    shopName_TIET.setError(""+R.string.enterShopNAme);
                    return;
                } else if (!shopMail.isEmpty() && !shopMail.matches(emailPattern)) {
                    shopMail_TIET.setError(""+R.string.ivaildEmail);
                    return;
                } else if (shopPhone.isEmpty()) {
                    shopPhone_TIET.setError(""+R.string.enterphone);
                    return;
                } else {
                    shopsPojo.setShopName(shopName);
                    shopsPojo.setLat("38.9637");
                    shopsPojo.setLog("35.2433");
                    shopsPojo.setId(u_id);
                    shopsPojo.setShopMail(shopMail);
                    shopsPojo.setShopPhone(shopPhone);
                    shopsPojo.setShopProfileImageUrl("Empty");

                    databaseReferenceForShop.child(u_id).setValue(shopsPojo);
                    Toast.makeText(ShopListActivity.this, R.string.shopCreated, Toast.LENGTH_SHORT).show();
                    addShopDialog.dismiss();
                    Config.shopStatus = "true";
                    updateShopStatus();
                }

                break;


            case R.id.cancel_BT:
                addShopDialog.dismiss();
                break;
        }

    }

    private void updateShopStatus() {
        userPojo = new UserPojo();
        userPojo.setUserId(Config.user_id);
        userPojo.setUserName(Config.user_name);
        userPojo.setUserPhone(Config.user_phone);
        userPojo.setUserMail(Config.user_email);
        userPojo.setShopStatus("true");
        databaseReferenceUsersData.setValue(userPojo);
    }

    private void addShopDialogBox() {
        addShopDialog = new Dialog(this);
        addShopDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        addShopDialog.setCancelable(false);
        addShopDialog.setContentView(R.layout.shop_info_dialog);
        addShopDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        cancel_BT = addShopDialog.findViewById(R.id.cancel_BT);
        create_BT = addShopDialog.findViewById(R.id.create_BT);
        cancel_BT.setOnClickListener(this);
        create_BT.setOnClickListener(this);
        shopName_TIET = addShopDialog.findViewById(R.id.shopName_TIET);
        shopPhone_TIET = addShopDialog.findViewById(R.id.shopPhone_TIET);
        shopMail_TIET = addShopDialog.findViewById(R.id.shopMail_TIET);

        addShopDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        cancel_BT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addShopDialog.dismiss();
            }
        });

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        Window window = addShopDialog.getWindow();
        lp.copyFrom(window.getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);
        addShopDialog.show();

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
    public void onBackPressed() {
        super.onBackPressed();
        finishAffinity();
    }
}
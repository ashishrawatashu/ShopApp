package com.example.shopapp.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.shopapp.Models.ShopsPojo;
import com.example.shopapp.Models.UserPojo;
import com.example.shopapp.R;
import com.example.shopapp.Utils.Config;
import com.example.shopapp.Utils.MediaUtils;
import com.example.shopapp.Utils.Methods;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.jetbrains.annotations.NotNull;

import java.io.File;

public class ShopActivity extends AppCompatActivity implements View.OnClickListener, MediaUtils.GetImg {

    EditText shopName_ET;
    ShopsPojo shopsPojo;
    MaterialCardView edit_profile_CV, back_arrow_CV, edit_image_CV, my_products_MCV, my_orders_MCV;
    FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReferenceShopData, databaseReferenceForShop;
    FirebaseAuth.AuthStateListener authStateListener;
    long maxIdFood = 0;
    String u_id;
    ImageView profile_image_IV;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

    TextView shopName, phone, email;
    MediaUtils mediaUtils;
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageReference = storage.getReference("ShopsProfileImages");
    Methods methods;

    //dialog
    Dialog addShopDialog;
    TextInputEditText shopName_TIET, shopMail_TIET, shopPhone_TIET;
    Button cancel_BT;
    Button create_BT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop);
//        shopName_ET = findViewById(R.id.shopName_ET);
        back_arrow_CV = findViewById(R.id.back_arrow_CV);
        my_products_MCV = findViewById(R.id.my_products_MCV);
        my_orders_MCV = findViewById(R.id.my_orders_MCV);
        shopName = findViewById(R.id.shopName);
        phone = findViewById(R.id.phone);
        email = findViewById(R.id.email);
        profile_image_IV = findViewById(R.id.profile_image_IV);
        edit_image_CV = findViewById(R.id.edit_image_CV);
        edit_profile_CV = findViewById(R.id.edit_profile_CV);
        edit_profile_CV.setOnClickListener(this);
        edit_image_CV.setOnClickListener(this);
        my_orders_MCV.setOnClickListener(this);
        my_products_MCV.setOnClickListener(this);
        back_arrow_CV.setOnClickListener(this);
        mediaUtils = new MediaUtils(this);
        methods = new Methods(this);


        getShopDetails();

        shopsPojo = new ShopsPojo();
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        databaseReferenceForShop = firebaseDatabase.getReference().child("ShopApp").child("Shops");

        u_id = firebaseUser.getUid();
        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser currentUser = firebaseAuth.getCurrentUser();

                if (currentUser != null) {
                } else {
                }
            }
        };
    }

    private void getShopDetails() {
        if (Config.shopStatus.equals("true")) {
            databaseReferenceShopData = FirebaseDatabase.getInstance().getReference().child("ShopApp").child("Shops").child(Config.user_id);
            databaseReferenceShopData.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    ShopsPojo pojo = dataSnapshot.getValue(ShopsPojo.class);
                    Log.e("datafound", pojo.toString());
                    email.setText(pojo.getShopMail());
                    phone.setText(pojo.getShopPhone());
                    shopName.setText(pojo.getShopName());
                    if (!pojo.getShopProfileImageUrl().equals("Empty")) {
                        Glide.with(getApplicationContext())
                                .load(pojo.getShopProfileImageUrl())
                                .placeholder(R.drawable.shop)
                                .into(profile_image_IV);
                    }
                    shopsPojo.setShopName(pojo.getShopName());
                    shopsPojo.setLat(pojo.getLat());
                    shopsPojo.setLog(pojo.getLog());
                    shopsPojo.setId(pojo.getId());
                    shopsPojo.setShopMail(pojo.getShopMail());
                    shopsPojo.setShopPhone(pojo.getShopPhone());


                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Log.e("errmsg", databaseError.getMessage());
                }
            });

        } else {

        }
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
            case R.id.edit_image_CV:
                mediaUtils.openGallery();

                break;

            case R.id.addShop_BT:
//                shopsPojo.setShopName(shopName_ET.getText().toString());
//                shopsPojo.setLat("22.449249");
//                shopsPojo.setLog("88.371628");
//                shopsPojo.setId(u_id);
//                databaseReference.setValue(shopsPojo);
//                Toast.makeText(ShopActivity.this, "inserted succesfully", Toast.LENGTH_SHORT).show();
                break;

            case R.id.my_products_MCV:
                Intent intent = new Intent(ShopActivity.this, ShopProductListActivity.class);
                intent.putExtra("shopName",shopsPojo.getShopName());
                startActivity(intent);

                break;

            case R.id.my_orders_MCV:
                startActivity(new Intent(ShopActivity.this, MyOrdersActivity.class));

                break;

            case R.id.back_arrow_CV:
                onBackPressed();
                break;

            case R.id.edit_profile_CV:
                addShopDialogBox();
                break;

            case R.id.create_BT:
                String shopName = shopName_TIET.getText().toString();
                String shopMail = shopMail_TIET.getText().toString();
                String shopPhone = shopPhone_TIET.getText().toString();
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
                    databaseReferenceForShop.child(u_id).child("shopName").setValue(shopName);
                    databaseReferenceForShop.child(u_id).child("shopPhone").setValue(shopPhone);
                    databaseReferenceForShop.child(u_id).child("shopMail").setValue(shopMail);
                    Toast.makeText(ShopActivity.this, R.string.shopUpadted, Toast.LENGTH_SHORT).show();
                    addShopDialog.dismiss();
                    Config.shopStatus = "true";
                    getShopDetails();
                }

                break;


            case R.id.cancel_BT:
                addShopDialog.dismiss();
                break;
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.FROYO)
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mediaUtils.onActivityResult(requestCode, resultCode, data);
    }


    private void UploadImages(Uri imgFile, File path) {
        methods.showCustomProgressBarDialog(this);
        if (imgFile != null) {
            StorageReference childRef = storageReference.child(path.getName());
            UploadTask uploadTask = childRef.putFile(imgFile);
            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    childRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            methods.customProgressDismiss();
                            shopsPojo.setShopProfileImageUrl(uri.toString());
                            databaseReferenceForShop.child(u_id).child("shopProfileImageUrl").setValue(uri.toString());
                            Toast.makeText(ShopActivity.this, R.string.uploaded, Toast.LENGTH_SHORT).show();
                            Glide.with(ShopActivity.this)
                                    .load(uri)
                                    .placeholder(R.drawable.shop)
                                    .into(profile_image_IV);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull @NotNull Exception e) {
                            methods.customProgressDismiss();
                            Toast.makeText(ShopActivity.this, R.string.failed, Toast.LENGTH_SHORT).show();

                        }
                    });

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull @NotNull Exception e) {
                    Toast.makeText(ShopActivity.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();

                }
            });
        } else {
            Toast.makeText(ShopActivity.this, R.string.selectImage, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void imgdata(Uri imgPath, String path) {
        File imgFile = new File(path);
        Log.d("imgdata: ", imgFile.toString());
        UploadImages(imgPath, imgFile);
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
        create_BT.setText("UPDATE");
        shopName_TIET.setText(shopsPojo.getShopName());
        shopMail_TIET.setText(shopsPojo.getShopMail());
        shopPhone_TIET.setText(shopsPojo.getShopPhone());
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

}
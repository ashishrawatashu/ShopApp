package com.example.shopapp.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.shopapp.AdapterClicks.ClickOnPosition;
import com.example.shopapp.Adapters.ProductListAdapter;
import com.example.shopapp.Models.ProductPojo;
import com.example.shopapp.R;
import com.example.shopapp.Utils.Config;
import com.example.shopapp.Utils.MediaUtils;
import com.example.shopapp.Utils.Methods;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.card.MaterialCardView;
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
import java.util.ArrayList;

public class AddUpdateProduct extends AppCompatActivity implements View.OnClickListener, ClickOnPosition, MediaUtils.GetImg {
    Button addProduct_BT;
    EditText addProductName_ET, addProductPrice_ET;
    ProductPojo productPojo;
    DatabaseReference databaseReference;
    FirebaseDatabase firebaseDatabase;
    long maxIdFood = 0;
    MaterialCardView back_arrow_CV,delete_CV;
    String type, id, productName, price, imageUrl,shopName;
    ImageView product_image_IV,change_image_IV;

    MediaUtils mediaUtils;
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageReference = storage.getReference("Products");
    Methods methods;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_update_product);
        Intent intent = getIntent();
        type = intent.getStringExtra("type");
        shopName = intent.getStringExtra("shopName");

        mediaUtils = new MediaUtils(this);
        methods = new Methods(this);

        addProduct_BT = findViewById(R.id.addProduct_BT);
        addProductName_ET = findViewById(R.id.addProductName_ET);
        addProductPrice_ET = findViewById(R.id.addProductPrice_ET);
        back_arrow_CV = findViewById(R.id.back_arrow_CV);
        product_image_IV = findViewById(R.id.product_image_IV);
        change_image_IV = findViewById(R.id.change_image_IV);
        delete_CV = findViewById(R.id.delete_CV);
        delete_CV.setOnClickListener(this);
        change_image_IV.setOnClickListener(this);
        back_arrow_CV.setOnClickListener(this);
        addProduct_BT.setOnClickListener(this);
        productPojo = new ProductPojo();
        firebaseDatabase = FirebaseDatabase.getInstance();


        if (type.equals("UPDATE")) {
            delete_CV.setVisibility(View.VISIBLE);
            id = intent.getStringExtra("id");
            productName = intent.getStringExtra("productName");
            imageUrl = intent.getStringExtra("imageUrl");
            price = intent.getStringExtra("price");
            setValues();

        }else {
            delete_CV.setVisibility(View.GONE);
        }


        databaseReference = firebaseDatabase.getReference().child("ShopApp").child("Shops").child(Config.user_id).child("Products");
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

    private void setValues() {
        addProduct_BT.setText("Update Product");
        addProductName_ET.setText(productName);
        addProductPrice_ET.setText(price);
        if (!imageUrl.equals("Empty")) {
            Glide.with(AddUpdateProduct.this)
                    .load(imageUrl)
                    .placeholder(R.drawable.shop_logo)
                    .into(product_image_IV);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.addProduct_BT:
                String productName = addProductName_ET.getText().toString();
                String price = addProductPrice_ET.getText().toString();

                if (TextUtils.isEmpty(productName)){
                    addProductName_ET.setError(""+R.string.enterName);
                    addProductPrice_ET.setError(null);
                }else if (TextUtils.isEmpty(price)){
                    addProductPrice_ET.setError(""+R.string.enterPrice);
                    addProductName_ET.setError(null);
                }else {
                    if (type.equals("UPDATE")) {
                        productPojo.setId(id);
                        productPojo.setPrice(addProductPrice_ET.getText().toString());
                        productPojo.setProductName(addProductName_ET.getText().toString());
                        productPojo.setShopName(shopName);
                        databaseReference.child(id).setValue(productPojo);
                        Toast.makeText(AddUpdateProduct.this, R.string.productAdded, Toast.LENGTH_SHORT).show();
                        setResult(RESULT_OK);
                        finish();

                    } else {
                        productPojo.setPrice(addProductPrice_ET.getText().toString());
                        productPojo.setProductName(addProductName_ET.getText().toString());
                        productPojo.setId(String.valueOf(maxIdFood + 1));
                        productPojo.setShopName(shopName);
                        databaseReference.child(String.valueOf(maxIdFood + 1)).setValue(productPojo);
                        Toast.makeText(AddUpdateProduct.this, R.string.productAdded, Toast.LENGTH_SHORT).show();
                        setResult(RESULT_OK);
                        finish();

                    }
                }





                break;

            case R.id.back_arrow_CV:
                onBackPressed();
                break;

            case R.id.change_image_IV:
                mediaUtils.openGallery();
                break;

            case R.id.delete_CV:
                databaseReference.child(id).removeValue();
                Toast.makeText(AddUpdateProduct.this, R.string.productDeleted, Toast.LENGTH_SHORT).show();
                setResult(RESULT_OK);
                finish();
                break;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.FROYO)
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mediaUtils.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onViewDetailsClick(int position) {

    }

    @Override
    public void imgdata(Uri imgPath, String path) {
        File imgFile = new File(path);
        Log.d("imgdata: ", imgFile.toString());
        UploadImages(imgPath, imgFile);
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
                            Glide.with(AddUpdateProduct.this)
                                    .load(uri)
                                    .placeholder(R.drawable.shop_logo)
                                    .into(product_image_IV);
                            productPojo.setImageUrl(uri.toString());
                            Toast.makeText(AddUpdateProduct.this, R.string.updated, Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull @NotNull Exception e) {
                            methods.customProgressDismiss();
                            Toast.makeText(AddUpdateProduct.this, R.string.failed, Toast.LENGTH_SHORT).show();

                        }
                    });

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull @NotNull Exception e) {
                    Toast.makeText(AddUpdateProduct.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(AddUpdateProduct.this, R.string.selectImage, Toast.LENGTH_SHORT).show();
        }
    }
}
package com.example.shopapp.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.shopapp.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.card.MaterialCardView;

public class ShopDetailsActivity extends AppCompatActivity implements View.OnClickListener {
    MaterialCardView back_arrow_CV;
    ImageView profile_image_IV;
    TextView shopName, phone, email;
    Button show_products_BT;
    String strShopName, strShopPhone, strShopEmail, strShopId, strShopLat, strShopLog,strShopImageUrl;
    GoogleMap googleMap;
    MapFragment mapFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_details);
        Intent intent = getIntent();
        strShopId = intent.getStringExtra("shopId");
        strShopName = intent.getStringExtra("shopName");
        strShopImageUrl = intent.getStringExtra("shopImageUrl");
        strShopPhone=intent.getStringExtra("shopPhone");
        strShopEmail = intent.getStringExtra("shopMail");
        strShopLat = intent.getStringExtra("lat");
        strShopLog = intent.getStringExtra("log");



        back_arrow_CV = findViewById(R.id.back_arrow_CV);
        shopName = findViewById(R.id.shopName);
        phone = findViewById(R.id.phone);
        email = findViewById(R.id.email);
        profile_image_IV = findViewById(R.id.profile_image_IV);
        show_products_BT = findViewById(R.id.show_products_BT);
        show_products_BT.setOnClickListener(this);
        back_arrow_CV.setOnClickListener(this);
        final double lat = Double.parseDouble(strShopLat);
        final double log = Double.parseDouble(strShopLog);
        SupportMapFragment mapFragment;
                mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map_fragment);
        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                googleMap.clear();
                LatLng sydney = new LatLng(lat, log);
                googleMap.addMarker(new MarkerOptions().position(sydney).title("Click To Navigate").snippet(strShopName));

                googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));

                googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            }
        });
        shopName.setText(strShopName);
        email.setText(strShopEmail);
        phone.setText(strShopPhone);
        if (!strShopImageUrl.equals("Empty")){
            Glide.with(getApplicationContext())
                    .load(strShopImageUrl)
                    .placeholder(R.drawable.shop)
                    .into(profile_image_IV);
        }



    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back_arrow_CV:
                onBackPressed();
                break;

            case R.id.show_products_BT:
                Intent intent = new Intent(this, ProductListActivity.class);
                intent.putExtra("shopId",strShopId);
                intent.putExtra("shopName", strShopName);
                startActivity(intent);
                break;

        }
    }
}
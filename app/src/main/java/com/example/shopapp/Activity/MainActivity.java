package com.example.shopapp.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.shopapp.R;
import com.example.shopapp.Utils.Config;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    Button addShop_BT, showShop_BT,showCart_BT;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SharedPreferences sharedPreferences = getSharedPreferences("userDetails", 0);
        Config.user_id = sharedPreferences.getString("userId", "");
        Config.user_email = sharedPreferences.getString("userMail", "");
        Config.user_phone = sharedPreferences.getString("userPhone", "");
        Config.user_name = sharedPreferences.getString("userName", "");

        addShop_BT=findViewById(R.id.addShop_BT);
        showShop_BT=findViewById(R.id.showShop_BT);
        showCart_BT=findViewById(R.id.showCart_BT);

        showCart_BT.setOnClickListener(this);
        addShop_BT.setOnClickListener(this);
        showShop_BT.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.addShop_BT:
                startActivity(new Intent(MainActivity.this,ShopActivity.class));
                break;

            case R.id.showShop_BT:
                startActivity(new Intent(MainActivity.this,ShopListActivity.class));
                break;

            case R.id.showCart_BT:
                startActivity(new Intent(MainActivity.this,MyCartActivity.class));
                break;
        }

    }
}
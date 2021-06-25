package com.example.shopapp.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.shopapp.AdapterClicks.ClickOnPosition;
import com.example.shopapp.Models.OrdersPojo;
import com.example.shopapp.R;

import java.util.ArrayList;

public class MyCartAdapter extends RecyclerView.Adapter<MyCartAdapter.MyViewHolder> {

    private Context context;
    private ArrayList<OrdersPojo> userCartPojoArrayList;
    ClickOnPosition clickOnPosition;

    public MyCartAdapter(Context context, ArrayList<OrdersPojo> userCartPojoArrayList, ClickOnPosition clickOnPosition) {

        this.context = context;
        this.userCartPojoArrayList = userCartPojoArrayList;
        this.clickOnPosition = clickOnPosition;

    }


    @Override
    public MyCartAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.my_cart_layout, viewGroup, false);
        // set the view's size, margins, paddings and layout parameters
        MyCartAdapter.MyViewHolder vh = new MyCartAdapter.MyViewHolder(v); // pass the view to View Holder
        return vh;
    }

    public void onBindViewHolder(@NonNull final MyCartAdapter.MyViewHolder myViewHolder, final int i) {


        int price = Integer.parseInt(userCartPojoArrayList.get(i).getPrice());
        int quantity = Integer.parseInt(userCartPojoArrayList.get(i).getQuantity());
        int totalPrice = price * quantity;

        myViewHolder.productName_TV.setText(userCartPojoArrayList.get(i).getProductName());
        myViewHolder.quantity_TV.setText(R.string.quantity + userCartPojoArrayList.get(i).getQuantity());
        myViewHolder.shopName_TV.setText(R.string.seller + userCartPojoArrayList.get(i).getShopName());
        myViewHolder.totalPrice_TV.setText(String.valueOf(R.string.myTotalPrice) +totalPrice);
        if (userCartPojoArrayList.get(i).getStatus().equals("true")) {
            myViewHolder.status_TV.setText(R.string.orderDone);
            myViewHolder.cancel_TV.setVisibility(View.GONE);
        } else {
            myViewHolder.status_TV.setText(R.string.orderPending);
            myViewHolder.cancel_TV.setVisibility(View.VISIBLE);

        }

        if (!userCartPojoArrayList.get(i).getProductImageUrl().equals("Empty")) {
            Glide.with(context)
                    .load(userCartPojoArrayList.get(i).getProductImageUrl())
                    .placeholder(R.drawable.shop_logo)
                    .into(myViewHolder.product_image_IV);
        }

    }

    @Override
    public int getItemCount() {
        return userCartPojoArrayList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView productName_TV, quantity_TV, totalPrice_TV, status_TV, cancel_TV, shopName_TV;
        ImageView product_image_IV;

        MyViewHolder(@NonNull View itemView) {
            super(itemView);
            productName_TV = itemView.findViewById(R.id.productName_TV);
            quantity_TV = itemView.findViewById(R.id.quantity_TV);
            totalPrice_TV = itemView.findViewById(R.id.totalPrice_TV);
            status_TV = itemView.findViewById(R.id.status_TV);
            product_image_IV = itemView.findViewById(R.id.product_image_IV);
            cancel_TV = itemView.findViewById(R.id.cancel_TV);
            shopName_TV = itemView.findViewById(R.id.shopName_TV);

            cancel_TV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (clickOnPosition != null) {
                        clickOnPosition.onViewDetailsClick(getAdapterPosition());
                    }

                }
            });

        }
    }
}
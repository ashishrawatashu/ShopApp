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

public class MyOrdersAdapter extends RecyclerView.Adapter<MyOrdersAdapter.MyViewHolder> {

    private Context context;
    private ArrayList<OrdersPojo> shopsPojoArrayList;
    ClickOnPosition clickOnPosition;

    public MyOrdersAdapter(Context context, ArrayList<OrdersPojo> shopsPojoArrayList, ClickOnPosition clickOnPosition) {

        this.context = context;
        this.shopsPojoArrayList = shopsPojoArrayList;
        this.clickOnPosition = clickOnPosition;

    }


    @Override
    public MyOrdersAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.my_orders_layout, viewGroup, false);
        // set the view's size, margins, paddings and layout parameters
        MyOrdersAdapter.MyViewHolder vh = new MyOrdersAdapter.MyViewHolder(v); // pass the view to View Holder
        return vh;
    }

    public void onBindViewHolder(@NonNull final MyOrdersAdapter.MyViewHolder myViewHolder, final int i) {

        int price = Integer.parseInt(shopsPojoArrayList.get(i).getPrice());
        int quantity = Integer.parseInt(shopsPojoArrayList.get(i).getQuantity());
        int totalPrice = price*quantity;


        myViewHolder.productName_TV.setText(shopsPojoArrayList.get(i).getProductName());
        myViewHolder.quantity_TV.setText(R.string.quantity + shopsPojoArrayList.get(i).getQuantity());
        myViewHolder.shopName_TV.setText(R.string.custumerName + shopsPojoArrayList.get(i).getCustomerName());
        myViewHolder.totalPrice_TV.setText(String.valueOf(R.string.myTotalPrice) +totalPrice);
        if (shopsPojoArrayList.get(i).getStatus().equals("true")){
            myViewHolder.status_TV.setText(R.string.orderDone);
            myViewHolder.click_here_TV.setVisibility(View.GONE);
        }else {
            myViewHolder.status_TV.setText(R.string.orderPending);
            myViewHolder.click_here_TV.setVisibility(View.VISIBLE);

        }

        if (!shopsPojoArrayList.get(i).getProductImageUrl().equals("Empty")) {
            Glide.with(context)
                    .load(shopsPojoArrayList.get(i).getProductImageUrl())
                    .placeholder(R.drawable.shop_logo)
                    .into(myViewHolder.product_image_IV);
        }
    }

    @Override
    public int getItemCount() {
        return shopsPojoArrayList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView productName_TV, quantity_TV, totalPrice_TV, status_TV, click_here_TV, shopName_TV;
        ImageView product_image_IV;
        MyViewHolder(@NonNull View itemView) {
            super(itemView);
            productName_TV = itemView.findViewById(R.id.productName_TV);
            quantity_TV = itemView.findViewById(R.id.quantity_TV);
            totalPrice_TV = itemView.findViewById(R.id.totalPrice_TV);
            status_TV = itemView.findViewById(R.id.status_TV);
            click_here_TV = itemView.findViewById(R.id.click_here_TV);
            shopName_TV = itemView.findViewById(R.id.shopName_TV);
            product_image_IV = itemView.findViewById(R.id.product_image_IV);
            click_here_TV.setOnClickListener(new View.OnClickListener() {
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
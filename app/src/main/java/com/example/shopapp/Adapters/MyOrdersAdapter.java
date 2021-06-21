package com.example.shopapp.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

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
        myViewHolder.quantity_TV.setText(""+totalPrice);
        myViewHolder.totalPrice_TV.setText(shopsPojoArrayList.get(i).getProductName());
        if (shopsPojoArrayList.get(i).getStatus().equals("true")){
            myViewHolder.status_TV.setText("Order Delivered");
        }else {
            myViewHolder.status_TV.setText("Order Pending");
        }


    }

    @Override
    public int getItemCount() {
        return shopsPojoArrayList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView productName_TV,quantity_TV,totalPrice_TV,status_TV;

        MyViewHolder(@NonNull View itemView) {
            super(itemView);
            productName_TV = itemView.findViewById(R.id.productName_TV);
            quantity_TV = itemView.findViewById(R.id.quantity_TV);
            totalPrice_TV = itemView.findViewById(R.id.totalPrice_TV);
            status_TV = itemView.findViewById(R.id.status_TV);
            itemView.setOnClickListener(new View.OnClickListener() {
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
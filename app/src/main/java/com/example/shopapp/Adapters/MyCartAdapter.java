package com.example.shopapp.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shopapp.Models.OrdersPojo;
import com.example.shopapp.R;

import java.util.ArrayList;

public class MyCartAdapter extends RecyclerView.Adapter<MyCartAdapter.MyViewHolder> {

    private Context context;
    private ArrayList<OrdersPojo> userCartPojoArrayList;

    public MyCartAdapter(Context context, ArrayList<OrdersPojo> userCartPojoArrayList) {

        this.context = context;
        this.userCartPojoArrayList = userCartPojoArrayList;

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
        int totalPrice = price*quantity;

        myViewHolder.productName_TV.setText(userCartPojoArrayList.get(i).getProductName());
        myViewHolder.quantity_TV.setText(""+totalPrice);
        myViewHolder.totalPrice_TV.setText(userCartPojoArrayList.get(i).getProductName());
        if (userCartPojoArrayList.get(i).getStatus().equals("true")){
            myViewHolder.status_TV.setText("Order Delivered");
        }else {
            myViewHolder.status_TV.setText("Order Pending");
        }

    }

    @Override
    public int getItemCount() {
        return userCartPojoArrayList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView productName_TV,quantity_TV,totalPrice_TV,status_TV;

        MyViewHolder(@NonNull View itemView) {
            super(itemView);
            productName_TV = itemView.findViewById(R.id.productName_TV);
            quantity_TV = itemView.findViewById(R.id.quantity_TV);
            totalPrice_TV = itemView.findViewById(R.id.totalPrice_TV);
            status_TV = itemView.findViewById(R.id.status_TV);

        }
    }
}
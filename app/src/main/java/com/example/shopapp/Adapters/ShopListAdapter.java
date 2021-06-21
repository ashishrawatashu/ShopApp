package com.example.shopapp.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shopapp.AdapterClicks.ClickOnPosition;
import com.example.shopapp.Models.ShopsPojo;
import com.example.shopapp.R;

import java.util.ArrayList;

public class ShopListAdapter extends RecyclerView.Adapter<ShopListAdapter.MyViewHolder> {

    private Context context;
    private ArrayList<ShopsPojo> shopsPojoArrayList;
    ClickOnPosition clickOnPosition;

    public ShopListAdapter(Context context, ArrayList<ShopsPojo> shopsPojoArrayList,ClickOnPosition clickOnPosition) {

        this.context = context;
        this.shopsPojoArrayList = shopsPojoArrayList;
        this.clickOnPosition = clickOnPosition;

    }


    @Override
    public ShopListAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.shop_list_layout, viewGroup, false);
        // set the view's size, margins, paddings and layout parameters
        ShopListAdapter.MyViewHolder vh = new ShopListAdapter.MyViewHolder(v); // pass the view to View Holder
        return vh;
    }

    public void onBindViewHolder(@NonNull final ShopListAdapter.MyViewHolder myViewHolder, final int i) {


        myViewHolder.shopName_TV.setText(shopsPojoArrayList.get(i).getShopName());

    }

    @Override
    public int getItemCount() {
        return shopsPojoArrayList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView shopName_TV;

        MyViewHolder(@NonNull View itemView) {
            super(itemView);
            shopName_TV = itemView.findViewById(R.id.shopName_TV);

            shopName_TV.setOnClickListener(new View.OnClickListener() {
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
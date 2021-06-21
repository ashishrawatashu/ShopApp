package com.example.shopapp.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shopapp.AdapterClicks.ClickOnPosition;
import com.example.shopapp.Models.ProductPojo;
import com.example.shopapp.Models.ShopsPojo;
import com.example.shopapp.R;

import java.util.ArrayList;

public class ProductListAdapter extends RecyclerView.Adapter<ProductListAdapter.MyViewHolder> {

    private Context context;
    private ArrayList<ProductPojo> productPojoArrayList;
    ClickOnPosition clickOnPosition;

    public ProductListAdapter(Context context, ArrayList<ProductPojo> productPojoArrayList,ClickOnPosition clickOnPosition) {

        this.context = context;
        this.productPojoArrayList = productPojoArrayList;
        this.clickOnPosition = clickOnPosition;

    }


    @Override
    public ProductListAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.shop_list_layout, viewGroup, false);
        // set the view's size, margins, paddings and layout parameters
        ProductListAdapter.MyViewHolder vh = new ProductListAdapter.MyViewHolder(v); // pass the view to View Holder
        return vh;
    }

    public void onBindViewHolder(@NonNull final ProductListAdapter.MyViewHolder myViewHolder, final int i) {


        myViewHolder.shopName_TV.setText(productPojoArrayList.get(i).getProductName());

    }

    @Override
    public int getItemCount() {
        return productPojoArrayList.size();
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
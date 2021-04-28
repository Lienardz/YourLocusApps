package com.thelocus.yourlocusapps.ViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.thelocus.yourlocusapps.Interface.StockClickListener;
import com.thelocus.yourlocusapps.R;


import androidx.recyclerview.widget.RecyclerView;

public class Stockview extends RecyclerView.ViewHolder implements View.OnClickListener
{
    public TextView txtstockname, txtstockdescription, txtprice;
    public ImageView viewImage;
    public StockClickListener listener;

    public Stockview(View itemView)
    {
        super(itemView);


        viewImage = (ImageView) itemView.findViewById(R.id.addImage);
        txtstockname = (TextView) itemView.findViewById(R.id.productsname);
        txtstockdescription = (TextView) itemView.findViewById(R.id.productsdescription);
        txtprice = (TextView) itemView.findViewById(R.id.productsprice);

    }

    public void setStockClickListener(StockClickListener listener)
    {
        this.listener = listener;
    }



    @Override
    public void onClick(View view)
    {
        listener.onClick(view, getAdapterPosition(), false);
    }
}

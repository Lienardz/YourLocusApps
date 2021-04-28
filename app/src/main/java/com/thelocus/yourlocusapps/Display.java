package com.thelocus.yourlocusapps;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;
import com.thelocus.yourlocusapps.Model.Stock;
import com.thelocus.yourlocusapps.ViewHolder.Stockview;

public class Display extends AppCompatActivity {

    private DatabaseReference stockRef;
    private RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display);

        recyclerView = findViewById(R.id.menurecycler);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        stockRef = FirebaseDatabase.getInstance().getReference().child("Stock");

    }

    @Override
    protected void onStart()
    {
        super.onStart();
        FirebaseRecyclerOptions<Stock> options = new FirebaseRecyclerOptions.Builder<Stock>().setQuery(stockRef, Stock.class).build();

        FirebaseRecyclerAdapter<Stock, Stockview> adapter = new FirebaseRecyclerAdapter<Stock, Stockview>(options) {
            @Override
            protected void onBindViewHolder(@NonNull Stockview holder, int position, @NonNull Stock model)
            {
                holder.txtstockname.setText(model.getPname());
                holder.txtstockdescription.setText(model.getDescription());
                holder.txtprice.setText("Price = Rp." + model.getPrice());
                Picasso.get().load(model.getImage()).into(holder.viewImage);
            }

            @NonNull
            @Override
            public Stockview onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
            {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.stock_items_layout, parent, false);
                Stockview holder = new Stockview(view);
                return holder;
            }
        };
        recyclerView.setAdapter(adapter);
        adapter.startListening();


    }
}
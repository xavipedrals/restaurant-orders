package com.example.xavi.comandesidi.LlistarComandes;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.xavi.comandesidi.LlistarComandes.ComandaItemFragment.OnListFragmentInteractionListener;
import com.example.xavi.comandesidi.R;
import com.example.xavi.comandesidi.DBWrappers.OrderContainer;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;


public class MyComandaItemRecyclerViewAdapter extends RecyclerView.Adapter<MyComandaItemRecyclerViewAdapter.ViewHolder> {

    private final List<OrderContainer.Order> orderList;
    private final OnListFragmentInteractionListener mListener;
    private List<ViewHolder> viewHolderList;

    public double getTotalPrice(){
        double price = 0;
        for(ViewHolder holder: viewHolderList){
            price += holder.order.getPrice();
        }
        return price;
    }

    public MyComandaItemRecyclerViewAdapter(List<OrderContainer.Order> items, OnListFragmentInteractionListener listener) {
        orderList = items;
        mListener = listener;
        viewHolderList = new ArrayList<>();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_comandaitem, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.order = orderList.get(position);
        DecimalFormat decimalFormat = new DecimalFormat("#.##");
        String price = decimalFormat.format(orderList.get(position).getPrice());
        String priceText = price + " €";
        holder.priceTv.setText(priceText);
        holder.numTaulaTv.setText(String.valueOf(orderList.get(position).getTableNum()));
        holder.diaTv.setText(String.valueOf(orderList.get(position).getDay()));
        holder.horaTv.setText(String.valueOf(orderList.get(position).getHour()));
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(holder.order);
                }
            }
        });
        viewHolderList.add(holder);
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView numTaulaTv;
        public final TextView priceTv;
        public final TextView diaTv;
        public final TextView horaTv;
        public OrderContainer.Order order;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            numTaulaTv = (TextView) view.findViewById(R.id.numTableTv);
            diaTv = (TextView) view.findViewById(R.id.textViewDia);
            horaTv = (TextView) view.findViewById(R.id.textViewHora);
            priceTv = (TextView) view.findViewById(R.id.comandaPriceTv);
        }

    }
}

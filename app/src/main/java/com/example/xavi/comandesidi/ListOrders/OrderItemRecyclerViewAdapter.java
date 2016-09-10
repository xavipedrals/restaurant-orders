package com.example.xavi.comandesidi.ListOrders;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.xavi.comandesidi.ListOrders.OrderItemFragment.OnListFragmentInteractionListener;
import com.example.xavi.comandesidi.R;
import com.example.xavi.comandesidi.DBWrappers.OrderContainer;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;


public class OrderItemRecyclerViewAdapter extends RecyclerView.Adapter<OrderItemRecyclerViewAdapter.ViewHolder> {

    private final List<OrderContainer.Order> orderList;
    private final OnListFragmentInteractionListener mListener;
    private List<ViewHolder> viewHolderList;

    public double getTotalPrice(){
        double price = 0;
        for(ViewHolder holder: viewHolderList){
            price += holder.order.price;
        }
        return price;
    }

    public OrderItemRecyclerViewAdapter(List<OrderContainer.Order> items, OnListFragmentInteractionListener listener) {
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
        setViewHolderData(holder, position);
        setHolderOnClickListener(holder);
        viewHolderList.add(holder);
    }

    private void setViewHolderData(final ViewHolder holder, int position) {
        holder.order = orderList.get(position);
        DecimalFormat decimalFormat = new DecimalFormat("#.##");
        String price = decimalFormat.format(orderList.get(position).price);
        String priceText = price + " €";

        holder.priceTv.setText(priceText);
        holder.tableNumberTv.setText(String.valueOf(orderList.get(position).tableNum));
        holder.dateTv.setText(String.valueOf(orderList.get(position).getDay()));
        holder.horaTv.setText(String.valueOf(orderList.get(position).getHour()));
    }

    private void setHolderOnClickListener(final ViewHolder holder){
        holder.containerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    mListener.onListFragmentInteraction(holder.order);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View containerView;
        public final TextView tableNumberTv;
        public final TextView priceTv;
        public final TextView dateTv;
        public final TextView horaTv;
        public OrderContainer.Order order;

        public ViewHolder(View view) {
            super(view);
            containerView = view;
            tableNumberTv = (TextView) view.findViewById(R.id.numTableTv);
            dateTv = (TextView) view.findViewById(R.id.textViewDia);
            horaTv = (TextView) view.findViewById(R.id.textViewHora);
            priceTv = (TextView) view.findViewById(R.id.comandaPriceTv);
        }

    }
}

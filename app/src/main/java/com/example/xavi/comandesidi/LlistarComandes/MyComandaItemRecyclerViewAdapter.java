package com.example.xavi.comandesidi.LlistarComandes;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.xavi.comandesidi.LlistarComandes.ComandaItemFragment.OnListFragmentInteractionListener;
import com.example.xavi.comandesidi.R;
import com.example.xavi.comandesidi.domini.ComandaContainer;

import java.util.List;


public class MyComandaItemRecyclerViewAdapter extends RecyclerView.Adapter<MyComandaItemRecyclerViewAdapter.ViewHolder> {

    private final List<ComandaContainer.Comanda> comandaList;
    private final OnListFragmentInteractionListener mListener;

    public MyComandaItemRecyclerViewAdapter(List<ComandaContainer.Comanda> items, OnListFragmentInteractionListener listener) {
        comandaList = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_comandaitem, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.comanda = comandaList.get(position);
        holder.priceTv.setText(String.valueOf(comandaList.get(position).getPrice()));
        holder.numTaulaTv.setText(String.valueOf(comandaList.get(position).getTableNum()));
        holder.dateTv.setText(String.valueOf(comandaList.get(position).getDay()));
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(holder.comanda);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return comandaList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView numTaulaTv;
        public final TextView priceTv;
        public final TextView dateTv;
        public ComandaContainer.Comanda comanda;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            numTaulaTv = (TextView) view.findViewById(R.id.numTableTv);
            dateTv = (TextView) view.findViewById(R.id.dateTv);
            priceTv = (TextView) view.findViewById(R.id.comandaPriceTv);
        }

    }
}

package com.example.xavi.comandesidi;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.xavi.comandesidi.NovaComanda.MyItemRecyclerViewAdapter;
import com.example.xavi.comandesidi.domini.ProductsContainer;

/**
 * Created by xavi on 07/01/16.
 */
public class AjudaFragment extends Fragment {
    //TODO: Fer editar nom, correu foto restarurant més opció netejar BD plats i BD comandes


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_help, container, false);

        return view;
    }
}

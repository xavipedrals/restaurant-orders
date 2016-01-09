package com.example.xavi.comandesidi;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

/**
 * Created by xavi on 07/01/16.
 */
public class AjudaFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_help, container, false);
        ImageView iconNovaComanda = (ImageView) view.findViewById(R.id.iconNovaComanda);
        ImageView iconEditarPlats = (ImageView) view.findViewById(R.id.iconEditarPlats);
        ImageView iconLlistarComandes = (ImageView) view.findViewById(R.id.iconLlistarComandes);
        ImageView iconStocPlats = (ImageView) view.findViewById(R.id.iconStocProductes);
        ImageView iconConfiguracio = (ImageView) view.findViewById(R.id.iconConfiguracio);

        iconNovaComanda.setAlpha(138);
        iconEditarPlats.setAlpha(138);
        iconLlistarComandes.setAlpha(138);
        iconStocPlats.setAlpha(138);
        iconConfiguracio.setAlpha(138);
        return view;
    }
}

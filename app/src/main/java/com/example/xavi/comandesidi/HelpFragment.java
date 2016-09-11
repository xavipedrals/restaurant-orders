package com.example.xavi.comandesidi;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.xavi.comandesidi.Utils.ConstantValues;

/**
 * Created by xavi on 07/01/16.
 */
public class HelpFragment extends Fragment {

    ImageView newOrderIcon;
    ImageView editDishesIcon;
    ImageView listOrdersIcon;
    ImageView dishesStockIcon;
    ImageView settingsIcon;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_help, container, false);
        initVisuals(view);
        setIconAlphas();
        return view;
    }

    private void initVisuals(View view) {
        newOrderIcon = (ImageView) view.findViewById(R.id.iconNovaComanda);
        editDishesIcon = (ImageView) view.findViewById(R.id.iconEditarPlats);
        listOrdersIcon = (ImageView) view.findViewById(R.id.iconLlistarComandes);
        dishesStockIcon = (ImageView) view.findViewById(R.id.iconStocProductes);
        settingsIcon = (ImageView) view.findViewById(R.id.iconConfiguracio);
    }

    private void setIconAlphas() {
        newOrderIcon.setAlpha(ConstantValues.alpha);
        editDishesIcon.setAlpha(ConstantValues.alpha);
        listOrdersIcon.setAlpha(ConstantValues.alpha);
        dishesStockIcon.setAlpha(ConstantValues.alpha);
        settingsIcon.setAlpha(ConstantValues.alpha);
    }
}

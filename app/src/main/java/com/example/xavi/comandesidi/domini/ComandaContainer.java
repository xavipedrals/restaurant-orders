package com.example.xavi.comandesidi.domini;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.example.xavi.comandesidi.R;
import com.example.xavi.comandesidi.data.GestorBD;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xavi on 24/12/15.
 */
public class ComandaContainer {

    private List<Comanda> comandaList;
    private Context context;
    private static ComandaContainer instance;

    public ComandaContainer(Context context){
        comandaList = new ArrayList<>();
        this.context = context;
        getComandes();
    }

    private void getComandes(){
        Cursor cursor = GestorBD.getInstance(context).getAllComandes();
        if (cursor.moveToFirst()) {
            do {
                Comanda comanda = new Comanda();
                comanda.setDate(cursor.getString(cursor.getColumnIndex(GestorBD.COMANDES_COL_DATA)));
                comanda.setPrice(cursor.getDouble(cursor.getColumnIndex(GestorBD.COMANDES_COL_PRICE)));
                comanda.setTableNum(cursor.getInt(cursor.getColumnIndex(GestorBD.COMANDES_COL_NUM_TABLE)));
                comandaList.add(comanda);
            } while (cursor.moveToNext());
        }
    }

    /**Per tenir una sola instància**/
    public static ComandaContainer getInstance(Context context){
        if(instance == null){
            instance = new ComandaContainer(context);
        }
        return instance;
    }

    public static void refresh(Context context){
        instance = new ComandaContainer(context);
    }

    public void addComanda(Comanda comanda){
        comandaList.add(comanda);
    }

    public List<Comanda> getComandaList() {
        return comandaList;
    }

    public class Comanda{

        private String date;
        private String day;
        private String hour;
        private int tableNum;
        private double price;

        public Comanda(){}

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
            String[] aux = date.split(" ");
            day = aux[0];
            hour = aux[1] + " " + aux[2];
        }

        public String getDay() {
            return day;
        }

        public void setDay(String day) {
            this.day = day;
        }

        public String getHour() {
            return hour;
        }

        public void setHour(String hour) {
            this.hour = hour;
        }

        public int getTableNum() {
            return tableNum;
        }

        public void setTableNum(int tableNum) {
            this.tableNum = tableNum;
        }

        public double getPrice() {
            return price;
        }

        public void setPrice(double price) {
            this.price = price;
        }
    }
}

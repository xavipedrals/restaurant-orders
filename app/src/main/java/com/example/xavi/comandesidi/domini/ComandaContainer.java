package com.example.xavi.comandesidi.domini;

import android.content.Context;
import android.database.Cursor;

import com.example.xavi.comandesidi.R;
import com.example.xavi.comandesidi.data.GestorBD;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
        //getComandes();
        SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy hh:mm a");
        Date date = new Date();
        String dateStr = df.format(date);
        String aux[] = dateStr.split(" ");
        String onlyDay = aux[0];
        populateBDifNotPopulated(onlyDay);
    }

//    private void getComandes(){
//        Cursor cursor = GestorBD.getInstance(context).getAllComandes();
//        if (cursor.moveToFirst()) {
//            do {
//                Comanda comanda = new Comanda();
//                comanda.setDate(cursor.getString(cursor.getColumnIndex(GestorBD.COMANDES_COL_DATA)));
//                comanda.setPrice(cursor.getDouble(cursor.getColumnIndex(GestorBD.COMANDES_COL_PRICE)));
//                comanda.setTableNum(cursor.getInt(cursor.getColumnIndex(GestorBD.COMANDES_COL_NUM_TABLE)));
//                comandaList.add(comanda);
//            } while (cursor.moveToNext());
//        }
//    }
    private boolean checkIfBDhasSomething(){
        Cursor cursor = GestorBD.getInstance(context).getAllComandes();
        return cursor.moveToFirst();
    }

    private void populateBDifNotPopulated(String date){
        Cursor cursor = GestorBD.getInstance(context).getComandesByDay(date);
        if (cursor.moveToFirst()) {
            do {
                Comanda comanda = new Comanda();
                comanda.setDate(cursor.getString(cursor.getColumnIndex(GestorBD.COMANDES_COL_DATA)));
                comanda.setPrice(cursor.getDouble(cursor.getColumnIndex(GestorBD.COMANDES_COL_PRICE)));
                comanda.setTableNum(cursor.getInt(cursor.getColumnIndex(GestorBD.COMANDES_COL_NUM_TABLE)));
                comandaList.add(comanda);
            } while (cursor.moveToNext());
        } else {
            if (!checkIfBDhasSomething()) {
                String hour = " 12:00 AM";
                String hour2 = " 11:30 PM";
                String hour3 = " 09:00 AM";
                String hour4 = " 02:30 PM";
                String hour5 = " 08:40 PM";
                String hour6 = " 10:00 PM";
                GestorBD.getInstance(context).insertComanda((double) 15, date + hour, 3);
                GestorBD.getInstance(context).insertComanda((double) 23.50, date + hour2, 13);
                GestorBD.getInstance(context).insertComanda((double) 7.45, date + hour3, 4);
                GestorBD.getInstance(context).insertComanda((double) 28, date + hour4, 8);
                GestorBD.getInstance(context).insertComanda((double) 19.99, date + hour5, 20);
                GestorBD.getInstance(context).insertComanda((double) 43.25, date + hour6, 1);
                populateBDifNotPopulated(date);
            }
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

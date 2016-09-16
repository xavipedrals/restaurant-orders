package com.example.xavi.comandesidi.DBWrappers;

import android.content.Context;
import android.database.Cursor;

import com.example.xavi.comandesidi.DBManager.DBManager;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by xavi on 24/12/15.
 */
public class OrderContainer {

    private List<Order> orderList;
    private Context context;
    private static OrderContainer instance;

    /**Singleton**/
    public static OrderContainer getInstance(Context context){
        if(instance == null){
            instance = new OrderContainer(context);
        }
        return instance;
    }

    public static OrderContainer getFirstInstance(Context context){
        if(instance == null){
            instance = new OrderContainer(context, true);
        }
        return instance;
    }

    public static void refresh(Context context){
        instance = new OrderContainer(context);
    }

    public static void refreshAfterReset(Context context){
        instance = new OrderContainer(context, false);
    }

    public OrderContainer(Context context){
        orderList = new ArrayList<>();
        this.context = context;
        SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy hh:mm a");
        Date date = new Date();
        String dateStr = df.format(date);
        String aux[] = dateStr.split(" ");
        String onlyDay = aux[0];
        fetchOrderItems(onlyDay);
    }

    public OrderContainer(Context context, boolean populate){
        orderList = new ArrayList<>();
        this.context = context;
        if (populate){
            SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy hh:mm a");
            Date date = new Date();
            String dateStr = df.format(date);
            String aux[] = dateStr.split(" ");
            String onlyDay = aux[0];
            populateBDifNotPopulated(onlyDay);
        }
    }

    private boolean checkIfBDhasSomething(){
        Cursor cursor = DBManager.getInstance(context).getAllOrders();
        return cursor.moveToFirst();
    }

    private void populateBDifNotPopulated(String date){
        Cursor cursor = DBManager.getInstance(context).getOrdersByDay(date);
        if (cursor.moveToFirst()) {
            do {
                Order order = initNewOrder(cursor);
                orderList.add(order);
            } while (cursor.moveToNext());
        } else {
            if (!checkIfBDhasSomething()) {
                initStub(date);
                populateBDifNotPopulated(date);
            }
        }
    }

    private void fetchOrderItems(String date){
        Cursor cursor = DBManager.getInstance(context).getOrdersByDay(date);
        if (cursor.moveToFirst()) {
            do {
                Order order = initNewOrder(cursor);
                orderList.add(order);
            } while (cursor.moveToNext());
        }
    }

    private Order initNewOrder(Cursor cursor) {
        Order order = new Order();
        order.setDate(cursor.getString(cursor.getColumnIndex(DBManager.ORDERS_COL_DATA)));
        order.price = cursor.getDouble(cursor.getColumnIndex(DBManager.ORDERS_COL_PRICE));
        order.tableNum = cursor.getInt(cursor.getColumnIndex(DBManager.ORDERS_COL_NUM_TABLE));
        return order;
    }

    private void initStub(String date) {
        String hour = " 12:00 AM";
        String hour2 = " 11:30 PM";
        String hour3 = " 09:00 AM";
        String hour4 = " 02:30 PM";
        String hour5 = " 08:40 PM";
        String hour6 = " 10:00 PM";
        DBManager.getInstance(context).insertOrder((double) 15, date + hour, 3);
        DBManager.getInstance(context).insertOrder((double) 23.50, date + hour2, 13);
        DBManager.getInstance(context).insertOrder((double) 7.45, date + hour3, 4);
        DBManager.getInstance(context).insertOrder((double) 28, date + hour4, 8);
        DBManager.getInstance(context).insertOrder((double) 19.99, date + hour5, 20);
        DBManager.getInstance(context).insertOrder((double) 43.25, date + hour6, 1);
    }

    public List<Order> getOrderList() {
        return orderList;
    }

    public class Order {

        private String date;
        private String day;
        private String hour;
        public int tableNum;
        public double price;

        public Order(){}

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

        public String getHour() {
            return hour;
        }

    }
}

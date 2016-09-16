package com.example.xavi.comandesidi.DBWrappers;

import android.content.Context;
import android.database.Cursor;

import com.example.xavi.comandesidi.R;
import com.example.xavi.comandesidi.DBManager.DBManager;

import java.util.ArrayList;
import java.util.List;


public class DishesContainer {

    private static DishesContainer instance;

    private final List<Dish> dishList;
    private Context context;

    /**Singleton**/
    public static DishesContainer getInstance(Context context){
        if(instance == null){
            instance = new DishesContainer(context);
        }
        return instance;
    }

    public static DishesContainer getFirstInstance(Context context){
        if(instance == null){
            instance = new DishesContainer(context, true);
        }
        return instance;
    }

    public static void refresh(Context context){
        instance = new DishesContainer(context);
    }

    public static void refreshAfterReset(Context context){
        instance = new DishesContainer(context, false);
    }

    private void populateBDifNotPopulated(){
        Cursor cursor = DBManager.getInstance(context).getAllDishes();
        if (cursor.moveToFirst()) {
            do {
                Dish dish = initProduct(cursor);
                dishList.add(dish);
            } while (cursor.moveToNext());
        } else {
            initStub();
            populateBDifNotPopulated();
        }
    }

    private Dish initProduct (Cursor cursor) {
        Dish dish = new Dish();
        dish.id = (cursor.getInt(cursor.getColumnIndex(DBManager.DISHES_COL_ID)));
        dish.name = (cursor.getString(cursor.getColumnIndex(DBManager.DISHES_COL_NAME)));
        dish.price = (cursor.getDouble(cursor.getColumnIndex(DBManager.DISHES_COL_PRICE)));
        dish.stock = (cursor.getInt(cursor.getColumnIndex(DBManager.DISHES_COL_STOCK)));
        dish.mipmapId = (cursor.getInt(cursor.getColumnIndex(DBManager.DISHES_COL_IMG)));
        int hasImage = cursor.getInt(cursor.getColumnIndex(DBManager.DISHES_COL_HAS_IMAGE));
        if (hasImage != 0){
            dish.hasImage = true;
            dish.imgUri = cursor.getString(cursor.getColumnIndex(DBManager.DISHES_COL_IMAGE_URI));
        } else {
            dish.hasImage = false;
            dish.imgUri = null;
        }
        return dish;
    }

    private void initStub () {
        DBManager.getInstance(context).insertDish(R.mipmap.alberginia, 7.50, "Eggplant with bacon", 100);
        DBManager.getInstance(context).insertDish(R.mipmap.arros, 8.40, "Rice with chicken", 50);
        DBManager.getInstance(context).insertDish(R.mipmap.cranc, 11.99, "Crab with garnish", 20);
        DBManager.getInstance(context).insertDish(R.mipmap.kabab, 8.00, "Kabab", 1);
        DBManager.getInstance(context).insertDish(R.mipmap.sopa, 6.70, "Vegetables soup", 0);
        DBManager.getInstance(context).insertDish(R.mipmap.postres, 4.50, "Chocolate coulant", 40);
        DBManager.getInstance(context).insertDish(R.mipmap.pizza, 7.30, "Vegeterian pizza", 0);
        DBManager.getInstance(context).insertDish(R.mipmap.fajitas, 4.50, "Fajitas", 3);
        DBManager.getInstance(context).insertDish(R.mipmap.tacos, 4, "Tacos", 99);
    }

    private void fetchDishItems(){
        Cursor cursor = DBManager.getInstance(context).getAllDishes();
        if (cursor.moveToFirst()) {
            do {
                Dish dish = initProduct(cursor);
                dishList.add(dish);
            } while (cursor.moveToNext());
        }
    }

    private DishesContainer(Context context){
        dishList = new ArrayList<>();
        this.context = context;
        fetchDishItems();
    }

    private DishesContainer(Context context, boolean populate){
        dishList = new ArrayList<>();
        this.context = context;
        if (populate) populateBDifNotPopulated();
    }

//    public static int getSize(){
//        return instance.dishList.size();
//    }

    public List<Dish> getDishList(){
        return dishList;
    }


    public class Dish {
        public int id;
        public int mipmapId;
        public double price;
        public String name;
        public int stock;
        public boolean hasImage;
        public String imgUri;

        public Dish(){}
    }
}

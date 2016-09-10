package com.example.xavi.comandesidi.DBWrappers;

import android.content.Context;
import android.database.Cursor;

import com.example.xavi.comandesidi.R;
import com.example.xavi.comandesidi.DBManager.DBManager;

import java.util.ArrayList;
import java.util.List;


public class ProductsContainer {

    private static ProductsContainer instance;

    private final List<Product> productList;
    private Context context;

    /**Singleton**/
    public static ProductsContainer getInstance(Context context){
        if(instance == null){
            instance = new ProductsContainer(context);
        }
        return instance;
    }

    public static ProductsContainer getFirstInstance(Context context){
        if(instance == null){
            instance = new ProductsContainer(context, true);
        }
        return instance;
    }

    public static void refresh(Context context){
        instance = new ProductsContainer(context);
    }

    public static void refreshAfterReset(Context context){
        instance = new ProductsContainer(context, false);
    }

    private void populateBDifNotPopulated(){
        Cursor cursor = DBManager.getInstance(context).getAllPlats();
        if (cursor.moveToFirst()) {
            do {
                Product product = initProduct(cursor);
                productList.add(product);
            } while (cursor.moveToNext());
        } else {
            initStub();
            populateBDifNotPopulated();
        }
    }

    private Product initProduct (Cursor cursor) {
        Product product = new Product();
        product.setId(cursor.getInt(cursor.getColumnIndex(DBManager.PLATS_COL_ID)));
        product.setName(cursor.getString(cursor.getColumnIndex(DBManager.PLATS_COL_NAME)));
        product.setPrice(cursor.getDouble(cursor.getColumnIndex(DBManager.PLATS_COL_PRICE)));
        product.setStock(cursor.getInt(cursor.getColumnIndex(DBManager.PLATS_COL_STOCK)));
        product.setMipmapId(cursor.getInt(cursor.getColumnIndex(DBManager.PLATS_COL_IMG)));
        int hasImage = cursor.getInt(cursor.getColumnIndex(DBManager.PLATS_COL_HAS_IMAGE));
        if (hasImage != 0){
            product.setHasImage(true);
            product.setImgUri(cursor.getString(cursor.getColumnIndex(DBManager.PLATS_COL_IMAGE_URI)));
        } else {
            product.setHasImage(false);
            product.setImgUri(null);
        }
        return product;
    }

    private void initStub () {
        DBManager.getInstance(context).insertPlat(R.mipmap.alberginia, 7.50, "Eggplant with bacon", 100);
        DBManager.getInstance(context).insertPlat(R.mipmap.arros, 8.40, "Rice with chicken", 50);
        DBManager.getInstance(context).insertPlat(R.mipmap.cranc, 11.99, "Crab with garnish", 20);
        DBManager.getInstance(context).insertPlat(R.mipmap.kabab, 8.00, "Kabab", 1);
        DBManager.getInstance(context).insertPlat(R.mipmap.sopa, 6.70, "Vegetables soup", 0);
        DBManager.getInstance(context).insertPlat(R.mipmap.postres, 4.50, "Chocolate coulant", 40);
        DBManager.getInstance(context).insertPlat(R.mipmap.pizza, 7.30, "Vegeterian pizza", 0);
        DBManager.getInstance(context).insertPlat(R.mipmap.fajitas, 4.50, "Fajitas", 3);
        DBManager.getInstance(context).insertPlat(R.mipmap.tacos, 4, "Tacos", 99);
    }

    private void fetchPlatItems(){
        Cursor cursor = DBManager.getInstance(context).getAllPlats();
        if (cursor.moveToFirst()) {
            do {
                Product product = initProduct(cursor);
                productList.add(product);
            } while (cursor.moveToNext());
        }
    }

    private ProductsContainer(Context context){
        productList = new ArrayList<>();
        this.context = context;
        //populateBDifNotPopulated();
        fetchPlatItems();
    }

    private ProductsContainer(Context context, boolean populate){
        productList = new ArrayList<>();
        this.context = context;
        if (populate) populateBDifNotPopulated();
    }

    public static int getSize(){
        return instance.productList.size();
    }

    public List<Product> getProductList(){
        return productList;
    }


    public class Product {

        private int id;
        private int mipmapId;
        private double price;
        private String name;
        private int stock;
        private boolean hasImage;
        private String imgUri;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public boolean hasImage() {
            return hasImage;
        }

        public void setHasImage(boolean hasImage) {
            this.hasImage = hasImage;
        }

        public String getImgUri() {
            return imgUri;
        }

        public void setImgUri(String imgUri) {
            this.imgUri = imgUri;
        }

        public int getMipmapId() {
            return mipmapId;
        }

        public double getPrice() {
            return price;
        }

        public String getName() {
            return name;
        }

        public void setMipmapId(int mipmapId) {
            this.mipmapId = mipmapId;
        }

        public void setPrice(double price) {
            this.price = price;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getStock() {
            return stock;
        }

        public void setStock(int stock) {
            this.stock = stock;
        }

        public Product(){}
    }
}

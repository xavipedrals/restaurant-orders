package com.example.xavi.comandesidi.domini;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.example.xavi.comandesidi.R;
import com.example.xavi.comandesidi.data.GestorBD;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xavi on 04/12/15.
 */
public class ProductsContainer {

    private static ProductsContainer instance;

    private final List<Product> productList;
    private Context context;

    /**Per tenir una sola instància**/
    public static ProductsContainer getInstance(Context context){
        if(instance == null){
            instance = new ProductsContainer(context);
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
        Cursor cursor = GestorBD.getInstance(context).getAllPlats();
        if (cursor.moveToFirst()) {
            do {
                Product product = new Product();
                product.setId(cursor.getInt(cursor.getColumnIndex(GestorBD.PLATS_COL_ID)));
                product.setName(cursor.getString(cursor.getColumnIndex(GestorBD.PLATS_COL_NAME)));
                product.setPrice(cursor.getDouble(cursor.getColumnIndex(GestorBD.PLATS_COL_PRICE)));
                product.setStock(cursor.getInt(cursor.getColumnIndex(GestorBD.PLATS_COL_STOCK)));
                product.setMipmapId(cursor.getInt(cursor.getColumnIndex(GestorBD.PLATS_COL_IMG)));
                int hasImage = cursor.getInt(cursor.getColumnIndex(GestorBD.PLATS_COL_HAS_IMAGE));
                if (hasImage != 0){
                    product.setHasImage(true);
                    product.setImgUri(cursor.getString(cursor.getColumnIndex(GestorBD.PLATS_COL_IMAGE_URI)));
                } else {
                    product.setHasImage(false);
                    product.setImgUri(null);
                }
                productList.add(product);
            } while (cursor.moveToNext());
        } else {
            GestorBD.getInstance(context).insertPlat(R.mipmap.alberginia, 7.50, "Alberginia amb bacon", 100);
            GestorBD.getInstance(context).insertPlat(R.mipmap.arros, 8.40, "Arròs amb pollastre", 50);
            GestorBD.getInstance(context).insertPlat(R.mipmap.cranc, 11.99, "Cranc amb guarnició", 20);
            GestorBD.getInstance(context).insertPlat(R.mipmap.kabab, 8.00, "Kabab", 1);
            GestorBD.getInstance(context).insertPlat(R.mipmap.sopa, 6.70, "Sopa de verdures", 0);
            GestorBD.getInstance(context).insertPlat(R.mipmap.postres, 4.50, "Coulan de xocolata", 40);
            GestorBD.getInstance(context).insertPlat(R.mipmap.pizza, 7.30, "Pizza vegetariana", 0);
            GestorBD.getInstance(context).insertPlat(R.mipmap.fajitas, 4.50, "Fajitas", 3);
            GestorBD.getInstance(context).insertPlat(R.mipmap.tacos, 4, "Tacos", 99);
            populateBDifNotPopulated();
        }
    }

    private ProductsContainer(Context context){
        productList = new ArrayList<>();
        this.context = context;
        populateBDifNotPopulated();
    }

    private ProductsContainer(Context context, boolean populate){
        productList = new ArrayList<>();
        this.context = context;
    }

    public List<Product> getProductList(){
        return productList;
    }


    /**Classe item**/
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

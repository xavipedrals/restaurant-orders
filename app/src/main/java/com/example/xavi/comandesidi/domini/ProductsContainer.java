package com.example.xavi.comandesidi.domini;

import android.content.Context;
import android.database.Cursor;

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
    private int count;
    private Context context;

    /**Per tenir una sola instància**/
    public static ProductsContainer getInstance(Context context){
        if(instance == null){
            instance = new ProductsContainer(context);
        }
        return instance;
    }

    private void fillWithDefaultProducts(){
        Product p1, p2, p3, p4, p5, p6;
        p1 = new Product(R.mipmap.alberginia, 7.50, "Alberginia amb bacon");
        p2 = new Product(R.mipmap.arros, 8.40, "Arròs amb pollastre");
        p3 = new Product(R.mipmap.cranc, 11.99, "Cranc amb gurnició");
        p4 = new Product(R.mipmap.kabab, 8.00, "Kabab");
        p5 = new Product(R.mipmap.sopa, 6.70, "Sopa de verdures");
        p6 = new Product(R.mipmap.postres, 4.50, "Coulan de xocolata");
        productList.add(p1);
        productList.add(p2);
        productList.add(p3);
        productList.add(p4);
        productList.add(p5);
        productList.add(p6);
        count = 6;
    }

    private void populateBDifNotPopulated(){
        Cursor cursor = GestorBD.getInstance(context).getAllPlats();
        if (cursor.moveToFirst()) {
            do {
                Product product = new Product();
                product.setName(cursor.getString(cursor.getColumnIndex(GestorBD.PLATS_COL_NAME)));
                product.setPrice(cursor.getDouble(cursor.getColumnIndex(GestorBD.PLATS_COL_PRICE)));
                product.setMipmapId(cursor.getInt(cursor.getColumnIndex(GestorBD.PLATS_COL_IMG)));
                productList.add(product);
            } while (cursor.moveToNext());
        } else {
            GestorBD.getInstance(context).insertPlat(R.mipmap.alberginia, 7.50, "Alberginia amb bacon");
            GestorBD.getInstance(context).insertPlat(R.mipmap.arros, 8.40, "Arròs amb pollastre");
            GestorBD.getInstance(context).insertPlat(R.mipmap.cranc, 11.99, "Cranc amb gurnició");
            GestorBD.getInstance(context).insertPlat(R.mipmap.kabab, 8.00, "Kabab");
            GestorBD.getInstance(context).insertPlat(R.mipmap.sopa, 6.70, "Sopa de verdures");
            GestorBD.getInstance(context).insertPlat(R.mipmap.postres, 4.50, "Coulan de xocolata");
            populateBDifNotPopulated();
        }
    }

    private ProductsContainer(Context context){
        productList = new ArrayList<>();
        count = 0;
        this.context = context;
        populateBDifNotPopulated();
        //fillWithDefaultProducts();
    }

    public Product getProdByIndex(int index){
        return productList.get(index);
    }

    public List<Product> getProductList(){
        return productList;
    }

    public int getCount() {
        return count;
    }

    public void addProduct(int mipmapId, double price, String name){
        productList.add(new Product(mipmapId, price, name));
        GestorBD.getInstance(context).insertPlat(mipmapId, price, name);
    }


    /**Classe item**/
    public class Product {

        private int mipmapId;
        private double price;
        private String name;

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

        public Product(int mipmapId, double price, String name){
            this.mipmapId = mipmapId;
            this.price = price;
            this.name = name;
        }

        public Product(){}
    }
}

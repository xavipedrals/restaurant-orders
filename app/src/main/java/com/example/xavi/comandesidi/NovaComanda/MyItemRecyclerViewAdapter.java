package com.example.xavi.comandesidi.NovaComanda;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.xavi.comandesidi.NovaComanda.ItemFragment.OnListFragmentInteractionListener;
import com.example.xavi.comandesidi.R;
import com.example.xavi.comandesidi.domini.ProductsContainer;
import com.example.xavi.comandesidi.widgets.ImageHelper;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;


public class MyItemRecyclerViewAdapter extends RecyclerView.Adapter<MyItemRecyclerViewAdapter.ViewHolder> {

    private List<ProductsContainer.Product> productList;
    private final OnListFragmentInteractionListener mListener;
    private Context context;
    private ViewHolder lastClickedView;
    private List<ViewHolder> viewHolderList;

    private int position;

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public ViewHolder getLastClickedView() {
        return lastClickedView;
    }

    public void setLastClickedView(ViewHolder lastClickedView) {
        this.lastClickedView = lastClickedView;
    }

    public double getTotalPrice(){
        double price = 0;
        for(ViewHolder holder: viewHolderList){
            price += holder.quantity * holder.product.getPrice();
        }
        return price;
    }

    public List<ProductsContainer.Product> getProductesActualitzats(){
        List<ProductsContainer.Product> productesActualitzats = new ArrayList<>();
        for(ViewHolder holder: viewHolderList){
            if (holder.checkIfOrdered()) {
                holder.updateOrderedProduct();
                productesActualitzats.add(holder.product);
            }
        }
        return productesActualitzats;
    }

    public void resetView(){
        for(ViewHolder holder: viewHolderList){
            holder.decreaseQuantityToZero();
        }
    }

    public MyItemRecyclerViewAdapter(ProductsContainer productsContainer, OnListFragmentInteractionListener listener, Context context) {
        productList = productsContainer.getProductList();
        mListener = listener;
        this.context = context;
        viewHolderList = new ArrayList<>();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.product = productList.get(position);
        holder.stock = holder.product.getStock();
        if (holder.stock == 0) holder.setOutOfStockBackgroundColor();
        holder.nameTv.setText(productList.get(position).getName());
        String priceStr = String.valueOf(productList.get(position).getPrice()) + " €";
        holder.priceTv.setText(priceStr);
        Bitmap bitmap = null;
        if(holder.product.hasImage()){
            Log.d("CARREGO IMATGE", "Ei colega");
            Uri uri = Uri.parse(holder.product.getImgUri());
            try {
                bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), uri);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            bitmap = BitmapFactory.decodeResource(context.getResources(), productList.get(position).getMipmapId());
        }
        Bitmap round = ImageHelper.getRoundedShape(bitmap, 128);
        holder.imageView.setImageBitmap(round);


        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    //mListener.onListFragmentInteraction(holder.product);
                    if (holder.checkStock()) holder.increaseQuantityByOne();
                    else {
                        holder.setOutOfStockBackgroundColor();
                        mListener.onListFragmentInteraction(holder.product);
                    }
                }
            }
        });
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                setPosition(holder.getPosition());
                setLastClickedView(holder);
                return false;
            }
        });
        viewHolderList.add(holder);
    }

    @Override
    public void onViewRecycled(ViewHolder holder) {
        holder.itemView.setOnLongClickListener(null);
        super.onViewRecycled(holder);
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {
        //TODO: Tenir en compte si hi ha bitmap o imageUri
        public final View mView;
        public final LinearLayout itemContainer;
        public final TextView nameTv;
        public final TextView priceTv;
        public TextView quantitatTv;
        public final TextView xTv;
        public ImageView imageView;
        public ProductsContainer.Product product;
        public int quantity;
        public int stock;

        public ViewHolder(View view) {
            super(view);
            itemContainer = (LinearLayout) view.findViewById(R.id.item_container);
            mView = view;
            xTv = (TextView) view.findViewById(R.id.textViewX);
            nameTv = (TextView) view.findViewById(R.id.product_name);
            priceTv = (TextView) view.findViewById(R.id.product_price);
            quantitatTv = (TextView) view.findViewById(R.id.quantitat);
            imageView = (ImageView) view.findViewById(R.id.listImageView);
            int alpha = 54 * 255 / 100; //54% de opacitat, secondary text
            priceTv.setTextColor(Color.argb(alpha, 0, 0, 0));
            quantity = 0;
            view.setOnCreateContextMenuListener(this);
        }

        public boolean checkIfOrdered(){
            return quantity != 0;
        }

        public void updateOrderedProduct(){
            stock -= quantity;
            product.setStock(stock);
        }

        public boolean checkStock(){
            return stock > quantity;
        }

        public boolean checkStock(int quantity){
            return stock > quantity;
        }

        public void decreaseQuantityByOne(){
            String s = quantitatTv.getText().toString();
            if (!s.equals("")) {
                if(s.equals("1")){
                    this.setNormalBackgroundColor();
                    xTv.setVisibility(View.GONE);
                    quantitatTv.setText("");
                    quantity = 0;
                } else {
                    if(!checkStock()) setSelectionedBackgroundColor();
                    int i = Integer.parseInt(s);
                    i--;
                    quantity = i;
                    quantitatTv.setText(String.valueOf(i));
                }
            }
        }

        public void increaseQuantityByOne(){
            String number = quantitatTv.getText().toString();
            if(number.equals("")){
                xTv.setVisibility(View.VISIBLE);
                quantitatTv.setText("1");
                this.setSelectionedBackgroundColor();
                quantity = 1;
            } else {
                int i = Integer.parseInt(number);
                i++;
                quantitatTv.setText(String.valueOf(i));
                quantity = i;
            }
        }

        public void decreaseQuantityToZero(){
            quantitatTv.setText(String.valueOf(""));
            xTv.setVisibility(View.GONE);
            quantity = 0;
            if (stock == 0) this.setOutOfStockBackgroundColor();
            else this.setNormalBackgroundColor();
        }

        public void setExactQuantity(int quantity){
                xTv.setVisibility(View.VISIBLE);
                quantitatTv.setText(String.valueOf(quantity));
                this.quantity = quantity;
                this.setSelectionedBackgroundColor();
        }

        public void setSelectionedBackgroundColor(){
            //itemContainer.setBackgroundColor(Color.parseColor("#E1F5FE")); //Light blue
            itemContainer.setBackgroundColor(context.getResources().getColor(R.color.accent_translucent)); //Light blue
        }

        public void setNormalBackgroundColor(){
            itemContainer.setBackgroundColor(Color.parseColor("#FAFAFA"));   //Default background color
        }

        public void setOutOfStockBackgroundColor(){
            itemContainer.setBackgroundColor(Color.parseColor("#FFCDD2")); //Light red
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            //Necessari pel context menu del fragment
        }

    }
}

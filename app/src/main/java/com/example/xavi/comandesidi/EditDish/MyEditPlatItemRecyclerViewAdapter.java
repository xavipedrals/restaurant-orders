package com.example.xavi.comandesidi.EditDish;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.xavi.comandesidi.EditDish.EditDishItemFragment.OnListFragmentInteractionListener;
import com.example.xavi.comandesidi.widgets.ImageHelper;
import com.example.xavi.comandesidi.R;
import com.example.xavi.comandesidi.DBWrappers.ProductsContainer;

import java.io.IOException;
import java.util.List;


public class MyEditPlatItemRecyclerViewAdapter extends RecyclerView.Adapter<MyEditPlatItemRecyclerViewAdapter.ViewHolder> {

    private final OnListFragmentInteractionListener mListener;
    private List<ProductsContainer.Product> productList;
    private Context context;

    public MyEditPlatItemRecyclerViewAdapter(ProductsContainer productsContainer, OnListFragmentInteractionListener listener, Context context) {
        mListener = listener;
        this.productList = productsContainer.getProductList();
        this.context = context;
    }

    public void refreshView(List<ProductsContainer.Product> products){
        productList.clear();
        productList.addAll(products);
        notifyDataSetChanged();
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
        holder.nameTv.setText(productList.get(position).getName());
        String priceStr = String.valueOf(productList.get(position).getPrice()) + " €";
        holder.priceTv.setText(priceStr);

        Bitmap bitmap = null;
        if(holder.product.hasImage()){
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
                    mListener.onListFragmentInteraction(holder.product);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final LinearLayout itemContainer;
        public final TextView nameTv;
        public final TextView priceTv;
        public TextView quantitatTv;
        public final TextView xTv;
        public ImageView imageView;
        public ProductsContainer.Product product;
        public int quantity;

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
        }

    }
}
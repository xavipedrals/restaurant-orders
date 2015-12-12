package com.example.xavi.comandesidi;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.xavi.comandesidi.ItemFragment.OnListFragmentInteractionListener;
import com.example.xavi.comandesidi.domini.ProductsContainer;
import com.example.xavi.comandesidi.dummy.DummyContent.DummyItem;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link DummyItem} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class MyItemRecyclerViewAdapter extends RecyclerView.Adapter<MyItemRecyclerViewAdapter.ViewHolder> {

    private List<ProductsContainer.Product> productList;
    private final OnListFragmentInteractionListener mListener;
    private Context context;

    public MyItemRecyclerViewAdapter(ProductsContainer productsContainer, OnListFragmentInteractionListener listener, Context context) {
        productList = productsContainer.getProductList();
        mListener = listener;
        this.context = context;
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
        holder.price.setText(priceStr);
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), productList.get(position).getMipmapId());
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
        public final TextView nameTv;
        public final TextView price;
        public ImageView imageView;
        public ProductsContainer.Product product;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            nameTv = (TextView) view.findViewById(R.id.id);
            price = (TextView) view.findViewById(R.id.content);
            imageView = (ImageView) view.findViewById(R.id.listImageView);

            //Bitmap round = ImageHelper.getRoundedCornerBitmap(bitmap, 10);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + price.getText() + "'";
        }
    }
}

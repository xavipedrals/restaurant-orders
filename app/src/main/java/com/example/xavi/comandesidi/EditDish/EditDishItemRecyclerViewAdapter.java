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
import com.example.xavi.comandesidi.Utils.ConstantValues;
import com.example.xavi.comandesidi.widgets.ImageHelper;
import com.example.xavi.comandesidi.R;
import com.example.xavi.comandesidi.DBWrappers.DishesContainer;

import java.io.IOException;
import java.util.List;


public class EditDishItemRecyclerViewAdapter extends RecyclerView.Adapter<EditDishItemRecyclerViewAdapter.ViewHolder> {

    private final OnListFragmentInteractionListener mListener;
    private List<DishesContainer.Dish> dishList;
    private Context context;

    public EditDishItemRecyclerViewAdapter(DishesContainer dishesContainer, OnListFragmentInteractionListener listener, Context context) {
        mListener = listener;
        this.dishList = dishesContainer.getDishList();
        this.context = context;
    }

    public void refreshView(List<DishesContainer.Dish> dishs){
        dishList.clear();
        dishList.addAll(dishs);
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
        holder.dish = dishList.get(position);
        holder.nameTv.setText(dishList.get(position).name);
        String priceStr = String.valueOf(dishList.get(position).price) + " €";
        holder.priceTv.setText(priceStr);

        setImageViewRoundImage(holder, position);
        setDishClickListener(holder);
    }

    public void setImageViewRoundImage(ViewHolder holder, int position) {
        Bitmap bitmap = null;
        if(holder.dish.hasImage){
            Uri uri = Uri.parse(holder.dish.imgUri);
            try {
                bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), uri);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            bitmap = BitmapFactory.decodeResource(context.getResources(), dishList.get(position).mipmapId);
        }
        Bitmap round = ImageHelper.getRoundedShape(bitmap, 128);
        holder.imageView.setImageBitmap(round);
    }

    public void setDishClickListener(final ViewHolder holder) {
        holder.containerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    mListener.onListFragmentInteraction(holder.dish);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return dishList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View containerView;
        public final LinearLayout itemContainer;
        public final TextView nameTv;
        public final TextView priceTv;
        public TextView quantityTv;
        public final TextView xTv;
        public ImageView imageView;
        public DishesContainer.Dish dish;
        public int quantity;

        public ViewHolder(View view) {
            super(view);
            containerView = view;
            itemContainer = (LinearLayout) view.findViewById(R.id.item_container);
            xTv = (TextView) view.findViewById(R.id.textViewX);
            nameTv = (TextView) view.findViewById(R.id.product_name);
            priceTv = (TextView) view.findViewById(R.id.product_price);
            quantityTv = (TextView) view.findViewById(R.id.quantitat);
            imageView = (ImageView) view.findViewById(R.id.listImageView);
            priceTv.setTextColor(Color.argb(ConstantValues.alpha, 0, 0, 0));
            quantity = 0;
        }
    }
}

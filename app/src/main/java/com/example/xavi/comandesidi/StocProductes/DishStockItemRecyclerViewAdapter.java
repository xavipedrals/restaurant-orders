package com.example.xavi.comandesidi.StocProductes;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.xavi.comandesidi.R;
import com.example.xavi.comandesidi.DBWrappers.DishesContainer;
import com.example.xavi.comandesidi.Utils.BitmapUtils;
import com.example.xavi.comandesidi.Utils.ConstantValues;

import java.util.ArrayList;
import java.util.List;


public class DishStockItemRecyclerViewAdapter extends RecyclerView.Adapter<DishStockItemRecyclerViewAdapter.ViewHolder> {

    private List<DishesContainer.Dish> dishList;
    private final DishStockItemFragment.OnListFragmentInteractionListener mListener;
    private Context context;
    private List<ViewHolder> viewHolderList;

    public DishStockItemRecyclerViewAdapter(DishesContainer dishesContainer, DishStockItemFragment.OnListFragmentInteractionListener listener, Context context) {
        dishList = dishesContainer.getDishList();
        mListener = listener;
        this.context = context;
        viewHolderList = new ArrayList<>();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_itemstoc, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        configViewHolder(holder, position);
        setImageViewBitmap(holder, position);
        setHolderClickListener(holder);
        viewHolderList.add(holder);
    }

    private void configViewHolder(ViewHolder holder, int position) {
        holder.dish = dishList.get(position);
        holder.nameTv.setText(dishList.get(position).name);
        String priceStr = String.valueOf(dishList.get(position).price) + " €";
        holder.priceTv.setText(priceStr);
        holder.stock = holder.dish.stock;
        if(holder.stock == 0) holder.setNoStockBackgroundColor();
        holder.quantitatTv.setText(String.valueOf(holder.stock));
    }

    private void setImageViewBitmap(ViewHolder holder, int position) {
        Bitmap bitmap = null;
        if(holder.dish.hasImage){
            BitmapUtils.getBitmapFromUri(holder.dish.imgUri, context);
        } else {
            bitmap = BitmapFactory.decodeResource(context.getResources(), dishList.get(position).mipmapId);
        }
        Bitmap round = BitmapUtils.getRoundedShape(bitmap, 128);
        holder.imageView.setImageBitmap(round);
    }

    private void setHolderClickListener(ViewHolder holder) {
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                }
            }
        });
    }

    @Override
    public void onViewRecycled(ViewHolder holder) {
        holder.itemView.setOnLongClickListener(null);
        super.onViewRecycled(holder);
    }

    @Override
    public int getItemCount() {
        return dishList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final LinearLayout itemContainer;
        public final TextView nameTv;
        public final TextView priceTv;
        public TextView quantitatTv;
        public final TextView stocTv;
        public ImageView imageView;
        public DishesContainer.Dish dish;
        public int stock;

        public ViewHolder(View view) {
            super(view);
            itemContainer = (LinearLayout) view.findViewById(R.id.item_container);
            mView = view;
            stocTv = (TextView) view.findViewById(R.id.textViewStoc);
            stocTv.setVisibility(View.GONE);
            nameTv = (TextView) view.findViewById(R.id.product_name);
            priceTv = (TextView) view.findViewById(R.id.product_price);
            quantitatTv = (TextView) view.findViewById(R.id.TextViewQuantitat);
            imageView = (ImageView) view.findViewById(R.id.listImageView);
            priceTv.setTextColor(Color.argb(ConstantValues.alpha, 0, 0, 0));
        }

        public void decreaseQuantityByX(int x) {
            String s = quantitatTv.getText().toString();
            int aux = Integer.parseInt(s);
            if (aux > 0) {
                if (aux >= x){
                    aux -= x;
                    quantitatTv.setText(String.valueOf(aux));
                    stock = aux;
                    if (stock == 0) setNoStockBackgroundColor();
                } else {
                    stock = 0;
                    setNoStockBackgroundColor();
                    quantitatTv.setText(String.valueOf(stock));
                }
            }
        }

        public void increaseQuantityByX(int x) {
            String number = quantitatTv.getText().toString();
            int aux = Integer.parseInt(number);
            if (x > 0) {
                if (aux == 0) this.setNormalBackgroundColor();
                aux += x;
                quantitatTv.setText(String.valueOf(aux));
                stock = aux;
            }
        }

        public void decreaseQuantityToZero() {
            quantitatTv.setText(String.valueOf("0"));
            stock = 0;
            this.setNoStockBackgroundColor();
        }

        public void setNoStockBackgroundColor() {
            itemContainer.setBackgroundColor(Color.parseColor("#FFCDD2")); //Light red
        }

        public void setNormalBackgroundColor() {
            itemContainer.setBackgroundColor(Color.parseColor("#FAFAFA"));   //Default background color
        }
    }
}

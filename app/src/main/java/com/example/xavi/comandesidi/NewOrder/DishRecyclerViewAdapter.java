package com.example.xavi.comandesidi.NewOrder;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.xavi.comandesidi.NewOrder.DishItemFragment.OnListFragmentInteractionListener;
import com.example.xavi.comandesidi.R;
import com.example.xavi.comandesidi.DBWrappers.DishesContainer;
import com.example.xavi.comandesidi.Utils.ConstantValues;
import com.example.xavi.comandesidi.Utils.BitmapUtils;

import java.util.ArrayList;
import java.util.List;


public class DishRecyclerViewAdapter extends RecyclerView.Adapter<DishRecyclerViewAdapter.DishViewHolder> {

    private List<DishesContainer.Dish> dishList;
    private final OnListFragmentInteractionListener mListener;
    private Context context;
    private DishViewHolder lastClickedView;
    private List<DishViewHolder> dishViewHolderList;

    public DishViewHolder getLastClickedView() {
        return lastClickedView;
    }

    public void setLastClickedView(DishViewHolder lastClickedView) {
        this.lastClickedView = lastClickedView;
    }

    public double getTotalPrice(){
        double price = 0;
        for(DishViewHolder holder: dishViewHolderList){
            price += holder.quantity * holder.dish.price;
        }
        return price;
    }

    public List<DishesContainer.Dish> getUpdatedDishes(){
        List<DishesContainer.Dish> updatedDishes = new ArrayList<>();
        for(DishViewHolder holder: dishViewHolderList){
            if (holder.checkIfOrdered()) {
                holder.updateOrderedProduct();
                updatedDishes.add(holder.dish);
            }
        }
        return updatedDishes;
    }

    public void resetView(){
        for(DishViewHolder holder: dishViewHolderList){
            holder.decreaseQuantityToZero();
        }
    }

    public DishRecyclerViewAdapter(OnListFragmentInteractionListener listener, Context context) {
        DishesContainer dishesContainer = DishesContainer.getInstance(context);
        dishList = dishesContainer.getDishList();
        mListener = listener;
        this.context = context;
        dishViewHolderList = new ArrayList<>();
    }

    @Override
    public DishViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_item, parent, false);
        return new DishViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final DishViewHolder holder, int position) {
        holder.setDish(dishList.get(position));
        setImageView(holder, dishList.get(position));
        setHolderOnClickListener(holder);
        setHolderOnLongClickListener(holder);
        dishViewHolderList.add(holder);
    }

    private void setImageView(final DishViewHolder holder, DishesContainer.Dish dish) {
        Bitmap bitmap;
        if(dish.hasImage){
            bitmap = BitmapUtils.getBitmapFromUri(dish.imgUri, context);
        } else {
            bitmap = BitmapFactory.decodeResource(context.getResources(), dish.mipmapId);
        }
        Bitmap round = BitmapUtils.getRoundedShape(bitmap, 128);
        holder.imageView.setImageBitmap(round);
    }

    private void setHolderOnClickListener(final DishViewHolder holder) {
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    //mListener.onListFragmentInteraction(holder.dish);
                    if (holder.isStockSufficient()) holder.increaseQuantityByOne();
                    else {
                        holder.setOutOfStockBackgroundColor();
                        mListener.onListFragmentInteraction(holder.dish);
                    }
                }
            }
        });
    }

    private void setHolderOnLongClickListener(final DishViewHolder holder) {
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
//                setPosition(holder.getPosition());
                setLastClickedView(holder);
                return false;
            }
        });
    }

    @Override
    public void onViewRecycled(DishViewHolder holder) {
        holder.itemView.setOnLongClickListener(null);
        super.onViewRecycled(holder);
    }

    @Override
    public int getItemCount() {
        return dishList.size();
    }


    public class DishViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {
        public final View mView;
        public final LinearLayout itemContainer;
        public final TextView nameTv;
        public final TextView priceTv;
        public TextView quantitatTv;
        public final TextView xTv;
        public ImageView imageView;
        public DishesContainer.Dish dish;
        public int quantity;
        public int stock;

        public DishViewHolder(View view) {
            super(view);
            itemContainer = (LinearLayout) view.findViewById(R.id.item_container);
            mView = view;
            xTv = (TextView) view.findViewById(R.id.textViewX);
            nameTv = (TextView) view.findViewById(R.id.product_name);
            priceTv = (TextView) view.findViewById(R.id.product_price);
            quantitatTv = (TextView) view.findViewById(R.id.quantitat);
            imageView = (ImageView) view.findViewById(R.id.listImageView);
            priceTv.setTextColor(Color.argb(ConstantValues.alpha, 0, 0, 0));
            quantity = 0;
            view.setOnCreateContextMenuListener(this);
        }

        public void setDish(DishesContainer.Dish dish) {
            this.dish = dish;
            this.stock = dish.stock;
            if (stock == 0) setOutOfStockBackgroundColor();
            this.nameTv.setText(dish.name);
            String priceStr = String.valueOf(dish.price) + " €";
            this.priceTv.setText(priceStr);
        }

        public boolean checkIfOrdered(){
            return quantity != 0;
        }

        public void updateOrderedProduct(){
            stock -= quantity;
            dish.stock = stock;
        }

        public boolean isStockSufficient(){
            return stock > quantity;
        }

        public boolean isStockSufficient(int quantity){
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
                    if(!isStockSufficient()) setSelectionedBackgroundColor();
                    int i = Integer.parseInt(s);
                    //i--;
                    quantity = i - 1;
                    quantitatTv.setText(String.valueOf(quantity));
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
                //i++;
                quantity = i + 1;
                quantitatTv.setText(String.valueOf(quantity));
                //quantity = i;
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
            //Needed by it's fragmet context menu
        }
    }
}

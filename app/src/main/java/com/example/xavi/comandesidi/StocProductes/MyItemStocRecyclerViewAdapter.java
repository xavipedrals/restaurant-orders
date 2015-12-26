package com.example.xavi.comandesidi.StocProductes;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.xavi.comandesidi.R;
import com.example.xavi.comandesidi.domini.ProductsContainer;
import com.example.xavi.comandesidi.widgets.ImageHelper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class MyItemStocRecyclerViewAdapter extends RecyclerView.Adapter<MyItemStocRecyclerViewAdapter.ViewHolder> {

    private List<ProductsContainer.Product> productList;
    private final ItemStocFragment.OnListFragmentInteractionListener mListener;
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

    public double getTotalPrice() {
        double price = 0;
        for (ViewHolder holder : viewHolderList) {
            price += holder.stock * holder.product.getPrice();
        }
        return price;
    }

    public void resetView() {
        for (ViewHolder holder : viewHolderList) {
            holder.decreaseQuantityToZero();
        }
    }

    public MyItemStocRecyclerViewAdapter(ProductsContainer productsContainer, ItemStocFragment.OnListFragmentInteractionListener listener, Context context) {
        productList = productsContainer.getProductList();
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
        holder.product = productList.get(position);
        holder.nameTv.setText(productList.get(position).getName());
        String priceStr = String.valueOf(productList.get(position).getPrice()) + " €";
        holder.priceTv.setText(priceStr);
        holder.stock = holder.product.getStock();
        if(holder.stock == 0) holder.setNoStockBackgroundColor();
        holder.quantitatTv.setText(String.valueOf(holder.stock));

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
                    //TODO: Fer dialog
                }
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

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final LinearLayout itemContainer;
        public final TextView nameTv;
        public final TextView priceTv;
        public TextView quantitatTv;
        public final TextView stocTv;
        public ImageView imageView;
        public ProductsContainer.Product product;
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
            int alpha = 54 * 255 / 100; //54% de opacitat, secondary text
            priceTv.setTextColor(Color.argb(alpha, 0, 0, 0));
        }

        public void decreaseQuantityByX(int x) {
            //TODO: Toasts control errors
            String s = quantitatTv.getText().toString();
            int aux = Integer.parseInt(s);
            if (aux > 0) {
                if (aux >= x){
                    aux -= x;
                    quantitatTv.setText(String.valueOf(aux));
                    stock = aux;
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

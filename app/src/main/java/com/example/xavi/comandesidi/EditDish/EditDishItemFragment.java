package com.example.xavi.comandesidi.EditDish;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.xavi.comandesidi.R;
import com.example.xavi.comandesidi.RecyclerItemClickListener;
import com.example.xavi.comandesidi.DBWrappers.ProductsContainer;

import java.util.List;

/**
 * A fragment representing a list of Items.
 * <p>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class EditDishItemFragment extends Fragment {

    private static final String ARG_COLUMN_COUNT = "column-count";
    private int mColumnCount = 1;
    private OnListFragmentInteractionListener mListener;
    private ProductsContainer productsContainer;
    private MyEditPlatItemRecyclerViewAdapter myEditPlatItemRecyclerViewAdapter;
    RecyclerView recyclerView;
    private Context context;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public EditDishItemFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        productsContainer = ProductsContainer.getInstance(getActivity().getApplicationContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_editplatitem_list, container, false);
        if (view instanceof RecyclerView) {
            context = view.getContext();
            recyclerView = (RecyclerView) view;
            setRecyclerViewItemTouchListener();
            manageRecyclerViewLayout();
            setRecyclerViewAdapter();
        }
        return view;
    }

    private void setRecyclerViewItemTouchListener() {
        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(context, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {
                        MyEditPlatItemRecyclerViewAdapter.ViewHolder v = (MyEditPlatItemRecyclerViewAdapter.ViewHolder) recyclerView.getChildViewHolder(view);
                        Bundle bundle = makeProductBundle(v.product);
                        Intent intent = new Intent(getActivity(), EditDishActivity.class);
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }
                })
        );
    }

    private Bundle makeProductBundle(ProductsContainer.Product product){
        Bundle bundle = new Bundle();
        bundle.putInt("mipmap", product.getMipmapId());
        bundle.putString("name", product.getName());
        bundle.putDouble("price", product.getPrice());
        bundle.putInt("id", product.getId());
        bundle.putBoolean("hasImage", product.hasImage());
        bundle.putString("image", product.getImgUri());
        return bundle;
    }

    private void manageRecyclerViewLayout() {
        if (mColumnCount <= 1) {
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
        } else {
            recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
        }
    }

    private void setRecyclerViewAdapter() {
        myEditPlatItemRecyclerViewAdapter = new MyEditPlatItemRecyclerViewAdapter(productsContainer, mListener, getActivity().getApplicationContext());
        recyclerView.setAdapter(myEditPlatItemRecyclerViewAdapter);
    }

    public void refreshAdapter(List<ProductsContainer.Product> products){
        myEditPlatItemRecyclerViewAdapter.refreshView(products);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnListFragmentInteractionListener {
        void onListFragmentInteraction(ProductsContainer.Product product);
    }
}

package com.example.xavi.comandesidi.EditDish;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.xavi.comandesidi.R;
import com.example.xavi.comandesidi.RecyclerItemClickListener;
import com.example.xavi.comandesidi.DBWrappers.DishesContainer;
import com.example.xavi.comandesidi.Utils.ItemFragmentUtils;

import java.util.List;

/**
 * A fragment representing a list of Items.
 * <p>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class EditDishItemFragment extends ItemFragmentUtils {

    private OnListFragmentInteractionListener mListener;
    private EditDishItemRecyclerViewAdapter editDishItemRecyclerViewAdapter;

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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_editplatitem_list, container, false);
        if (view instanceof RecyclerView) {
            super.context = view.getContext();
            super.recyclerView = (RecyclerView) view;
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
                        EditDishItemRecyclerViewAdapter.ViewHolder v = (EditDishItemRecyclerViewAdapter.ViewHolder) recyclerView.getChildViewHolder(view);
                        Bundle bundle = makeProductBundle(v.dish);
                        Intent intent = new Intent(getActivity(), EditDishActivity.class);
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }
                })
        );
    }

    private Bundle makeProductBundle(DishesContainer.Dish dish){
        Bundle bundle = new Bundle();
        bundle.putInt("mipmap", dish.mipmapId);
        bundle.putString("name", dish.name);
        bundle.putDouble("price", dish.price);
        bundle.putInt("id", dish.id);
        bundle.putBoolean("hasImage", dish.hasImage);
        bundle.putString("image", dish.imgUri);
        return bundle;
    }

    private void setRecyclerViewAdapter() {
        editDishItemRecyclerViewAdapter = new EditDishItemRecyclerViewAdapter(DishesContainer.getInstance(getActivity().getApplicationContext()), mListener, getActivity().getApplicationContext());
        recyclerView.setAdapter(editDishItemRecyclerViewAdapter);
    }

    public void refreshAdapter(List<DishesContainer.Dish> dishes){
        editDishItemRecyclerViewAdapter.refreshView(dishes);
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
        void onListFragmentInteraction(DishesContainer.Dish dish);
    }
}

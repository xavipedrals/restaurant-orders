package com.example.xavi.comandesidi.ListOrders;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.xavi.comandesidi.Utils.InfoDialog;
import com.example.xavi.comandesidi.R;
import com.example.xavi.comandesidi.DBWrappers.OrderContainer;
import com.example.xavi.comandesidi.Utils.ItemFragmentUtils;


/**
 * A fragment representing a list of Items.
 * <p>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class OrderItemFragment extends ItemFragmentUtils {

    private OnListFragmentInteractionListener orderItemfragmentInteractionListener;
    private OrderItemRecyclerViewAdapter orderItemRecyclerViewAdapter;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public OrderItemFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_comandaitem_list, container, false);
        if (view instanceof RecyclerView) {
            super.context = view.getContext();
            super.recyclerView = (RecyclerView) view;
            super.manageRecyclerViewLayout();
            setRecyclerViewAdapter();
        }
        return view;
    }

    private void setRecyclerViewAdapter() {
        OrderContainer orderContainer = OrderContainer.getInstance(getActivity().getApplicationContext());
        orderItemRecyclerViewAdapter = new OrderItemRecyclerViewAdapter(orderContainer.getOrderList(), orderItemfragmentInteractionListener);
        recyclerView.setAdapter(orderItemRecyclerViewAdapter);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            orderItemfragmentInteractionListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        orderItemfragmentInteractionListener = null;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.llistar_comandes_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_info){
            showInfoDialog();
        }
        return super.onOptionsItemSelected(item);
    }

    private void showInfoDialog() {
        InfoDialog infoDialog = new InfoDialog();
        infoDialog.setStyle(DialogFragment.STYLE_NO_TITLE, 0);
        Bundle bundle = makeInfoDialogBundle();
        infoDialog.setArguments(bundle);
        android.support.v4.app.FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        infoDialog.show(fragmentManager, "tag");
    }

    private Bundle makeInfoDialogBundle() {
        Bundle bundle = new Bundle();
        bundle.putString("Type", "See total");
        bundle.putDouble("price", orderItemRecyclerViewAdapter.getTotalPrice());
        return bundle;
    }

    public interface OnListFragmentInteractionListener {
        void onListFragmentInteraction(OrderContainer.Order order);
    }
}

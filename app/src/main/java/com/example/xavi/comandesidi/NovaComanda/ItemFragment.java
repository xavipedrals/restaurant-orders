package com.example.xavi.comandesidi.NovaComanda;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.xavi.comandesidi.IntrQuantDialog;
import com.example.xavi.comandesidi.R;
import com.example.xavi.comandesidi.RecyclerItemClickListener;
import com.example.xavi.comandesidi.domini.ProductsContainer;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class ItemFragment extends Fragment {

    private static final String ARG_COLUMN_COUNT = "column-count";
    private int mColumnCount = 1;
    private OnListFragmentInteractionListener mListener;
    private ProductsContainer productsContainer;
    private RecyclerView recyclerView;
    private MyItemRecyclerViewAdapter myItemRecyclerViewAdapter;


    public ItemFragment() {
        //Required empty constructor
    }

    public MyItemRecyclerViewAdapter getMyItemRecyclerViewAdapter() {
        return myItemRecyclerViewAdapter;
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
        View view = inflater.inflate(R.layout.fragment_item_list, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            recyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
            myItemRecyclerViewAdapter = new MyItemRecyclerViewAdapter(ProductsContainer.getInstance(getActivity().getApplicationContext()), mListener, getContext());
            recyclerView.setAdapter(myItemRecyclerViewAdapter);
        }
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        registerForContextMenu(recyclerView);
        super.onViewCreated(view, savedInstanceState);
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

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.nova_comanda_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_info) {
            Bundle b = new Bundle();
            b.putDouble("price", myItemRecyclerViewAdapter.getTotalPrice());
            b.putString("Type", "See price");
            InfoDialog infoDialog = new InfoDialog();
            android.support.v4.app.FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            infoDialog.setStyle(DialogFragment.STYLE_NO_TITLE, 0);
            infoDialog.setArguments(b);
            infoDialog.show(fragmentManager, "tag");
        } else if(id == R.id.menu_reset_comanda){
            myItemRecyclerViewAdapter.resetView();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getActivity().getMenuInflater();
        inflater.inflate(R.menu.nova_comanda_context, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        MyItemRecyclerViewAdapter.ViewHolder viewHolder = null;
        int position = -1;
        try {
            viewHolder = myItemRecyclerViewAdapter.getLastClickedView();
            position = myItemRecyclerViewAdapter.getPosition();
        } catch (Exception e) {
            Log.d("PUTAAA", e.getLocalizedMessage(), e);
            return super.onContextItemSelected(item);
        }
        switch (item.getItemId()) {
            case R.id.context_menys:
                viewHolder.decreaseQuantityByOne();
                break;
            case R.id.context_zero:
                viewHolder.decreaseQuantityToZero();
                break;
            case R.id.context_num_pers:
                IntrQuantDialog intrQuantDialog = new IntrQuantDialog();
                android.support.v4.app.FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                intrQuantDialog.setStyle(DialogFragment.STYLE_NO_TITLE, 0);
                final MyItemRecyclerViewAdapter.ViewHolder finalViewHolder = viewHolder;
                intrQuantDialog.setOnDialogResultListener(new IntrQuantDialog.OnDialogResultListener() {
                    @Override
                    public void onPositiveResult(int value) {
                        if (finalViewHolder.checkStock(value)) finalViewHolder.setExactQuantity(value);
                        else{
                            Toast.makeText(getActivity().getApplicationContext(), "Poducte sense stoc suficient", Toast.LENGTH_LONG).show();
                        }
                    }
                    @Override
                    public void onNegativeResult() {
                    }
                });
                intrQuantDialog.show(fragmentManager, "tag");
                break;
        }
        return super.onContextItemSelected(item);

    }

    public boolean checkIfPriceIsZero(){
        return (myItemRecyclerViewAdapter.getTotalPrice() == 0);
    }


    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnListFragmentInteractionListener {
        void onListFragmentInteraction(ProductsContainer.Product product);
    }
}

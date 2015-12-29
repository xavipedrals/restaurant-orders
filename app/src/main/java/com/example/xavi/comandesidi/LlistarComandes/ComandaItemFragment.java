package com.example.xavi.comandesidi.LlistarComandes;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.xavi.comandesidi.NovaComanda.InfoDialog;
import com.example.xavi.comandesidi.R;
import com.example.xavi.comandesidi.domini.ComandaContainer;


/**
 * A fragment representing a list of Items.
 * <p>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class ComandaItemFragment extends Fragment {

    private static final String ARG_COLUMN_COUNT = "column-count";
    private int mColumnCount = 1;
    private OnListFragmentInteractionListener mListener;
    private MyComandaItemRecyclerViewAdapter myComandaItemRecyclerViewAdapter;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ComandaItemFragment() {
    }

    @SuppressWarnings("unused")
    public static ComandaItemFragment newInstance(int columnCount) {
        ComandaItemFragment fragment = new ComandaItemFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_comandaitem_list, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
            ComandaContainer comandaContainer = ComandaContainer.getInstance(getActivity().getApplicationContext());
            myComandaItemRecyclerViewAdapter = new MyComandaItemRecyclerViewAdapter(comandaContainer.getComandaList(), mListener);
            recyclerView.setAdapter(myComandaItemRecyclerViewAdapter);
        }
        return view;
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
        inflater.inflate(R.menu.llistar_comandes_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_filter) {
            DatePickerFragment datePickerFragment = new DatePickerFragment();
            datePickerFragment.setStyle(DialogFragment.STYLE_NO_TITLE, 0);
            android.support.v4.app.FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            datePickerFragment.show(fragmentManager, "tag");
        } else if (id == R.id.action_info){
            InfoDialog infoDialog = new InfoDialog();
            infoDialog.setStyle(DialogFragment.STYLE_NO_TITLE, 0);
            Bundle bundle = new Bundle();
            bundle.putString("Type", "See total");
            bundle.putDouble("price", myComandaItemRecyclerViewAdapter.getTotalPrice());
            infoDialog.setArguments(bundle);
            android.support.v4.app.FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            infoDialog.show(fragmentManager, "tag");
        }
        return super.onOptionsItemSelected(item);
    }

    public interface OnListFragmentInteractionListener {
        void onListFragmentInteraction(ComandaContainer.Comanda comanda);
    }
}

package com.example.xavi.comandesidi.EditarPlats;

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

import com.example.xavi.comandesidi.EditPlatActivity;
import com.example.xavi.comandesidi.R;
import com.example.xavi.comandesidi.EditarPlats.dummy.DummyContent;
import com.example.xavi.comandesidi.EditarPlats.dummy.DummyContent.DummyItem;
import com.example.xavi.comandesidi.RecyclerItemClickListener;
import com.example.xavi.comandesidi.domini.ProductsContainer;

import java.util.List;

/**
 * A fragment representing a list of Items.
 * <p>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class EditPlatItemFragment extends Fragment {

    private static final String ARG_COLUMN_COUNT = "column-count";
    private int mColumnCount = 1;
    private OnListFragmentInteractionListener mListener;
    private ProductsContainer productsContainer;
    private MyEditPlatItemRecyclerViewAdapter myEditPlatItemRecyclerViewAdapter;
    RecyclerView recyclerView;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public EditPlatItemFragment() {
    }

    public static EditPlatItemFragment newInstance(int columnCount) {
        EditPlatItemFragment fragment = new EditPlatItemFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
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

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            recyclerView = (RecyclerView) view;
            recyclerView.addOnItemTouchListener(
                    new RecyclerItemClickListener(context, new RecyclerItemClickListener.OnItemClickListener() {
                        @Override public void onItemClick(View view, int position) {
                            MyEditPlatItemRecyclerViewAdapter.ViewHolder v = (MyEditPlatItemRecyclerViewAdapter.ViewHolder) recyclerView.getChildViewHolder(view);
                            Bundle bundle = new Bundle();
                            bundle.putInt("mipmap", v.product.getMipmapId());
                            bundle.putString("name", v.product.getName());
                            bundle.putDouble("price", v.product.getPrice());
                            Intent intent = new Intent(getActivity(), EditPlatActivity.class);
                            intent.putExtras(bundle);
                            startActivity(intent);
                        }
                    })
            );
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
            myEditPlatItemRecyclerViewAdapter = new MyEditPlatItemRecyclerViewAdapter(productsContainer, mListener, getActivity().getApplicationContext());
            recyclerView.setAdapter(myEditPlatItemRecyclerViewAdapter);
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

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        void onListFragmentInteraction(ProductsContainer.Product product);
    }
}

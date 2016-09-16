package com.example.xavi.comandesidi.StocProductes;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.xavi.comandesidi.R;
import com.example.xavi.comandesidi.RecyclerItemClickListener;
import com.example.xavi.comandesidi.DBManager.DBManager;
import com.example.xavi.comandesidi.DBWrappers.DishesContainer;
import com.example.xavi.comandesidi.Utils.ItemFragmentUtils;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class DishStockItemFragment extends ItemFragmentUtils {

    private OnListFragmentInteractionListener mListener;
//    private DishesContainer dishesContainer;
    private DishStockItemRecyclerViewAdapter dishStockItemRecyclerViewAdapter;

    public DishStockItemFragment() {
        //Required empty constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        dishesContainer = DishesContainer.getInstance(getActivity().getApplicationContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_itemstoc_list, container, false);
        if (view instanceof RecyclerView) {
            super.context = view.getContext();
            super.recyclerView = (RecyclerView) view;
            setRecyclerViewClickListener();
            super.manageRecyclerViewLayout();
            setRecyclerViewAdapter();
        }
        return view;
    }

    private void setRecyclerViewAdapter() {
        dishStockItemRecyclerViewAdapter = new DishStockItemRecyclerViewAdapter(DishesContainer.getInstance(getActivity().getApplicationContext()), mListener, getContext());
        recyclerView.setAdapter(dishStockItemRecyclerViewAdapter);
    }

    private void setRecyclerViewClickListener() {
        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(context, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(final View view, int position) {
                        showStockDialog(view);
                    }
                })
        );
    }

    private void showStockDialog(View view) {
        EditStockDialog editStockDialog = new EditStockDialog();
        android.support.v4.app.FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        editStockDialog.setStyle(DialogFragment.STYLE_NO_TITLE, 0);
        setStockDialogResultListener(editStockDialog, view);
        editStockDialog.show(fragmentManager, "tag");
    }

    private void setStockDialogResultListener(EditStockDialog editStockDialog, final View view) {
        editStockDialog.setOnStockDialogResultListener(new EditStockDialog.OnStockDialogResultListener() {
            @Override
            public void onPositiveResult(Bundle bundle) {
                manageStockDialogPositiveResult(view, bundle);
            }
            @Override
            public void onNegativeResult() {
            }
        });
    }

    private void manageStockDialogPositiveResult(View view, Bundle bundle) {
        DishStockItemRecyclerViewAdapter.ViewHolder v = (DishStockItemRecyclerViewAdapter.ViewHolder) recyclerView.getChildViewHolder(view);
        String opcio = bundle.getString("opcio", "");
        if (opcio.equals("zero")){
            v.decreaseQuantityToZero();
        } else if (opcio.equals("incrementar")){
            v.increaseQuantityByX(bundle.getInt("quantitat"));
        } else if (opcio.equals("decrementar")){
            v.decreaseQuantityByX(bundle.getInt("quantitat"));
        }
        DBManager.getInstance(getActivity().getApplicationContext()).updateDish(v.dish.id, v.stock);
        DishesContainer.refresh(getActivity().getApplicationContext());
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

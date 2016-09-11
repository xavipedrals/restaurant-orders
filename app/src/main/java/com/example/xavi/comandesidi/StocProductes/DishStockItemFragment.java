package com.example.xavi.comandesidi.StocProductes;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.xavi.comandesidi.R;
import com.example.xavi.comandesidi.RecyclerItemClickListener;
import com.example.xavi.comandesidi.DBManager.DBManager;
import com.example.xavi.comandesidi.DBWrappers.DishesContainer;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class DishStockItemFragment extends Fragment {

    private static final String ARG_COLUMN_COUNT = "column-count";
    private int mColumnCount = 1;
    private OnListFragmentInteractionListener mListener;
    private DishesContainer dishesContainer;
    private RecyclerView recyclerView;
    private Context context;
    private DishStockItemRecyclerViewAdapter dishStockItemRecyclerViewAdapter;


    public DishStockItemFragment() {
        //Required empty constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dishesContainer = DishesContainer.getInstance(getActivity().getApplicationContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_itemstoc_list, container, false);
        if (view instanceof RecyclerView) {
            context = view.getContext();
            recyclerView = (RecyclerView) view;
            setRecyclerViewClickListener();
            manageRecyclerViewLayout();
            setRecyclerViewAdapter();
        }
        return view;
    }

    private void manageRecyclerViewLayout() {
        if (mColumnCount <= 1) {
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
        } else {
            recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
        }
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
        StockDialog stockDialog = new StockDialog();
        android.support.v4.app.FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        stockDialog.setStyle(DialogFragment.STYLE_NO_TITLE, 0);
        setStockDialogResultListener(stockDialog, view);
        stockDialog.show(fragmentManager, "tag");
    }

    private void setStockDialogResultListener(StockDialog stockDialog, final View view) {
        stockDialog.setOnStockDialogResultListener(new StockDialog.OnStockDialogResultListener() {
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
        DBManager.getInstance(getActivity().getApplicationContext()).updatePlat(v.dish.id, v.stock);
        DishesContainer.refresh(getActivity().getApplicationContext());
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
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

    public interface OnListFragmentInteractionListener {
        void onListFragmentInteraction(DishesContainer.Dish dish);
    }
}

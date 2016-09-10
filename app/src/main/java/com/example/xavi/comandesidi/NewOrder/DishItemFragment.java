package com.example.xavi.comandesidi.NewOrder;

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
import com.example.xavi.comandesidi.DBManager.DBManager;
import com.example.xavi.comandesidi.DBWrappers.DishesContainer;

import java.util.List;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class DishItemFragment extends Fragment {

    private static final String ARG_COLUMN_COUNT = "column-count";
    private int mColumnCount = 1;
    private OnListFragmentInteractionListener mListener;
    private DishesContainer dishesContainer;
    private RecyclerView recyclerView;
    private Context context;
    private DishRecyclerViewAdapter dishRecyclerViewAdapter;

    public DishItemFragment() {
        //Required empty constructor
    }

    public DishRecyclerViewAdapter getDishRecyclerViewAdapter() {
        return dishRecyclerViewAdapter;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        dishesContainer = DishesContainer.getInstance(getActivity().getApplicationContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_item_list, container, false);
        if (view instanceof RecyclerView) {
            context = view.getContext();
            recyclerView = (RecyclerView) view;
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
        dishRecyclerViewAdapter = new DishRecyclerViewAdapter(DishesContainer.getInstance(getActivity().getApplicationContext()), mListener, getContext());
        recyclerView.setAdapter(dishRecyclerViewAdapter);
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
            showInfoDialog();
        } else if(id == R.id.menu_reset_comanda){
            dishRecyclerViewAdapter.resetView();
        }
        return super.onOptionsItemSelected(item);
    }

    private void showInfoDialog() {
        InfoDialog infoDialog = new InfoDialog();
        android.support.v4.app.FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        infoDialog.setStyle(DialogFragment.STYLE_NO_TITLE, 0);
        Bundle bundle = makeInfoDialogBundle();
        infoDialog.setArguments(bundle);
        infoDialog.show(fragmentManager, "tag");
    }

    private Bundle makeInfoDialogBundle() {
        Bundle bundle = new Bundle();
        bundle.putDouble("price", dishRecyclerViewAdapter.getTotalPrice());
        bundle.putString("Type", "See price");
        return bundle;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getActivity().getMenuInflater();
        inflater.inflate(R.menu.nova_comanda_context, menu);
    }

    /**Menu that appears in new command after making a long click on a dish**/
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        DishRecyclerViewAdapter.ViewHolder viewHolder = getlastClickedViewHolder();

        switch (item.getItemId()) {
            case R.id.contextualMenuDecrease:
                viewHolder.decreaseQuantityByOne();
                break;
            case R.id.contextualMenuSetZero:
                viewHolder.decreaseQuantityToZero();
                break;
            case R.id.contextualMenuSetCustomNumber:
                IntrQuantDialog intrQuantDialog = new IntrQuantDialog();
                android.support.v4.app.FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                intrQuantDialog.setStyle(DialogFragment.STYLE_NO_TITLE, 0);
                final DishRecyclerViewAdapter.ViewHolder finalViewHolder = viewHolder;
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

    public DishRecyclerViewAdapter.ViewHolder getlastClickedViewHolder() {
        DishRecyclerViewAdapter.ViewHolder viewHolder = null;
        try {
            viewHolder = dishRecyclerViewAdapter.getLastClickedView();
        } catch (Exception e) {
            Log.d("EXCEPTION", e.getLocalizedMessage(), e);
        }
        return viewHolder;
    }

    public boolean checkIfPriceIsZero(){
        return (dishRecyclerViewAdapter.getTotalPrice() == 0);
    }

    public void updateStockDb(){
        List<DishesContainer.Dish> dishList = dishRecyclerViewAdapter.getProductesActualitzats();
        for (DishesContainer.Dish dish : dishList){
            DBManager.getInstance(getActivity().getApplicationContext()).updatePlat(dish.name, dish.stock);
        }
    }

    public interface OnListFragmentInteractionListener {
        void onListFragmentInteraction(DishesContainer.Dish dish);
    }
}

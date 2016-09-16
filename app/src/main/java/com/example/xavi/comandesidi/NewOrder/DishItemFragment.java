package com.example.xavi.comandesidi.NewOrder;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
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

import com.example.xavi.comandesidi.IntroduceQuantityDialog;
import com.example.xavi.comandesidi.R;
import com.example.xavi.comandesidi.DBManager.DBManager;
import com.example.xavi.comandesidi.DBWrappers.DishesContainer;
import com.example.xavi.comandesidi.Utils.InfoDialog;
import com.example.xavi.comandesidi.Utils.ItemFragmentUtils;

import java.util.List;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class DishItemFragment extends ItemFragmentUtils {

    private OnListFragmentInteractionListener mListener;
//    private DishesContainer dishesContainer;
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
//        dishesContainer = DishesContainer.getInstance(getActivity().getApplicationContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_item_list, container, false);
        if (view instanceof RecyclerView) {
            super.context = view.getContext();
            super.recyclerView = (RecyclerView) view;
            manageRecyclerViewLayout();
            setRecyclerViewAdapter();
        }
        return view;
    }

    private void setRecyclerViewAdapter() {
        dishRecyclerViewAdapter = new DishRecyclerViewAdapter(mListener, getContext());
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
                showIntroduceQuantityDialog(viewHolder);
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

    private void showIntroduceQuantityDialog(DishRecyclerViewAdapter.ViewHolder viewHolder) {
        IntroduceQuantityDialog introduceQuantityDialog = new IntroduceQuantityDialog();
        android.support.v4.app.FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        introduceQuantityDialog.setStyle(DialogFragment.STYLE_NO_TITLE, 0);
        setResultListenerIntrQuantDialog(introduceQuantityDialog, viewHolder);
        introduceQuantityDialog.show(fragmentManager, "tag");
    }

    private void setResultListenerIntrQuantDialog(IntroduceQuantityDialog introduceQuantityDialog, final DishRecyclerViewAdapter.ViewHolder viewHolder) {
        introduceQuantityDialog.setOnDialogResultListener(new IntroduceQuantityDialog.OnDialogResultListener() {
            @Override
            public void onPositiveResult(int value) {
                if (viewHolder.checkStock(value)) viewHolder.setExactQuantity(value);
                else {
                    Toast.makeText(getActivity().getApplicationContext(), "Product without enough stock", Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onNegativeResult() {
            }
        });
    }

    public boolean checkIfPriceIsZero(){
        return (dishRecyclerViewAdapter.getTotalPrice() == 0);
    }

    public void updateStockDb(){
        List<DishesContainer.Dish> dishList = dishRecyclerViewAdapter.getProductesActualitzats();
        for (DishesContainer.Dish dish : dishList){
            DBManager.getInstance(getActivity().getApplicationContext()).updateDish(dish.name, dish.stock);
        }
    }

    public interface OnListFragmentInteractionListener {
        void onListFragmentInteraction(DishesContainer.Dish dish);
    }
}

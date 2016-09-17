package com.example.xavi.comandesidi;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.xavi.comandesidi.EditDish.CreateDishDialog;
import com.example.xavi.comandesidi.EditDish.EditDishItemFragment;
import com.example.xavi.comandesidi.ListOrders.OrderItemFragment;
import com.example.xavi.comandesidi.Utils.InfoDialog;
import com.example.xavi.comandesidi.NewOrder.DishItemFragment;
import com.example.xavi.comandesidi.NewOrder.TableDialog;
import com.example.xavi.comandesidi.StocProductes.DishStockItemFragment;
import com.example.xavi.comandesidi.DBManager.DBManager;
import com.example.xavi.comandesidi.DBWrappers.OrderContainer;
import com.example.xavi.comandesidi.DBWrappers.DishesContainer;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, DishItemFragment.OnListFragmentInteractionListener,
        OrderItemFragment.OnListFragmentInteractionListener, EditDishItemFragment.OnListFragmentInteractionListener,
        DishStockItemFragment.OnListFragmentInteractionListener {

    private Toolbar toolbar;
    private DrawerLayout drawer;
    private NavigationView navigationView;
    private ActionBarDrawerToggle toggle;
    private FloatingActionButton fab;
    private int actualFragment;
    private DishItemFragment dishItemFragment;
    private EditDishItemFragment editDishItemFragment;
    private TextView titleTv;
    private TextView emailTv;
    private LinearLayout linearLayout;

    private final int NEW_ORDER_FRAGMENT = 1;
    public static final int EDIT_DISHES_FRAGMENT = 2;
    private final int LIST_ORDERS_FRAGMENT = 3;
    private final int DISHES_STOCK_FRAGMENT = 4;
    private final int SETTINGS_FRAGMENT = 5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initVisuals();
        if (getIntent().hasExtra("Fragment")) toolbar.setTitle("Edit Dishes");
        else toolbar.setTitle("New order");
        setSupportActionBar(toolbar);
        setFloatingButtonClickListener();

        toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);

        configNavigationHeaderView();

//        boolean fisrtLaunch = prefs.getBoolean("FirstLaunch", true);
        if (isFirstLaunch()){
            manageFirstLaunch();
        }
        else {
            if (getIntent().hasExtra("Fragment")) {
                Bundle b = getIntent().getExtras();
                if (b.getInt("Fragment") == EDIT_DISHES_FRAGMENT) {
                    actualFragment = EDIT_DISHES_FRAGMENT;
                    configureFab(EDIT_DISHES_FRAGMENT);
                    editDishItemFragment = new EditDishItemFragment();
                    getSupportFragmentManager().beginTransaction().replace(R.id.mainLayout, editDishItemFragment).commit();
                }
            } else {
                manageDefaultStart();
            }
        }
    }

    private void initVisuals() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        View header = navigationView.getHeaderView(0);
        titleTv = (TextView) header.findViewById(R.id.navDrawerTitle);
        emailTv = (TextView) header.findViewById(R.id.navDrawerEmail);
        linearLayout = (LinearLayout) header.findViewById(R.id.navBackground);
    }

    private void setFloatingButtonClickListener() {
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (actualFragment) {
                    case NEW_ORDER_FRAGMENT:
                        if (dishItemFragment.checkIfPriceIsZero()) {
                            showNoProductsSelectedDialog();
                        }
                        else {
                            showTableDialog();
                        }
                        break;
                    case EDIT_DISHES_FRAGMENT:
                        showCreateDishDialog();
                        break;
                    default:
                        break;
                }
            }
        });
    }

    private void showNoProductsSelectedDialog() {
        Bundle b = new Bundle();
        b.putString("Type", "No products selected");
        InfoDialog infoDialog = new InfoDialog();
        android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
        infoDialog.setStyle(DialogFragment.STYLE_NO_TITLE, 0);
        infoDialog.setArguments(b);
        infoDialog.show(fragmentManager, "tag");
    }

    private void showTableDialog() {
        TableDialog tableDialog = new TableDialog();
        android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
        tableDialog.setStyle(DialogFragment.STYLE_NO_TITLE, 0);
        tableDialog.setOnTableDialogResultListener(new TableDialog.OnTableDialogResultListener() {
            @Override
            public void onPositiveResult(int numTaula) {
                double price = dishItemFragment.getDishRecyclerViewAdapter().getTotalPrice();
                SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy hh:mm a");
                Date date = new Date();
                String dateStr = df.format(date);
                DBManager.getInstance(getApplicationContext()).insertOrder(price, dateStr, numTaula);
                OrderContainer.refresh(getApplicationContext());
                dishItemFragment.updateStockDb();
                DishesContainer.refresh(getApplicationContext());
                dishItemFragment.getDishRecyclerViewAdapter().resetView();
                Toast.makeText(getApplicationContext(), "Comanda tramitada", Toast.LENGTH_LONG).show();
            }
            @Override
            public void onNegativeResult() {
                //Do nothing
            }
        });
        tableDialog.show(fragmentManager, "tag");
    }

    public void showCreateDishDialog() {
        CreateDishDialog createDishDialog = new CreateDishDialog();
        android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
        createDishDialog.setStyle(DialogFragment.STYLE_NO_TITLE, 0);
        createDishDialog.setOnCreatePlatDialogResultListener(new CreateDishDialog.OnCreatePlatDialogResultListener() {
            @Override
            public void onPositiveResult(Bundle bundle) {
                String name = bundle.getString("name");
                double price = bundle.getDouble("price");
                int stock = bundle.getInt("stock");
                String imgUri = bundle.getString("imgUri");
                DBManager.getInstance(getApplicationContext()).insertDish(imgUri, price, name, stock);
                DishesContainer.refresh(getApplicationContext());
                editDishItemFragment.refreshAdapter(DishesContainer.getInstance(getApplicationContext()).getDishList());
            }
            @Override
            public void onNegativeResult() {
            }
        });
        createDishDialog.show(fragmentManager, "tag");
    }

    private void configNavigationHeaderView() {
        SharedPreferences prefs = this.getSharedPreferences("com.example.app", Context.MODE_PRIVATE);
        String restaurantName = prefs.getString("NomRestaurant", getResources().getString(R.string.nav_drawer_title));
        String restaurantEmail = prefs.getString("EmailRestaurant", getResources().getString(R.string.nav_drawer_email));
        String backgroundUri = prefs.getString("BackgroundUri", "");
        titleTv.setText(restaurantName);
        emailTv.setText(restaurantEmail);
        if (!backgroundUri.equals("")) {
            Uri uri = Uri.parse(backgroundUri);
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                BitmapDrawable bitmapDrawable = new BitmapDrawable(bitmap);
                linearLayout.setBackground(bitmapDrawable);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private boolean isFirstLaunch() {
        SharedPreferences prefs = this.getSharedPreferences("com.example.app", Context.MODE_PRIVATE);
        return prefs.getBoolean("FirstLaunch", true);
    }

    private void manageFirstLaunch() {
        SharedPreferences prefs = this.getSharedPreferences("com.example.app", Context.MODE_PRIVATE);
        prefs.edit().putBoolean("FirstLaunch", false).apply();
        DishesContainer.initInstanceWithStubs(getApplicationContext());
        OrderContainer.initInstanceWithStubs(getApplicationContext());
        setToolbarTitle("Help");
        configureFab(SETTINGS_FRAGMENT);
        HelpFragment f = new HelpFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.mainLayout, f).commit();
    }

    private void setToolbarTitle(String title){
        toolbar.setTitle(title);
        setSupportActionBar(toolbar);
        toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
    }

    private void manageDefaultStart() {
        actualFragment = NEW_ORDER_FRAGMENT;
        configureFab(NEW_ORDER_FRAGMENT);
        dishItemFragment = new DishItemFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.mainLayout, dishItemFragment).commit();
    }

    private void configureFab(int fragmentTag){
        switch (fragmentTag){
            case NEW_ORDER_FRAGMENT:
                fab.setImageDrawable(getResources().getDrawable(R.mipmap.ic_done_white_64dp_1x));
                fab.setVisibility(View.VISIBLE);
                break;
            case EDIT_DISHES_FRAGMENT:
                fab.setImageDrawable(getResources().getDrawable(R.mipmap.ic_add_white_48dp));
                fab.setVisibility(View.VISIBLE);
                break;
            case LIST_ORDERS_FRAGMENT:
                fab.setVisibility(View.GONE);
                break;
            case DISHES_STOCK_FRAGMENT:
                fab.setVisibility(View.GONE);
                break;
            case SETTINGS_FRAGMENT:
                fab.setVisibility(View.GONE);
                break;
        }
        actualFragment = fragmentTag;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.nav_nova_comanda) {
            showNewOrderFragment();
        }
        else if (id == R.id.nav_editar_plats) {
            showEditDishes();
        }
        else if (id == R.id.nav_llistat_comandes) {
            showListOrdersFragments();
        }
        else if (id == R.id.nav_stoc_productes) {
            showDishesStockFragment();
        }
        else if (id == R.id.nav_config) {
            startActivity(new Intent(MainActivity.this, ConfigActivity.class));
        }
        else if (id == R.id.nav_ajuda){
            showHelpFragment();
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void showNewOrderFragment() {
        setToolbarTitle("New order");
        configureFab(NEW_ORDER_FRAGMENT);
        dishItemFragment = new DishItemFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.mainLayout, dishItemFragment).commit();
    }

    private void showEditDishes() {
        setToolbarTitle("Edit dishes");
        configureFab(EDIT_DISHES_FRAGMENT);
        editDishItemFragment = new EditDishItemFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.mainLayout, editDishItemFragment).commit();
    }

    private void showListOrdersFragments() {
        setToolbarTitle("List orders");
        configureFab(LIST_ORDERS_FRAGMENT);
        OrderItemFragment orderItemFragment = new OrderItemFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.mainLayout, orderItemFragment).commit();
    }

    private void showDishesStockFragment() {
        setToolbarTitle("Dishes stock");
        configureFab(DISHES_STOCK_FRAGMENT);
        DishStockItemFragment f = new DishStockItemFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.mainLayout, f).commit();
    }

    private void showHelpFragment() {
        setToolbarTitle("Help");
        configureFab(SETTINGS_FRAGMENT);
        HelpFragment f = new HelpFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.mainLayout, f).commit();
    }

    @Override
    public void onListFragmentInteraction(DishesContainer.Dish dish) {
        Toast.makeText(getApplicationContext(), "Dish out of stock", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onListFragmentInteraction(OrderContainer.Order order) {

    }
}

package com.example.xavi.comandesidi;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
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

import com.example.xavi.comandesidi.EditDish.CrearPlatDialog;
import com.example.xavi.comandesidi.EditDish.EditDishItemFragment;
import com.example.xavi.comandesidi.ListOrders.OrderItemFragment;
import com.example.xavi.comandesidi.NewOrder.InfoDialog;
import com.example.xavi.comandesidi.NewOrder.DishItemFragment;
import com.example.xavi.comandesidi.NewOrder.TableDialog;
import com.example.xavi.comandesidi.StocProductes.ItemStocFragment;
import com.example.xavi.comandesidi.DBManager.DBManager;
import com.example.xavi.comandesidi.DBWrappers.OrderContainer;
import com.example.xavi.comandesidi.DBWrappers.DishesContainer;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, DishItemFragment.OnListFragmentInteractionListener,
        OrderItemFragment.OnListFragmentInteractionListener, EditDishItemFragment.OnListFragmentInteractionListener,
        ItemStocFragment.OnListFragmentInteractionListener {

    private Toolbar toolbar;
    private DrawerLayout drawer;
    private NavigationView navigationView;
    private ActionBarDrawerToggle toggle;
    private FloatingActionButton fab;
    private int actualFragment;
    private DishItemFragment dishItemFragment;
    private EditDishItemFragment editDishItemFragment;

    private final int NOVA_COMANDA_FRAGMENT = 1;
    public static final int EDITAR_PLATS_FRAGMENT = 2;
    private final int LLISTAR_COMANDES_FRAGMENT = 3;
    private final int STOC_PRODUCTES_FRAGMENT = 4;
    private final int CONFIGURACIO_FRAGMENT = 5;

    private void configureFab(int fragmentTag){
        switch (fragmentTag){
            case NOVA_COMANDA_FRAGMENT:
                //fab.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#64DD17"))); //Verd
                fab.setImageDrawable(getResources().getDrawable(R.mipmap.ic_done_white_64dp_1x));
                fab.setVisibility(View.VISIBLE);
                break;
            case EDITAR_PLATS_FRAGMENT:
                fab.setImageDrawable(getResources().getDrawable(R.mipmap.ic_add_white_48dp));
                fab.setVisibility(View.VISIBLE);
                break;
            case LLISTAR_COMANDES_FRAGMENT:
                fab.setVisibility(View.GONE);
                break;
            case STOC_PRODUCTES_FRAGMENT:
                fab.setVisibility(View.GONE);
                break;
            case CONFIGURACIO_FRAGMENT:
                fab.setVisibility(View.GONE);
                break;
        }
        actualFragment = fragmentTag;
    }

    private void setToolbarTitle(String title){
        toolbar.setTitle(title);
        setSupportActionBar(toolbar);
        toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (getIntent().hasExtra("Fragment")) toolbar.setTitle("Editar Plats");
        else toolbar.setTitle("Nova comanda");
        setSupportActionBar(toolbar);

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (actualFragment) {
                    case NOVA_COMANDA_FRAGMENT:
                        if (dishItemFragment.checkIfPriceIsZero()) {
                            Bundle b = new Bundle();
                            b.putString("Type", "No products selected");
                            InfoDialog infoDialog = new InfoDialog();
                            android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
                            infoDialog.setStyle(DialogFragment.STYLE_NO_TITLE, 0);
                            infoDialog.setArguments(b);
                            infoDialog.show(fragmentManager, "tag");
                        } else {
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
                                    DBManager.getInstance(getApplicationContext()).insertComanda(price, dateStr, numTaula);
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
                        break;
                    case EDITAR_PLATS_FRAGMENT:
                        CrearPlatDialog crearPlatDialog = new CrearPlatDialog();
                        android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
                        crearPlatDialog.setStyle(DialogFragment.STYLE_NO_TITLE, 0);
                        crearPlatDialog.setOnCreatePlatDialogResultListener(new CrearPlatDialog.OnCreatePlatDialogResultListener() {
                            @Override
                            public void onPositiveResult(Bundle bundle) {
                                String name = bundle.getString("name");
                                double price = bundle.getDouble("price");
                                int stock = bundle.getInt("stock");
                                String imgUri = bundle.getString("imgUri");
                                DBManager.getInstance(getApplicationContext()).insertPlat(imgUri, price, name, stock);
                                DishesContainer.refresh(getApplicationContext());
                                editDishItemFragment.refreshAdapter(DishesContainer.getInstance(getApplicationContext()).getDishList());
                            }

                            @Override
                            public void onNegativeResult() {

                            }
                        });
                        crearPlatDialog.show(fragmentManager, "tag");
                        break;
                    default:
                        break;
                }
            }
        });

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View header = navigationView.getHeaderView(0);
        TextView titleTv = (TextView) header.findViewById(R.id.navDrawerTitle);
        TextView emailTv = (TextView) header.findViewById(R.id.navDrawerEmail);
        LinearLayout linearLayout = (LinearLayout) header.findViewById(R.id.navBackground);
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

        boolean fisrtLaunch = prefs.getBoolean("FirstLaunch", true);
        if (fisrtLaunch){
            prefs.edit().putBoolean("FirstLaunch", false).apply();
            DishesContainer.getFirstInstance(getApplicationContext());
            OrderContainer.getFirstInstance(getApplicationContext());
            setToolbarTitle("Ajuda");
            configureFab(CONFIGURACIO_FRAGMENT);
            AjudaFragment f = new AjudaFragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.mainLayout, f).commit();
        }
        else {
            if (getIntent().hasExtra("Fragment")) {
                Bundle b = getIntent().getExtras();
                if (b.getInt("Fragment") == EDITAR_PLATS_FRAGMENT) {
                    actualFragment = EDITAR_PLATS_FRAGMENT;
                    configureFab(EDITAR_PLATS_FRAGMENT);
                    editDishItemFragment = new EditDishItemFragment();
                    getSupportFragmentManager().beginTransaction().replace(R.id.mainLayout, editDishItemFragment).commit();
                }
            } else {
                actualFragment = NOVA_COMANDA_FRAGMENT;
                configureFab(NOVA_COMANDA_FRAGMENT);
                dishItemFragment = new DishItemFragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.mainLayout, dishItemFragment).commit();
            }
        }
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_nova_comanda) {
            setToolbarTitle("Nova comanda");
            configureFab(NOVA_COMANDA_FRAGMENT);
            dishItemFragment = new DishItemFragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.mainLayout, dishItemFragment).commit();

        } else if (id == R.id.nav_editar_plats) {
            setToolbarTitle("Editar plats");
            configureFab(EDITAR_PLATS_FRAGMENT);
            editDishItemFragment = new EditDishItemFragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.mainLayout, editDishItemFragment).commit();

        } else if (id == R.id.nav_llistat_comandes) {
            setToolbarTitle("Llistar comandes");
            configureFab(LLISTAR_COMANDES_FRAGMENT);
            OrderItemFragment orderItemFragment = new OrderItemFragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.mainLayout, orderItemFragment).commit();

        } else if (id == R.id.nav_stoc_productes) {
            setToolbarTitle("Stoc plats");
            configureFab(STOC_PRODUCTES_FRAGMENT);
            ItemStocFragment f = new ItemStocFragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.mainLayout, f).commit();

        } else if (id == R.id.nav_config) {
            startActivity(new Intent(MainActivity.this, ConfigActivity.class));

        } else if (id == R.id.nav_ajuda){
            setToolbarTitle("Ajuda");
            configureFab(CONFIGURACIO_FRAGMENT);
            AjudaFragment f = new AjudaFragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.mainLayout, f).commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    @Override
    public void onListFragmentInteraction(DishesContainer.Dish dish) {
        Toast.makeText(getApplicationContext(), "Poducte fora de stoc", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onListFragmentInteraction(OrderContainer.Order order) {

    }
}

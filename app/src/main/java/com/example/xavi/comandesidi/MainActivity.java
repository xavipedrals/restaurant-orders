package com.example.xavi.comandesidi;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
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
import android.widget.Toast;

import com.example.xavi.comandesidi.EditarPlats.EditPlatItemFragment;
import com.example.xavi.comandesidi.LlistarComandes.ComandaItemFragment;
import com.example.xavi.comandesidi.NovaComanda.InfoDialog;
import com.example.xavi.comandesidi.NovaComanda.ItemFragment;
import com.example.xavi.comandesidi.NovaComanda.TableDialog;
import com.example.xavi.comandesidi.StocProductes.ItemStocFragment;
import com.example.xavi.comandesidi.data.GestorBD;
import com.example.xavi.comandesidi.domini.ComandaContainer;
import com.example.xavi.comandesidi.domini.ProductsContainer;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, ItemFragment.OnListFragmentInteractionListener,
        ComandaItemFragment.OnListFragmentInteractionListener, EditPlatItemFragment.OnListFragmentInteractionListener,
        ItemStocFragment.OnListFragmentInteractionListener {

    Toolbar toolbar;
    DrawerLayout drawer;
    NavigationView navigationView;
    ActionBarDrawerToggle toggle;
    FloatingActionButton fab;
    private int actualFragment;
    ItemFragment itemFragment;

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
                //fab.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#F44336"))); //Vermell
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
                        if (itemFragment.checkIfPriceIsZero()) {
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
                                    double price = itemFragment.getMyItemRecyclerViewAdapter().getTotalPrice();
                                    SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy hh:mm a");
                                    Date date = new Date();
                                    String dateStr = df.format(date);
                                    String[] aux = dateStr.split(" ");
                                    String day = aux[0];
                                    String hour = aux[1];
                                    Log.d("DIA", day);
                                    Log.d("HORA", hour);
                                    GestorBD.getInstance(getApplicationContext()).insertComanda(price, dateStr, numTaula);
                                    ComandaContainer.refresh(getApplicationContext());
                                    itemFragment.updateStockDb();
                                    ProductsContainer.refresh(getApplicationContext());
                                    itemFragment.getMyItemRecyclerViewAdapter().resetView();
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
                        //TODO: Afegir funcionalitat crear plats
                        Toast.makeText(getApplicationContext(), "Editar plats", Toast.LENGTH_LONG).show();
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

        if(getIntent().hasExtra("Fragment")){
            Bundle b = getIntent().getExtras();
            if(b.getInt("Fragment") == EDITAR_PLATS_FRAGMENT){
                actualFragment = EDITAR_PLATS_FRAGMENT;
                configureFab(EDITAR_PLATS_FRAGMENT);
                EditPlatItemFragment editPlatItemFragment = new EditPlatItemFragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.mainLayout, editPlatItemFragment).commit();
            }
        } else {
            actualFragment = NOVA_COMANDA_FRAGMENT;
            configureFab(NOVA_COMANDA_FRAGMENT);
            itemFragment = new ItemFragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.mainLayout, itemFragment).commit();
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
            itemFragment = new ItemFragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.mainLayout, itemFragment).commit();

        } else if (id == R.id.nav_editar_plats) {
            setToolbarTitle("Editar plats");
            configureFab(EDITAR_PLATS_FRAGMENT);
            EditPlatItemFragment f = new EditPlatItemFragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.mainLayout, f).commit();

        } else if (id == R.id.nav_llistat_comandes) {
            setToolbarTitle("Llistar comandes");
            configureFab(LLISTAR_COMANDES_FRAGMENT);
            ComandaItemFragment comandaItemFragment = new ComandaItemFragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.mainLayout, comandaItemFragment).commit();

        } else if (id == R.id.nav_stoc_productes) {
            setToolbarTitle("Stoc productes");
            configureFab(STOC_PRODUCTES_FRAGMENT);
            ItemStocFragment f = new ItemStocFragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.mainLayout, f).commit();

        } else if (id == R.id.nav_config) {
            setToolbarTitle("Configuració");
            configureFab(CONFIGURACIO_FRAGMENT);
            Fragment f = new Fragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.mainLayout, f).commit();

        } else if (id == R.id.nav_ajuda){
            startActivity(new Intent(MainActivity.this, AboutActivity.class));
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    @Override
    public void onListFragmentInteraction(ProductsContainer.Product product) {
        Toast.makeText(getApplicationContext(), "Poducte fora de stoc", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onListFragmentInteraction(ComandaContainer.Comanda comanda) {

    }
}

package com.example.xavi.comandesidi;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
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

import com.example.xavi.comandesidi.domini.ProductsContainer;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, ItemFragment.OnListFragmentInteractionListener {

    Toolbar toolbar;
    DrawerLayout drawer;
    NavigationView navigationView;
    ActionBarDrawerToggle toggle;
    FloatingActionButton fab;
    private int actualFragment;

    private final int MAIN_FRAGMENT = 1;
    private final int NOVA_COMANDA_FRAGMENT = 2;
    private final int EDITAR_PLATS_FRAGMENT = 3;

    private void configureFab(int fragmentTag){
        switch (fragmentTag){
            case MAIN_FRAGMENT:
                fab.setVisibility(View.GONE);
                break;
            case NOVA_COMANDA_FRAGMENT:
                fab.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#64DD17"))); //Verd
                fab.setImageDrawable(getResources().getDrawable(R.mipmap.ic_done_white_64dp_1x));
                fab.setVisibility(View.VISIBLE);
                break;
            case EDITAR_PLATS_FRAGMENT:
                fab.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#F44336"))); //Vermell
                fab.setImageDrawable(getResources().getDrawable(R.mipmap.ic_add_white_48dp));
                fab.setVisibility(View.VISIBLE);
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
        toolbar.setTitle("Main");
        setSupportActionBar(toolbar);


        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (actualFragment){
                    case MAIN_FRAGMENT:
                        break;
                    case NOVA_COMANDA_FRAGMENT:
                        Toast.makeText(getApplicationContext(), "I'm in new comanda", Toast.LENGTH_LONG).show();
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
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            setToolbarTitle("Nova comanda");
            configureFab(NOVA_COMANDA_FRAGMENT);
            ItemFragment f = new ItemFragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.mainLayout, f).commit();
        } else if (id == R.id.nav_gallery) {
            setToolbarTitle("Hola crac");
            configureFab(MAIN_FRAGMENT);
            Fragment f = new Fragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.mainLayout, f).commit();

        } else if (id == R.id.nav_slideshow) {
            setToolbarTitle("Editar plats");
            configureFab(EDITAR_PLATS_FRAGMENT);
            ItemFragment f = new ItemFragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.mainLayout, f).commit();

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    @Override
    public void onListFragmentInteraction(ProductsContainer.Product product) {

    }
}

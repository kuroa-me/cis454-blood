package com.example.bloodbank;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;

import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import android.util.Log;
import android.view.View;

import androidx.core.view.GravityCompat;
import androidx.appcompat.app.ActionBarDrawerToggle;

import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.view.Menu;

public class MainActivity extends AppCompatActivity
        implements select.OnFragmentInteractionListener,
        register.OnFragmentInteractionListener,
        login.OnFragmentInteractionListener,
        donor_dash.OnFragmentInteractionListener,
        donor_upinfo.OnFragmentInteractionListener,
        donor_history.OnFragmentInteractionListener,
        donor_vreq.OnFragmentInteractionListener,
        donor_donate.OnFragmentInteractionListener,
        reque_dash.OnFragmentInteractionListener,
        reque_history.OnFragmentInteractionListener,
        reque_mreq.OnFragmentInteractionListener,
        reque_upinfo.OnFragmentInteractionListener,
        reque_check.OnFragmentInteractionListener
{

    private DrawerLayout drawerLayout;
    private NavController navController;
    private NavigationView navigationView;

    AppBarConfiguration appBarConfiguration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        navController = Navigation.findNavController(findViewById(R.id.nav_host_fragment));

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        appBarConfiguration = new AppBarConfiguration.Builder(R.id.des_donor_dash, R.id.des_reque_dash, R.id.des_select)
                .setDrawerLayout(drawerLayout)
                .build();
        //NavigationUI.setupActionBarWithNavController(this, navController, drawerLayout);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);

        //NavigationUI.setupWithNavController(toolbar, navController, drawerLayout);
        NavigationUI.setupWithNavController(toolbar, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navigationView,navController);



    }

    @Override
    public boolean onSupportNavigateUp() {
        // Allows NavigationUI to support proper up navigation or the drawer layout
        // drawer menu, depending on the situation.
        return NavigationUI.navigateUp(navController, appBarConfiguration);
        //return NavigationUI.navigateUp(navController, drawerLayout);

    }

    @Override
    public void onFragmentInteraction(Uri uri){}


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
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
        if (id == R.id.des_select) {
            Log.d("out","Me Erase You!");
            Context ctx = getBaseContext();
            SharedPreferences prefs = ctx.getSharedPreferences("com.example.bloodbank.usertoken", Context.MODE_PRIVATE);
            prefs.edit().putString("token", "qqqaq").apply();
            NavigationUI.onNavDestinationSelected(item, Navigation.findNavController(findViewById(R.id.nav_host_fragment)));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}

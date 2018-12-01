package com.example.oronz.mobiclientapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.oronz.mobiclientapp.Fragemnts.OneWayTripFragement;
import com.example.oronz.mobiclientapp.Fragemnts.TwowayFragemnt;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private MobiClientApplication app;
    TextView textViewUsername;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        app = (MobiClientApplication) getApplication();

        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(app.getLogged_user());

        ViewPager viewPager = findViewById(R.id.pager);
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());

        // Add Fragments to adapter one by one
        adapter.addFragment(new OneWayTripFragement(), "One Way Trip");
        adapter.addFragment(new TwowayFragemnt(), "Two Way Trip");
        viewPager.setAdapter(adapter);

        TabLayout tabLayout = findViewById(R.id.tabs);

        tabLayout.setupWithViewPager(viewPager);





        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View headerView = navigationView.getHeaderView(0);
        textViewUsername= (TextView) headerView.findViewById((R.id.menutxt));

        textViewUsername.setText(app.getLogged_user());
    }


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

        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        else if (id == R.id.menu_logout) {

            startActivity(new Intent(getApplicationContext(),LoginActivity.class));
            finish();
        }
        else if(id==R.id.menu_manifest){
            startActivity(new Intent(getApplicationContext(), ManifestActivity.class));

        }
        else if(id==R.id.menu_add){
            startActivity(new Intent(getApplicationContext(), MainActivity.class));

        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            startActivity(new Intent(getApplicationContext(), MainActivity.class));

        } else if (id == R.id.nav_gallery) {
            startActivity(new Intent(getApplicationContext(), NewsActivity.class));


        } else if (id == R.id.menu_search) {
            startActivity(new Intent(getApplicationContext(), MyTrips.class));


        } else if (id == R.id.menu_manifest) {
            startActivity(new Intent(getApplicationContext(), ManifestActivity.class));


        } else if (id == R.id.about) {
            startActivity(new Intent(getApplicationContext(), AboutUsActivity.class));

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


}

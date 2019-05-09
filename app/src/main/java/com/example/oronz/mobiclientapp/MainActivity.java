package com.example.oronz.mobiclientapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import androidx.core.view.GravityCompat;
import androidx.viewpager.widget.ViewPager;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.oronz.mobiclientapp.Fragemnts.OneWayTripFragement;
import com.example.oronz.mobiclientapp.Fragemnts.TwowayFragemnt;
import com.example.oronz.mobiclientapp.Utilities.SaveSharedPreference;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    TextView textViewUsername;
    Context context;
    private MobiClientApplication app;

    private Context mContext;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        app = (MobiClientApplication) getApplication();

        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(app.getLogged_user());
        mContext = getApplicationContext();


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
        textViewUsername = (TextView) headerView.findViewById((R.id.menutxt));

        textViewUsername.setText(String.format("%s\n%s\n%s", app.getLogged_user(), app.getPhone_num(), app.getEmail_address()));

        app.setPrice_class("10");


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
//        if (id == R.id.action_settings) {
//            return true;
//        }

        if (id == R.id.menu_logout) {

            SaveSharedPreference.setLoggedIn(getApplicationContext(), false);

            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
            finish();
        } else if (id == R.id.menu_manifest) {
            startActivity(new Intent(getApplicationContext(), ManifestActivity.class));

        } else if (id == R.id.menu_add) {
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

        }

        else if (id == R.id.menu_search) {
            startActivity(new Intent(getApplicationContext(), SearchTicketActivity.class));


        }

        else if (id == R.id.search_nav) {
            startActivity(new Intent(getApplicationContext(), SearchPaymentActivity.class));


        }


        else if (id == R.id.menu_manifest) {
            startActivity(new Intent(getApplicationContext(), ManifestActivity.class));


        } else if (id == R.id.about) {
            startActivity(new Intent(getApplicationContext(), AboutUsActivity.class));


        } else if (id == R.id.update) {

            Intent i = new Intent(this, UpdateApp.class);
            startActivity(i);


        }else if (id==R.id.nav_submit_cash){

            startActivity(new Intent(this,SubmitPaymentActivity.class));

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


}

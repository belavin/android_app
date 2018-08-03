package com.android.gpstest;

import android.Manifest;

import android.os.Bundle;

import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;

import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity{

    public static final String TAG = "MainActivity";


    private static final String GPS_STARTED = "gps_started";



    private static MainActivity sInstance;

    public MainActivity() {
        sInstance =this;
    }

    public static MainActivity getInstance() {
        return sInstance;
    }

    private Toolbar mToolbar;

    private boolean mUseDarkTheme = false;
    private static final int REQUEST_CODE_PERMISSION = 2;





    private TestMurka testMurka;



    private AboutHelp aboutHelp;





    boolean mStarted;




    @Override
    public void onCreate(Bundle savedInstanceState) {



        ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION,
                        android.Manifest.permission.READ_PHONE_STATE,
                        android.Manifest.permission.ACCESS_COARSE_LOCATION,
                        android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        android.Manifest.permission.READ_EXTERNAL_STORAGE,
                        android.Manifest.permission.ACCESS_NETWORK_STATE,
                        android.Manifest.permission.READ_SMS,
                        Manifest.permission.READ_PHONE_NUMBERS},
                REQUEST_CODE_PERMISSION);


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Загружаю фрагмент с мурка данными
        showMurkaFragment();



        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.nav_drawer_left_pane);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

//        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
//        navigationView.setNavigationItemSelectedListener();



    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        // Сохранить текущее состояние GPS-навигации
        outState.putBoolean(GPS_STARTED, mStarted);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Если тема изменилась (например, из Предпочтения), уничтожьте и воссоздайте, чтобы отразить изменения
        boolean useDarkTheme = Application.getPrefs().getBoolean(getString(R.string.pref_key_dark_theme), false);
        if (mUseDarkTheme != useDarkTheme) {
            mUseDarkTheme = useDarkTheme;
            recreate();
        }



    }

    @Override
    protected void onPause() {

        super.onPause();
    }








    private void showMurkaFragment() {
        FragmentManager fm = getSupportFragmentManager();

        hideAboutHelpFragment();

        if (testMurka == null) {
            // Сначала проверьте, существует ли уже экземпляр фрагмента
            testMurka = (TestMurka) fm.findFragmentByTag(TestMurka.TAG);

            if (testMurka == null) {
                // Существующий фрагмент не найден, поэтому создайте новый
                Log.d(TAG, "Creating new GpsStatusFragment");
                testMurka = new TestMurka();
                fm.beginTransaction()
                        .add(R.id.fragment_container, testMurka, TestMurka.TAG)
                        .commit();
            }
        }

        getSupportFragmentManager().beginTransaction().show(testMurka).commit();
        setTitle(getResources().getString(R.string.gps_status_title));
    }



    private void showAboutHelpFragment() {
        FragmentManager fm = getSupportFragmentManager();

        hideMurkaFragment();

        if (aboutHelp == null) {
            // Сначала проверьте, существует ли уже экземпляр фрагмента
            aboutHelp = (AboutHelp) fm.findFragmentByTag(AboutHelp.TAG);

            if (aboutHelp == null) {
                // Существующий фрагмент не найден, поэтому создайте новый
                Log.d(TAG, "Creating new About Help Fragment");
                aboutHelp = new AboutHelp();
                fm.beginTransaction()
                        .add(R.id.fragment_container, aboutHelp, AboutHelp.TAG)
                        .commit();
            }
        }

        getSupportFragmentManager().beginTransaction().show(aboutHelp).commit();
        setTitle(getResources().getString(R.string.gps_status_title));
    }


    private void hideMurkaFragment() {
        FragmentManager fm = getSupportFragmentManager();
        testMurka = (TestMurka) fm.findFragmentByTag(TestMurka.TAG);
        if (testMurka != null && !testMurka.isHidden()) {
            fm.beginTransaction().hide(testMurka).commit();
        }
    }


    private void hideAboutHelpFragment() {
        FragmentManager fm = getSupportFragmentManager();
        aboutHelp = (AboutHelp) fm.findFragmentByTag(AboutHelp.TAG);
        if (aboutHelp != null && !aboutHelp.isHidden()) {
            fm.beginTransaction().hide(aboutHelp).commit();
        }
    }




    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.nav_drawer_left_pane);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }




    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        boolean success;
        // Handle menu item selection
        switch (item.getItemId()) {
            case R.id.action_settings:
                showMurkaFragment();
                return true;
            case R.id.about:
                showAboutHelpFragment();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }



//    @Override
//    public boolean onNavigationItemSelected(MenuItem item) {
//
//        // Handle navigation view item clicks here.
//        int id = item.getItemId();
//
////        if (id == R.id.nav_camera) {
////            // Handle the camera action
////        } else if (id == R.id.nav_gallery) {
////
////        } else if (id == R.id.nav_slideshow) {
////
////        } else if (id == R.id.nav_manage) {
////
////        } else if (id == R.id.nav_share) {
////
////        } else if (id == R.id.nav_send) {
////
////        }
//
//
//
//        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.nav_drawer_left_pane);
//        drawer.closeDrawer(GravityCompat.START);
//        return true;
//    }
}

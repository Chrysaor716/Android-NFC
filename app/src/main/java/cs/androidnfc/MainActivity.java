package cs.androidnfc;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;

import cs.androidnfc.adapters.DividerItemDecoration;
import cs.androidnfc.adapters.WifiRecyclerViewAdapter;
import jp.wasabeef.recyclerview.animators.SlideInRightAnimator;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private FloatingActionButton mFloatingActionButton;
    private Toolbar mToolbar;
    private DrawerLayout mNavigationDrawer;
    private ActionBarDrawerToggle mActionBarDrawerToggle;
    private NavigationView mNavigationView;

    private WifiManager mWifiManager;
    private WifiBroadcastReceiver mWifiBroadcastReceiver;

    private ArrayList<ScanResult> mWifiScanResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mWifiScanResult = new ArrayList<ScanResult>();

        setContentView(R.layout.activity_main);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        mFloatingActionButton = (FloatingActionButton) findViewById(R.id.fab);
        mFloatingActionButton.setOnClickListener(this);

        mNavigationDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        mActionBarDrawerToggle = new ActionBarDrawerToggle(
                this, mNavigationDrawer, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mNavigationDrawer.setDrawerListener(mActionBarDrawerToggle);
        mActionBarDrawerToggle.syncState();

        mNavigationView = (NavigationView) findViewById(R.id.nav_view);
        mNavigationView.setNavigationItemSelectedListener(this);

        // Initializing recycler view
        mRecyclerView = (RecyclerView) findViewById(R.id.wifi_recyclerview);
        mRecyclerView.setItemAnimator(new SlideInRightAnimator());

        // Use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(mLayoutManager);

        // Specify adapter
        mAdapter = new WifiRecyclerViewAdapter(mWifiScanResult);
        mRecyclerView.setAdapter(mAdapter);

        // Add the little grey lines between each list item
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));

        mWifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        mWifiBroadcastReceiver = new WifiBroadcastReceiver();
        if (mWifiManager.isWifiEnabled()) {
            registerReceiver(mWifiBroadcastReceiver, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
        }
    }

    @Override
    protected void onResume() {
        mWifiManager.startScan();
        registerReceiver(mWifiBroadcastReceiver, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));

        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
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

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        //Handle each button press here
        switch (id) {

            case R.id.nav_camera:
            case R.id.nav_gallery:
            case R.id.nav_slideshow:
            case R.id.nav_manage:
            case R.id.nav_share:
            case R.id.nav_send:

            default:
                break;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fab:
                if (mWifiManager != null) {
                    mWifiManager.startScan();
                    registerReceiver(mWifiBroadcastReceiver, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
                }
//                Snackbar.make(view, "Added item.", Snackbar.LENGTH_SHORT)
//                        .setAction("Action", null).show();
                break;
        }
    }

    private class WifiBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            unregisterReceiver(mWifiBroadcastReceiver);
            List<ScanResult> wifiScanResultList = mWifiManager.getScanResults();

            //Filtering the Wifi with hidden SSID
            if (wifiScanResultList.size() > 0) {
                int size = mWifiScanResult.size();
                mWifiScanResult.clear();
                mAdapter.notifyItemRangeRemoved(0, size);
            }

            for (int i = 0; i < wifiScanResultList.size(); i++) {
                if (wifiScanResultList.get(i).SSID.length() > 0) {
                    mWifiScanResult.add(((wifiScanResultList.get(i))));
                    mAdapter.notifyItemInserted(mWifiScanResult.size());
                }
            }

        }
    }
}

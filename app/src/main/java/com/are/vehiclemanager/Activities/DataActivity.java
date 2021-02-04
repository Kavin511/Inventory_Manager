package com.are.vehiclemanager.Activities;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.are.vehiclemanager.R;
import com.are.vehiclemanager.ui.equipment.DataEntry;
import com.are.vehiclemanager.ui.equipment.graph;
import com.are.vehiclemanager.ui.equipment.history;
import com.are.vehiclemanager.ui.filler.FillerDataEntry;
import com.are.vehiclemanager.ui.filler.filler_graph;
import com.are.vehiclemanager.ui.filler.filler_history;
import com.are.vehiclemanager.ui.stock.Stock_data_entry;
import com.are.vehiclemanager.ui.stock.Stock_graph;
import com.are.vehiclemanager.ui.stock.Stock_history;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.tabs.TabLayout;

import java.util.Objects;

public class DataActivity extends AppCompatActivity {
    int n = 0;
    ViewPager viewPager;
    SearchView searchView;
    String type = "";
    int flag = 0;
    TabLayout tabLayout;
    MaterialToolbar toolbar;
    View v;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_data);
        Log.d("DataActivity", "onCreate: " + getApplicationContext() + "," + getBaseContext());
        toolbar = findViewById(R.id.toolbar);
        toolbar.setElevation(0);
        type = getIntent().getStringExtra("Type");
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        tabLayout = findViewById(R.id.tab_layout_data);
        v = getLayoutInflater().inflate(R.layout.fragment_stock_history, null);
        searchView = v.findViewById(R.id.search_view);
        String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
        requestPermissions(permissions, 1);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (type != null) {
            switch (type) {
                case "equipment":
                    toolbar.setTitle("Equipment Inspection Report");
                    break;
                case "filter":
                    toolbar.setTitle("Filter Inspection Report");
                    break;
                case "stock":
                    toolbar.setTitle("Stock Management Report");
                    break;
            }
        } else {
            toolbar.setTitle("Equipment Inspection Report");
        }
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setHomeAsUpIndicator(R.drawable.ic_arrow_left);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Objects.requireNonNull(getSupportActionBar()).setElevation(0);
        DataPageAdapter dataPageAdapter = new DataPageAdapter(getSupportFragmentManager(), getApplicationContext(), tabLayout.getTabCount());
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        viewPager.setAdapter(dataPageAdapter);
    }

    @Override
    public void onBackPressed() {
        n++;
        if (!searchView.isIconified()) {
            searchView.setIconified(true);
        }
        if (n == 1 && viewPager.getCurrentItem() != 0) {
            viewPager.setCurrentItem(0);
        } else {
            finish();
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    flag = 1;
                } else {
                    flag = 0;
                    Toast.makeText(this, "Storage Permission needed,Allow to continue", Toast.LENGTH_SHORT).show();
                    finish();
                }
                break;
        }
    }

    class DataPageAdapter extends FragmentPagerAdapter {
        Context context;
        int totalTabs;

        String type = getIntent().getStringExtra("Type");

        public DataPageAdapter(FragmentManager fm, Context applicationContext, int totalTabs) {
            super(fm);
            this.context = applicationContext;
            this.totalTabs = totalTabs;
        }

        @NonNull
        public Fragment getItem(int i) {
            Fragment _dataEntry = null;
            Fragment _history = null;
            Fragment _graph = null;
            if (type.equals("equipment")) {
                _dataEntry = new DataEntry();
                _history = new history();
                _graph = new graph();
            } else if (type.equals("filter")) {
                _dataEntry = new FillerDataEntry();
                _history = new filler_history();
                _graph = new filler_graph();
            } else if (type.equals("stock")) {
                _dataEntry = new Stock_data_entry();
                _history = new Stock_history();
                _graph = new Stock_graph();
            } else {
                _dataEntry = new DataEntry();
                _history = new history();
                _graph = new graph();
            }
            switch (i) {
                case 0:
                    return _dataEntry;
                case 1:
                    return _history;
                case 2:
                    return _graph;
                default:
                    return null;
            }
        }

        public int getCount() {
            return totalTabs;
        }
    }

}

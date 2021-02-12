package com.are.vehiclemanager.authentication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.interpolator.view.animation.FastOutSlowInInterpolator;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.are.vehiclemanager.R;
import com.are.vehiclemanager.ZoomOutPageTransformer;
import com.are.vehiclemanager.Activities.navigation_drawer;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;

import net.skoumal.fragmentback.BackFragmentHelper;

public class MainActivity extends AppCompatActivity {
    ViewPager viewPager;
    TabLayout tabLayout;
    private FirebaseAuth mAuth;

    public void onBackPressed() {
        if (!BackFragmentHelper.fireOnBackPressedEvent(this)) {
            finish();
            super.onBackPressed();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        viewPager = findViewById(R.id.viewPager);
        tabLayout = findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText("Login"));
        tabLayout.animate()
                .scaleX(1f)
                .scaleY(1f)
                .setInterpolator(new FastOutSlowInInterpolator())
                .setDuration(500)
                .start();
        tabLayout.addTab(tabLayout.newTab().setText("Register"));
        AuthPagerAdapter authPagerAdapter = new AuthPagerAdapter(getSupportFragmentManager(), getApplicationContext(), tabLayout.getTabCount());
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
        mAuth = FirebaseAuth.getInstance();
        if (mAuth.getCurrentUser() != null) {
            Intent startactivity = new Intent(getApplicationContext(), navigation_drawer.class);
            startActivity(startactivity);
            finish();
        } else {
            viewPager.setPageTransformer(true, new ZoomOutPageTransformer());
            viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
            viewPager.setAdapter(authPagerAdapter);
        }
    }
}

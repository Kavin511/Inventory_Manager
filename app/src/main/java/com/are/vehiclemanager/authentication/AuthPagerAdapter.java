package com.are.vehiclemanager.authentication;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

class AuthPagerAdapter extends FragmentPagerAdapter {
    Context context;
    int totalTabs;
    Login login_fragment = new Login();
    SignUp signin = new SignUp();

    public AuthPagerAdapter(FragmentManager fm, Context applicationContext, int totalTabs) {
        super(fm);
        this.context = applicationContext;
        this.totalTabs = totalTabs;
    }

    @NonNull
    public Fragment getItem(int i) {
        switch (i) {
            case 0:
                return login_fragment;
            case 1:
                return signin;
            default:
                return null;
        }
    }

    public int getCount() {
        return totalTabs;
    }
}

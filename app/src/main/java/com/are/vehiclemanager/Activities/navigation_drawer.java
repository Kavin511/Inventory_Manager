package com.are.vehiclemanager.Activities;

import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.ColorInt;
import androidx.annotation.ColorRes;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.are.vehiclemanager.DrawerAdapter;
import com.are.vehiclemanager.DrawerItem;
import com.are.vehiclemanager.ProductFinder;
import com.are.vehiclemanager.R;
import com.are.vehiclemanager.Reports;
import com.are.vehiclemanager.SimpleItem;
import com.are.vehiclemanager.ui.account.accountFragment;
import com.are.vehiclemanager.ui.vehicle.Equipments;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;
import com.yarolegovich.slidingrootnav.SlidingRootNav;
import com.yarolegovich.slidingrootnav.SlidingRootNavBuilder;
import com.yarolegovich.slidingrootnav.SlidingRootNavLayout;

import java.util.Arrays;

public class navigation_drawer extends AppCompatActivity implements DrawerAdapter.OnItemSelectedListener {

    private static final int POS_REPORTS = 0;
    private static final int POS_EQUIPMENT = 1;
    private static final int POS_SETTING = 2;
    private static final int POS_PRODUCT_FINDER = 3;
    private static final String BACK_STACK_ROOT_TAG = "root_fragment";
    public static Fragment fragment;
    ViewPager viewPager;
    FirebaseAuth firebaseAuth;
    StorageReference ref;
    private String[] screenTitles;
    private Drawable[] screenIcons;
    private SlidingRootNav slidingRootNav;
    private StorageReference mStorageRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_navigation_drawer);
        Toolbar toolbar = findViewById(R.id.toolbar);
        firebaseAuth = FirebaseAuth.getInstance();
        setSupportActionBar(toolbar);
        slidingRootNav = new SlidingRootNavBuilder(this)
                .withToolbarMenuToggle(toolbar)
                .withMenuOpened(false)
                .withContentClickableWhenMenuOpened(true)
                .withMenuLayout(R.layout.menu_left_drawer)
                .withRootViewScale(.8f)
                .withRootViewElevation(20)
                .inject();
        SlidingRootNavLayout linearLayout1 = slidingRootNav.getLayout();
        CircularImageView imageView = linearLayout1.findViewById(R.id.img_profile);
        mStorageRef = FirebaseStorage.getInstance().getReference();
        ref = mStorageRef.child("users/" + firebaseAuth.getCurrentUser().getUid() + "/profile.jpg");
        ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(imageView);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Picasso.get().load(R.drawable.ic_user).into(imageView);
            }
        });
        screenIcons = loadScreenIcons();
        screenTitles = loadScreenTitles();
        DrawerAdapter adapter = new DrawerAdapter(Arrays.asList(
                createItemFor(POS_REPORTS).setChecked(true),
                createItemFor(POS_EQUIPMENT),
                createItemFor(POS_SETTING),
                createItemFor(POS_PRODUCT_FINDER)));
        adapter.setListener(this);
        RecyclerView list = findViewById(R.id.list);
        list.setNestedScrollingEnabled(false);
        list.setLayoutManager(new LinearLayoutManager(this));
        list.setAdapter(adapter);
        adapter.setSelected(POS_REPORTS);
        viewPager = findViewById(R.id.viewpager_nav);
        NavigationAdapter navigationAdapter = new NavigationAdapter(getSupportFragmentManager());
        viewPager.setAdapter(navigationAdapter);
        viewPager.setCurrentItem(0);
    }

    public void onItemSelected(int position) {
        Fragment selectedScreen = new Reports();
        View view = getLayoutInflater().inflate(R.layout.menu_left_drawer, null);
        view.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.actionbar_background));
        switch (position) {
            case 0:
                selectedScreen = new Reports();
                break;
            case 1:
                selectedScreen = new Equipments();
                break;
            case 2:
                selectedScreen = new ProductFinder();
                break;
            case 3:
                selectedScreen = new accountFragment();
                break;
        }
        SlidingRootNavLayout layout = slidingRootNav.getLayout();
        layout.setBackgroundColor(color(R.color.colorAccent));
        slidingRootNav.closeMenu(true);
        showFragment(selectedScreen);
    }

    private void showFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, fragment).addToBackStack("root_fragment")
                .commit();
    }

    @SuppressWarnings("rawtypes")
    private DrawerItem createItemFor(int position) {
        return new SimpleItem(screenIcons[position], screenTitles[position])
                .withIconTint(color(R.color.white))
                .withTextTint(color(R.color.white))
                .withSelectedIconTint(color(R.color.selceted_nav))
                .withSelectedTextTint(color(R.color.selceted_nav));
    }

    private String[] loadScreenTitles() {
        return getResources().getStringArray(R.array.ld_activityScreenTitles);
    }

    private Drawable[] loadScreenIcons() {
        TypedArray ta = getResources().obtainTypedArray(R.array.ld_activityScreenIcons);
        Drawable[] icons = new Drawable[ta.length()];
        for (int i = 0; i < ta.length(); i++) {
            int id = ta.getResourceId(i, 0);
            if (id != 0) {
                icons[i] = ContextCompat.getDrawable(this, id);
            }
        }
        ta.recycle();
        return icons;
    }

    @Override
    public void onBackPressed() {
        int n = getSupportFragmentManager().getBackStackEntryCount();
        if (n > 1) {
            getSupportFragmentManager().popBackStack();
        } else {
            finish();
        }
    }

    @ColorInt
    private int color(@ColorRes int res) {
        return ContextCompat.getColor(this, res);
    }

}

class NavigationAdapter extends FragmentPagerAdapter {

    public NavigationAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return new Reports();
    }

    @Override
    public int getCount() {
        return 1;
    }
}
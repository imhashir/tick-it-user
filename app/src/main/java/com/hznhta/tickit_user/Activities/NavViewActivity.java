package com.hznhta.tickit_user.Activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;
import com.hznhta.tickit_user.Fragments.HomeFragment;
import com.hznhta.tickit_user.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NavViewActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener{

    private FragmentManager mFragmentManager;
    private Fragment mFragment;
    @BindView(R.id.id_nav_view) NavigationView mNavigationView;
    @BindView(R.id.id_nav_drawer) DrawerLayout mDrawerLayout;

    private static final String FRAGMENT_EXTRA = "NavViewActivity.Fragment";

    public static Intent newIntent(Context context, Integer fragmentId) {
        Intent i = new Intent(context, NavViewActivity.class);
        i.putExtra(FRAGMENT_EXTRA, fragmentId == null ? -1 : fragmentId);
        return i;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(FirebaseAuth.getInstance().getCurrentUser() == null) {
            startActivity(SignInActivity.newIntent(this));
            finish();
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nav_view);
        ButterKnife.bind(this);
        mFragmentManager = getSupportFragmentManager();
        mNavigationView.setNavigationItemSelectedListener(this);

        if(getIntent().getExtras() != null) {
            int fragmentId = getIntent().getExtras().getInt(FRAGMENT_EXTRA);
            if(fragmentId != -1) {
                chooseNavItem(fragmentId);
            } else {
                setFragmentView(HomeFragment.newInstance());
            }
        } else {
            setFragmentView(HomeFragment.newInstance());
        }
    }

    public void setFragmentView(Fragment fragment) {
        mFragment = mFragmentManager.findFragmentById(R.id.nav_fragment_container);
        if(mFragment == null) {
            mFragment = fragment;
            mFragmentManager.beginTransaction()
                    .add(R.id.nav_fragment_container, fragment)
                    .commit();
        } else {
            mFragment = fragment;
            mFragmentManager.beginTransaction()
                    .replace(R.id.nav_fragment_container, fragment)
                    .commit();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        chooseNavItem(item.getItemId());
        return true;
    }

    public void chooseNavItem(int id) {
        switch (id) {
            case R.id.menu_home:
                setFragmentView(HomeFragment.newInstance());
                break;
            case R.id.menu_credit_reqs:
                break;
            case R.id.menu_logout:
                FirebaseAuth.getInstance().signOut();
                Intent i = NavViewActivity.newIntent(this, null);
                startActivity(i);
                finish();
                break;
            default:
                setFragmentView(HomeFragment.newInstance());
        }
        mDrawerLayout.closeDrawers();
    }
}

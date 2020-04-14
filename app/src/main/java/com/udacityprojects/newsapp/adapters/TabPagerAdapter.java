package com.udacityprojects.newsapp.adapters;

import android.content.Context;
import android.util.Log;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.udacityprojects.newsapp.R;
import com.udacityprojects.newsapp.ui.CategoryFragment;
import com.udacityprojects.newsapp.ui.HeadlinesFragment;
import com.udacityprojects.newsapp.ui.MainActivity;

public class TabPagerAdapter extends FragmentPagerAdapter {
    Context context;

    public TabPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        switch (position){
            case 0:
                fragment = new HeadlinesFragment();
                break;
            case 1:
                fragment = new CategoryFragment();
                break;
        }
        return fragment;
    }


    @Override
    public int getCount() {
        return 2;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position){
            case 0:
                return context.getResources().getString(R.string.recent_tab);
            case 1:
                return context.getResources().getString(R.string.category_tab);
        }
        return null;
    }
}

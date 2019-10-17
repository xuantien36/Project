package com.t3h.project;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.t3h.project.fragment.BaseFragment;


public class PageNewsAdapter extends FragmentPagerAdapter {
    private BaseFragment<MainActivity>[] arrFragment;
    public PageNewsAdapter(@NonNull FragmentManager fm, BaseFragment<MainActivity>... arrFragment) {
        super(fm);
        this.arrFragment=arrFragment;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return arrFragment[position];

    }

    @Override
    public int getCount() {
        return arrFragment.length;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
            return arrFragment[position].getTitle();
    }
}

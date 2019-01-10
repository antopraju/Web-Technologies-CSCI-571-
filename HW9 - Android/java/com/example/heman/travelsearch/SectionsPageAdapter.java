package com.example.heman.travelsearch;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class SectionsPageAdapter extends FragmentPagerAdapter {

    private final List<Fragment> myFragmentList = new ArrayList<>();
    private final List<String> myFragmentTitleList = new ArrayList<>();

    public void addFragment(Fragment fragment,String title){
        myFragmentList.add(fragment);
        myFragmentTitleList.add(title);
    }

    public Fragment getFragment(String title){
        int index = myFragmentTitleList.indexOf(title);
        return myFragmentList.get(index);
    }

    public SectionsPageAdapter(FragmentManager fn){
        super(fn);
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return myFragmentTitleList.get(position);
    }

    @Override
    public Fragment getItem(int position) {
        Log.d(" currently checking tab",position+"");
        return myFragmentList.get(position);
    }

    @Override
    public int getCount() {
        return myFragmentList.size();
    }
}

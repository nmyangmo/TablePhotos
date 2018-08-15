package com.yangmo.tablephotos.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * Author: yankeliang
 * Date: 2018/7/25 18:31
 * Description:
 */

public class FragmentAdapter extends FragmentPagerAdapter {

    private List<Fragment> fragments; // 每个Fragment对应一个Page
    private FragmentManager fragmentManager;


    public FragmentAdapter(FragmentManager fragmentManager, List<Fragment> fragments) {
        super(fragmentManager);
        this.fragments = fragments;
        this.fragmentManager = fragmentManager;
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }
}

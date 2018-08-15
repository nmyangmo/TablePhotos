package com.yangmo.tablephotos;

import android.os.Bundle;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.view.View;

import com.yangmo.tablephotos.adapter.FragmentAdapter;
import com.yangmo.tablephotos.fragment.IndexFragment;
import com.yangmo.tablephotos.fragment.MyFragment;
import com.yangmo.tablephotos.rx.RxBus;
import com.yangmo.tablephotos.view.CustomViewPager;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;


public class MainActivity extends BaseActivity {


    private TabLayout mTabLayout;
    private CustomViewPager mTabViewPager;
    private IndexFragment mIndexFragment;
    private MyFragment mMyFragment;
    private List<Fragment> mTabFragments = new ArrayList<>();
    private FragmentAdapter mTabAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initTabFragments();
        setTab();
    }

    private void initView() {
        mTabLayout = findViewById(R.id.tabLayout);
        mTabViewPager = findViewById(R.id.viewPager);
    }


    private void initTabFragments() {
        if (getSupportFragmentManager() != null) {
            mIndexFragment = (IndexFragment) getSupportFragmentManager().findFragmentByTag(IndexFragment.class.getSimpleName());
            mMyFragment = (MyFragment) getSupportFragmentManager().findFragmentByTag(MyFragment.class.getSimpleName());
        }

        if (mIndexFragment == null) {
            mIndexFragment = IndexFragment.newInstance("", "");
        }


        if (mMyFragment == null) {
            mMyFragment = mMyFragment.newInstance("", "");
        }
        mTabFragments.add(mIndexFragment);
        mTabFragments.add(mMyFragment);
    }

    private void setTab() {
        mTabAdapter = new FragmentAdapter(getSupportFragmentManager(), mTabFragments);
        mTabViewPager.setAdapter(mTabAdapter);
//        mTabViewPager.setOffscreenPageLimit(3);
        mTabViewPager.setScanScroll(true);
        mTabViewPager.setCurrentItem(0, false);

        mTabLayout.setupWithViewPager(mTabViewPager);
        mTabLayout.setTabMode(TabLayout.MODE_FIXED);
        mTabLayout.getTabAt(0).setCustomView(R.layout.activity_main_tab1);
        mTabLayout.getTabAt(1).setCustomView(R.layout.activity_main_tab2);
        mTabLayout.getTabAt(0).getCustomView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TabLayout.Tab tab = mTabLayout.getTabAt(0);
                if (tab != null) {
                    tab.select();
                }
            }
        });
        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                mTabViewPager.setCurrentItem(tab.getPosition(), false);
                if (tab.getPosition() == 0) {
                } else if (tab.getPosition() == 1) {
                }

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    private void subscriptionRxBus() {
        try {
            RxBus.getInstance()
                    .toObserverable(Message.class)
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnSubscribe(new Consumer<Disposable>() {
                        @Override
                        public void accept(@NonNull Disposable disposable) throws Exception {
                            addSubscrebe(disposable);
                        }
                    })
                    .subscribe(new Consumer<Message>() {
                        @Override
                        public void accept(@NonNull Message message) {
                            switch (message.what) {

                            }
                        }
                    });
        } catch (Exception e) {

        }
    }

}

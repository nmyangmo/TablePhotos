package com.yangmo.tablephotos;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

public abstract class BaseActivity extends AppCompatActivity implements View.OnClickListener {

    protected Toolbar mToolbar;


    protected CompositeDisposable mComDisposable; // 管理rx订阅，避免内存泄露

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {

    }


    /**
     * 将所有subscription放入,集中处理
     */
    public void addSubscrebe(Disposable disposable) {
        if (mComDisposable == null) {
            mComDisposable = new CompositeDisposable();
        }
        mComDisposable.add(disposable);
    }

    /**
     * 取消订阅
     */
    protected void unSubscribe() {
        if (mComDisposable != null) {
            mComDisposable.clear();
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        unSubscribe();
    }
}
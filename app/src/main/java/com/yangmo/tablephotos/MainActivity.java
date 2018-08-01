package com.yangmo.tablephotos;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.yangmo.tablephotos.matisseUtils.GifSizeFilter;
import com.yangmo.tablephotos.matisseUtils.Glide4Engine;
import com.yangmo.tablephotos.utils.PreferencesUtil;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.filter.Filter;
import com.zhihu.matisse.internal.entity.CaptureStrategy;
import com.zhihu.matisse.internal.entity.SelectionSpec;
import com.zhihu.matisse.listener.OnCheckedListener;
import com.zhihu.matisse.listener.OnSelectedListener;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private static final int REQUEST_CODE_CHOOSE = 23;
    private GridView gridView;
    private PhotoAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        GridView recyclerView = (GridView) findViewById(R.id.grid_view);
        recyclerView.setAdapter(mAdapter = new PhotoAdapter(this));
    }

    @Override
    public void onClick(final View v) {
        RxPermissions rxPermissions = new RxPermissions(this);
        rxPermissions.request(Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE)
                .subscribe(new Observer<Boolean>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Boolean aBoolean) {
                        if (aBoolean) {
                            switch (v.getId()) {
                                case R.id.pick_photos:
                                    Matisse.from(MainActivity.this)
                                            .choose(MimeType.ofAll(), false)
                                            .countable(true)
                                            .capture(true)
                                            .captureStrategy(
                                                    new CaptureStrategy(true, "com.zhihu.matisse.sample.fileprovider"))
                                            .maxSelectable(9)
                                            .addFilter(new GifSizeFilter(2, 2, 5 * Filter.K * Filter.K))
                                            .gridExpectedSize(
                                                    getResources().getDimensionPixelSize(R.dimen.grid_expected_size))
                                            .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
                                            .thumbnailScale(0.85f)
                                            // for glide-V3
//                                            .imageEngine(new GlideEngine())
                                            // for glide-V4
                                            .imageEngine(new Glide4Engine())
                                            .setOnSelectedListener(new OnSelectedListener() {
                                                @Override
                                                public void onSelected(
                                                        @NonNull List<Uri> uriList, @NonNull List<String> pathList) {
                                                    // DO SOMETHING IMMEDIATELY HERE
                                                    Log.e("onSelected", "onSelected: pathList=" + pathList);

                                                }
                                            })
                                            .originalEnable(true)
                                            .maxOriginalSize(10)
                                            .setOnCheckedListener(new OnCheckedListener() {
                                                @Override
                                                public void onCheck(boolean isChecked) {
                                                    // DO SOMETHING IMMEDIATELY HERE
                                                    Log.e("isChecked", "onCheck: isChecked=" + isChecked);
                                                }
                                            })
                                            .forResult(REQUEST_CODE_CHOOSE);
                                    break;
//                                case R.id.dracula:
//                                    Matisse.from(MainActivity.this)
//                                            .choose(MimeType.ofImage())
//                                            .theme(R.style.Matisse_Dracula)
//                                            .countable(false)
//                                            .addFilter(new GifSizeFilter(320, 320, 5 * Filter.K * Filter.K))
//                                            .maxSelectable(9)
//                                            .originalEnable(true)
//                                            .maxOriginalSize(10)
//                                            .imageEngine(new PicassoEngine())
//                                            .forResult(REQUEST_CODE_CHOOSE);
//                                    break;
                                default:
                                    break;
                            }
                        } else {
                            Toast.makeText(MainActivity.this, R.string.permission_request_denied, Toast.LENGTH_LONG)
                                    .show();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_CHOOSE && resultCode == RESULT_OK) {
            mAdapter.setData(Matisse.obtainResult(data));
            PreferencesUtil.setPhotoList(JSON.toJSONString(Matisse.obtainPathResult(data)));
            findViewById(R.id.tips_photo_choice).setVisibility(View.VISIBLE);
        }
    }

    private class PhotoAdapter extends BaseAdapter{
        private List<Uri> mUris=new ArrayList<>();
        private  Drawable mPlaceholder;
        private Context context;
        public PhotoAdapter(Context context){
            this.context=context;
            TypedArray ta = context.getTheme().obtainStyledAttributes(
                    new int[]{com.zhihu.matisse.R.attr.album_thumbnail_placeholder});
            mPlaceholder = ta.getDrawable(0);
        }
        void setData(List<Uri> uris) {
            mUris.clear();
            mUris.addAll(uris) ;
            notifyDataSetChanged();
        }
        @Override
        public int getCount() {
            return mUris == null ? 0 : mUris.size();
        }

        @Override
        public Uri getItem(int position) {
            return mUris.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            UriViewHolder holder;
            if (convertView == null) {
                convertView = LayoutInflater.from(context).inflate(R.layout.photo_item, null);
                holder = new UriViewHolder(convertView);
                convertView.setTag(holder);
            } else {
                holder = (UriViewHolder) convertView.getTag();
            }
            SelectionSpec.getInstance().imageEngine.loadThumbnail(context, context.getResources().getDimensionPixelSize(com.zhihu.matisse.R
                            .dimen.media_grid_size), mPlaceholder,
                    holder.photoView, mUris.get(position));
            return convertView;
        }
        class UriViewHolder extends RecyclerView.ViewHolder {
            private ImageView photoView;

            UriViewHolder(View contentView) {
                super(contentView);
                photoView =  contentView.findViewById(R.id.image_view);
            }
        }
    }
}

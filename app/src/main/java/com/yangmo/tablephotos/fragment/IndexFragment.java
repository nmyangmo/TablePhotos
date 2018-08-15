package com.yangmo.tablephotos.fragment;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.huxq17.handygridview.HandyGridView;
import com.huxq17.handygridview.scrollrunner.OnItemMovedListener;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.yangmo.tablephotos.MainActivity;
import com.yangmo.tablephotos.R;
import com.yangmo.tablephotos.matisseUtils.GifSizeFilter;
import com.yangmo.tablephotos.matisseUtils.Glide4Engine;
import com.yangmo.tablephotos.rx.RxBus;
import com.yangmo.tablephotos.utils.PreferencesUtil;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.filter.Filter;
import com.zhihu.matisse.internal.entity.CaptureStrategy;
import com.zhihu.matisse.listener.OnCheckedListener;
import com.zhihu.matisse.listener.OnSelectedListener;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link IndexFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class IndexFragment extends Fragment implements View.OnClickListener{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private static final int REQUEST_CODE_CHOOSE = 23;
    private PhotoAdapter mAdapter;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    List<String> list;

    public IndexFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment IndexFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static IndexFragment newInstance(String param1, String param2) {
        IndexFragment fragment = new IndexFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        list= PreferencesUtil.getPhotoList();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_index, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        HandyGridView recyclerView =  view.findViewById(R.id.grid_view);
        recyclerView.setAdapter(mAdapter = new PhotoAdapter(getContext()));
        recyclerView.setMode(HandyGridView.MODE.LONG_PRESS);
        recyclerView.setEmptyView(view.findViewById(R.id.text_empty_view));
        view.findViewById(R.id.pick_photos).setOnClickListener(this);
        if (list!=null){
            mAdapter.setData(list);
            view.findViewById(R.id.tips_photo_choice).setVisibility(View.VISIBLE);
        }
    }


    @Override
    public void onClick(final View v) {
        RxPermissions rxPermissions = new RxPermissions(getActivity());
        rxPermissions.request(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE)
                .subscribe(new Observer<Boolean>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }
                    @Override
                    public void onNext(Boolean aBoolean) {
                        if (aBoolean) {
                            switch (v.getId()) {
                                case R.id.pick_photos:
                                    Matisse.from(IndexFragment.this)
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
                                default:
                                    break;
                            }
                        } else {
                            Toast.makeText(getContext(), R.string.permission_request_denied, Toast.LENGTH_LONG)
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_CHOOSE && resultCode == RESULT_OK) {
            mAdapter.setData(Matisse.obtainPathResult(data));
            PreferencesUtil.setPhotoList(JSON.toJSONString(Matisse.obtainPathResult(data)));
            getActivity().findViewById(R.id.tips_photo_choice).setVisibility(View.VISIBLE);
        }
    }



    @Override
    public void onPause() {
        super.onPause();
        mAdapter.saveData();
    }



    private class PhotoAdapter extends BaseAdapter implements OnItemMovedListener {
        private List<String> mUris = new ArrayList<>();
        private Drawable mPlaceholder;
        private Context context;

        public PhotoAdapter(Context context) {
            this.context = context;
            TypedArray ta = context.getTheme().obtainStyledAttributes(
                    new int[]{com.zhihu.matisse.R.attr.album_thumbnail_placeholder});
            mPlaceholder = ta.getDrawable(0);
        }

        void saveData(){
            PreferencesUtil.setPhotoList(JSON.toJSONString(mUris));
        }
        void setData(List<String> uris) {
            mUris.clear();
            mUris.addAll(uris);
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return mUris == null ? 0 : mUris.size();
        }

        @Override
        public String getItem(int position) {
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
//            SelectionSpec.getInstance().imageEngine.loadThumbnail(context, context.getResources().getDimensionPixelSize(com.zhihu.matisse.R
//                            .dimen.media_grid_size), mPlaceholder,
//                    holder.photoView, mUris.get(position));
            int resize=context.getResources().getDimensionPixelSize(com.zhihu.matisse.R
                    .dimen.media_grid_size);
            Glide.with(context)
                    .asBitmap() // some .jpeg files are actually gif
                    .load(mUris.get(position))
                    .apply(new RequestOptions()
                            .override(resize, resize)
                            .placeholder(mPlaceholder)
                            .centerCrop())
                    .into(holder.photoView);
            return convertView;
        }

        @Override
        public void onItemMoved(int from, int to) {
            String s = mUris.remove(from);
            mUris.add(to, s);
        }

        @Override
        public boolean isFixed(int position) {
//            if (position == 0) {  //首位不可拖动
//                return true;
//            }
            return false;
        }

        class UriViewHolder extends RecyclerView.ViewHolder {
            private ImageView photoView;

            UriViewHolder(View contentView) {
                super(contentView);
                photoView = contentView.findViewById(R.id.image_view);
            }
        }
    }
}

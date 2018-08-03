package com.yangmo.tablephotos;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.widget.RemoteViews;

import com.yangmo.tablephotos.configs.CommonConfig;
import com.yangmo.tablephotos.utils.PreferencesUtil;
import com.zhihu.matisse.Matisse;

import java.io.File;
import java.util.List;

import static android.media.ThumbnailUtils.OPTIONS_RECYCLE_INPUT;

/**
 * Implementation of App Widget functionality.
 */
public class PhotosWidget extends AppWidgetProvider {
    int count;
    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.photos_widget);
        List<String> list= PreferencesUtil.getPhotoList();
        if (list!=null&&!list.isEmpty()){
            Bitmap bitmap=BitmapFactory.decodeFile(list.get(0));
            if (bitmap.getByteCount()> CommonConfig.MAX_BYTE_COUNT_BY_BITMAP){
                Log.e("OnActivityResult","count:"+bitmap.getByteCount()+",width:"+bitmap.getWidth()+",height:"+bitmap.getHeight());
                float maxCount=CommonConfig.MAX_BYTE_COUNT_BY_BITMAP;
                float key=maxCount/(float) bitmap.getByteCount();
                Bitmap newBitmap=ThumbnailUtils.extractThumbnail(bitmap, (int) (bitmap.getWidth()*key), (int) (bitmap.getHeight()*key), OPTIONS_RECYCLE_INPUT);
                views.setImageViewBitmap(R.id.img, newBitmap);
//                bitmap.recycle();
            }else {
                views.setImageViewBitmap(R.id.img, bitmap);
            }
        }
        //views.setImageViewUri();
        //Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        count++;
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}


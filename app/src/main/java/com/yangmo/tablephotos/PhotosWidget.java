package com.yangmo.tablephotos;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.widget.RemoteViews;

import com.yangmo.tablephotos.utils.PreferencesUtil;
import com.zhihu.matisse.Matisse;

import java.io.File;
import java.util.List;

/**
 * Implementation of App Widget functionality.
 */
public class PhotosWidget extends AppWidgetProvider {

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        CharSequence widgetText = context.getString(R.string.appwidget_text);
        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.photos_widget);
        List<String> list= PreferencesUtil.getPhotoList();
        if (list!=null&&!list.isEmpty()){
//            views.setImageViewUri(R.id.img,Uri.fromFile(new File(list.get(0))));
            if (Build.VERSION.SDK_INT > 23) {
                Uri uri=FileProvider.getUriForFile(context, context.getApplicationContext().getPackageName() + ".provider", new File(list.get(0)));
                Log.e("OnActivityResult ", uri.toString());
                Log.e("OnActivityResult ", uri.getPath());
                views.setImageViewUri(R.id.img, uri);
            } else {
                views.setImageViewUri(R.id.img, Uri.fromFile(new File(list.get(0))));
            }
            Bitmap bitmap=BitmapFactory.decodeFile(list.get(0));
            if (bitmap.getByteCount()>12441600){
                bitmap.recycle();
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


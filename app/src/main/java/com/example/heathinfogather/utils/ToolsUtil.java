package com.example.heathinfogather.utils;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.PopupWindow;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class ToolsUtil {

    //根据uri获取filepath
    public static String getRealPathFromURI(Activity activity,Uri contentUri) {
        String result = null;
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = activity.getContentResolver().query(contentUri, proj, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            result = cursor.getString(column_index);
        }
        if (cursor != null) {
            cursor.close();
        }
        return result;
    }

    //获取当前时间
    public static String[] getCurrentDates(){
        String[] s=new String[2];
        String[] weeks={"周日","周一","周二","周三","周四","周五","周六"};
        Calendar calendar = Calendar.getInstance();
        int index=calendar.get(Calendar.DAY_OF_WEEK)-1;
        int day= calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH)+1;
        s[0]= String.valueOf(day);
        s[1]=month+"月/"+weeks[index];
        return  s;
    }

    //设置窗口透明度
    public static void setTransparent(Activity activity,PopupWindow popWnd) {
        final Window window = activity.getWindow();
        final WindowManager.LayoutParams lp = window.getAttributes();
        lp.alpha = 0.5f;//设置透明度
        window.setAttributes(lp);
        popWnd.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                //恢复此时的透明度
                lp.alpha = 1f;
                window.setAttributes(lp);
            }
        });
    }

    //深度拷贝
    @SuppressWarnings("unchecked")
    public static <T> List<T> deepCopyList(List<T> src)
    {
        List<T> dest = null;
        try
        {
            ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
            ObjectOutputStream out = new ObjectOutputStream(byteOut);
            out.writeObject(src);
            ByteArrayInputStream byteIn = new ByteArrayInputStream(byteOut.toByteArray());
            ObjectInputStream in = new ObjectInputStream(byteIn);
            dest = (List<T>) in.readObject();
        }
        catch (IOException e)
        {
            Log.d("111", e.toString());
        }
        catch (ClassNotFoundException e)
        {
            Log.d("111", e.toString());
        }
        return dest;
    }

}

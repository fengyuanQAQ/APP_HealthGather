package com.example.heathinfogather.activities.student;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.Switch;
import android.widget.Toast;

import com.example.heathinfogather.R;
import com.example.heathinfogather.activities.LoginActivity;
import com.example.heathinfogather.server.RetrofitRequest;
import com.example.heathinfogather.utils.GlobalManager;
import com.example.heathinfogather.utils.SingleInstance;
import com.example.heathinfogather.utils.ToolsUtil;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ModActivity extends AppCompatActivity implements View.OnClickListener {

    private Switch stAutoLogin;
    public final int REQUEST_IMAGE_CAPTURE = 1;
    public final int REQUEST_OPEN_PHOTOS = 2;

    private File captureFile = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mod);
        init();
    }

    private void init() {
        ImageButton btnBack = findViewById(R.id.home_setting_back);
        Button btnUpload = findViewById(R.id.home_setting_upload);
        Button btnMod = findViewById(R.id.home_setting_mod);
        Button btnQuitLogin = findViewById(R.id.home_setting_quitLogin);

        stAutoLogin = findViewById(R.id.home_setting_autologin);
        stAutoLogin.setOnClickListener(this);
        //设置自动登陆属性
        SharedPreferences sp = SingleInstance.getSP(this, "Login");
        stAutoLogin.setChecked(sp.getBoolean("autoLogin", false));

        btnBack.setOnClickListener(this);
        btnUpload.setOnClickListener(this);
        btnMod.setOnClickListener(this);
        btnQuitLogin.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.home_setting_back:
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        finish();
                    }
                }, 150);
        break;

        case R.id.home_setting_autologin:
        isAutoLogin();
        break;

        case R.id.home_setting_upload:
        getSomePermissions();
        break;

        case R.id.home_setting_mod:
        startActivity(new Intent(this, CompleteInfoActivity.class));
        break;

        case R.id.home_setting_quitLogin:
        quitLogin();
        break;
    }

}

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1000) {//声请码
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //成功
                headImageEdit();
            } else {
                //失败
                Toast.makeText(this, "打开失败", Toast.LENGTH_SHORT).show();
            }
        }
    }

    //动态获取权限
    public void getSomePermissions() {
        //仅SDK版本>23才会动态申请
        if (Build.VERSION.SDK_INT >= 23) {
            //申请权限
            String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
            //定义申请吗
            int request_code_writePermission = 1000;
            //对于每一个权限进行处理
            for (String permission :
                    permissions) {
                if (checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(permissions, request_code_writePermission);
                    return;
                } else {
                    headImageEdit();
                }
            }
        }
    }

    private void isAutoLogin() {
        SharedPreferences sp = SingleInstance.getSP(this, "Login");
        SharedPreferences.Editor edit = sp.edit();
        if (!stAutoLogin.isChecked()) {
            edit.putBoolean("autoLogin", false);
        } else {
            edit.putBoolean("autoLogin", true);
        }
        edit.apply();
    }

    private void quitLogin() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("确认退出").setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(ModActivity.this, LoginActivity.class).
                        setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                SharedPreferences sp = SingleInstance.getSP(getParent(), "Login");
                SharedPreferences.Editor edit = sp.edit();
                edit.putBoolean("autoLogin", false);
                edit.apply();
                finish();
                startActivity(intent);
            }
        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }).setCancelable(true).show();
    }

    private void headImageEdit() {
        final PopupWindow popWnd = new PopupWindow();
        View view = LayoutInflater.from(this).inflate(R.layout.popwnd_activity_edit_headopen, null);

        //设置点击事件
        Button btnCapture = view.findViewById(R.id.home_setting_upload_capture);
        Button btnFromPhoto = view.findViewById(R.id.home_setting_upload_fromPhoto);
        btnCapture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCamera();
                popWnd.dismiss();
            }
        });
        btnFromPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openPhotos();
                popWnd.dismiss();
            }
        });

        //设置透明度
        ToolsUtil.setTransparent(this, popWnd);

        //设置视图  设置宽度高度
        popWnd.setContentView(view);
        popWnd.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        popWnd.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);

        //显示相关
        popWnd.setFocusable(true);
        popWnd.setOutsideTouchable(true);//点击外面可被销毁
        popWnd.setAnimationStyle(R.style.PopupWindowAnimation);
        popWnd.showAtLocation(view
                , Gravity.BOTTOM, 0, 0);
    }


    //打开相机 返回图片路径
    private void openCamera() {
        Intent takePhotoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);//打开相机
        String time = new SimpleDateFormat("yyyyMMdd-HHmmss").format(new Date());
        String name = "Jpeg_" + time + ".jpg";

        captureFile = createImageFile(name);

        //拍照后原图回存入此路径下
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.M) {
            GlobalManager.uri = Uri.fromFile(captureFile);
        } else {
            /**
             * 7.0 调用系统相机拍照不再允许使用Uri方式，应该替换为FileProvider
             * 并且这样可以解决MIUI系统上拍照返回size为0的情况
             */
            GlobalManager.uri = FileProvider.getUriForFile(this, "com.example.android.fileprovider", captureFile);
        }
        takePhotoIntent.putExtra(MediaStore.EXTRA_OUTPUT, GlobalManager.uri);//此时getData返回位null
        startActivityForResult(takePhotoIntent, REQUEST_IMAGE_CAPTURE);
    }

    private File createImageFile(String imageName) {
        String storagePath = getExternalFilesDir(Environment.DIRECTORY_PICTURES).getAbsolutePath();
//        Toast.makeText(this, storagePath, Toast.LENGTH_SHORT).show();
//        Log.i("mxy", storagePath);
        File storageDir = new File(storagePath);
        return new File(storageDir, imageName);
    }

    //打开相册
    private void openPhotos() {
        Intent photoIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(photoIntent, REQUEST_OPEN_PHOTOS);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            GlobalManager.file = captureFile;
            PopupWindow loadingPwd = SingleInstance.getLoadingPwd(this, "正在上传...");
            new RetrofitRequest().uploadFile(this, captureFile, 8, loadingPwd);
        } else if (requestCode == REQUEST_OPEN_PHOTOS && resultCode == RESULT_OK) {
            //保存图片的uri
            PopupWindow loadingPwd = SingleInstance.getLoadingPwd(this, "正在上传...");
            GlobalManager.uri = data.getData();
            File file = new File(ToolsUtil.getRealPathFromURI(this, GlobalManager.uri));
            GlobalManager.file = file;
            new RetrofitRequest().uploadFile(this, file, 8, loadingPwd);
        }
    }

}

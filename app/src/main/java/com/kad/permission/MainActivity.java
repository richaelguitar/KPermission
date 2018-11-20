package com.kad.permission;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.kad.kpermission.Action;
import com.kad.kpermission.KPermission;
import com.kad.kpermission.Permission;
import com.kad.kpermission.setting.KPermissionSetting;
import com.soundcloud.android.crop.Crop;

import java.io.File;
import java.util.List;


public class MainActivity extends Activity {

    private static final String TAG = "k_permission";
    private KPermissionSetting setting;


    public static  final int TAKE_PHOTO = 100;

    private String cameraPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        cameraPath = getNewCameraPath(this);
        findViewById(R.id.btn_phone).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestPermission(1001,Manifest.permission.READ_PHONE_STATE,Manifest.permission.WRITE_EXTERNAL_STORAGE);
            }
        });
        findViewById(R.id.btn_camera).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestPermission(1002,Manifest.permission.CAMERA);
            }
        });
        setting = new KPermissionSetting(this);
    }



    public void requestPermission(final int requestCode, String ...permissions){
        KPermission.with()
                .source(this)
                .permission(permissions)
                .onGranted(new Action() {
                    @Override
                    public void onAction(List<String> permissions) {
                       if(requestCode == 1001){
                           getDeviceId();
                       }else if(requestCode == 1002){
                           takePhoto();
                       }
                        Log.d(TAG,"成功获取拍照权限");
                    }
                })
                .onDenied(new Action() {
                    @Override
                    public void onAction(List<String> permissions) {
                        String permission = getString(R.string.message_permission_rationale,Permission.transformText(MainActivity.this,permissions));
                        Toast.makeText(getApplicationContext(),"获取"+permission+"权限失败",Toast.LENGTH_SHORT).show();
                        Log.d(TAG,"获取拍照权限失败");
                        if(KPermission.hasAlwaysDeniedPermission(MainActivity.this,permissions)){
                            Log.d(TAG,"总是拒绝跳转手机设置");
                            Toast.makeText(getApplicationContext(),"总是拒绝跳转手机设置",Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .start();
    }

    private void getDeviceId() {
        String deviceId =   ((TelephonyManager)getSystemService("phone")).getDeviceId();
        Toast.makeText(getApplicationContext(),"成功获取设备id:"+deviceId,Toast.LENGTH_SHORT).show();
    }

    private void takePhoto() {

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        String outFilePath = getLocation(this,true);
        File file = new File(outFilePath);

        if(!file.exists()){
            file.mkdirs();
        }

        Uri uri = Uri.fromFile(new File(cameraPath));
        intent.putExtra(MediaStore.EXTRA_OUTPUT,uri);
        startActivityForResult(intent,TAKE_PHOTO);


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent result) {
        if(resultCode ==RESULT_OK){
            if(requestCode == TAKE_PHOTO){
                Uri destination = Uri.fromFile(new File(getCacheDir(),"user_logo"));
                Uri source = Uri.parse("file://"+cameraPath);
                Crop.of(source,destination).asSquare().start(MainActivity.this);
            }else if(requestCode == Crop.REQUEST_CROP){

                Uri uri = Crop.getOutput(result);
            }
        }
    }


    public static String getNewCameraPath(Context context) {
        return getLocation(context, true) + File.separator + System.currentTimeMillis() + ".jpg";
    }

    public static String getLocation(Context context, boolean isCameraType) {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            if (isCameraType) {
                return Environment.getExternalStorageDirectory().getAbsolutePath() + "/kad/image/camera";
            } else {
                return Environment.getExternalStorageDirectory().getAbsolutePath() + "/kad/image/compress";
            }
        } else {
            return context.getCacheDir().getAbsolutePath();
        }
    }




    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG,"Activity销毁");
    }
}

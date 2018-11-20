package com.kad.kpermission;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import pub.devrel.easypermissions.EasyPermissions;

public class KPermissionActivity extends AppCompatActivity implements EasyPermissions.PermissionCallbacks {

    private static PermissionListener mPermissionListener;

    private String[] mPermissions;

    public static final int PER_REQUEST_CODE = 123;

    public static final String PERMISSION_KEY = "permission_key";

    private List<String> grantedPermissionList = new ArrayList<>();

    private List<String> deniedPermissionList = new ArrayList<>();


    public static void launchActivity(Activity context, PermissionListener permissionListener, String... permissions){
        mPermissionListener = permissionListener;
        Intent intent = new Intent(context,KPermissionActivity.class);
        intent.putExtra(PERMISSION_KEY,permissions);
        context.startActivity(intent);
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mPermissions = getIntent().getStringArrayExtra(PERMISSION_KEY);

        //获取到没有授权的权限
        for(String permission:mPermissions){
            if(!EasyPermissions.hasPermissions(this,permission)){
                deniedPermissionList.add(permission);
            }
        }

        List<String> permissionNameList = Permission.transformText(this,this.deniedPermissionList);
        String message = getString(R.string.message_permission_rationale, permissionNameList);
        EasyPermissions.requestPermissions(this,message,PER_REQUEST_CODE,mPermissions);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // 回调授权结果
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }



    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {

        for(int i=0;i<perms.size();i++){
            if(EasyPermissions.hasPermissions(this,perms.get(i))){

                if(!grantedPermissionList.contains(perms.get(i))){
                   grantedPermissionList.add(perms.get(i));
                }

                if(deniedPermissionList.contains(perms.get(i))){
                    deniedPermissionList.remove(perms.get(i));
                }

            }else{
                if(!deniedPermissionList.contains(perms.get(i))){
                    deniedPermissionList.add(perms.get(i));
                }
            }
        }
       if(mPermissions!=null&&grantedPermissionList.size() == mPermissions.length){
           if(mPermissionListener!=null){
               mPermissionListener.onPermissionsGranted(requestCode,perms);
           }
       }else{
           if(mPermissionListener!=null){
               mPermissionListener.onPermissionsDenied(requestCode,deniedPermissionList);
           }
       }
        finish();
    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {

        for(int i=0;i<perms.size();i++){
            if(!deniedPermissionList.contains(perms.get(i))){
                deniedPermissionList.add(perms.get(i));
            }
        }
        if(mPermissionListener!=null){
            mPermissionListener.onPermissionsDenied(requestCode,deniedPermissionList);
            finish();
        }
    }


}

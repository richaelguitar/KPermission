package com.kad.kpermission;

import android.app.Activity;
import android.os.Build;
import android.support.annotation.NonNull;

import java.util.Arrays;
import java.util.List;

import pub.devrel.easypermissions.EasyPermissions;

/**
 * android6.0以上的权限申请
 */
public class MRequest implements Request {


    private String[] permissions;//权限申请列表

    private Action grantedAction;//权限申请成功回调

    private Action deniedAction;//权限申请失败回调

    private Activity source;


    public MRequest(Activity activity){
        this.source = activity;
    }




    @Override
    public Request permission(String... permissions) {
        this.permissions = permissions;
        return this;
    }

    @Override
    public Request onGranted(Action action) {
        this.grantedAction = action;
        return this;
    }

    @Override
    public Request onDenied(Action action) {
        this.deniedAction = action;
        return this;
    }

    @Override
    public void start() {
        //先判断是否需要运行时权限
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
            //判断应用是否已经具有该权限
            if(hasPermissions(permissions)){
                grantedAction.onAction(Arrays.asList(permissions));
            }else{
                //发出权限申请请求
                KPermissionActivity.launchActivity(source,permissionListener,permissions);
            }

        }else{
            grantedAction.onAction(Arrays.asList(permissions));
        }
    }



    private PermissionListener permissionListener = new PermissionListener() {
        @Override
        public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {
            if(grantedAction!=null){
                grantedAction.onAction(perms);
            }
        }

        @Override
        public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {
            if(deniedAction!=null){
                deniedAction.onAction(perms);
            }
        }

        @Override
        public void onRationaleAccepted(int requestCode) {

        }

        @Override
        public void onRationaleDenied(int requestCode) {

        }
    };

    private boolean  hasPermissions(String[] permissions) {
        return EasyPermissions.hasPermissions(source,permissions);
    }

}

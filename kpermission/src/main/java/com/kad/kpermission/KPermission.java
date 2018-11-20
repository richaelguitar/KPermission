
package com.kad.kpermission;

import android.app.Activity;
import android.support.v4.app.Fragment;

import com.kad.kpermission.factory.RequestFactory;


import java.util.List;

import pub.devrel.easypermissions.EasyPermissions;


/**
 * @author xww
 * @since 2018-10-31
 * 权限封装工具类
 */
public class KPermission {



    private static KPermission kPermission;

    private MRequestFactoryImpl requestFactory;

    private KPermission(){
        requestFactory = new MRequestFactoryImpl();
    }

    public static KPermission with(){
        if(kPermission == null){
            synchronized (KPermission.class){
                if(kPermission == null){
                    kPermission = new KPermission();
                }
            }
        }
        return kPermission;
    }


    public Request source(Activity activity){
        return requestFactory.create(activity);
    }

    public Request source(Fragment fragment){
        return requestFactory.create(fragment.getActivity());
    }


    public static boolean hasAlwaysDeniedPermission(Activity activity,List<String> perms){
        return EasyPermissions.somePermissionPermanentlyDenied(activity, perms);
    }


    public static class MRequestFactoryImpl implements RequestFactory{
        @Override
        public Request create(Activity activity) {
            return new MRequest(activity);
        }
    }
}
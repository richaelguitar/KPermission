package com.kad.kpermission.factory;

import android.app.Activity;

import com.kad.kpermission.Request;

/**
 * 创建request工厂
 */
public interface RequestFactory {

    Request create(Activity activity);
}

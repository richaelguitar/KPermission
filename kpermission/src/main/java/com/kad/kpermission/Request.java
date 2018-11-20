package com.kad.kpermission;

/**
 * 权限请求构建
 */
public interface Request {


    Request permission(String ...permissions);

    Request onGranted(Action action);

    Request onDenied(Action action);

    void start();
}

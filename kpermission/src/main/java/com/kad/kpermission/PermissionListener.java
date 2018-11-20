package com.kad.kpermission;

import android.support.annotation.NonNull;

import java.io.Serializable;
import java.util.List;

public interface PermissionListener extends Serializable {

    void onPermissionsGranted(int requestCode, @NonNull List<String> perms);

    void onPermissionsDenied(int requestCode, @NonNull List<String> perms);

    void onRationaleAccepted(int requestCode);

    void onRationaleDenied(int requestCode);
}

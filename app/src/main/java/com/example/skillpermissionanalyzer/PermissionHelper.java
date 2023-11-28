package com.example.skillpermissionanalyzer;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PermissionInfo;
import android.util.Log;
import java.util.Arrays;

public class PermissionHelper {

    public void getPermissionsUsedByApp(PackageManager packageManager, String packageName) {
        try {
            PackageInfo packageInfo = packageManager.getPackageInfo(packageName, PackageManager.GET_PERMISSIONS);

            if (packageInfo.requestedPermissions != null) {
                Log.d("PermissionList", "Permissions used by the app: " + Arrays.toString(packageInfo.requestedPermissions));

                for (String permission : packageInfo.requestedPermissions) {
                    try {
                        PermissionInfo permissionInfo = packageManager.getPermissionInfo(permission, 0);
                        Log.d("PermissionList", "Permission Name: " + permissionInfo.name);
                        Log.d("PermissionList", "Permission Label: " + permissionInfo.loadLabel(packageManager));
                        // Additional information about the permission can be accessed using permissionInfo
                    } catch (PackageManager.NameNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }
}

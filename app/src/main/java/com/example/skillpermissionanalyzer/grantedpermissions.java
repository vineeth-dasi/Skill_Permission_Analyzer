package com.example.skillpermissionanalyzer;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PermissionInfo;
import android.util.Log;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PermissionInfo;
import android.os.Build;
import android.os.Bundle;
import android.widget.TextView;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PermissionInfo;
import android.util.Log;



public class grantedpermissions  {

    private final Context context;

    public grantedpermissions(Context context) {
        this.context = context;
    }    public String getpermissions(String packageName) {

        // Get the PackageInfo for the specified package
        try {
            PackageInfo packageInfo =context.getPackageManager().getPackageInfo(packageName, PackageManager.GET_PERMISSIONS);

            // Retrieve the requested permissions
            String[] requestedPermissions = packageInfo.requestedPermissions;

            if (requestedPermissions != null) {
                StringBuilder permissionsStringBuilder = new StringBuilder();

                // Iterate through the permissions
                for (String permission : requestedPermissions) {
                    // Check if the permission is granted
                    if (context.checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED) {
                        PermissionInfo permissionInfo = null;

                        try {
                            // Get additional information about the permission
                            permissionInfo = context.getPackageManager().getPermissionInfo(permission, 0);
                        } catch (PackageManager.NameNotFoundException e) {
                            e.printStackTrace();
                        }

                        if (permissionInfo != null) {
                            permissionsStringBuilder.append(permission+":");
                            permissionsStringBuilder.append(permissionInfo.loadLabel(context.getPackageManager()));
                            permissionsStringBuilder.append(",");
                        }
                    }
                }

                // Display the granted permissions in a TextView
                if (permissionsStringBuilder.length() > 0) {
                    Log.d("permissions for this app are", permissionsStringBuilder.toString());
                    return permissionsStringBuilder.toString();
                } else {
                    Log.d("ddd", "No permissions requested for this app.");
                }
            } else {
                Log.d("ddd", "No permissions requested for this app.");
            }

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();

        }
        return"no permissions granted";

    }
}


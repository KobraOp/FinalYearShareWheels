package com.android.sharewheelsnewui.permissioncaller;

import android.app.Activity;
import android.content.pm.PackageManager;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

public class PermissionCaller {

    public interface PermissionResultCallback {
        void onPermissionGranted();
        void onPermissionDenied(List<String> deniedPermissions);
    }

    public static void requestPermissions(Activity activity, String[] permissions, int requestCode) {
        List<String> neededPermissions = new ArrayList<>();

        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(activity, permission) != PackageManager.PERMISSION_GRANTED) {
                neededPermissions.add(permission);
            }
        }

        if (!neededPermissions.isEmpty()) {
            ActivityCompat.requestPermissions(activity,
                    neededPermissions.toArray(new String[0]),
                    requestCode);
        } else {
            if (activity instanceof PermissionResultCallback) {
                ((PermissionResultCallback) activity).onPermissionGranted();
            }
        }
    }

    public static void handlePermissionsResult(Activity activity,
                                               int requestCode,
                                               int expectedRequestCode,
                                               String[] permissions,
                                               int[] grantResults) {
        if (requestCode != expectedRequestCode) return;

        List<String> deniedPermissions = new ArrayList<>();
        for (int i = 0; i < permissions.length; i++) {
            if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                deniedPermissions.add(permissions[i]);
            }
        }

        if (activity instanceof PermissionResultCallback) {
            PermissionResultCallback callback = (PermissionResultCallback) activity;
            if (deniedPermissions.isEmpty()) {
                callback.onPermissionGranted();
            } else {
                callback.onPermissionDenied(deniedPermissions);
            }
        }
    }
}

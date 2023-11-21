package com.example.crowdhubharmony;

import com.huawei.hms.location.harmony.*;
import ohos.aafwk.ability.Ability;
import ohos.aafwk.content.Intent;
import ohos.aafwk.content.Operation;
import ohos.agp.components.Button;
import ohos.bundle.IBundleManager;
import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;

public class FusedLocationAbility extends Ability {
    Button getCurrentAddressBtn, stopAccessLoation, getContinuousUserLocation;
    private static final HiLogLabel LABEL_LOG = new HiLogLabel(3, 0xD001100, "RequestLocationCallbackAbilitySlice");

    public FusedLocationClient fusedLocationClient;
    private final int REQUEST_CODE = 100;

    private Intent mIntent;
    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setUIContent(ResourceTable.Layout_ability_fused_location);

        mIntent = intent;

        fusedLocationClient = new FusedLocationClient(this);

        getContinuousUserLocation = (Button) findComponentById(ResourceTable.Id_getContinuousUserLocation);
        stopAccessLoation = (Button) findComponentById(ResourceTable.Id_stopAccessLoation);

        clickListener();
    }

    private void clickListener() {
        getContinuousUserLocation.setClickedListener(component -> {

            checkLocationSettings();
        });
        stopAccessLoation.setClickedListener(component -> {

        });
    }

    private void checkLocationSettings() {
        SettingsProviderClient settingsProviderClient = new SettingsProviderClient(this);
        LocationRequest locationRequest = new LocationRequest();
        locationRequest.setPriority(100);
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        LocationSettingsRequest request =
                builder.addLocationRequest(locationRequest).setAlwaysShow(false).setNeedBle(false).build();
        settingsProviderClient.checkLocationSettings(request)
                .addOnSuccessListener(response -> {

                    getPermission();
                })
                .addOnFailureListener(exp -> {
                    // Device location settings do not meet the requirements.
                });
    }

    LocationCallback locationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            if (locationResult != null) {
                HiLog.debug(LABEL_LOG, "Location Successful"+locationResult.getLastHWLocation().getLatitude());

            }
        }

        @Override
        public void onLocationAvailability(LocationAvailability locationAvailability) {
            super.onLocationAvailability(locationAvailability);
            if (locationAvailability != null) {
                // Process the location status.
            }
        }
    };

    private void getLocation() {
        LocationRequest locationRequest = new LocationRequest();
// Set the location update interval, in milliseconds.
        locationRequest.setInterval(5000);
// Set the weight.
        locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
// (Optional) Set whether to return the address information.
        locationRequest.setNeedAddress(true);
// (Optional) Set the language for displaying the obtained address information.

        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback)
                .addOnSuccessListener(v -> {
                    HiLog.info(LABEL_LOG, "Location Successful");

                })
                .addOnFailureListener(e -> {
                    HiLog.info(LABEL_LOG, "TAG", "Location Failed");

                });
    }

    private void getPermission() {

        if (verifySelfPermission("ohos.permission.LOCATION") != IBundleManager.PERMISSION_GRANTED) {
            HiLog.info(LABEL_LOG, "TAG", "Self: LOCATION permission not granted!");
            if (canRequestPermission("ohos.permission.LOCATION")) {
                HiLog.info(LABEL_LOG, "Self: can request permission here");
                requestPermissionsFromUser(
                        new String[]{"ohos.permission.LOCATION"}, REQUEST_CODE);
            } else {
                HiLog.warn(LABEL_LOG, "Self: enter settings to set permission");
            }
        } else {
            HiLog.info(LABEL_LOG, "Self: LOCATION permission granted!");
        }

        if (verifySelfPermission("ohos.permission.LOCATION_IN_BACKGROUND") != IBundleManager.PERMISSION_GRANTED) {
            printLog(HiLog.INFO, "TAG", "Self: LOCATION_IN_BACKGROUND permission not granted!");
            if (canRequestPermission("ohos.permission.LOCATION_IN_BACKGROUND")) {
                printLog(HiLog.INFO, "TAG", "Self: can request permission here");
                requestPermissionsFromUser(new String[]{"ohos.permission.LOCATION_IN_BACKGROUND"}, REQUEST_CODE);
            } else {
                printLog(HiLog.WARN, "TAG", "Self: enter settings to set permission");
            }
        } else {
            printLog(HiLog.INFO, "TAG", "Self: LOCATION_IN_BACKGROUND permission granted!");

//            getLocation();
            startLocationService();
        }
    }

    private void startLocationService(){

        Operation operation = new Intent.OperationBuilder()
                .withDeviceId("")
                .withAbilityName("com.example.crowdhubharmony.LocationService")
                .build();
        mIntent.setOperation(operation);
        startAbility(mIntent);
    }
    private void printLog(int logType, String tag, String message) {
        HiLog.debug(LABEL_LOG, message);

    }

    @Override
    public void onRequestPermissionsFromUserResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE: {
                // Match requestCode of requestPermissions.
                if (grantResults.length > 0
                        && grantResults[0] == IBundleManager.PERMISSION_GRANTED) {
                    //getLocation();
                    startLocationService();
                    // The permission is granted.
                    //Note: During permission check, an interface may be considered to have no required permissions due to time difference. Therefore, it is necessary to capture and process the exception thrown by such an interface.
                } else {
                    // The permission request is rejected.
                }
                return;
            }
            default:
                throw new IllegalStateException("Unexpected value: " + requestCode);
        }
    }
}

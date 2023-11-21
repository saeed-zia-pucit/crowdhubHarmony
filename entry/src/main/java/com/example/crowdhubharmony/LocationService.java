package com.example.crowdhubharmony;

import com.huawei.hms.location.harmony.*;
import ohos.aafwk.ability.Ability;
import ohos.aafwk.content.Intent;
import ohos.event.notification.NotificationRequest;
import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;
import ohos.rpc.IRemoteObject;

public class LocationService extends Ability {
    private static final HiLogLabel LABEL_LOG = new HiLogLabel(3, 0xD001100, "Demo");
    public FusedLocationClient fusedLocationClient;

    @Override
    public void onStart(Intent intent) {
        HiLog.error(LABEL_LOG, "LocationService::onStart");
        super.onStart(intent);
        NotificationRequest request = new NotificationRequest(1005);
        NotificationRequest.NotificationNormalContent content = new NotificationRequest.NotificationNormalContent();
        content.setTitle("title").setText("text");
        NotificationRequest.NotificationContent notificationContent = new NotificationRequest.NotificationContent(content);
        request.setContent(notificationContent);

// Bind the notification (notification ID: 1005) to the Service ability.
        keepBackgroundRunning(1005, request);

        getLocation();
    }

    @Override
    public void onBackground() {
        super.onBackground();
        HiLog.info(LABEL_LOG, "LocationService::onBackground");
    }

    @Override
    public void onStop() {
        super.onStop();
        HiLog.info(LABEL_LOG, "LocationService::onStop");
    }

    @Override
    public void onCommand(Intent intent, boolean restart, int startId) {
    }

    @Override
    public IRemoteObject onConnect(Intent intent) {
        return null;
    }

    @Override
    public void onDisconnect(Intent intent) {
    }
    private void getLocation() {
        LocationRequest locationRequest = new LocationRequest();
// Set the location update interval, in milliseconds.
        locationRequest.setInterval(3000);
// Set the weight.
        locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
// (Optional) Set whether to return the address information.
        locationRequest.setNeedAddress(true);
// (Optional) Set the language for displaying the obtained address information.
        fusedLocationClient = new FusedLocationClient(this);

        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback)
                .addOnSuccessListener(v -> {
                    HiLog.info(LABEL_LOG, "Location Successful");

                })
                .addOnFailureListener(e -> {
                    HiLog.info(LABEL_LOG, "TAG", "Location Failed");

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
}
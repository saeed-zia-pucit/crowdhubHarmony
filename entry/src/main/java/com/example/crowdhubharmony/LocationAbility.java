package com.example.crowdhubharmony;

import com.example.crowdhubharmony.api.ApiInterface;
import com.example.crowdhubharmony.api.RetrofitClient;
import com.example.crowdhubharmony.helper.GlobalConstants;
import com.example.crowdhubharmony.model.CheckinResponseModel;
import ohos.aafwk.ability.Ability;
import ohos.aafwk.content.Intent;
import ohos.agp.components.Button;
import ohos.agp.components.Text;
import ohos.bundle.IBundleManager;
import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;
import ohos.location.*;
import retrofit2.Response;
import retrofit2.Retrofit;

public class LocationAbility extends Ability {
    Locator locator;
    MyLocatorCallback locatorCallback;
    HiLogLabel LABEL_LOG;
    Retrofit retrofit = RetrofitClient.getInstance();
    ApiInterface apiInterface = retrofit.create(ApiInterface.class);
    RequestParam requestParam;
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 100;
    Button getCurrentAddressBtn, stopAccessLoation, getContinuousUserLocation;
    Text userAddress;

    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setUIContent(ResourceTable.Layout_ability_location);
        LABEL_LOG = new HiLogLabel(HiLog.LOG_APP, 0x00201, "Location_ability");
        userAddress = (Text) findComponentById(ResourceTable.Id_userAddress);
        getContinuousUserLocation = (Button) findComponentById(ResourceTable.Id_getContinuousUserLocation);
        stopAccessLoation = (Button) findComponentById(ResourceTable.Id_stopAccessLoation);
        locator = new Locator(LocationAbility.this);
        requestParam = new RequestParam(RequestParam.PRIORITY_ACCURACY, 10, 0);
        locatorCallback = new MyLocatorCallback();
        requestPermission();
//        getCurrentAddressBtn.setClickedListener(component -> {
//            locator.requestOnce(requestParam, locatorCallback);
//        });
        getContinuousUserLocation.setClickedListener(component -> {
            locator.stopLocating(locatorCallback);
            locator.startLocating(requestParam, locatorCallback);
        });
        stopAccessLoation.setClickedListener(component -> {
            locator.stopLocating(locatorCallback);
        });

    }

    private void requestPermission() {
        if (verifySelfPermission("ohos.permission.LOCATION") != IBundleManager.PERMISSION_GRANTED || verifySelfPermission("ohos.permission.LOCATION_IN_BACKGROUND") != IBundleManager.PERMISSION_GRANTED) {
            // The application has not been granted the permission.
            if (canRequestPermission("ohos.permission.LOCATION") || canRequestPermission("ohos.permission.LOCATION_IN_BACKGROUND")) {
                // Check whether permission authorization can be implemented via a dialog box (at initial request or when the user has not chosen the option of "don't ask again" after rejecting a previous request).
                requestPermissionsFromUser(
                        new String[]{"ohos.permission.LOCATION", "ohos.permission.LOCATION_IN_BACKGROUND"}, MY_PERMISSIONS_REQUEST_LOCATION);
            } else {
                // Display the reason why the application requests the permission and prompt the user to grant the permission.
            }
        } else {

            // The permission has been granted.
        }
    }


    public class MyLocatorCallback implements LocatorCallback {
        @Override
        public void onLocationReport(Location location) {
            GeoConvert geoConvert = new GeoConvert();
            HiLog.info(LABEL_LOG, "geLatitude:" + location.getLatitude() + "\n geLatitude" + location.getLongitude());

            try {

              //  checkIn(location.getLatitude(), location.getLongitude());
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        @Override
        public void onStatusChanged(int type) {
            if (type == 3) {
                HiLog.info(LABEL_LOG, "geLatitude:" + "Stop Location accessing");
            } else
                HiLog.info(LABEL_LOG, "geLatitude:" + "onChange status");

        }

        @Override
        public void onErrorReport(int type) {
            HiLog.info(LABEL_LOG, "geLatitude:" + "error");

        }
    }


    @Override
    protected void onBackground() {
        super.onBackground();
        locator.stopLocating(locatorCallback);
    }

    @Override
    public void onRequestPermissionsFromUserResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // Match requestCode of requestPermissions.
                if (grantResults.length > 0
                        && grantResults[0] == IBundleManager.PERMISSION_GRANTED) {
                    locator.startLocating(requestParam, locatorCallback);
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

    private void checkIn(Double lat, Double longitude) {

        try {

            //userAddress.setText( lat+"");
            Response<CheckinResponseModel> response = apiInterface.checkIn(GlobalConstants.deviceId, lat, longitude, 0F).execute();

            if (response.isSuccessful()) {
                HiLog.debug(LABEL_LOG, "checkIn Success");

            } else {
                HiLog.debug(LABEL_LOG, "checkIn Failure");

            }
        } catch (Exception ex) {
            HiLog.debug(LABEL_LOG, "checkIn Exception" + ex.toString());

        }

    }

    @Override
    protected void onStop() {
        super.onStop();
    }
}

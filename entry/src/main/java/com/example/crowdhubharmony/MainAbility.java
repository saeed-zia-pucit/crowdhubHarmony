package com.example.crowdhubharmony;


import com.example.crowdhubharmony.api.ApiInterface;
import com.example.crowdhubharmony.helper.DateUtil;
import com.example.crowdhubharmony.helper.GlobalConstants;
import com.example.crowdhubharmony.helper.SharedPref;
import com.example.crowdhubharmony.model.LoginResponseModel;
import com.example.crowdhubharmony.model.UpdateStatusModel;
import com.example.crowdhubharmony.model.UpdateStatusRequestModel;
import com.kongzue.dialog.util.Log;
import ohos.aafwk.ability.Ability;
import ohos.aafwk.content.Intent;
import ohos.aafwk.content.Operation;
import ohos.agp.components.Button;
import retrofit2.Response;

public final class MainAbility extends Ability {
    private boolean isLoginHit;

    private String deviceid;
    private ApiInterface apiInterface;
    private Button loginButton, signupButton;


    @Override
    protected void onStart(Intent intent) {
        super.onStart(intent);
        super.setUIContent(ResourceTable.Layout_ability_main);
//        loginButton =(Button) findComponentById(ResourceTable.btnLogin);
    }

    private void hitLogin() {
        if (!isLoginHit) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        isLoginHit = true;

                        GlobalConstants.showProgressDialog(com.example.crowdhubharmony.MainAbility.this,"Dialog message");

                        Response<LoginResponseModel> response = apiInterface.login(deviceid, SharedPref.getInstance(getContext()).getFcmToken()).execute();

                        GlobalConstants.hideProgressDialog();

                        if (response.isSuccessful()) {
                            isLoginHit = false;
                            if (response.body().getError()) {

                            } else {
                                hitUpdateStatus();
                                SharedPref.getInstance(getContext()).setDeviceServerId(response.body().getSmart_watch() != null ? response.body().getSmart_watch() : -1);
                                SharedPref.getInstance(getContext()).setIsLoggedIn(true);
                                SharedPref.getInstance(getContext()).setIsCheckedIn(false);
                                SharedPref.getInstance(getContext()).setIsCheckOutHit(false);
                                SharedPref.getInstance(getContext()).setCheckActionTime(DateUtil.getCurrentDate());
                                startCheckOutWorkManager();

                                Intent navIntent = new Intent();
                                Operation operation = new Intent.OperationBuilder()
                                        .withAbilityName("com.example.crowdhubharmony.TestAbility")
                                        .build();
                                navIntent.setOperation(operation);
                                startAbility(navIntent);


                            }
                        } else {
                            isLoginHit = false;

                        }
                    } catch (Exception Ex) {
                        isLoginHit = false;
                        Log.e("Error", Ex.getLocalizedMessage());
                    }
                }
            }).start();
        }
    }


    private void hitUpdateStatus() {
        if (!isLoginHit) {
            try {
                isLoginHit = true;
                UpdateStatusModel smartWatch = new UpdateStatusModel(0.0,0.0,"",0,deviceid, SharedPref.getInstance(getContext()).getFcmToken());
                UpdateStatusRequestModel body = new UpdateStatusRequestModel(smartWatch);
                Response<LoginResponseModel> response = apiInterface.updateStatus(SharedPref.getInstance(getContext()).getDeviceServerId(), body).execute();
                if (response.isSuccessful()) {
                    isLoginHit = false;
                    if (response.body().getError()) {

                    } else {

                    }
                } else {
                    isLoginHit = false;
                }
            } catch (Exception Ex) {
                isLoginHit = false;
                Log.e("Error", Ex.getLocalizedMessage());
            }
        }
    }


    private void startCheckOutWorkManager() {
//        long delay = 1000L;
//
//        long repeatTime = 1440L;
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//
//            Data data = new Data.Builder()
//                    .putString("device_id", deviceid)
//                    .build();
//            PeriodicWorkRequest.Builder workRequest = new PeriodicWorkRequest.Builder(
//                    CheckOutWorkManager.class,
//                    repeatTime,
//                    TimeUnit.MINUTES
//            );
//            PeriodicWorkRequest workRequestBuilder = workRequest.setInputData(data)
//                    .setInitialDelay(delay + 2, TimeUnit.MINUTES)
//                    .build();
//
//            WorkManager.getInstance(this).enqueueUniquePeriodicWork(
//                    "app_work",
//                    ExistingPeriodicWorkPolicy.REPLACE,
//                    workRequestBuilder
//            );
//        }
    }


}
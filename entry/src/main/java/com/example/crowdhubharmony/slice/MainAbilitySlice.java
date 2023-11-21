package com.example.crowdhubharmony.slice;


import com.example.crowdhubharmony.ResourceTable;
import com.example.crowdhubharmony.api.ApiInterface;
import com.example.crowdhubharmony.api.RetrofitClient;
import com.example.crowdhubharmony.helper.DateUtil;
import com.example.crowdhubharmony.helper.GlobalConstants;
import com.example.crowdhubharmony.helper.SharedPref;
import com.example.crowdhubharmony.model.LoginResponseModel;
import com.example.crowdhubharmony.model.UpdateStatusModel;
import com.example.crowdhubharmony.model.UpdateStatusRequestModel;
import com.huawei.hms.push.common.ApiException;
import com.huawei.hms.push.ohos.HmsInstanceId;
import com.huawei.watch.kit.hiwear.HiWear;
import com.huawei.watch.kit.hiwear.p2p.HiWearMessage;
import com.huawei.watch.kit.hiwear.p2p.P2pClient;
import com.huawei.watch.kit.hiwear.p2p.Receiver;
import com.kongzue.dialog.util.Log;
import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;
import ohos.aafwk.content.Operation;
import ohos.agp.components.Button;
import ohos.agp.components.Component;
import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;
import retrofit2.Response;
import retrofit2.Retrofit;

import java.io.File;
import java.nio.charset.StandardCharsets;

public class MainAbilitySlice extends AbilitySlice {
    private Button loginButton, signupButton;
    private boolean isLoginHit;
    private static final HiLogLabel HILOG_LABEL = new HiLogLabel(0, 0, "MainAbility");
    Retrofit retrofit = RetrofitClient.getInstance();
    ApiInterface apiInterface = retrofit.create(ApiInterface.class);
    Intent myIntent;
    private static final int MY_MODULE = 0x00201;
    private static final HiLogLabel LABEL = new HiLogLabel(HiLog.LOG_APP, MY_MODULE, "MainAbilitySlice");
    private P2pClient mP2pClient;
    private Receiver mReceiver;
    // Set the package name of your app on the phone
    private static final String TEST_PEER_PACKAGE = "saeed.demo.phonetowatch";
    // Set the fingerprint information for the phone app
    private static final String TEST_FINGERPRINT = "72:AA:02:2F:BD:0B:A7:C0:72:85:75:A1:36:9D:E9:C9:7C:F6:C1:8D:49:DF:DA:FE:A6:29:0D:1C:43:84:BE:E0";

    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        this.myIntent = intent;
        super.setUIContent(ResourceTable.Layout_ability_main);
        //  deviceId = AccountAbility.getAccountAbility().getDistributedVirtualDeviceId();


        loginButton = (Button) findComponentById(ResourceTable.Id_btnLogin);

        signupButton = (Button) findComponentById(ResourceTable.Id_btnSignUp);
        clickListener();
        mP2pClient = HiWear.getP2pClient(this);

        // Step 2: Set the package name of your app on the phone
        mP2pClient.setPeerPkgName(TEST_PEER_PACKAGE);

        // Step 3: Set the fingerprint information for the phone app
        mP2pClient.setPeerFingerPrint(TEST_FINGERPRINT);


    }
    private void registerReceiver() {
        mReceiver = message -> {
            switch (message.getType()) {
                // Receive the short message
                case HiWearMessage.MESSAGE_TYPE_DATA: {
                    HiLog.info(LABEL, "receive P2P message");
                    if (message.getData() != null) {
                        String msg = new String(message.getData(), StandardCharsets.UTF_8);

                        loginButton.setText(msg);

                    }
                    break;
                }
                // Receive the file
                case HiWearMessage.MESSAGE_TYPE_FILE: {
                    HiLog.info(LABEL, "receive file");
                    File file = message.getFile();
                    if (file != null && file.exists()) {
                        // receive file
                        HiLog.info(LABEL, "receive file");
                    }
                    break;
                }
                default:
                    HiLog.error(LABEL, "unsupported message type");
            }
        };
        mP2pClient.registerReceiver(mReceiver);
    }

    private void unregisterReceiver() {
        if (mP2pClient != null && mReceiver != null) {
            mP2pClient.unregisterReceiver(mReceiver);
        }
    }

    private void clickListener() {


//        signupButton.setClickedListener(new Component.ClickedListener() {
//            @Override
//            public void onClick(Component component) {
//                try {
//
//                    requestPermission();
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//
//            }
//        });
        signupButton.setClickedListener(new Component.ClickedListener() {
            @Override
            public void onClick(Component component) {
                // getToken()
                registerReceiver();

            }
        });
        loginButton.setClickedListener(new Component.ClickedListener() {
            @Override
            public void onClick(Component component) {
                unregisterReceiver();
               // hitLogin();
            }
        });

    }



    private void hitLogin() {
        HiLog.debug(HILOG_LABEL, "1");

        if (!isLoginHit) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        isLoginHit = true;

                        GlobalConstants.showProgressDialog(getContext(), "Dialog message");

                        Response<LoginResponseModel> response = apiInterface.login(GlobalConstants.deviceId, SharedPref.getInstance(getContext()).getFcmToken()).execute();
                        HiLog.debug(HILOG_LABEL, "2");

                        GlobalConstants.hideProgressDialog();

                        if (response.isSuccessful()) {
                            HiLog.debug(HILOG_LABEL, "3");
                            HiLog.debug(HILOG_LABEL, response.body().toString());

                            isLoginHit = false;
                            if (response.body().getError()) {
                                HiLog.debug(HILOG_LABEL, "error" + response.body().getMessage().toString());
                                HiLog.debug(HILOG_LABEL, response.body().getSmart_watch().toString());
                                HiLog.debug(HILOG_LABEL, response.body().getSuccess().toString());

                            } else {
                                HiLog.debug(HILOG_LABEL, "getSmart_watch" + response.body().getSmart_watch().toString());

                                hitUpdateStatus();
                                SharedPref.getInstance(getContext()).setDeviceServerId(response.body().getSmart_watch() != null ? response.body().getSmart_watch() : -1);
                                SharedPref.getInstance(getContext()).setIsLoggedIn(true);
                                SharedPref.getInstance(getContext()).setIsCheckedIn(false);
                                SharedPref.getInstance(getContext()).setIsCheckOutHit(false);
                                SharedPref.getInstance(getContext()).setCheckActionTime(DateUtil.getCurrentDate());
                                // startCheckOutWorkManager();
                                Operation operation = new Intent.OperationBuilder()
                                        .withAbilityName("com.example.crowdhubharmony.DeviceIdAbility")
                                        .build();
                                myIntent.setOperation(operation);
                                startAbility(myIntent);

                            }
                        } else {
                            isLoginHit = false;
                            HiLog.debug(HILOG_LABEL, "4");
                            HiLog.debug(HILOG_LABEL, response.body().toString());

                        }
                    } catch (Exception Ex) {
                        HiLog.debug(HILOG_LABEL, Ex.toString());

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
                HiLog.debug(HILOG_LABEL, "hitUpdateStatus");

                UpdateStatusModel smartWatch = new UpdateStatusModel(0.0, 0.0, "", 0, GlobalConstants.deviceId, SharedPref.getInstance(getContext()).getFcmToken());
                UpdateStatusRequestModel requestModel = new UpdateStatusRequestModel(smartWatch);
                HiLog.debug(HILOG_LABEL, SharedPref.getInstance(getContext()).getDeviceServerId() + "server id");
                HiLog.debug(HILOG_LABEL, SharedPref.getInstance(getContext()).getFcmToken());

                Response<LoginResponseModel> response = apiInterface.updateStatus(SharedPref.getInstance(getContext()).getDeviceServerId(), requestModel).execute();
                if (response.isSuccessful()) {
                    HiLog.debug(HILOG_LABEL, "success" + response.body().toString());

                    isLoginHit = false;
                    if (response.body().getError()) {
                        HiLog.debug(HILOG_LABEL, "error" + response.body().getError().toString());

                    } else {
                        HiLog.debug(HILOG_LABEL, "fine");

                    }
                } else {
                    HiLog.debug(HILOG_LABEL, "failure");
                    // HiLog.debug(HILOG_LABEL, "message"+response.body().toString());

                    isLoginHit = false;
                }
            } catch (Exception Ex) {
                HiLog.debug(HILOG_LABEL, "exception" + Ex.toString());

                isLoginHit = false;
                Log.e("Error", Ex.getLocalizedMessage());
            }
        }
    }

    @Override
    public void onActive() {
        super.onActive();
    }

    @Override
    public void onForeground(Intent intent) {
        super.onForeground(intent);
    }

    private void getToken() {
//        try {
//            AGConnectOptionsBuilder builder = new AGConnectOptionsBuilder();
//            ResourceManager resourceManager = getResourceManager();
//            // Path to the agconnect-services.json file.
//            RawFileEntry rawFileEntry = resourceManager.getRawFileEntry("resources/rawfile/agconnect-services.json");
//            Resource resource = rawFileEntry.openRawFile();
//            builder.setInputStream(resource);
//            // If the client_id, client_secret, and api_key parameters are not set in your JSON file, call the following APIs to set them:
//            builder.setClientId("1103649825661930752");
//            builder.setClientSecret("0F49A147834F4C739B234A63E2E3303E934C2CE13FBC7B407E20FD500124D6D2");
//            builder.setApiKey("DAEDACDdpGp95lPnMlaX7fmBCvlbZNYbzcD3OZr33H7vmIj4my12PS8aSVRS8YJFzKWap5QhlBs8rtdi01C2ORo1XWf0usosJnCOdg==");
//            AGConnectInstance.initialize(getAbility().getAbilityPackage(), builder);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }


        // Create a thread.
        new Thread("getToken") {
            @Override
            public void run() {
                try {
                    // Obtain the value of client/app_id from the agconnect-services.json file.
                    String appId = "1103649833840814912";
                    // Set tokenScope to HCM.
                    String tokenScope = "HCM";
                    // Obtain a push token.
                    String token = HmsInstanceId.getInstance(getAbility().getAbilityPackage(), MainAbilitySlice.this).getToken(appId, tokenScope);
                    HiLog.error(HILOG_LABEL, "token", token.toString());

                } catch (ApiException e) {
                    // An error code is recorded when the push token fails to be obtained.
                    HiLog.error(HILOG_LABEL, "get token failed, the error code is %{public}d", e.getStatusCode());
                }
            }
        }.start();
    }
}


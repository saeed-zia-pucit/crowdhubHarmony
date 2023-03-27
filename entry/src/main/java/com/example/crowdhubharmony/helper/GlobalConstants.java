package com.example.crowdhubharmony.helper;


import com.kongzue.dialog.v3.MessageDialog;
import ohos.app.Context;
import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public final class GlobalConstants {
    private static boolean isNetworkReceiverRegistered;

    @NotNull
    private static final String BASE_URL_DIRECTIONS = "https://mapapi.cloud.huawei.com/mapApi/v1/";

    public final boolean isNetworkReceiverRegistered() {
        return GlobalConstants.isNetworkReceiverRegistered;
    }

    public final void setNetworkReceiverRegistered(boolean var1) {
        GlobalConstants.isNetworkReceiverRegistered = var1;
    }


    @NotNull
    public final String getBASE_URL_DIRECTIONS() {
        return GlobalConstants.BASE_URL_DIRECTIONS;
    }

//        private final void registerNetworkListener(Context context) {
//            NetworkSpecifier networkRequest = (new Builder()).addCapability(12).addTransportType(1).addTransportType(0).build();
//            if (((GlobalConstants.Companion)this).getNetworkCallback() == null) {
//                ((GlobalConstants.Companion)this).setNetworkCallback((NetworkCallback)(new NetworkCallback() {
//                    public void onAvailable(@NotNull Network network) {
//                        Intrinsics.checkNotNullParameter(network, "network");
//                        super.onAvailable(network);
//                    }
//                }));
//            }
//
//            Object var10000 = context.getSystemService(ConnectivityManager.class);
//            if (var10000 == null) {
//                throw new NullPointerException("null cannot be cast to non-null type android.net.ConnectivityManager");
//            } else {
//                ConnectivityManager connectivityManager = (ConnectivityManager)var10000;
//                NetworkCallback var7 = ((GlobalConstants.Companion)this).getNetworkCallback();
//                if (var7 != null) {
//                    NetworkCallback var4 = var7;
//                    int var6 = false;
//                    connectivityManager.requestNetwork(networkRequest, var4);
//                    GlobalConstants.Companion.setNetworkReceiverRegistered(true);
//                }
//
//            }
//        }

//        public final boolean isNetworkAvailable(@NotNull Context context) {
//            Intrinsics.checkNotNullParameter(context, "context");
//            ConnectivityManager connectivityManager = (ConnectivityManager)context.getSystemService("connectivity");
//            Intrinsics.checkNotNull(connectivityManager);
//            NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
//            return activeNetworkInfo != null && activeNetworkInfo.isConnected();
//        }


    public static final void showProgressDialog(@NotNull Context context, String txt) {
//            Dialog dialog = new Dialog(getContext());
//            dialog.setContentView(R.layout.custom_dialog);
//            dialog.setTitle("My Custom Dialog");
//            dialog.show();
        MessageDialog.show(context, txt, "", "");
    }

    public static final void hideProgressDialog() {
//            AlertDialog var10000 = GlobalConstants.progressDialog;
//            if (var10000 != null) {
//                AlertDialog var1 = var10000;
//                int var3 = false;
//                if (var1.isShowing()) {
//                    var1.dismiss();
//                }
//            }

    }

    public final String today() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(Calendar.getInstance().getTime());

    }

    public final void savePreferences(@NotNull String key, int value) {
//        SharedPrefHelper.editor?.putInt(key, value)
//        SharedPrefHelper.editor?.commit()

    }

    public final int getPreferences(@NotNull String key) {
//        return SharedPrefHelper.pref?.getInt(key, 0) ?: 0

        return 0;
    }


}


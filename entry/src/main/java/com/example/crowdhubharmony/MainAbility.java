package com.example.crowdhubharmony;


import com.example.crowdhubharmony.slice.MainAbilitySlice;
import ohos.aafwk.ability.Ability;
import ohos.aafwk.content.Intent;

public  class MainAbility extends Ability {

    @Override
    protected void onStart(Intent intent) {
        super.onStart(intent);
        super.setMainRoute(MainAbilitySlice.class.getName());


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
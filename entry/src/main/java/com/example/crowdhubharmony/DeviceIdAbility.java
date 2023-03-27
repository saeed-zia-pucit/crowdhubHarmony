package com.example.crowdhubharmony;

import ohos.aafwk.ability.Ability;
import ohos.aafwk.content.Intent;

import java.util.UUID;

public class DeviceIdAbility extends Ability {


    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setUIContent(ResourceTable.Layout_ability_device_id);

        String deviceId = UUID.randomUUID().toString();

    }
}
package com.example.crowdhubharmony;

import com.example.crowdhubharmony.slice.DeviceIdAbilitySlice;
import ohos.aafwk.ability.Ability;
import ohos.aafwk.content.Intent;

public class DeviceIdAbility extends Ability {

    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setMainRoute(DeviceIdAbilitySlice.class.getName());

    }
}
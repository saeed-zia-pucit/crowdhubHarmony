package com.example.crowdhubharmony;

import com.example.crowdhubharmony.slice.MapDirectionAbilitySlice;
import ohos.aafwk.ability.Ability;
import ohos.aafwk.content.Intent;

public class MapDirectionAbility extends Ability {
    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setMainRoute(MapDirectionAbilitySlice.class.getName());
    }
}

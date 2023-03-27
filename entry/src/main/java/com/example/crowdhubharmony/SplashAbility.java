package com.example.crowdhubharmony;


import com.example.crowdhubharmony.helper.SharedPref;
import ohos.aafwk.ability.Ability;
import ohos.aafwk.content.Intent;
import ohos.aafwk.content.Operation;

import java.util.Timer;
import java.util.TimerTask;

public class SplashAbility extends Ability {
    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setUIContent(ResourceTable.Layout_ability_splash_activity);
        setTimer();
    }

    @Override
    public void onActive() {
        super.onActive();
    }

    @Override
    public void onForeground(Intent intent) {
        super.onForeground(intent);
    }

    private final void setTimer() {
        Timer timer = new Timer();
        timer.schedule((TimerTask)(new TimerTask() {
            public void run() {
                Intent intent = new Intent();

                if (SharedPref.getInstance(getContext()).getIsLoggedIn()) {
                    Operation operation = new Intent.OperationBuilder()
                            .withAbilityName("com.example.threadingsample.second.MainAbility")
                            .build();

                    intent.setOperation(operation);
                    startAbility(intent);
                } else {
                    Operation operation = new Intent.OperationBuilder()
                            .withAbilityName("com.example.threadingsample.second.MainAbility")
                            .build();

                    intent.setOperation(operation);
                    startAbility(intent);
                }
            }
        }), 3000L);
    }
}


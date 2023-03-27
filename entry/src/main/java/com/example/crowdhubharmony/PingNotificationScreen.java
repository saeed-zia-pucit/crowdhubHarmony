package com.example.crowdhubharmony;

import ohos.aafwk.ability.Ability;
import ohos.aafwk.content.Intent;
import ohos.agp.components.Button;
import ohos.media.audio.SoundPlayer;
import ohos.media.audio.ToneDescriptor;

public class PingNotificationScreen extends Ability {
    SoundPlayer soundPlayer;


    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        setUIContent(ResourceTable.Layout_ability_ping_notification_screen);
        soundPlayer = new SoundPlayer();

        playRingtone();
        Button btnPing = (Button) findComponentById(ResourceTable.Id_btnPing);
        btnPing.setClickedListener(component -> {
            stopRingtone();
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    public void playRingtone() {
        //Step 2: Create a DTMF_0 tone (high frequency: 1336 Hz and low frequency: 941 Hz) with a duration of 1000 milliseconds.
        soundPlayer.createSound(ToneDescriptor.ToneType.DTMF_0, 1000);
        soundPlayer.play();

    }

    public void stopRingtone() {
        if (soundPlayer != null) {
            soundPlayer.pause();
            soundPlayer.release();
        }
    }
}

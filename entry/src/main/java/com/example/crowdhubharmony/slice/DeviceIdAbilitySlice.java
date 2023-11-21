package com.example.crowdhubharmony.slice;

import com.example.crowdhubharmony.ResourceTable;
import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;
import ohos.agp.components.Text;
import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;


public class DeviceIdAbilitySlice extends AbilitySlice {
    private static final HiLogLabel HILOG_LABEL = new HiLogLabel(0, 0, "HmsMessageService");

    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setUIContent(ResourceTable.Layout_ability_device_id);
        Text deviceIdText = (Text) findComponentById(ResourceTable.Id_deviceIdTv);
        String deviceId = "234adfg-343fd-df";
        deviceIdText.setText(deviceId);
        HiLog.debug(HILOG_LABEL, deviceId);

        // deviceId = AccountAbility.getAccountAbility().getDistributedVirtualDeviceId();
        deviceIdText.setText(deviceId);
        HiLog.debug(HILOG_LABEL, deviceId);

    }


}

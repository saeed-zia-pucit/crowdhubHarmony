package com.example.crowdhubharmony.services;


import com.example.crowdhubharmony.helper.SharedPref;
import com.huawei.hms.push.ohos.HmsMessageService;
import com.huawei.hms.push.ohos.ZRemoteMessage;
import ohos.aafwk.content.Intent;
import ohos.agp.utils.Color;
import ohos.event.notification.NotificationHelper;
import ohos.event.notification.NotificationRequest;
import ohos.event.notification.NotificationSlot;
import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;
import ohos.rpc.RemoteException;

public class MessagingService extends HmsMessageService {
    private static final HiLogLabel HILOG_LABEL = new HiLogLabel(0, 0, "HmsMessageServiceToken");

    @Override
    public void onMessageReceived(ZRemoteMessage message) {
        HiLog.info(HILOG_LABEL, "get token, %{public}s", message.getToken());
        HiLog.info(HILOG_LABEL, "get data, %{public}s", message.getData());

        ZRemoteMessage.Notification notification = message.getNotification();
        if (notification != null) {
            HiLog.info(HILOG_LABEL, "get title, %{public}s", notification.getTitle());
            HiLog.info(HILOG_LABEL, "get body, %{public}s", notification.getBody());

            sendNotification(message);
        } else {
            HiLog.info(HILOG_LABEL, "onMessageReceived: No Notification");
        }
    }

    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
        HiLog.debug(HILOG_LABEL, s);
        SharedPref.getInstance(getContext()).setFcmToken(s);
    }

    private void sendNotification(ZRemoteMessage message) {
        NotificationSlot slot = new NotificationSlot("slot_001", "slot_default", NotificationSlot.LEVEL_MIN); // Create a NotificationSlot object.
        slot.setDescription("NotificationSlotDescription");
        slot.setEnableVibration(true); // Enable vibration when a notification is received.
        slot.setEnableLight(true); // Enable the notification light.
        slot.setLedLightColor(Color.RED.getValue());// Set the color of the notification light.
        try {
            NotificationHelper.addNotificationSlot(slot);
        } catch (RemoteException ex) {
            HiLog.error(HILOG_LABEL, "Exception occurred during addNotificationSlot invocation.");
        }

        int notificationId = 1;
        NotificationRequest request = new NotificationRequest(notificationId);
        request.setSlotId(slot.getId());

        String title = "title";
        String text = "There is a normal notification content.";
        NotificationRequest.NotificationNormalContent content = new NotificationRequest.NotificationNormalContent();
        content.setTitle(title)
                .setText(text);
        NotificationRequest.NotificationContent notificationContent = new NotificationRequest.NotificationContent(content);
        request.setContent(notificationContent); // Set the content type of the notification.


        try {
            NotificationHelper.publishNotification(request);
        } catch (RemoteException ex) {
            HiLog.error(HILOG_LABEL, "Exception occurred during publishNotification invocation.");
        }
    }

    @Override
    public void onStart(Intent intent) {
        HiLog.error(HILOG_LABEL, "PushServiceAbility::onStart");
        super.onStart(intent);
    }

    @Override
    public void onBackground() {
        super.onBackground();
        HiLog.info(HILOG_LABEL, "PushServiceAbility::onBackground");
    }

    @Override
    public void onStop() {
        super.onStop();
        HiLog.info(HILOG_LABEL, "PushServiceAbility::onStop");
    }

    @Override
    public void onCommand(Intent intent, boolean restart, int startId) {
    }

    public void onTokenError(Exception exception) {
        HiLog.debug(HILOG_LABEL, "onTokenError" + exception.toString());
    }

}



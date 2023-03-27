package com.example.crowdhubharmony.services;


import com.kongzue.dialog.util.Log;
import ohos.agp.utils.Color;
import ohos.event.notification.NotificationHelper;
import ohos.event.notification.NotificationRequest;
import ohos.event.notification.NotificationSlot;
import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;
import ohos.rpc.RemoteException;

public class MessagingService extends HmsMessageService {
    private static final HiLogLabel HILOG_LABEL = new HiLogLabel(0, 0, "HmsMessageService");

    @Override
    public void onMessageReceived(RemoteMessage message) {
        super.onMessageReceived(message);
        HiLog.info(HILOG_LABEL, "onMessageReceived: " + message.getNotification());

        if (message.getNotification() != null) {
            sendNotification(message);
        } else {
            HiLog.info(HILOG_LABEL, "onMessageReceived: Data");
            showNotification();
            HiLog.info(HILOG_LABEL, "onMessageReceived: END");
        }
    }

    private void sendNotification(RemoteMessage message) {
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

    private void showNotification() {

    }
}



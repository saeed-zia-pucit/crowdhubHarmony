package com.example.crowdhubharmony.helper;

public class AlarmHelper {
//    private static final HiLogLabel LABEL = new HiLogLabel(HiLog.LOG_APP, 0x00201, "AlarmExample");
//    private static final long INTERVAL_MINUTES = 10;
//
//    public static void main(String[] args) {
//        IAlarmSysAbilityManager asm = AlarmManager.getAlarmSysAbilityManager();
//        if (asm == null) {
//            HiLog.error(LABEL, "Failed to get IAlarmSysAbilityManager");
//            return;
//        }
//
//        long currentTime = System.currentTimeMillis();
//        long startTime = roundUp(currentTime, INTERVAL_MINUTES);
//        long interval = INTERVAL_MINUTES * 60 * 1000; // in milliseconds
//
//        AlarmManager am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
//
//        Alarm alarm = new Alarm.Builder()
//                .setAlarmId(1)
//                .setOperationType(Alarm.OperationType.NOTIFICATION)
//                .setTriggerTime(startTime)
//                .setInterval(interval)
//                .setRepeating(true)
//                .setAlarmListener(new MyAlarmListener())
//                .build();
//
//        boolean result = asm.set(alarm);
//        if (!result) {
//            HiLog.error(LABEL, "Failed to set alarm");
//        } else {
//            HiLog.info(LABEL, "Alarm set for %d, repeating every %d minutes", startTime, INTERVAL_MINUTES);
//        }
//    }
//
//    private static long roundUp(long time, long interval) {
//        Calendar cal = Calendar.getInstance();
//        cal.setTimeInMillis(time);
//        cal.add(Calendar.MINUTE, (int) interval - (cal.get(Calendar.MINUTE) % interval));
//        cal.set(Calendar.SECOND, 0);
//        cal.set(Calendar.MILLISECOND, 0);
//        return cal.getTimeInMillis();
//    }
//
//    private static class MyAlarmListener implements IAlarmListener {
//        @Override
//        public void onAlarm() {
//            HiLog.info(LABEL, "Alarm triggered!");
//
//            // Execute your code here
//            // ...
//        }
//    }
}


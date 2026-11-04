package com.example.vendasdemo;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.app.Notification;
import android.app.NotificationManager;
import android.os.Build;
import java.util.Locale;
import java.util.Random;

public class SaleAlarmReceiver extends BroadcastReceiver {
    private static final int ALARM_ID = 4102;
    private static final Random RANDOM = new Random();

    @Override public void onReceive(Context context, Intent intent) {
        MainActivity.createChannel(context);
        int[] cents = {1790, 2790, 4790, 5790, 9790, 14790, 19700};
        int amount = cents[RANDOM.nextInt(cents.length)];
        String value = String.format(Locale.forLanguageTag("pt-BR"), "R$ %d,%02d", amount / 100, amount % 100);
        Notification.Builder builder = Build.VERSION.SDK_INT >= 26
                ? new Notification.Builder(context, "sales_demo") : new Notification.Builder(context);
        builder.setSmallIcon(android.R.drawable.ic_dialog_info)
                .setContentTitle("Venda aprovada! • DEMONSTRAÇÃO")
                .setContentText("Venda fictícia de " + value)
                .setStyle(new Notification.BigTextStyle().bigText("Venda fictícia de " + value + ". Notificação de demonstração pessoal; nenhuma transação real ocorreu."))
                .setAutoCancel(true);
        ((NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE)).notify((int)System.currentTimeMillis(), builder.build());
        scheduleNext(context);
    }

    static void scheduleNext(Context context) {
        int minutes = 2 + RANDOM.nextInt(14); // 2..15
        long when = System.currentTimeMillis() + minutes * 60_000L;
        AlarmManager manager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        PendingIntent pi = pendingIntent(context);
        if (Build.VERSION.SDK_INT >= 23) manager.setAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, when, pi);
        else manager.set(AlarmManager.RTC_WAKEUP, when, pi);
    }

    static void cancel(Context context) {
        ((AlarmManager)context.getSystemService(Context.ALARM_SERVICE)).cancel(pendingIntent(context));
    }

    private static PendingIntent pendingIntent(Context context) {
        Intent i = new Intent(context, SaleAlarmReceiver.class);
        int flags = PendingIntent.FLAG_UPDATE_CURRENT;
        if (Build.VERSION.SDK_INT >= 23) flags |= PendingIntent.FLAG_IMMUTABLE;
        return PendingIntent.getBroadcast(context, ALARM_ID, i, flags);
    }
}

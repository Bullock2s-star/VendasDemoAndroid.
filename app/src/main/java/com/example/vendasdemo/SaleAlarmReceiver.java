
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

    @Override
    public void onReceive(Context context, Intent intent) {
        showNotification(context);
        scheduleNext(context);
    }

    static void showTestNotification(Context context) {
        showNotification(context);
    }

    private static void showNotification(Context context) {

        MainActivity.createChannel(context);

        int amountCents = 200 + RANDOM.nextInt(4800);

        String value = String.format(
                Locale.forLanguageTag("pt-BR"),
                "R$ %d,%02d",
                amountCents / 100,
                amountCents % 100
        );

        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        // PRIMEIRA NOTIFICAÇÃO: PIX
        Notification.Builder pixBuilder;

        if (Build.VERSION.SDK_INT >= 26) {
            pixBuilder = new Notification.Builder(context, "sales_demo");
        } else {
            pixBuilder = new Notification.Builder(context);
        }

        pixBuilder.setSmallIcon(R.drawable.logo_cakto)
                .setContentTitle("Pix gerado!")
                .setContentText("Sua comissão: " + value)
                .setAutoCancel(true)
                .setPriority(Notification.PRIORITY_HIGH);

        notificationManager.notify(4103, pixBuilder.build());

        // SEGUNDA NOTIFICAÇÃO: VENDA
        Notification.Builder saleBuilder;

        if (Build.VERSION.SDK_INT >= 26) {
            saleBuilder = new Notification.Builder(context, "sales_demo");
        } else {
            saleBuilder = new Notification.Builder(context);
        }

        saleBuilder.setSmallIcon(R.drawable.logo_cakto)
                .setContentTitle("Venda aprovada!")
                .setContentText("Sua comissão: " + value)
                .setAutoCancel(true)
                .setPriority(Notification.PRIORITY_HIGH);

        notificationManager.notify(4104, saleBuilder.build());
    }

    static void scheduleNext(Context context) {

        int minutes = 2 + RANDOM.nextInt(14);

        long when = System.currentTimeMillis() + minutes * 60_000L;

        AlarmManager manager =
                (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        PendingIntent pi = pendingIntent(context);

        if (Build.VERSION.SDK_INT >= 23) {
            manager.setAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    when,
                    pi
            );
        } else {
            manager.set(
                    AlarmManager.RTC_WAKEUP,
                    when,
                    pi
            );
        }
    }

    static void cancel(Context context) {
        AlarmManager manager =
                (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        manager.cancel(pendingIntent(context));
    }

    private static PendingIntent pendingIntent(Context context) {

        Intent intent = new Intent(context, SaleAlarmReceiver.class);

        int flags = PendingIntent.FLAG_UPDATE_CURRENT;

        if (Build.VERSION.SDK_INT >= 23) {
            flags |= PendingIntent.FLAG_IMMUTABLE;
        }

        return PendingIntent.getBroadcast(
                context,
                ALARM_ID,
                intent,
                flags
        );
    }
}

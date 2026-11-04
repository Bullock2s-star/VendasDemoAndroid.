
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

    // Notificação de teste acionada pelo botão do aplicativo
    static void showTestNotification(Context context) {
        showNotification(context);
    }

    private static void showNotification(Context context) {

        MainActivity.createChannel(context);

        // Valor ilustrativo entre R$ 2,00 e R$ 49,99
        int amountCents = 200 + RANDOM.nextInt(4800);

        String value = String.format(
                Locale.forLanguageTag("pt-BR"),
                "R$ %d,%02d",
                amountCents / 100,
                amountCents % 100
        );

        // Escolhe aleatoriamente o tipo de notificação
        boolean isPix = RANDOM.nextBoolean();

        String title = isPix
                ? "Pix gerado!"
                : "Venda aprovada!";

        String mainText = "Sua comissão: " + value;

        // Identifica claramente que os valores são demonstrativos
        String detailText = isPix
                ? "DEMONSTRAÇÃO — Notificação ilustrativa de Pix. Valor: " + value
                : "DEMONSTRAÇÃO — Notificação ilustrativa de venda. Comissão: " + value;

        Notification.Builder builder;

        if (Build.VERSION.SDK_INT >= 26) {
            builder = new Notification.Builder(context, "sales_demo");
        } else {
            builder = new Notification.Builder(context);
        }

        builder.setSmallIcon(R.drawable.logo_cakto)
                .setContentTitle(title)
                .setContentText(mainText)
                .setStyle(new Notification.BigTextStyle().bigText(detailText))
                .setAutoCancel(true)
                .setPriority(Notification.PRIORITY_HIGH);

        int notificationId = (int) System.currentTimeMillis();

        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(notificationId, builder.build());
    }

    static void scheduleNext(Context context) {

        // Intervalo aleatório entre 2 e 15 minutos
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

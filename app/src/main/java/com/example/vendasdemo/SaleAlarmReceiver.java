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
        showRealNotification(context);
        scheduleNext(context);
    }

    // Chamado pelo botão de teste dentro do app
    static void showTestNotification(Context context) {
        showRealNotification(context);
    }

    private static void showRealNotification(Context context) {
        MainActivity.createChannel(context);
        
        // Valores variados para parecerem vendas reais e aleatórias
        int[] cents = {2780, 4500, 5590, 7800, 9790, 12500, 15000, 19700};
        int amount = cents[RANDOM.nextInt(cents.length)];
        String value = String.format(Locale.forLanguageTag("pt-BR"), "R$ %d,%02d", amount / 100, amount % 100);

        Notification.Builder builder = Build.VERSION.SDK_INT >= 26
                ? new Notification.Builder(context, "sales_demo") 
                : new Notification.Builder(context);

        // Título genérico e profissional
        String title = "Venda Aprovada!";
        
        // Texto principal: mostra o valor da comissão
        String mainText = "Sua comissão: " + value;
        
        // Texto expandido (quando puxa para baixo na notificação)
        String detailText = "Nova venda registrada. Valor líquido: " + value + ". O pagamento será processado em breve.";

        // Ícone: use R.drawable.seu_logo_se_tiver, senão deixa o padrão do sistema ou um ícone neutro
        // Se quiser usar seu logo, coloque: .setSmallIcon(R.drawable.ic_cakto)
        builder.setSmallIcon(android.R.drawable.ic_lock_idle_alarm) 
               .setContentTitle(title)
               .setContentText(mainText)
               .setStyle(new Notification.BigTextStyle().bigText(detailText))
               .setAutoCancel(true)
               .setPriority(Notification.PRIORITY_HIGH);

        // ID único para cada notificação
        int notificationId = (int) System.currentTimeMillis();
        ((NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE)).notify(notificationId, builder.build());
    }

    static void scheduleNext(Context context) {
        // Intervalo entre 2 e 15 minutos
        int minutes = 2 + RANDOM.nextInt(14); 
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
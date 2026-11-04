package com.example.vendasdemo; // Verifique se o pacote está correto no topo do seu projeto

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.app.Notification;
import android.app.NotificationManager;
import android.os.Build;
import java.util.Locale;
import java.util.Random;

public class SaleAlarmReceiver extends BroadcastReceiver {
    private static final int ALARM_ID = 4102;
    private static final Random RANDOM = new Random();

    @Override public void onReceive(Context context, Intent intent) {
        showNotification(context);
        scheduleNext(context);
    }

    // Chamado pelo botão de teste dentro do app
    static void showTestNotification(Context context) {
        showNotification(context);
    }

    private static void showNotification(Context context) {
        MainActivity.createChannel(context);

        // 1. Gera um valor monetário aleatório entre R$ 2,00 e R$ 50,00
        int amountCents = 200 + RANDOM.nextInt(4800); // 200 a 5000 centavos
        String value = String.format(Locale.forLanguageTag("pt-BR"), "R$ %d,%02d", amountCents / 100, amountCents % 100);

        // 2. Decide aleatoriamente qual título mostrar: "Pix Gerado!" ou "Venda Aprovada!"
        boolean isPix = RANDOM.nextBoolean(); 
        String title = isPix ? "Pix gerado!" : "Venda aprovada!";

        String mainText = "Sua comissão: " + value;
        String detailText = isPix 
            ? "Pagamento Pix confirmado. Valor: " + value + " creditado na sua conta." 
            : "Venda confirmada. Comissão de " + value + " adicionada.";

        Notification.Builder builder = Build.VERSION.SDK_INT >= 26
                ? new Notification.Builder(context, "sales_demo") 
                : new Notification.Builder(context);

        // --- CONFIGURAÇÃO DA IMAGEM ---
        try {
            // Substitua 'minha_logo' pelo NOME EXATO do seu arquivo na pasta drawable (sem .png/.jpg)
            // Se você ainda não colocou a imagem, use o ícone padrão do sistema para não dar erro
            int resourceId = context.getResources().getIdentifier("logo_cakto", "drawable", context.getPackageName());
            
            if (resourceId != 0) {
                Bitmap largeIconBitmap = BitmapFactory.decodeResource(context.getResources(), resourceId);
                builder.setLargeIcon(largeIconBitmap);
            } else {
                // Fallback caso a imagem não seja encontrada
                builder.setLargeIcon(BitmapFactory.decodeResource(context.getResources(), android.R.drawable.ic_menu_gallery));
            }
        } catch (Exception e) {
            // Silencia erros caso a imagem falhe
        }
        // -----------------------------

        builder.setSmallIcon(android.R.drawable.ic_lock_idle_alarm) // Ícone pequeno (canto superior direito)
               .setContentTitle(title)
               .setContentText(mainText)
               .setStyle(new Notification.BigTextStyle().bigText(detailText))
               .setAutoCancel(true)
               .setPriority(Notification.PRIORITY_HIGH);

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
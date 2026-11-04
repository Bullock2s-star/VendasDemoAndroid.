package com.example.vendasdemo;

import android.Manifest;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.graphics.Color;
import android.view.Gravity;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
    private static final int REQ_NOTIFICATIONS = 100;

    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        createChannel(this);

        LinearLayout root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setPadding(36, 48, 36, 36);
        root.setBackgroundColor(Color.rgb(246, 248, 246));
        root.setGravity(Gravity.CENTER_HORIZONTAL);

        TextView title = new TextView(this);
        title.setText("Vendas Demo"); title.setTextSize(28); title.setTextColor(Color.rgb(18, 53, 36));
        title.setGravity(Gravity.CENTER); title.setPadding(0, 12, 0, 8);
        root.addView(title, new LinearLayout.LayoutParams(-1, -2));

        TextView subtitle = new TextView(this);
        subtitle.setText("Simulação pessoal de notificações\nIntervalos aleatórios de 2 a 15 minutos");
        subtitle.setTextSize(16); subtitle.setTextColor(Color.DKGRAY); subtitle.setGravity(Gravity.CENTER);
        subtitle.setPadding(0, 8, 0, 28);
        root.addView(subtitle, new LinearLayout.LayoutParams(-1, -2));

        Button start = new Button(this); start.setText("Iniciar notificações");
        start.setOnClickListener(v -> requestPermissionAndStart());
        root.addView(start, new LinearLayout.LayoutParams(-1, -2));

        Button stop = new Button(this); stop.setText("Pausar notificações");
        stop.setOnClickListener(v -> { SaleAlarmReceiver.cancel(this); Toast.makeText(this, "Notificações pausadas", Toast.LENGTH_SHORT).show(); });
        LinearLayout.LayoutParams sp = new LinearLayout.LayoutParams(-1, -2); sp.topMargin = 12;
        root.addView(stop, sp);

        TextView note = new TextView(this);
        note.setText("As notificações são fictícias e identificadas como DEMONSTRAÇÃO. O Android pode atrasar entregas em economia de bateria ou modo Doze.");
        note.setTextSize(13); note.setTextColor(Color.GRAY); note.setGravity(Gravity.CENTER);
        note.setPadding(0, 28, 0, 0);
        root.addView(note, new LinearLayout.LayoutParams(-1, -2));
        setContentView(root);
    }

    private void requestPermissionAndStart() {
        if (Build.VERSION.SDK_INT >= 33 && checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.POST_NOTIFICATIONS}, REQ_NOTIFICATIONS);
        } else startSimulation();
    }

    @Override public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] results) {
        super.onRequestPermissionsResult(requestCode, permissions, results);
        if (requestCode == REQ_NOTIFICATIONS && results.length > 0 && results[0] == PackageManager.PERMISSION_GRANTED) startSimulation();
        else Toast.makeText(this, "Permita notificações para usar a demonstração", Toast.LENGTH_LONG).show();
    }

    private void startSimulation() {
        SaleAlarmReceiver.scheduleNext(this);
        Toast.makeText(this, "Demonstração iniciada", Toast.LENGTH_SHORT).show();
    }

    static void createChannel(Context c) {
        if (Build.VERSION.SDK_INT >= 26) {
            NotificationChannel channel = new NotificationChannel("sales_demo", "Vendas de demonstração", NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription("Notificações fictícias para demonstração pessoal");
            ((NotificationManager)c.getSystemService(Context.NOTIFICATION_SERVICE)).createNotificationChannel(channel);
        }
    }
}

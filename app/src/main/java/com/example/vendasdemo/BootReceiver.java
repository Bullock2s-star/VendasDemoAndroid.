package com.example.vendasdemo;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class BootReceiver extends BroadcastReceiver {
    @Override public void onReceive(Context context, Intent intent) {
        // O agendamento só é retomado automaticamente se o usuário tiver iniciado a demonstração.
        // Para manter o controle simples, o app não inicia sozinho após reinicialização.
    }
}

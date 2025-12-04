package com.example.barberiashop_app.workers;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.example.barberiashop_app.R;
import com.example.barberiashop_app.data.repository.NotificacionRepository;
import com.example.barberiashop_app.domain.entity.Notificacion;

public class ReminderWorker extends Worker {

    public ReminderWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        try {
            String titulo = getInputData().getString("titulo");
            String mensaje = getInputData().getString("mensaje");
            String usuarioEmail = getInputData().getString("usuarioEmail");

            if (titulo == null || mensaje == null) {
                android.util.Log.e("ReminderWorker", "Titulo o mensaje nulos");
                return Result.failure();
            }

            // 1. Mostrar Notificación del Sistema (Push)
            showSystemNotification(titulo, mensaje);

            // 2. Guardar en Base de Datos (Historial)
            if (usuarioEmail != null) {
                NotificacionRepository repository = new NotificacionRepository(getApplicationContext());
                Notificacion notificacion = new Notificacion(
                        usuarioEmail,
                        titulo,
                        mensaje,
                        System.currentTimeMillis());
                // Usar insertSync porque ya estamos en un hilo de fondo (Worker)
                repository.insertSync(notificacion);
            }

            return Result.success();
        } catch (Exception e) {
            android.util.Log.e("ReminderWorker", "Error en doWork", e);
            return Result.failure();
        }
    }

    private void showSystemNotification(String title, String message) {
        NotificationManager notificationManager = (NotificationManager) getApplicationContext()
                .getSystemService(Context.NOTIFICATION_SERVICE);
        String channelId = "turno_reminders";

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId, "Recordatorios de Turnos",
                    NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(channel);
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), channelId)
                .setSmallIcon(R.drawable.ic_notifications_black_24dp) // Asegúrate de tener este icono o usa otro
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true);

        notificationManager.notify((int) System.currentTimeMillis(), builder.build());
    }
}

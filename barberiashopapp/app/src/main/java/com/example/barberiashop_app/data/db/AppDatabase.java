package com.example.barberiashop_app.data.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.barberiashop_app.data.dao.ServicioDao;
import com.example.barberiashop_app.data.dao.TurnoDao;
import com.example.barberiashop_app.domain.entity.Servicio;
import com.example.barberiashop_app.domain.entity.Turno;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {Turno.class, Servicio.class}, version = 2, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    // Instancia unica de la base de datos (Singleton)
    // 'volatile' garantiza que siempre se lea el valor actualizado en memoria,
    // evitando problemas si multiples hilos intentan acceder a la instancia.
    private static volatile AppDatabase INSTANCIA;

    // Numero de hilos que podra usar el pool para ejecutar operaciones en background
    private static final int NUMBER_OF_THREADS = 4;

    // Executor que maneja la ejecucion de consultas (insert, update, delete) en segundo plano,
    // asi no bloqueamos el hilo principal (UI thread).
    public static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    // Metodo abstracto que da acceso al DAO (Data Access Object) de Turno.
    // Room genera la implementacion en tiempo de compilacion.
    public abstract TurnoDao turnoDao();
    public abstract ServicioDao servicioDao();

    //  Metodo estatico que devuelve la instancia unica de la base de datos.
    // Si no existe, se crea utilizando Room.databaseBuilder.
    public static AppDatabase getDatabase(final Context context) {
        if (INSTANCIA == null) {
            // Bloque sincronizado para asegurarnos de que solo un hilo cree la instancia
            synchronized (AppDatabase.class) {
                if (INSTANCIA == null) {
                    INSTANCIA = Room.databaseBuilder(
                                    context.getApplicationContext(),
                                    AppDatabase.class, //clase de la db
                                    "turnos_db"//nombre del archivo de la base de datos
                            )
                            // Si cambias el esquema (por ej: agregas campos/entidades)
                            // y no defines una migracion, esta opcion BORRA y recrea la DB.
                            // Se pierden los datos, pero es util en desarrollo.
                            .fallbackToDestructiveMigration()
                            .build();
                }

            }
        }
        return INSTANCIA;
    }

}

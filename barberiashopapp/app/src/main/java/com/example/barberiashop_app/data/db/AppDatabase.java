package com.example.barberiashop_app.data.db;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.barberiashop_app.data.dao.EstadoTurnoDao;
import com.example.barberiashop_app.data.dao.RolDao;
import com.example.barberiashop_app.data.dao.ServicioDao;
import com.example.barberiashop_app.data.dao.TurnoDao;
import com.example.barberiashop_app.data.dao.TurnoServicioDao;
import com.example.barberiashop_app.data.dao.UsuarioDao;
import com.example.barberiashop_app.domain.entity.EstadoTurno;
import com.example.barberiashop_app.domain.entity.Rol;
import com.example.barberiashop_app.domain.entity.Servicio;
import com.example.barberiashop_app.domain.entity.Turno;
import com.example.barberiashop_app.domain.entity.TurnoServicio;
import com.example.barberiashop_app.domain.entity.Usuario;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {
        Turno.class,
        Servicio.class,
        Rol.class,
        EstadoTurno.class,
        Usuario.class,
        TurnoServicio.class,
}, version = 11, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    // Instancia unica de la base de datos (Singleton)
    // 'volatile' garantiza que siempre se lea el valor actualizado en memoria,
    // evitando problemas si multiples hilos intentan acceder a la instancia.
    private static volatile AppDatabase INSTANCIA;

    // Numero de hilos que podra usar el pool para ejecutar operaciones en
    // background
    private static final int NUMBER_OF_THREADS = 4;

    // Executor que maneja la ejecucion de consultas (insert, update, delete) en
    // segundo plano,
    // asi no bloqueamos el hilo principal (UI thread).
    public static final ExecutorService databaseWriteExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    // Metodo abstracto que da acceso al DAO (Data Access Object) de Turno.
    // Room genera la implementacion en tiempo de compilacion.
    public abstract TurnoDao turnoDao();

    public abstract ServicioDao servicioDao();

    public abstract UsuarioDao usuarioDao();

    public abstract RolDao rolDao();

    public abstract EstadoTurnoDao estadoTurnoDao();

    public abstract TurnoServicioDao turnoServicioDao();

    // Metodo estatico que devuelve la instancia unica de la base de datos.
    // Si no existe, se crea utilizando Room.databaseBuilder.
    public static AppDatabase getDatabase(final Context context) {
        if (INSTANCIA == null) {
            // Bloque sincronizado para asegurarnos de que solo un hilo cree la instancia
            synchronized (AppDatabase.class) {
                if (INSTANCIA == null) {
                    INSTANCIA = Room.databaseBuilder(
                            context.getApplicationContext(),
                            AppDatabase.class, // clase de la db
                            "turnos_db"// nombre del archivo de la base de datos
                    )
                            // Si cambias el esquema (por ej: agregas campos/entidades)
                            // y no defines una migracion, esta opcion BORRA y recrea la DB.
                            // Se pierden los datos, pero es util en desarrollo.
                            .fallbackToDestructiveMigration()
                            .addCallback(sRoomDatabaseCallback)
                            .build();
                }

            }
        }
        return INSTANCIA;
    }

    private static final RoomDatabase.Callback sRoomDatabaseCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);

            // Ejecutar la inserción en el pool de hilos de background
            databaseWriteExecutor.execute(() -> {
                // DAOs
                ServicioDao servicioDao = INSTANCIA.servicioDao();
                RolDao rolDao = INSTANCIA.rolDao();
                EstadoTurnoDao estadoTurnoDao = INSTANCIA.estadoTurnoDao();
                UsuarioDao usuarioDao = INSTANCIA.usuarioDao();

                // --- ROLES ---
                Rol rolCliente = new Rol("cliente");
                Rol rolBarbero = new Rol("barbero");
                Rol rolDueno = new Rol("dueño");
                rolDao.insert(rolCliente);
                rolDao.insert(rolBarbero);
                rolDao.insert(rolDueno);

                // --- SERVICIOS ---
                servicioDao.insert(new Servicio("Corte de pelo (Hombre)",
                        "Corte clásico o moderno con tijera y/o máquina.", 8.00, 40));
                servicioDao.insert(new Servicio("Arreglo de barba",
                        "Afeitado tradicional con navaja y perfilado de barba.", 7.50, 30));
                servicioDao.insert(new Servicio("Corte y Barba Full",
                        "Servicio completo de corte de pelo y arreglo de barba.", 14.50, 70));
                servicioDao.insert(
                        new Servicio("Diseño de cejas", "Perfilado profesional de cejas con cera o pinzas.", 4.00, 15));
                servicioDao.insert(new Servicio("Lavado y Secado",
                        "Lavado con productos especializados y peinado rápido.", 4.50, 20));

                // --- ESTADOS DE TURNO ---
                estadoTurnoDao.insert(new EstadoTurno("pendiente"));
                estadoTurnoDao.insert(new EstadoTurno("confirmado"));
                estadoTurnoDao.insert(new EstadoTurno("cancelado"));
                estadoTurnoDao.insert(new EstadoTurno("terminado"));

                // --- USUARIOS ---
                // Podés usar contraseñas simples por ahora, ya que no estás aplicando hash
                usuarioDao.insert(new Usuario("Carlos Gómez", "cliente@cliente.com", "1234", null, "1122334455", 1)); // cliente
                usuarioDao.insert(new Usuario("Juan Pérez", "barbero@barbero.com", "1234", null, "1166778899", 2)); // barbero
                usuarioDao.insert(new Usuario("Pedro López", "dueno@dueno.com", "1234", null, "1100112233", 3)); // dueño
            });
        }
    };

}

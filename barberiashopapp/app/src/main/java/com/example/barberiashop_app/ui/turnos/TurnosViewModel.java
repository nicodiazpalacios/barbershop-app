package com.example.barberiashop_app.ui.turnos;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.example.barberiashop_app.UserPreferences;
import com.example.barberiashop_app.data.repository.TurnoRepository;
import com.example.barberiashop_app.domain.entity.Turno;
import com.example.barberiashop_app.domain.entity.TurnoConServicio;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class TurnosViewModel extends AndroidViewModel {
    private final TurnoRepository turnoRepository;

    // 2. MutableLiveData para guardar el email del usuario
    private final UserPreferences prefs; // Guardar la instancia de UserPreferences
    private final MutableLiveData<String> usuarioEmail = new MutableLiveData<>();
    private final LiveData<List<TurnoConServicio>> turnosOriginal; // LiveData de la BD

    private final MediatorLiveData<List<TurnoConServicio>> turnosFiltradosYOrdenados = new MediatorLiveData<>(); // LiveData
                                                                                                                 // transformado
    private final MutableLiveData<String> filtroActual = new MutableLiveData<>("PENDIENTE"); // Estado inicial
    private final MutableLiveData<String> ordenActual = new MutableLiveData<>("DEFAULT"); // Orden inicial (Pila/LIFO)

    public TurnosViewModel(@NonNull Application application) {
        super(application);
        turnoRepository = new TurnoRepository(application);
        this.prefs = new UserPreferences(application); // Inicializarla aquí

        // Configurar el switchMap
        turnosOriginal = Transformations.switchMap(usuarioEmail, email -> {
            if (email == null || email.isEmpty()) {
                return new MutableLiveData<>(Collections.emptyList());
            }
            return turnoRepository.getTurnosConServicioByUsuario(email);
        });

        // Configuración del MediatorLiveData
        turnosFiltradosYOrdenados.addSource(turnosOriginal, this::aplicarFiltrosYOrden);
        turnosFiltradosYOrdenados.addSource(filtroActual, turnos -> aplicarFiltrosYOrden(turnosOriginal.getValue()));
        turnosFiltradosYOrdenados.addSource(ordenActual, turnos -> aplicarFiltrosYOrden(turnosOriginal.getValue()));

        // Asegurar que el estado inicial se aplique
        filtroActual.setValue("TODOS");

        // QUITAR LA CARGA DEL EMAIL DEL CONSTRUCTOR
        // String emailActual = prefs.getRegisteredEmail();
        // usuarioEmail.setValue(emailActual);
    }

    public LiveData<List<TurnoConServicio>> getTurnosUsuario() {
        return turnosFiltradosYOrdenados;
    }

    public void actualizarTurno(Turno turno) {
        turnoRepository.update(turno);
    }

    public void cancelarTurno(Turno turno, android.content.Context context) {
        com.example.barberiashop_app.data.db.AppDatabase.databaseWriteExecutor.execute(() -> {
            try {
                // 1. Actualizar estado en BD (SÍNCRONO y SEGURO)
                // Esto verificará si existe el estado "Cancelado" y lo creará si es necesario
                turnoRepository.cancelarTurnoSync(turno);

                // 2. Crear Notificación en BD
                if (turno.getUsuarioEmail() != null) {
                    com.example.barberiashop_app.data.repository.NotificacionRepository notificacionRepository = new com.example.barberiashop_app.data.repository.NotificacionRepository(
                            context);
                    com.example.barberiashop_app.domain.entity.Notificacion notificacion = new com.example.barberiashop_app.domain.entity.Notificacion(
                            turno.getUsuarioEmail(),
                            "Turno Cancelado",
                            "Has cancelado tu turno del " + turno.getFecha() + " a las " + turno.getHorarioInicio()
                                    + ".",
                            System.currentTimeMillis());
                    notificacionRepository.insertSync(notificacion); // Usar insertSync también para seguridad

                    // 3. Mostrar Notificación de Sistema (Push) en el hilo principal
                    new android.os.Handler(android.os.Looper.getMainLooper()).post(() -> {
                        try {
                            com.example.barberiashop_app.utils.NotificationHelper.showNotification(context,
                                    "Turno Cancelado",
                                    "Has cancelado tu turno del " + turno.getFecha() + " a las "
                                            + turno.getHorarioInicio());
                            android.widget.Toast
                                    .makeText(context, "Turno cancelado correctamente",
                                            android.widget.Toast.LENGTH_SHORT)
                                    .show();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    });
                }
            } catch (Exception e) {
                e.printStackTrace();
                new android.os.Handler(android.os.Looper.getMainLooper()).post(() -> {
                    android.widget.Toast
                            .makeText(context, "Error al cancelar: " + e.getMessage(), android.widget.Toast.LENGTH_LONG)
                            .show();
                });
            }
        });
    }

    // Nuevos métodos para la vista (Fragment)

    public void setFiltro(String filtro) {
        filtroActual.setValue(filtro);
    }

    public void setOrden(String orden) {
        ordenActual.setValue(orden);
    }

    // Lógica de transformación
    private void aplicarFiltrosYOrden(List<TurnoConServicio> turnos) {
        // 6. Verificación de nulidad mejorada
        if (turnos == null) {
            turnosFiltradosYOrdenados.setValue(Collections.emptyList());
            return;
        }

        // Hacemos una copia mutable para filtrar y ordenar
        List<TurnoConServicio> listaProcesada = new java.util.ArrayList<>(turnos);

        // 1. Filtrado
        String filtro = filtroActual.getValue();
        if (filtro != null && !filtro.equals("TODOS")) {
            listaProcesada = listaProcesada.stream()
                    .filter(t -> t.estadoTurno != null
                            && t.estadoTurno.getNombre().toUpperCase().contains(filtro.toUpperCase()))
                    .collect(Collectors.toList());
        }

        // 2. Ordenamiento
        String orden = ordenActual.getValue();
        if (orden != null) {
            Comparator<TurnoConServicio> comparator = getComparator(orden);
            Collections.sort(listaProcesada, comparator);
        }

        turnosFiltradosYOrdenados.setValue(listaProcesada);
    }

    // Lógica del Comparador ajustada a TurnoConServicio
    private Comparator<TurnoConServicio> getComparator(String orden) {
        // 1. COMPARADOR BASE: Prioridad a PENDIENTE (estado_id = 1)
        Comparator<TurnoConServicio> priorityComparator = (t1, t2) -> {
            boolean t1Pending = t1.estadoTurno != null && "Pendiente".equalsIgnoreCase(t1.estadoTurno.getNombre());
            boolean t2Pending = t2.estadoTurno != null && "Pendiente".equalsIgnoreCase(t2.estadoTurno.getNombre());

            if (t1Pending && !t2Pending)
                return -1; // t1 primero
            if (!t1Pending && t2Pending)
                return 1; // t2 primero
            return 0; // Ambos son pendientes o ambos no lo son
        };

        Comparator<TurnoConServicio> secondaryComparator;

        switch (orden) {
            case "FECHA_DESC":
                secondaryComparator = Comparator.comparing((TurnoConServicio t) -> t.turno.getFecha(),
                        Collections.reverseOrder());
                break;
            case "HORARIO_ASC":
                secondaryComparator = Comparator.comparing((TurnoConServicio t) -> t.turno.getHorarioInicio());
                break;
            case "SERVICIO_AZ":
                // AJUSTE: Ordenar por el nombre del primer servicio asociado
                secondaryComparator = Comparator.comparing(
                        (TurnoConServicio t) -> (t.servicios != null && !t.servicios.isEmpty())
                                ? t.servicios.get(0).getNombre()
                                : "");
                break;
            case "ESTADO":
                secondaryComparator = Comparator.comparing((TurnoConServicio t) -> t.turno.getEstadoId());
                break;
            case "PRECIO_DESC":
                // AJUSTE: Sumar precios si hay múltiples servicios o usar el primero
                secondaryComparator = (t1, t2) -> {
                    double precio1 = (t1.servicios != null && !t1.servicios.isEmpty()) ? t1.servicios.get(0).getPrecio()
                            : 0.0;
                    double precio2 = (t2.servicios != null && !t2.servicios.isEmpty()) ? t2.servicios.get(0).getPrecio()
                            : 0.0;
                    return Double.compare(precio2, precio1); // Descendente
                };
                break;
            case "FECHA_ASC":
                secondaryComparator = Comparator.comparing((TurnoConServicio t) -> t.turno.getFecha());
                break;
            case "DEFAULT":
            default:
                // LIFO (Stack): ID descendente
                secondaryComparator = (t1, t2) -> Integer.compare(t2.turno.getId(), t1.turno.getId());
                break;
        }

        return priorityComparator.thenComparing(secondaryComparator);
    }

    /**
     * Revisa el email del usuario logueado actualmente en UserPreferences
     * y actualiza el LiveData 'usuarioEmail' si ha cambiado.
     * Esto dispara el 'switchMap' para recargar los turnos.
     */
    public void refreshUserData() {
        String emailActual = prefs.getRegisteredEmail();

        // Solo actualiza si el email es diferente al que ya tiene
        // para evitar recargas innecesarias.
        if (usuarioEmail.getValue() == null || !usuarioEmail.getValue().equals(emailActual)) {
            usuarioEmail.setValue(emailActual);
        }
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        // Eliminar los observadores para prevenir memory leaks
        turnosFiltradosYOrdenados.removeSource(turnosOriginal);
        turnosFiltradosYOrdenados.removeSource(filtroActual);
        turnosFiltradosYOrdenados.removeSource(ordenActual);
    }
}
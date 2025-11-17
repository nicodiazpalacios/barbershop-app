package com.example.barberiashop_app.ui.turnos;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
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

    private final MediatorLiveData<List<TurnoConServicio>> turnosFiltradosYOrdenados = new MediatorLiveData<>(); // LiveData transformado
    private final MutableLiveData<String> filtroActual = new MutableLiveData<>("PENDIENTE"); // Estado inicial
    private final MutableLiveData<String> ordenActual = new MutableLiveData<>("FECHA_ASC"); // Orden inicial

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
                    .filter(t -> t.turno.getEstadoNombre().toUpperCase().contains(filtro.toUpperCase()))
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
        switch (orden) {
            case "FECHA_DESC":
                return Comparator.comparing((TurnoConServicio t) -> t.turno.getFecha(), Collections.reverseOrder());
            case "HORARIO_ASC":
                return Comparator.comparing((TurnoConServicio t) -> t.turno.getHorarioInicio());
            case "SERVICIO_AZ":
                //  AJUSTE: Ordenar por el nombre del primer servicio asociado
                return Comparator.comparing((TurnoConServicio t) ->
                        (t.servicios != null && !t.servicios.isEmpty()) ? t.servicios.get(0).getNombre() : ""
                );
            case "ESTADO":
                return Comparator.comparing((TurnoConServicio t) -> t.turno.getEstadoId());
            case "PRECIO_DESC":
                // AJUSTE: Sumar precios si hay múltiples servicios o usar el primero
                return (t1, t2) -> {
                    double precio1 = (t1.servicios != null && !t1.servicios.isEmpty()) ? t1.servicios.get(0).getPrecio() : 0.0;
                    double precio2 = (t2.servicios != null && !t2.servicios.isEmpty()) ? t2.servicios.get(0).getPrecio() : 0.0;
                    return Double.compare(precio2, precio1); // Descendente
                };
            case "FECHA_ASC":
            default:
                return Comparator.comparing((TurnoConServicio t) -> t.turno.getFecha());
        }
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
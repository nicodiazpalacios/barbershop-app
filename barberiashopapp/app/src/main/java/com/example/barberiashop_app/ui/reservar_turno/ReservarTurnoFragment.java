package com.example.barberiashop_app.ui.reservar_turno;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.example.barberiashop_app.R;
import com.example.barberiashop_app.UserPreferences;
import com.example.barberiashop_app.databinding.FragmentReservarTurnoBinding;
import com.example.barberiashop_app.domain.entity.Servicio;
import com.example.barberiashop_app.domain.entity.Turno;
import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.DateValidatorPointForward;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.Calendar;
import java.util.Locale;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class ReservarTurnoFragment extends Fragment {
    private FragmentReservarTurnoBinding binding;
    private ReservarTurnoViewModel viewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        binding = FragmentReservarTurnoBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        int servicioId = ReservarTurnoFragmentArgs.fromBundle(getArguments()).getServicioId();

        viewModel = new ViewModelProvider(this).get(ReservarTurnoViewModel.class);
        viewModel.getServicioById(servicioId).observe(getViewLifecycleOwner(), servicio -> {
            if (servicio != null) {
                displayServicioDetails(servicio);
            }
        });

        binding.inputFecha.setOnClickListener(v -> showDatePicker());
        binding.layoutFecha.setEndIconOnClickListener(v -> showDatePicker());

        binding.inputHorario.setOnClickListener(v -> showTimeOptions());
        binding.layoutHorario.setEndIconOnClickListener(v -> showTimeOptions());

        binding.btnConfirmarReserva.setOnClickListener(v -> confirmReservation());
//        binding.btnBack.setOnClickListener(v -> getParentFragmentManager().popBackStack());
        binding.btnBack
                .setOnClickListener(v -> Navigation
                        .findNavController(v)
                        .popBackStack());

        return root;
    }

    /**
     * Muestra los detalles del servicio en la vista
     */
    private void displayServicioDetails(Servicio servicio) {
        binding.tvNombreServicioReserva.setText(servicio.getNombre());

        String precioText = String.format(Locale.getDefault(), "$%.2f", servicio.getPrecio());
        binding.tvPrecioServicioReserva.setText(precioText);

        String duracionText = String.format(Locale.getDefault(), "%d min", servicio.getDuracion());
        binding.tvDuracionServicioReserva.setText(duracionText);

        binding.tvDescripcionServicioReserva.setText(servicio.getDescripcion());
    }



    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    //TODO: Pasar a utils functions
    /**
     * Mostrar selector de fecha
     */
    private void showDatePicker() {
        Calendar utcCalendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        utcCalendar.set(Calendar.HOUR_OF_DAY, 0);
        utcCalendar.set(Calendar.MINUTE, 0);
        utcCalendar.set(Calendar.SECOND, 0);
        utcCalendar.set(Calendar.MILLISECOND, 0);

        long startTodayInUtc = utcCalendar.getTimeInMillis();

        CalendarConstraints.Builder constraintsBuilder = new CalendarConstraints.Builder();
        constraintsBuilder.setValidator(DateValidatorPointForward.from(startTodayInUtc));
        constraintsBuilder.setStart(startTodayInUtc);

        MaterialDatePicker<Long> datePicker = MaterialDatePicker.Builder.datePicker()
                .setTitleText("Selecciona una fecha")
                .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                .setCalendarConstraints(constraintsBuilder.build())
                .build();

        datePicker.addOnPositiveButtonClickListener(selection -> {
            String selectedDate = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date(selection));
            binding.inputFecha.setText(selectedDate);
            binding.inputHorario.setEnabled(true);
            binding.layoutHorario.setAlpha(1f);
        });

        datePicker.show(getParentFragmentManager(), "DATE_PICKER");
    }



    /**
     * Mostrar lista de horarios hardcodeados
    */
    private void showTimeOptions() {
        if (binding.inputFecha.getText().toString().equals("Seleccionar fecha")) {
            Toast.makeText(getContext(), "Primero selecciona una fecha", Toast.LENGTH_SHORT).show();
            return;
        }

        // Rango de horarios (puedes ajustarlo)
        int horaInicio = 8;  // 8 AM
        int horaFin = 20;    // 8 PM

        // Generar lista de horarios cada 1 hora
        List<String> horarios = generateHourlySlots(horaInicio, horaFin);

        new MaterialAlertDialogBuilder(requireContext())
                .setTitle("Selecciona un horario")
                .setAdapter(new ArrayAdapter<>(requireContext(), android.R.layout.simple_list_item_1, horarios),
                        (dialog, which) -> binding.inputHorario.setText(horarios.get(which)))
                .show();
    }

    /**
     * Genera una lista de horarios con intervalo de 1 hora (ej: 08:00 AM, 09:00 AM, etc.)
     */
    private List<String> generateHourlySlots(int startHour, int endHour) {
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a", Locale.getDefault());
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        List<String> slots = new java.util.ArrayList<>();
        for (int hour = startHour; hour <= endHour; hour++) {
            calendar.set(Calendar.HOUR_OF_DAY, hour);
            slots.add(sdf.format(calendar.getTime()));
        }
        return slots;
    }

    /**
     * Confirmar reserva y navegar al fragment de "Mis Turnos"
     */
    private void confirmReservation() {
        String fecha = binding.inputFecha.getText().toString();
        String horario = binding.inputHorario.getText().toString();

        if (fecha.equals("Seleccionar fecha") || horario.equals("Seleccionar horario")) {
            Toast.makeText(getContext(), "Selecciona fecha y horario antes de confirmar", Toast.LENGTH_SHORT).show();
            return;
        }

        // Obtener email del usuario logueado
        UserPreferences userPrefs = new UserPreferences(requireContext());
        String usuarioEmail = userPrefs.getRegisteredEmail();

        // Crear turno (por ahora horarioFin puede ser el mismo o calculado según duración)
        Turno nuevoTurno = new Turno(fecha, horario, horario, usuarioEmail);

        // Insertar en la BD
        viewModel.insertTurno(nuevoTurno);

        Toast.makeText(getContext(), "✅ Reserva confirmada: " + fecha + " a las " + horario, Toast.LENGTH_LONG).show();
        NavController navController = Navigation.findNavController(requireView());
        navController.navigate(R.id.action_reservarTurnoFragment_to_navigation_turnos);

    }
}
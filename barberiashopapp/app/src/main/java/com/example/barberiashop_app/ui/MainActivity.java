package com.example.barberiashop_app.ui;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.barberiashop_app.R;
import com.example.barberiashop_app.ui.adapter.TurnoListAdapter;
import com.example.barberiashop_app.ui.viewmodel.TurnoViewModel;
import com.example.barberiashop_app.domain.entity.Turno;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity {
    private TurnoViewModel turnoViewModel;
    public static final int NEW_TURNO_REQUEST_CODE = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        final TurnoListAdapter adapter = new TurnoListAdapter(new TurnoListAdapter.TurnoDiff());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        turnoViewModel = new ViewModelProvider(this).get(TurnoViewModel.class);

        turnoViewModel.getAllTurnos().observe(this, adapter::submitList);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, NewTurnoActivity.class);
            startActivityForResult(intent, NEW_TURNO_REQUEST_CODE);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == NEW_TURNO_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            String fecha = data.getStringExtra(NewTurnoActivity.EXTRA_FECHA);
            String inicio = data.getStringExtra(NewTurnoActivity.EXTRA_INICIO);
            String fin = data.getStringExtra(NewTurnoActivity.EXTRA_FIN);

            Turno turno = new Turno(fecha, inicio, fin);
            turnoViewModel.insert(turno);
        }
    }
}
package com.example.barberiashop_app.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.barberiashop_app.R;

public class NewTurnoActivity extends AppCompatActivity {

    public static final String EXTRA_FECHA = "com.example.barberiashop_app.FECHA";
    public static final String EXTRA_INICIO = "com.example.barberiashop_app.INICIO";
    public static final String EXTRA_FIN = "com.example.barberiashop_app.FIN";

    private EditText editFecha;
    private EditText editInicio;
    private EditText editFin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_new_turno);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        editFecha = findViewById(R.id.edit_fecha);
        editInicio = findViewById(R.id.edit_inicio);
        editFin = findViewById(R.id.edit_fin);

        Button btnGuardar = findViewById(R.id.btn_guardar);
        btnGuardar.setOnClickListener(view -> {
            Intent replyIntent = new Intent();

            if (TextUtils.isEmpty(editFecha.getText()) ||
                    TextUtils.isEmpty(editInicio.getText()) ||
                    TextUtils.isEmpty(editFin.getText())) {
                setResult(RESULT_CANCELED, replyIntent);
            } else {
                String fecha = editFecha.getText().toString();
                String inicio = editInicio.getText().toString();
                String fin = editFin.getText().toString();

                replyIntent.putExtra(EXTRA_FECHA, fecha);
                replyIntent.putExtra(EXTRA_INICIO, inicio);
                replyIntent.putExtra(EXTRA_FIN, fin);
                setResult(RESULT_OK, replyIntent);
            }
            finish();
        });
    }
}
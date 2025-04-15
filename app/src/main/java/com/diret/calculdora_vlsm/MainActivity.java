package com.diret.calculdora_vlsm;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    private LinearLayout subnetRowsContainer;
    private EditText etSubredes;
    private Button btnCambiar;
    private int currentSubnetCount = 5; // Default value

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // Initialize UI components
        subnetRowsContainer = findViewById(R.id.subnetRowsContainer);
        etSubredes = findViewById(R.id.etSubredes);
        btnCambiar = findViewById(R.id.btnCambiar);

        // Set initial subnet rows
        createSubnetRows(currentSubnetCount);

        // Set click listener for "Cambiar" button
        btnCambiar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateSubnetRows();
            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
    /**
     * Updates the subnet rows based on the input value
     */
    private void updateSubnetRows() {
        String subredesText = etSubredes.getText().toString();
        if (!TextUtils.isEmpty(subredesText)) {
            try {
                int newCount = Integer.parseInt(subredesText);
                if (newCount > 0) {
                    currentSubnetCount = newCount;
                    createSubnetRows(currentSubnetCount);
                }
            } catch (NumberFormatException e) {
                etSubredes.setError("Ingrese un número válido");
            }
        }
    }

    /**
     * Creates the specified number of subnet rows
     * @param count Number of rows to create
     */
    private void createSubnetRows(int count) {
        // Clear existing rows
        subnetRowsContainer.removeAllViews();

        // Get layout inflater
        LayoutInflater inflater = LayoutInflater.from(this);

        // Create new rows
        for (int i = 0; i < count; i++) {
            // Get the letter for this row (A, B, C, etc.)
            char letter = (char) ('A' + i);

            // Inflate the row layout
            View rowView = inflater.inflate(R.layout.item_subnet_row, subnetRowsContainer, false);

            // Find views in the row
            EditText etNombre = rowView.findViewById(R.id.etNombre);
            EditText etTamano = rowView.findViewById(R.id.etTamano);

            // Set the initial name based on alphabetical order
            etNombre.setHint(String.valueOf(letter));

            // Add the row to the container
            subnetRowsContainer.addView(rowView);
        }
    }
}
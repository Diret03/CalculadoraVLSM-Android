package com.diret.calculdora_vlsm;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity"; // tag to Log
    private LinearLayout subnetRowsContainer;
    private EditText etSubredes;
    private Button btnCambiar;
    private int currentSubnetCount = 5; // Default value
    private EditText etRedPadre;
    private EditText etPrefijo;
    private Button btnSubir;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // Initialize UI components
        subnetRowsContainer = findViewById(R.id.subnetRowsContainer);
        etSubredes = findViewById(R.id.etSubredes);
        btnCambiar = findViewById(R.id.btnCambiar);
        btnSubir = findViewById(R.id.btnSubir);
        etRedPadre = findViewById(R.id.etRedPadre);
        etPrefijo = findViewById(R.id.etNetworkPrefix);

        // Set initial subnet rows
        createSubnetRows(currentSubnetCount);

        // Set click listener for "Cambiar" button
        btnCambiar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateSubnetRows();
            }
        });

        // Set click listener for "Cambiar" button
        btnSubir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleUserInput(); // Handle the input validation and navigation
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
//            etNombre.setHint(String.valueOf(letter));
            etNombre.setText(String.valueOf(letter));

            // Add the row to the container
            subnetRowsContainer.addView(rowView);
        }
    }

    // Handle the user's input data
    private void handleUserInput() {
        String majorNetwork = etRedPadre.getText().toString();
        String prefix = etPrefijo.getText().toString();
        String subredesText = etSubredes.getText().toString();

        // Log the input values
        Log.d(TAG, "Major Network: " + majorNetwork);
        Log.d(TAG, "Prefix: " + prefix);
        Log.d(TAG, "Number of Subnets: " + subredesText);

        // Validate network format
        if (!SubnetUtils.isValidNetworkFormat(majorNetwork)) {
            etRedPadre.setError("Formato inválido. Use formato decimal *.*.*.*");
            return;
        }

        // Validate prefix
        if (TextUtils.isEmpty(prefix) || Integer.parseInt(prefix) < 1 || Integer.parseInt(prefix) > 32) {
            etPrefijo.setError("El prefijo debe ser un número entre 1 y 32");
            return;
        }

        // Validate subnet count
        if (TextUtils.isEmpty(subredesText)) {
            etSubredes.setError("Por favor ingrese el número de subredes");
            return;
        }

        int numSubnets = Integer.parseInt(subredesText);
        if (numSubnets <= 0) {
            etSubredes.setError("El número de subredes debe ser mayor que 0");
            return;
        }

        // Collect host data (names and sizes)
        List<Host> hosts = new ArrayList<>();
        boolean isValid = true;
        for (int i = 0; i < subnetRowsContainer.getChildCount(); i++) {
            View rowView = subnetRowsContainer.getChildAt(i);
            EditText etHostName = rowView.findViewById(R.id.etNombre);
            EditText etHostSize = rowView.findViewById(R.id.etTamano);

            String hostName = etHostName.getText().toString();
            String hostSize = etHostSize.getText().toString();

            // Log host data for each row
            Log.d(TAG, "Subnet " + (char) ('A' + i) + " - Name: " + hostName + ", Size: " + hostSize);

            if (hostSize.isEmpty()) {
                isValid = false;
                etHostSize.setError("Tamaño del host no puede estar vacío");
            } else if (Integer.parseInt(hostSize) <= 0) {
                isValid = false;
                etHostSize.setError("Tamaño del host debe ser mayor que 0");
            } else {
                hosts.add(new Host(hostName, Integer.parseInt(hostSize)));
            }
        }

        // Proceed only if all data is valid
        if (!isValid) return;

        // Sort hosts by size
        Collections.sort(hosts, (h1, h2) -> Integer.compare(h2.getNumHosts(), h1.getNumHosts()));

        // Store the data for the next activity
        Intent intent = new Intent(MainActivity.this, Output.class);
        intent.putExtra("majorNetwork", majorNetwork);
        intent.putExtra("prefix", prefix);
        intent.putParcelableArrayListExtra("hosts", (ArrayList<? extends Parcelable>) hosts);

        startActivity(intent); // Navigate to SubnettingActivity
    }
}
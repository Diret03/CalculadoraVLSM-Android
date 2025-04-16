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
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

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

    private boolean isValidNetworkFormat(String network, EditText et) {
        // Regex to check the general format of an IPv4 address
        String networkFormatRegex = "^((\\d{1,3})\\.){3}(\\d{1,3})$";
        String[] octets = network.split("\\.");
        boolean isValid = false;

        if (network.matches(networkFormatRegex)) {
            isValid = true;

            // Check if each octet is a valid number between 0 and 255
            for (String octet : octets) {
                int numericValue = Integer.parseInt(octet);
                if (numericValue < 0 || numericValue > 255) {
                    isValid = false;
                    et.setError("Cada octeto debe ser un un número entre 0 y 255");
                    return false;
                }
            }
        } else {
            isValid = false;
            et.setError("Formato inválido de red. Por favor use el formato decimal IPv4.");
        }

        return isValid;
    }

    // Handle the user's input data
    private void handleUserInput() {
        String majorNetwork = etRedPadre.getText().toString();
        String prefixStr = etPrefijo.getText().toString();
        String subredesText = etSubredes.getText().toString();

        // Log the input values
        Log.d(TAG, "Major Network: " + majorNetwork);
        Log.d(TAG, "Prefix: " + prefixStr);
        Log.d(TAG, "Number of Subnets: " + subredesText);


        if (!isValidNetworkFormat(majorNetwork, etRedPadre)) {
            return;
        }

        if (TextUtils.isEmpty(prefixStr)) {
            etPrefijo.setError("Por favor ingrese el prefijo de red");
            return;
        }

        int prefix;
        try {
            prefix = Integer.parseInt(prefixStr);
            if (prefix < 1 || prefix > 30) {
                etPrefijo.setError("El prefijo debe estar entre 1 y 30");
                return;
            }
        } catch (NumberFormatException e) {
            etPrefijo.setError("Ingrese un número válido para el prefijo");
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
        int totalRequiredHosts = 0; // Initialize total required hosts
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
            } else {
                try {
                    int hostCount = Integer.parseInt(hostSize);
                    if (hostCount <= 0) {
                        isValid = false;
                        etHostSize.setError("Tamaño del host debe ser mayor que 0");
                    } else {
                        hosts.add(new Host(hostName, hostCount));
                        totalRequiredHosts += hostCount; // Accumulate required hosts
                    }
                } catch (NumberFormatException e) {
                    isValid = false;
                    etHostSize.setError("Ingrese un número válido para el tamaño del host");
                }
            }
        }

        // Proceed only if all data is valid so far
        if (!isValid) return;

        // Calculate available host bits and maximum usable hosts
        int availableHostBits = 32 - prefix;
        if (availableHostBits < 1) {
            Toast.makeText(this, "El prefijo es demasiado pequeño para tener hosts.", Toast.LENGTH_SHORT).show();
            return;
        }
        long maxUsableHosts = (long) Math.pow(2, availableHostBits) - 2;

        // Check if the total number of required hosts exceeds the available hosts
        if (totalRequiredHosts > maxUsableHosts) {
            Toast.makeText(this, "El número total de hosts requeridos excede la capacidad de la red padre.", Toast.LENGTH_LONG).show();
            return;
        }

        // Sort hosts by size (this is done after the check, as the check is on the total)
        Collections.sort(hosts, (h1, h2) -> Integer.compare(h2.getNumHosts(), h1.getNumHosts()));

        // Store the data for the next activity
        Intent intent = new Intent(MainActivity.this, Output.class);
        intent.putExtra("majorNetwork", majorNetwork);
        intent.putExtra("prefix", prefixStr);
        intent.putParcelableArrayListExtra("hosts", (ArrayList<? extends Parcelable>) hosts);


        Log.d(TAG,"ALL SUBNETS BEFORE MOVING TO THE NEXT ACTIVITY");
        for (int i=0;i<hosts.size();i++) {

            Log.d(TAG, "Subnet " + (char) ('A' + i) + " - Name: " + hosts.get(i).getName() + ", Size: " + hosts.get(i).getNumHosts() );
        }

        startActivity(intent); // Navigate to SubnettingActivity
    }
}
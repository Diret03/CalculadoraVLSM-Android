package com.diret.calculdora_vlsm;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Output extends AppCompatActivity {


    private static final String TAG = "Output";
    private TextView tvParentNetwork;
    private TextView tvNetworkMask;
    private TableLayout tlSubnetResults;
    private Button btnNewCalculation;

    // Store subnet masks for easy access
    private static final String[] subnetMasks = {
            "0.0.0.0",       // 0 (Not used, but avoids off-by-one errors)
            "128.0.0.0",    // 1
            "192.0.0.0",    // 2
            "224.0.0.0",    // 3
            "240.0.0.0",    // 4
            "248.0.0.0",    // 5
            "252.0.0.0",    // 6
            "254.0.0.0",    // 7
            "255.0.0.0",    // 8
            "255.128.0.0",  // 9
            "255.192.0.0",  // 10
            "255.224.0.0",  // 11
            "255.240.0.0",  // 12
            "255.248.0.0",  // 13
            "255.252.0.0",  // 14
            "255.254.0.0",  // 15
            "255.255.0.0",  // 16
            "255.255.128.0", // 17
            "255.255.192.0", // 18
            "255.255.224.0", // 19
            "255.255.240.0", // 20
            "255.255.248.0", // 21
            "255.255.252.0", // 22
            "255.255.254.0", // 23
            "255.255.255.0", // 24
            "255.255.255.128",// 25
            "255.255.255.192",// 26
            "255.255.255.224",// 27
            "255.255.255.240",// 28
            "255.255.255.248",// 29
            "255.255.255.252" // 30
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_output);

        tvParentNetwork = findViewById(R.id.tvParentNetwork);
        tvNetworkMask = findViewById(R.id.tvNetworkMask);
        tlSubnetResults = findViewById(R.id.tlSubnetResults);
        btnNewCalculation = findViewById(R.id.btnNewCalculation);

        // Retrieve data from Intent
        Intent intent = getIntent();
        String majorNetwork = intent.getStringExtra("majorNetwork");
        String prefixStr = intent.getStringExtra("prefix");
        ArrayList<Host> hosts = intent.getParcelableArrayListExtra("hosts");

        if (majorNetwork != null && prefixStr != null && hosts != null) {
            int prefix = Integer.parseInt(prefixStr);
            tvParentNetwork.setText("Red padre: " + majorNetwork + " / " + prefix);
            tvNetworkMask.setText("Máscara de red: " + subnetMasks[prefix]); // Display the mask

            List<Subnet> subnets = Calculator.finalSubnets(majorNetwork, prefix, hosts);

            if (subnets != null) {
                populateTable(subnets);
            } else {
                // Handle the error: VLSM calculation failed
                Log.e(TAG, "VLSM Calculation Failed");
                // You might want to display an error message to the user here
            }
        } else {
            // Handle the error: Missing data from Intent
            Log.e(TAG, "Missing data from Intent");
            // You might want to display an error message to the user here
        }

        btnNewCalculation.setOnClickListener(v -> {
            finish(); // Go back to MainActivity
        });
    }



    // Helper function to populate the TableLayout with subnet data
    private void populateTable(List<Subnet> subnets) {
        TableLayout tableLayout = findViewById(R.id.tlSubnetResults);
        tableLayout.removeAllViews(); // Clear existing rows

        // Determine orientation
        int orientation = getResources().getConfiguration().orientation;
        boolean isPortrait = orientation == Configuration.ORIENTATION_PORTRAIT;

        // Create table header
        TableRow headerRow = new TableRow(this);
        headerRow.setBackgroundColor(0xFFE0E0E0); // Light gray

        // Create header TextViews
        TextView tvNameHeader = createTextView("Nombre");
        TextView tvHostsHeader = createTextView("Hosts");
        TextView tvAllocatedHeader = createTextView("Tamaño");
        TextView tvAddressHeader = createTextView("Dirección");
        TextView tvPrefixHeader = createTextView("Prefijo");

        headerRow.addView(tvNameHeader);
        headerRow.addView(tvHostsHeader);
        headerRow.addView(tvAllocatedHeader);
        headerRow.addView(tvAddressHeader);
        headerRow.addView(tvPrefixHeader);

        // Add extra headers in landscape mode
        if (!isPortrait) {
            TextView tvBinAddressHeader = createTextView("Dirección binario");
            TextView tvMaskHeader = createTextView("Máscara");
            TextView tvMinHostHeader = createTextView("Primer host");
            TextView tvMaxHostHeader = createTextView("Último host");
            TextView tvBroadcastHeader = createTextView("Broadcast");

            headerRow.addView(tvBinAddressHeader);
            headerRow.addView(tvMaskHeader);
            headerRow.addView(tvMinHostHeader);
            headerRow.addView(tvMaxHostHeader);
            headerRow.addView(tvBroadcastHeader);
        }

        tableLayout.addView(headerRow);

        // Populate table rows with subnet data
        for (Subnet subnet : subnets) {
            TableRow row = new TableRow(this);

            TextView tvName = createTextView(subnet.getName());
            TextView tvHosts = createTextView(String.valueOf(subnet.getNumRequiredHosts()));
            TextView tvAllocated = createTextView(String.valueOf(subnet.getAllocatedHosts()));
            TextView tvAddress = createTextView(subnet.getAddress());
            TextView tvPrefix = createTextView("/" + subnet.getPrefix());

            row.addView(tvName);
            row.addView(tvHosts);
            row.addView(tvAllocated);
            row.addView(tvAddress);
            row.addView(tvPrefix);

            // Add extra data in landscape mode
            if (!isPortrait) {
                TextView tvBinAddress = createTextView(subnet.getBinAddress());
                TextView tvMask = createTextView(subnetMasks[subnet.getPrefix()]);
                TextView tvMinHost = createTextView(subnet.getMinHost());
                TextView tvMaxHost = createTextView(subnet.getMaxHost());
                TextView tvBroadcast = createTextView(subnet.getBroadcast());

                row.addView(tvBinAddress);
                row.addView(tvMask);
                row.addView(tvMinHost);
                row.addView(tvMaxHost);
                row.addView(tvBroadcast);
            }

            tableLayout.addView(row);
        }
    }

    // Helper function to create TextView for table cells
    private TextView createTextView(String text) {
        TextView textView = new TextView(this);
        textView.setText(text);
        textView.setPadding(8, 8, 8, 8);
        return textView;
    }
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        EdgeToEdge.enable(this);
//        setContentView(R.layout.activity_output);
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
//            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
//            return insets;
//        });
//    }
}
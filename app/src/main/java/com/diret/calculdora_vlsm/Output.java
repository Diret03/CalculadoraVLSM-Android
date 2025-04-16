package com.diret.calculdora_vlsm;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
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
        headerRow.setBackgroundColor(ContextCompat.getColor(this, R.color.table_header_background));
        headerRow.setPadding(dpToPx(12), dpToPx(12), dpToPx(12), dpToPx(12));

        // Create header TextViews
        TextView tvNameHeader = createTextView("Nombre", true);
        TextView tvHostsHeader = createTextView("Hosts", true);
        TextView tvAllocatedHeader = createTextView("Tamaño", true);
        TextView tvAddressHeader = createTextView("Dirección", true);
        TextView tvPrefixHeader = createTextView("Prefijo", true);

        headerRow.addView(tvNameHeader);
        headerRow.addView(tvHostsHeader);
        headerRow.addView(tvAllocatedHeader);
        headerRow.addView(tvAddressHeader);
        headerRow.addView(tvPrefixHeader);

        // Add extra headers in landscape mode
        if (!isPortrait) {
            TextView tvBinAddressHeader = createTextView("Dirección binario", true);
            TextView tvMaskHeader = createTextView("Máscara", true);
            TextView tvMinHostHeader = createTextView("Primer host", true);
            TextView tvMaxHostHeader = createTextView("Último host", true);
            TextView tvBroadcastHeader = createTextView("Broadcast", true);

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
            row.setPadding(dpToPx(8), dpToPx(8), dpToPx(8), dpToPx(8));

            String name = "";
            if (subnet.getName() != null) {
                name = subnet.getName();
            }
            TextView tvName = createTextView(name, false);
            TextView tvHosts = createTextView(String.valueOf(subnet.getNumRequiredHosts()), false);
            TextView tvAllocated = createTextView(String.valueOf(subnet.getAllocatedHosts()), false);
            TextView tvAddress = createTextView(subnet.getAddress(), false);
            TextView tvPrefix = createTextView("/" + subnet.getPrefix(), false);

            row.addView(tvName);
            row.addView(tvHosts);
            row.addView(tvAllocated);
            row.addView(tvAddress);
            row.addView(tvPrefix);

            // Add extra data in landscape mode
            if (!isPortrait) {
                TextView tvBinAddress = createTextView(subnet.getBinAddress(), false);
                TextView tvMask = createTextView(subnetMasks[subnet.getPrefix()], false);
                TextView tvMinHost = createTextView(subnet.getMinHost(), false);
                TextView tvMaxHost = createTextView(subnet.getMaxHost(), false);
                TextView tvBroadcast = createTextView(subnet.getBroadcast(), false);

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
    private TextView createTextView(String text, boolean isHeader) {
        TextView textView = new TextView(this);
        textView.setText(text);
        textView.setTextColor(ContextCompat.getColor(this, R.color.table_text_color));
        textView.setPadding(dpToPx(8), dpToPx(8), dpToPx(8), dpToPx(8));
        if (isHeader) {
            textView.setTypeface(null, Typeface.BOLD);
        }
        return textView;
    }

    // Helper function to convert dp to pixels
    private int dpToPx(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getResources().getDisplayMetrics());
    }
}
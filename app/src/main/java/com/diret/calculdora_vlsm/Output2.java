//package com.diret.calculdora_vlsm;
//
//import android.content.Intent;
//import android.content.res.Configuration;
//import android.graphics.Color;
//import android.graphics.Typeface;
//import android.os.Bundle;
//import android.util.Log;
//import android.util.TypedValue;
//import android.widget.Button;
//import android.widget.LinearLayout;
//import android.widget.TableLayout;
//import android.widget.TableRow;
//import android.widget.TextView;
//
//import androidx.appcompat.app.AppCompatActivity;
//
//import java.util.ArrayList;
//import java.util.Collections;
//import java.util.List;
//
//public class Output2 extends AppCompatActivity {
//
//
//    private static final String TAG = "Output2";
//    private TextView tvParentNetwork;
//    private TextView tvNetworkMask;
//    private TableLayout tlSubnetResults;
//    private Button btnNewCalculation;
//
//    // Store subnet masks for easy access
//    private static final String[] subnetMasks = {
//            "0.0.0.0",       // 0 (Not used, but avoids off-by-one errors)
//            "128.0.0.0",    // 1
//            "192.0.0.0",    // 2
//            "224.0.0.0",    // 3
//            "240.0.0.0",    // 4
//            "248.0.0.0",    // 5
//            "252.0.0.0",    // 6
//            "254.0.0.0",    // 7
//            "255.0.0.0",    // 8
//            "255.128.0.0",  // 9
//            "255.192.0.0",  // 10
//            "255.224.0.0",  // 11
//            "255.240.0.0",  // 12
//            "255.248.0.0",  // 13
//            "255.252.0.0",  // 14
//            "255.254.0.0",  // 15
//            "255.255.0.0",  // 16
//            "255.255.128.0", // 17
//            "255.255.192.0", // 18
//            "255.255.224.0", // 19
//            "255.255.240.0", // 20
//            "255.255.248.0", // 21
//            "255.255.252.0", // 22
//            "255.255.254.0", // 23
//            "255.255.255.0", // 24
//            "255.255.255.128",// 25
//            "255.255.255.192",// 26
//            "255.255.255.224",// 27
//            "255.255.255.240",// 28
//            "255.255.255.248",// 29
//            "255.255.255.252" // 30
//    };
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_output);
//
//        tvParentNetwork = findViewById(R.id.tvParentNetwork);
//        tvNetworkMask = findViewById(R.id.tvNetworkMask);
//        tlSubnetResults = findViewById(R.id.tlSubnetResults);
//        btnNewCalculation = findViewById(R.id.btnNewCalculation);
//
//        // Retrieve data from Intent
//        Intent intent = getIntent();
//        String majorNetwork = intent.getStringExtra("majorNetwork");
//        String prefixStr = intent.getStringExtra("prefix");
//        ArrayList<Host> hosts = intent.getParcelableArrayListExtra("hosts");
//
//        if (majorNetwork != null && prefixStr != null && hosts != null) {
//            int prefix = Integer.parseInt(prefixStr);
//            tvParentNetwork.setText("Red padre: " + majorNetwork + " / " + prefix);
//            tvNetworkMask.setText("Máscara de red: " + subnetMasks[prefix]); // Display the mask
//
//            List<Subnet> subnets = Calculator.finalSubnets(majorNetwork, prefix, hosts);
//
//            if (subnets != null) {
//                populateTable(subnets);
//            } else {
//                // Handle the error: VLSM calculation failed
//                Log.e(TAG, "VLSM Calculation Failed");
//                // You might want to display an error message to the user here
//            }
//        } else {
//            // Handle the error: Missing data from Intent
//            Log.e(TAG, "Missing data from Intent");
//            // You might want to display an error message to the user here
//        }
//
//        btnNewCalculation.setOnClickListener(v -> {
//            finish(); // Go back to MainActivity
//        });
//    }
//
//
//    private TextView createTextView(String text) {
//        TextView textView = new TextView(this);
//        textView.setText(text);
//
//        // Apply padding (in pixels)
//        int paddingInPx = dpToPx(8);
//        textView.setPadding(paddingInPx, paddingInPx, paddingInPx, paddingInPx);
//
//        // Set text properties
//        textView.setTextColor(getResources().getColor(R.color.text_dark, null)); // Create this color
//        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
//
//        // Set layout parameters
//        TableRow.LayoutParams params = new TableRow.LayoutParams(
//                TableRow.LayoutParams.WRAP_CONTENT,
//                TableRow.LayoutParams.WRAP_CONTENT);
//        textView.setLayoutParams(params);
//
//        return textView;
//    }
//
//    // Helper method to convert dp to pixels
//    private int dpToPx(int dp) {
//        return (int) (dp * getResources().getDisplayMetrics().density);
//    }
//
//    private void populateTable(List<Subnet> subnets) {
//        TableLayout tableLayout = findViewById(R.id.tlSubnetResults);
//        tableLayout.removeAllViews(); // Clear existing rows
//
//        // Add styling to the table layout itself
//        tableLayout.setBackgroundColor(Color.WHITE);
//        tableLayout.setPadding(dpToPx(4), dpToPx(4), dpToPx(4), dpToPx(4));
//
//        // Set dividers between rows
//        tableLayout.setShowDividers(LinearLayout.SHOW_DIVIDER_MIDDLE);
//        tableLayout.setDividerDrawable(getResources().getDrawable(R.drawable.table_divider, null)); // Create this drawable
//
//        // Determine orientation
//        int orientation = getResources().getConfiguration().orientation;
//        boolean isPortrait = orientation == Configuration.ORIENTATION_PORTRAIT;
//
//        // Create table header
//        TableRow headerRow = new TableRow(this);
//        headerRow.setBackgroundColor(0xFFE8E8E8); // Light gray from your new design
//
//        // Add top and bottom padding to the header row
//        headerRow.setPadding(0, dpToPx(12), 0, dpToPx(12));
//
//        // Create header TextViews
//        TextView tvNameHeader = createTextView("Nombre");
//        tvNameHeader.setTypeface(null, Typeface.BOLD);
//        TextView tvHostsHeader = createTextView("Hosts");
//        tvHostsHeader.setTypeface(null, Typeface.BOLD);
//        TextView tvAllocatedHeader = createTextView("Tamaño");
//        tvAllocatedHeader.setTypeface(null, Typeface.BOLD);
//        TextView tvAddressHeader = createTextView("Dirección");
//        tvAddressHeader.setTypeface(null, Typeface.BOLD);
//        TextView tvPrefixHeader = createTextView("Prefijo");
//        tvPrefixHeader.setTypeface(null, Typeface.BOLD);
//
//        // Add header views
//        headerRow.addView(tvNameHeader);
//        headerRow.addView(tvHostsHeader);
//        headerRow.addView(tvAllocatedHeader);
//        headerRow.addView(tvAddressHeader);
//        headerRow.addView(tvPrefixHeader);
//
//        // Add extra headers in landscape mode
//        if (!isPortrait) {
//            // Same as your existing code but add bold styling
//            TextView tvBinAddressHeader = createTextView("Dirección binario");
//            tvBinAddressHeader.setTypeface(null, Typeface.BOLD);
//            // Repeat for other headers...
//
//            headerRow.addView(tvBinAddressHeader);
//            // Add other headers...
//        }
//
//        tableLayout.addView(headerRow);
//
//        // Alternate row backgrounds for better readability
//        int rowColor1 = Color.WHITE;
//        int rowColor2 = 0xFFF9F9F9; // Very light gray for alternating rows
//        boolean alternate = false;
//
//        // Populate table rows with subnet data
//        for (Subnet subnet : subnets) {
//            TableRow row = new TableRow(this);
//
//            // Add alternating row background
//            row.setBackgroundColor(alternate ? rowColor2 : rowColor1);
//            alternate = !alternate;
//
//            // Add padding to rows
//            row.setPadding(0, dpToPx(8), 0, dpToPx(8));
//
//            TextView tvName = createTextView(subnet.getName());
//            TextView tvHosts = createTextView(String.valueOf(subnet.getNumRequiredHosts()));
//            TextView tvAllocated = createTextView(String.valueOf(subnet.getAllocatedHosts()));
//            TextView tvAddress = createTextView(subnet.getAddress());
//            TextView tvPrefix = createTextView("/" + subnet.getPrefix());
//
//            row.addView(tvName);
//            row.addView(tvHosts);
//            row.addView(tvAllocated);
//            row.addView(tvAddress);
//            row.addView(tvPrefix);
//
//            // Add extra data in landscape mode (same as your existing code)
//            if (!isPortrait) {
//                // Your existing code...
//            }
//
//            tableLayout.addView(row);
//        }
//    }
//}
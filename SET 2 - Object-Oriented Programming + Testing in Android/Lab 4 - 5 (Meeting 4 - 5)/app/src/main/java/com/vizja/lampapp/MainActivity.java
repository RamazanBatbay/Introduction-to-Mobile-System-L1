package com.vizja.lampapp;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private Lamp lamp;
    
    private TextView tvLampStatus;
    private TextView tvIntensity;
    private TextView tvBulbStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lamp = new Lamp();

        tvLampStatus = findViewById(R.id.tv_lamp_status);
        tvIntensity = findViewById(R.id.tv_intensity);
        tvBulbStatus = findViewById(R.id.tv_bulb_status);

        Button btnTurnOn = findViewById(R.id.btn_turn_on);
        Button btnTurnOff = findViewById(R.id.btn_turn_off);
        Button btnBrighten = findViewById(R.id.btn_brighten);
        Button btnDim = findViewById(R.id.btn_dim);
        Button btnReplace = findViewById(R.id.btn_replace);

        btnTurnOn.setOnClickListener(v -> {
            lamp.turnOn();
            updateUI();
        });

        btnTurnOff.setOnClickListener(v -> {
            lamp.turnOff();
            updateUI();
        });

        btnBrighten.setOnClickListener(v -> {
            lamp.brighten();
            updateUI();
        });

        btnDim.setOnClickListener(v -> {
            lamp.dim();
            updateUI();
        });

        btnReplace.setOnClickListener(v -> {
            boolean success = lamp.replaceBulb();
            if (success) {
                Toast.makeText(MainActivity.this, "Bulb Replaced!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(MainActivity.this, "Cannot replace while ON!", Toast.LENGTH_SHORT).show();
            }
            updateUI();
        });

        Button btnRunTests = findViewById(R.id.btn_run_tests);
        TextView tvTestReport = findViewById(R.id.tv_test_report);

        btnRunTests.setOnClickListener(v -> {
            String report = TestRunner.runAllTests();
            tvTestReport.setText(report);
            // Tests act purely passively, so regular UI stays unchanged.
            Toast.makeText(MainActivity.this, "Test Run Complete! Check Report.", Toast.LENGTH_SHORT).show();
        });

        updateUI();
    }

    private void updateUI() {
        if (lamp.isShining()) {
            tvLampStatus.setText(getString(R.string.lamp_status_on) + " (Shining)");
            tvLampStatus.setTextColor(Color.parseColor("#FFC107")); // Amber
        } else {
            tvLampStatus.setText(getString(R.string.lamp_status_off) + (lamp.isOn() ? " (Not Shining)" : ""));
            tvLampStatus.setTextColor(Color.GRAY);
        }

        tvIntensity.setText(getString(R.string.lamp_intensity, lamp.getIntensity()));

        if (lamp.isBulbBurned()) {
            tvBulbStatus.setText(getString(R.string.bulb_status_burned));
            tvBulbStatus.setTextColor(Color.RED);
        } else {
            tvBulbStatus.setText(getString(R.string.bulb_status_ok));
            tvBulbStatus.setTextColor(Color.GREEN);
        }
    }
}

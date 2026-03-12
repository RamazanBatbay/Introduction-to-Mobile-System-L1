package com.example.introduceyourself;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

/**
 * Course Name: Introduction to Mobile System
 * Lab Number: L2
 * Student Name: Ramazan Batbay
 * Student ID: 54813
 */

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        EditText nameInput = findViewById(R.id.nameInput);
        Button witajButton = findViewById(R.id.witajButton);
        TextView resultLabel = findViewById(R.id.resultLabel);

        witajButton.setOnClickListener(v -> {
            String input = nameInput.getText().toString().trim();
            if (input.isEmpty()) {
                resultLabel.setText("Przedstaw się.");
            } else {
                resultLabel.setText("Witaj " + input);
            }
        });
    }
}

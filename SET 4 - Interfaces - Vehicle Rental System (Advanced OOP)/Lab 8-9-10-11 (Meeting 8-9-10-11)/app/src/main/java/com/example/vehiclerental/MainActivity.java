package com.example.vehiclerental;

import android.content.Context;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

public class MainActivity extends AppCompatActivity {

    private Rental rental;
    private TextView tvConsole;
    private final String XML_FILENAME = "vehicles.xml";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rental = new Rental(5); // 5 Garages
        tvConsole = findViewById(R.id.tvConsole);

        Button btnAddVehicle = findViewById(R.id.btnAddVehicle);
        Button btnRemoveVehicle = findViewById(R.id.btnRemoveVehicle);
        Button btnParkVehicle = findViewById(R.id.btnParkVehicle);
        Button btnRefresh = findViewById(R.id.btnRefresh);

        btnAddVehicle.setOnClickListener(v -> showAddVehicleDialog());
        btnRemoveVehicle.setOnClickListener(v -> showRemoveVehicleDialog());
        btnParkVehicle.setOnClickListener(v -> showParkVehicleDialog());
        btnRefresh.setOnClickListener(v -> updateConsole());

        loadVehiclesFromXml();
        updateConsole();
    }

    @Override
    protected void onStop() {
        super.onStop();
        saveVehiclesToXml();
    }

    private void updateConsole() {
        rental.sortVehicles();
        StringBuilder sb = new StringBuilder();
        sb.append("--- VEHICLE LIST ---\n");
        List<Vehicle> vehicles = rental.getVehicles();
        if (vehicles.isEmpty()) {
            sb.append("No vehicles found.\n");
        } else {
            for (Vehicle v : vehicles) {
                sb.append(v.toString()).append("\n");
            }
        }
        sb.append("\n--- GARAGES ---\n");
        for (Garage g : rental.getGarages()) {
            sb.append("Garage ").append(g.getNumber()).append(": ");
            if (g.isEmpty()) {
                sb.append("Empty\n");
            } else {
                sb.append("Occupied by Vehicle [").append(((Vehicle) g.getParkedVehicle()).getId()).append("]\n");
            }
        }
        tvConsole.setText(sb.toString());
    }

    private void showAddVehicleDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add New Vehicle");

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(32, 32, 32, 32);

        final Spinner typeSpinner = new Spinner(this);
        String[] types = {"Car", "Motorboat", "Bicycle", "Scooter"};
        typeSpinner.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, types));
        layout.addView(typeSpinner);

        final EditText nameInput = new EditText(this);
        nameInput.setHint("Vehicle Name");
        layout.addView(nameInput);

        final EditText fuelMaskInput = new EditText(this);
        fuelMaskInput.setHint("Fuel Mask (Combustion Only)");
        fuelMaskInput.setInputType(InputType.TYPE_CLASS_NUMBER);
        layout.addView(fuelMaskInput);

        final EditText fuelAmountInput = new EditText(this);
        fuelAmountInput.setHint("Initial Fuel Amount");
        fuelAmountInput.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        layout.addView(fuelAmountInput);

        builder.setView(layout);

        builder.setPositiveButton("Add", (dialog, which) -> {
            String type = typeSpinner.getSelectedItem().toString();
            String name = nameInput.getText().toString();
            if (name.trim().isEmpty()) {
                Toast.makeText(this, "Name is required", Toast.LENGTH_SHORT).show();
                return;
            }

            int fuelMask = 0;
            double fuelAmount = 0.0;
            if (type.equals("Car") || type.equals("Motorboat")) {
                try {
                    fuelMask = Integer.parseInt(fuelMaskInput.getText().toString());
                    fuelAmount = Double.parseDouble(fuelAmountInput.getText().toString());
                } catch (NumberFormatException e) {
                    Toast.makeText(this, "Invalid fuel data", Toast.LENGTH_SHORT).show();
                    return;
                }
            }

            Vehicle vehicle = null;
            switch (type) {
                case "Car": vehicle = new Car(name, fuelMask, fuelAmount); break;
                case "Motorboat": vehicle = new Motorboat(name, fuelMask, fuelAmount); break;
                case "Bicycle": vehicle = new Bicycle(name); break;
                case "Scooter": vehicle = new Scooter(name); break;
            }

            if (vehicle != null) {
                rental.addVehicle(vehicle);
                updateConsole();
                Toast.makeText(this, "Vehicle Added", Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());
        builder.show();
    }

    private void showRemoveVehicleDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Remove Vehicle");

        final EditText input = new EditText(this);
        input.setHint("Vehicle ID");
        input.setInputType(InputType.TYPE_CLASS_NUMBER);
        builder.setView(input);

        builder.setPositiveButton("Remove", (dialog, which) -> {
            try {
                int id = Integer.parseInt(input.getText().toString());
                if (rental.removeVehicle(id)) {
                    updateConsole();
                    Toast.makeText(this, "Vehicle Removed", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Vehicle Not Found", Toast.LENGTH_SHORT).show();
                }
            } catch (NumberFormatException e) {
                Toast.makeText(this, "Invalid ID", Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());
        builder.show();
    }

    private void showParkVehicleDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Park Vehicle");

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(32, 32, 32, 32);

        final EditText vehicleIdInput = new EditText(this);
        vehicleIdInput.setHint("Vehicle ID");
        vehicleIdInput.setInputType(InputType.TYPE_CLASS_NUMBER);
        layout.addView(vehicleIdInput);

        final EditText garageNumInput = new EditText(this);
        garageNumInput.setHint("Garage Number (1-5)");
        garageNumInput.setInputType(InputType.TYPE_CLASS_NUMBER);
        layout.addView(garageNumInput);

        builder.setView(layout);

        builder.setPositiveButton("Park", (dialog, which) -> {
            try {
                int vid = Integer.parseInt(vehicleIdInput.getText().toString());
                int gid = Integer.parseInt(garageNumInput.getText().toString());

                Vehicle v = rental.getVehicleById(vid);
                Garage g = rental.getGarageByNumber(gid);

                if (v == null) {
                    Toast.makeText(this, "Vehicle not found", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (g == null) {
                    Toast.makeText(this, "Garage not found", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!(v instanceof Parkable)) {
                    Toast.makeText(this, "Vehicle is not parkable", Toast.LENGTH_SHORT).show();
                    return;
                }

                Parkable p = (Parkable) v;
                if (p.isParked()) {
                    Toast.makeText(this, "Vehicle is already parked", Toast.LENGTH_SHORT).show();
                } else if (!g.isEmpty()) {
                    Toast.makeText(this, "Garage is occupied", Toast.LENGTH_SHORT).show();
                } else {
                    if (p.park(g)) {
                        updateConsole();
                        Toast.makeText(this, "Vehicle Parked", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, "Failed to park", Toast.LENGTH_SHORT).show();
                    }
                }
            } catch (NumberFormatException e) {
                Toast.makeText(this, "Invalid input", Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());
        builder.show();
    }

    private void loadVehiclesFromXml() {
        File file = new File(getFilesDir(), XML_FILENAME);
        if (!file.exists()) return;

        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(new FileInputStream(file));
            doc.getDocumentElement().normalize();

            NodeList nList = doc.getDocumentElement().getChildNodes();

            for (int temp = 0; temp < nList.getLength(); temp++) {
                Node nNode = nList.item(temp);
                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) nNode;
                    String type = eElement.getNodeName();
                    String name = eElement.getElementsByTagName("name").item(0).getTextContent();
                    
                    int fuelType = 0;
                    double fuelAmount = 0.0;
                    if (type.equals("car") || type.equals("motorboat")) {
                        try {
                            fuelType = Integer.parseInt(eElement.getElementsByTagName("fuelType").item(0).getTextContent());
                            if (eElement.getElementsByTagName("fuelAmount").getLength() > 0) {
                                fuelAmount = Double.parseDouble(eElement.getElementsByTagName("fuelAmount").item(0).getTextContent());
                            }
                        } catch (Exception ignored) {}
                    }

                    Vehicle vehicle = null;
                    switch (type) {
                        case "car": vehicle = new Car(name, fuelType, fuelAmount); break;
                        case "motorboat": vehicle = new Motorboat(name, fuelType, fuelAmount); break;
                        case "bicycle": vehicle = new Bicycle(name); break;
                        case "scooter": vehicle = new Scooter(name); break;
                    }

                    if (vehicle != null) {
                        // Optional: Load ID if saved
                        if (eElement.getElementsByTagName("id").getLength() > 0) {
                            try {
                                int id = Integer.parseInt(eElement.getElementsByTagName("id").item(0).getTextContent());
                                // We use reflection or package-private helper if needed, but since id is final we just update nextId
                                // Since we already created it, it got a new ID. We can't change final ID without reflection.
                                // It's fine to just let them get new IDs on load unless exact IDs must be preserved.
                                // Assignment says: "While loading, assign a unique ID to each vehicle (using Vehicle.nextId)."
                                // So we just let the constructor assign the ID.
                            } catch (Exception ignored) {}
                        }
                        rental.addVehicle(vehicle);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Failed to load XML", Toast.LENGTH_SHORT).show();
        }
    }

    private void saveVehiclesToXml() {
        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.newDocument();

            Element rootElement = doc.createElement("vehicles");
            doc.appendChild(rootElement);

            for (Vehicle v : rental.getVehicles()) {
                String typeName = "";
                if (v instanceof Car) typeName = "car";
                else if (v instanceof Motorboat) typeName = "motorboat";
                else if (v instanceof Bicycle) typeName = "bicycle";
                else if (v instanceof Scooter) typeName = "scooter";

                Element vehicleElement = doc.createElement(typeName);

                Element nameElement = doc.createElement("name");
                nameElement.appendChild(doc.createTextNode(v.getName()));
                vehicleElement.appendChild(nameElement);

                if (v instanceof CombustionVehicle) {
                    CombustionVehicle cv = (CombustionVehicle) v;
                    Element fuelTypeElement = doc.createElement("fuelType");
                    fuelTypeElement.appendChild(doc.createTextNode(String.valueOf(cv.getSupportedFuelMask())));
                    vehicleElement.appendChild(fuelTypeElement);

                    Element fuelAmountElement = doc.createElement("fuelAmount");
                    fuelAmountElement.appendChild(doc.createTextNode(String.valueOf(cv.getFuelAmount())));
                    vehicleElement.appendChild(fuelAmountElement);
                }

                rootElement.appendChild(vehicleElement);
            }

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            DOMSource source = new DOMSource(doc);
            
            File file = new File(getFilesDir(), XML_FILENAME);
            StreamResult result = new StreamResult(new FileOutputStream(file));
            transformer.transform(source, result);
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

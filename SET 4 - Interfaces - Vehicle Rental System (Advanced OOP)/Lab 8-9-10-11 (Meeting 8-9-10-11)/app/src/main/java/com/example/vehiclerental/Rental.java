package com.example.vehiclerental;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Rental {
    private final List<Garage> garages;
    private final List<Vehicle> vehicles;

    public Rental(int garageCount) {
        garages = new ArrayList<>();
        for (int i = 1; i <= garageCount; i++) {
            garages.add(new Garage(i));
        }
        vehicles = new ArrayList<>();
    }

    public List<Garage> getGarages() {
        return garages;
    }

    public List<Vehicle> getVehicles() {
        return vehicles;
    }

    public Garage getGarageByNumber(int number) {
        for (Garage g : garages) {
            if (g.getNumber() == number) {
                return g;
            }
        }
        return null;
    }

    public Vehicle getVehicleById(int id) {
        for (Vehicle v : vehicles) {
            if (v.getId() == id) {
                return v;
            }
        }
        return null;
    }

    public void addVehicle(Vehicle vehicle) {
        if (getVehicleById(vehicle.getId()) == null) {
            vehicles.add(vehicle);
        }
    }

    public boolean removeVehicle(int id) {
        Vehicle v = getVehicleById(id);
        if (v != null) {
            if (v instanceof Parkable) {
                Parkable p = (Parkable) v;
                if (p.isParked()) {
                    p.unpark();
                }
            }
            vehicles.remove(v);
            return true;
        }
        return false;
    }

    public void sortVehicles() {
        Collections.sort(vehicles);
    }
}

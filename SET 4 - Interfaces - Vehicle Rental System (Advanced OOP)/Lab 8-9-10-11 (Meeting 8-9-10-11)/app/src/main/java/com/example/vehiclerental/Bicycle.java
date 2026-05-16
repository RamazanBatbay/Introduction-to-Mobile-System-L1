package com.example.vehiclerental;

public class Bicycle extends Vehicle implements Parkable {
    private Garage parkedGarage;

    public Bicycle(String name) {
        setName(name);
        this.parkedGarage = null;
    }

    @Override
    public boolean park(Garage garage) {
        if (garage.isEmpty() && !isParked()) {
            this.parkedGarage = garage;
            garage.setParkedVehicle(this);
            return true;
        }
        return false;
    }

    @Override
    public boolean unpark() {
        if (isParked()) {
            this.parkedGarage.setParkedVehicle(null);
            this.parkedGarage = null;
            return true;
        }
        return false;
    }

    @Override
    public boolean isParked() {
        return parkedGarage != null;
    }

    @Override
    public Garage getGarage() {
        return parkedGarage;
    }

    @Override
    public String toString() {
        String parkStatus = isParked() ? "Yes (Garage " + parkedGarage.getNumber() + ")" : "No (-)";
        return String.format("[%d] Bicycle %s | Parked: %s", getId(), getName(), parkStatus);
    }
}

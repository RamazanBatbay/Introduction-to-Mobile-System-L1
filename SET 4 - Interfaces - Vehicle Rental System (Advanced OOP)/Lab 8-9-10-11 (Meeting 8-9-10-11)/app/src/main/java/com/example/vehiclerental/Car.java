package com.example.vehiclerental;

public class Car extends Vehicle implements CombustionVehicle, Parkable {
    private final int supportedFuelMask;
    private double fuelAmount;
    private Garage parkedGarage;

    public Car(String name, int supportedFuelMask, double initialFuelAmount) {
        setName(name);
        this.supportedFuelMask = supportedFuelMask;
        this.fuelAmount = initialFuelAmount;
        this.parkedGarage = null;
    }

    @Override
    public boolean refuel(int fuelMask, double liters) {
        if (liters <= 0) return false;
        if ((supportedFuelMask & fuelMask) != 0) {
            fuelAmount += liters;
            return true;
        }
        return false;
    }

    @Override
    public int getSupportedFuelMask() {
        return supportedFuelMask;
    }

    @Override
    public double getFuelAmount() {
        return fuelAmount;
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
        return String.format("[%d] Car %s | Fuel Mask: %d | Fuel: %.2f | Parked: %s",
                getId(), getName(), supportedFuelMask, fuelAmount, parkStatus);
    }
}

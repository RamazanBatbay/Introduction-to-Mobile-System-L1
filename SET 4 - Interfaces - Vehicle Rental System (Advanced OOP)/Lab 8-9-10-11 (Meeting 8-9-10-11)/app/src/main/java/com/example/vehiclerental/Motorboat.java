package com.example.vehiclerental;

public class Motorboat extends Vehicle implements CombustionVehicle {
    private final int supportedFuelMask;
    private double fuelAmount;

    public Motorboat(String name, int supportedFuelMask, double initialFuelAmount) {
        setName(name);
        this.supportedFuelMask = supportedFuelMask;
        this.fuelAmount = initialFuelAmount;
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
    public String toString() {
        return String.format("[%d] Motorboat %s | Fuel Mask: %d | Fuel: %.2f",
                getId(), getName(), supportedFuelMask, fuelAmount);
    }
}

package com.example.vehiclerental;

public class Garage {
    private final int number;
    private Parkable parkedVehicle;

    public Garage(int number) {
        this.number = number;
        this.parkedVehicle = null;
    }

    public int getNumber() {
        return number;
    }

    public boolean isEmpty() {
        return parkedVehicle == null;
    }

    public Parkable getParkedVehicle() {
        return parkedVehicle;
    }

    // Package-private helper to maintain bidirectional association safely
    void setParkedVehicle(Parkable vehicle) {
        this.parkedVehicle = vehicle;
    }
}

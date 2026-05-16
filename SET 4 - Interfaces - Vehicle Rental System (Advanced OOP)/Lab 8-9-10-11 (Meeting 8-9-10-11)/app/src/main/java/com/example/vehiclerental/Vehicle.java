package com.example.vehiclerental;

public abstract class Vehicle implements Comparable<Vehicle> {
    private final int id;
    private String name;
    private static int nextId = 1;

    public Vehicle() {
        this.id = nextId++;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public abstract String toString();

    @Override
    public int compareTo(Vehicle other) {
        // 1. Parked first
        boolean thisParked = (this instanceof Parkable) && ((Parkable) this).isParked();
        boolean otherParked = (other instanceof Parkable) && ((Parkable) other).isParked();
        if (thisParked && !otherParked) return -1;
        if (!thisParked && otherParked) return 1;

        // 2. Vehicle type in fixed order: Car < Motorboat < Bicycle < Scooter
        int thisOrder = getTypeOrder();
        int otherOrder = other.getTypeOrder();
        if (thisOrder != otherOrder) {
            return Integer.compare(thisOrder, otherOrder);
        }

        // 3. Name ascending
        int nameCmp = this.name.compareTo(other.name);
        if (nameCmp != 0) return nameCmp;

        // 4. Fuel type ascending (0 for non-combustion)
        int thisFuelType = (this instanceof CombustionVehicle) ? ((CombustionVehicle) this).getSupportedFuelMask() : 0;
        int otherFuelType = (other instanceof CombustionVehicle) ? ((CombustionVehicle) other).getSupportedFuelMask() : 0;
        if (thisFuelType != otherFuelType) {
            return Integer.compare(thisFuelType, otherFuelType);
        }

        // 5. Fuel amount ascending (0.0 for non-combustion)
        double thisFuelAmount = (this instanceof CombustionVehicle) ? ((CombustionVehicle) this).getFuelAmount() : 0.0;
        double otherFuelAmount = (other instanceof CombustionVehicle) ? ((CombustionVehicle) other).getFuelAmount() : 0.0;
        return Double.compare(thisFuelAmount, otherFuelAmount);
    }

    private int getTypeOrder() {
        if (this instanceof Car) return 1;
        if (this instanceof Motorboat) return 2;
        if (this instanceof Bicycle) return 3;
        if (this instanceof Scooter) return 4;
        return 5;
    }

    // Static method to update nextId if loading from XML has larger IDs
    public static void updateNextId(int id) {
        if (id >= nextId) {
            nextId = id + 1;
        }
    }
}

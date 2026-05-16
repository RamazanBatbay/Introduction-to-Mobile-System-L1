package com.example.vehiclerental;

public class Scooter extends Vehicle {
    public Scooter(String name) {
        setName(name);
    }

    @Override
    public String toString() {
        return String.format("[%d] Scooter %s", getId(), getName());
    }
}

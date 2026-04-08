package com.vizja.lampapp;

public class Bulb {
    private boolean isOn;
    private boolean isBurned;

    public Bulb() {
        this.isOn = false;
        this.isBurned = false;
    }

    public void turnOn() {
        if (!isBurned) {
            isOn = true;
        }
    }

    public void turnOff() {
        isOn = false;
    }

    public boolean isOn() {
        return isOn;
    }

    public boolean isBurned() {
        return isBurned;
    }

    void burn() {
        this.isBurned = true;
    }
}

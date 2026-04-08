package com.vizja.lampapp;

public class Lamp {
    private boolean isOn;
    private int intensity; // 0-10
    private Bulb bulb;

    public Lamp() {
        this.isOn = false;
        this.intensity = 0;
        this.bulb = new Bulb();
    }

    public void turnOn() {
        if (bulb != null && !bulb.isBurned()) {
            isOn = true;
            if (intensity == 0) {
                intensity = 1;
            }
            bulb.turnOn();
        }
    }

    public void turnOff() {
        isOn = false;
        intensity = 0;
        if (bulb != null) {
            bulb.turnOff();
        }
    }

    public void brighten() {
        if (isOn) {
            intensity++;
            if (intensity > 10) {
                if (bulb != null) {
                    bulb.burn();
                }
                this.turnOff();
            }
        }
    }

    public void dim() {
        if (isOn) {
            intensity--;
            if (intensity <= 0) {
                this.turnOff();
            }
        }
    }

    public boolean replaceBulb() {
        if (isOn) {
            return false;
        } else {
            this.bulb = new Bulb();
            return true;
        }
    }

    public boolean isOn() {
        return isOn;
    }

    public boolean isShining() {
        return isOn && (intensity > 0) && bulb != null && bulb.isOn();
    }

    public boolean isBulbBurned() {
        return bulb != null && bulb.isBurned();
    }

    public int getIntensity() {
        return intensity;
    }
}

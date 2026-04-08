package com.vizja.lampapp;

import android.util.Log;

public class TestRunner {
    private static final String TAG = "LampTests";

    public static String runAllTests() {
        StringBuilder report = new StringBuilder();
        report.append("--- Starting LAMP Tests ---\n\n");

        runTest1(report);
        runTest2(report);
        runTest3(report);
        runTest4(report);
        runTest5(report);
        runTest6(report);
        runTest7(report);

        report.append("\n--- Testing Finished ---");
        return report.toString();
    }

    private static void logResult(StringBuilder report, String testName, boolean passed) {
        String status = passed ? "[PASS]" : "[FAIL]";
        String message = status + " " + testName;
        report.append(message).append("\n");
        Log.d(TAG, message);
    }

    // 1. Turn lamp on and off
    private static void runTest1(StringBuilder report) {
        Lamp l = new Lamp();
        l.turnOn();
        boolean p1 = l.isOn();
        l.turnOff();
        boolean p2 = !l.isOn();
        logResult(report, "T1: Turn lamp on and off", p1 && p2);
    }

    // 2. Brighten to 10
    private static void runTest2(StringBuilder report) {
        Lamp l = new Lamp();
        l.turnOn();
        for (int i = 0; i < 9; i++) l.brighten(); // goes 1 -> 10
        logResult(report, "T2: Brighten to 10", l.getIntensity() == 10 && !l.isBulbBurned() && l.isOn());
    }

    // 3. Attempt to brighten above 10 -> bulb burns -> lamp turns off
    private static void runTest3(StringBuilder report) {
        Lamp l = new Lamp();
        l.turnOn();
        for (int i = 0; i < 10; i++) l.brighten(); // goes 1 -> 11 (burns)
        logResult(report, "T3: Brighten > 10 burns bulb and turns lamp off",
                l.isBulbBurned() && !l.isOn() && l.getIntensity() == 0);
    }

    // 4. Dim to 0 -> lamp turns off
    private static void runTest4(StringBuilder report) {
        Lamp l = new Lamp();
        l.turnOn();
        l.dim(); // 1 -> 0
        logResult(report, "T4: Dim to 0 turns lamp off", !l.isOn() && l.getIntensity() == 0);
    }

    // 5. Replace bulb while lamp is off -> success
    private static void runTest5(StringBuilder report) {
        Lamp l = new Lamp(); // implicitly off
        boolean r = l.replaceBulb();
        logResult(report, "T5: Replace bulb while OFF succeeds", r);
    }

    // 6. Attempt to replace bulb while lamp is on -> failure
    private static void runTest6(StringBuilder report) {
        Lamp l = new Lamp();
        l.turnOn();
        boolean r = l.replaceBulb();
        logResult(report, "T6: Replace bulb while ON fails", !r && l.isOn());
    }

    // 7. Turn on lamp with burned bulb -> no light produced
    private static void runTest7(StringBuilder report) {
        Lamp l = new Lamp();
        l.turnOn();
        for (int i = 0; i < 10; i++) l.brighten(); // burns bulb
        // lamp is now off, bulb is burned
        l.turnOn(); // attempt to turn back on
        logResult(report, "T7: Turn ON with burned bulb gives no light",
                l.isBulbBurned() && !l.isShining());
    }
}

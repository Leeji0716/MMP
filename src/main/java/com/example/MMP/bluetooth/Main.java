package com.example.MMP.bluetooth;

import tinyb.BluetoothDevice;

public class Main {
    public static void main(String[] args) {
        BluetoothManager bluetoothManager = new BluetoothManager();
        GymAccessManager gymAccessManager = new GymAccessManager();

        String userId = "user123";
        String deviceAddress = "00-E0-4C-23-99-87";

        BluetoothDevice device = bluetoothManager.findDevice(deviceAddress);
        if (device != null) {
            gymAccessManager.checkIn(userId);

            try {
                Thread.sleep(10000); // Simulate time passing
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            gymAccessManager.checkOut(userId);
        } else {
            System.out.println("Device not found.");
        }
    }
}
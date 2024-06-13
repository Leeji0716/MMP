package com.example.MMP.bluetooth;

import tinyb.BluetoothDevice;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class BluetoothManager {
    BluetoothManager() {}

    private static final BluetoothManager manager = BluetoothManager.getBluetoothManager();

    public static BluetoothDevice findDevice(String address) {
        BluetoothDevice device = null;

        try {
            manager.startDiscovery();
            TimeUnit.MINUTES.sleep(1); // Scan for 5 seconds

            List<BluetoothDevice> devices = manager.getDevices();
            for (BluetoothDevice d : devices) {
                if (d.getAddress().equals(address)) {
                    device = d;
                    break;
                }
            }
            manager.stopDiscovery();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return device;
    }

    private void stopDiscovery() {
        BluetoothManager.getBluetoothManager().stopDiscovery();
    }

    private List<BluetoothDevice> getDevices() {
        return BluetoothManager.getBluetoothManager().getDevices();
    }

    private void startDiscovery() {
        BluetoothManager.getBluetoothManager().startDiscovery();
    }

    private static BluetoothManager getBluetoothManager() {
        return manager;
    }

    public static void main(String[] args) {
        BluetoothDevice device = findDevice("00:11:22:33:44:55");
        if (device != null) {
            System.out.println("Found device: " + device.getName());
        } else {
            System.out.println("Device not found.");
        }
    }
}
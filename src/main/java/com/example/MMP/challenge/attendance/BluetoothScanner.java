package com.example.MMP.challenge.attendance;

import javax.bluetooth.*;
import java.io.IOException;
import java.util.Date;
import java.util.Vector;

public class BluetoothScanner {

    private static final Vector<RemoteDevice> devicesDiscovered = new Vector<>();

    public static void main(String[] args) {
        final Object inquiryCompletedEvent = new Object();

        devicesDiscovered.clear();

        DiscoveryListener listener = new DiscoveryListener() {
            public void deviceDiscovered(RemoteDevice btDevice, DeviceClass cod) {
                devicesDiscovered.add(btDevice);
                try {
                    System.out.println("Device discovered: " + btDevice.getFriendlyName(false) + " [" + btDevice.getBluetoothAddress() + "]");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            public void inquiryCompleted(int discType) {
                synchronized (inquiryCompletedEvent) {
                    inquiryCompletedEvent.notifyAll();
                }
            }

            public void serviceSearchCompleted(int transID, int respCode) {}

            public void servicesDiscovered(int transID, ServiceRecord[] servRecord) {}
        };

        try {
            synchronized (inquiryCompletedEvent) {
                boolean started = LocalDevice.getLocalDevice().getDiscoveryAgent().startInquiry(DiscoveryAgent.GIAC, listener);
                if (started) {
                    System.out.println("Scanning for devices...");
                    inquiryCompletedEvent.wait();
                    System.out.println("Scan completed.");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 특정 MAC 주소 확인
        String targetMac = "00:11:22:33:44:55"; // 여기에 확인하려는 장치의 MAC 주소를 입력하세요
        boolean found = false;

        for (RemoteDevice device : devicesDiscovered) {
            if (device.getBluetoothAddress().equals(targetMac)) {
                found = true;
                break;
            }
        }

        if (found) {
            System.out.println("Target device found. Marking attendance...");
            markAttendance(targetMac);
        } else {
            System.out.println("Target device not found.");
        }
    }

    private static void markAttendance(String macAddress) {
        // 데이터베이스와 통신하여 출석을 체크하는 로직을 구현합니다.
        // 예를 들어, HTTP 요청을 사용하여 출석 체크 API를 호출할 수 있습니다.

        // 예시 코드: HTTP POST 요청을 사용하여 출석 체크
        String url = "http://your-server/api/attendance"; // 서버의 출석 체크 API 엔드포인트를 입력하세요

        try {
            java.net.URL apiUrl = new java.net.URL(url);
            java.net.HttpURLConnection connection = (java.net.HttpURLConnection) apiUrl.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setDoOutput(true);

            // JSON 데이터를 생성합니다.
            String jsonInputString = "{\"macAddress\": \"" + macAddress + "\", \"date\": \"" + new Date().toString() + "\"}";

            try (java.io.OutputStream os = connection.getOutputStream()) {
                byte[] input = jsonInputString.getBytes("utf-8");
                os.write(input, 0, input.length);
            }

            int responseCode = connection.getResponseCode();
            System.out.println("POST Response Code :: " + responseCode);

            if (responseCode == java.net.HttpURLConnection.HTTP_OK) { // 성공적인 응답을 받았을 때
                System.out.println("Attendance marked successfully.");
            } else {
                System.out.println("Failed to mark attendance.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
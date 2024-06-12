//package com.example.MMP.bluetooth;
//
//import java.time.LocalDateTime;
//import java.util.HashMap;
//import java.util.Map;
//
//public class GymAccessManager {
//    private Map<String, LocalDateTime> checkInTimes = new HashMap<>();
//
//    public void checkIn(String userId) {
//        checkInTimes.put(userId, LocalDateTime.now());
//        System.out.println("User " + userId + " checked in at " + checkInTimes.get(userId));
//    }
//
//    public void checkOut(String userId) {
//        LocalDateTime checkInTime = checkInTimes.remove(userId);
//        if (checkInTime != null) {
//            LocalDateTime checkOutTime = LocalDateTime.now();
//            System.out.println("User " + userId + " checked out at " + checkOutTime);
//            System.out.println("Duration: " + java.time.Duration.between(checkInTime, checkOutTime).toMinutes() + " minutes");
//        } else {
//            System.out.println("User " + userId + " has not checked in.");
//        }
//    }
//
//    public static void main(String[] args) {
//        GymAccessManager manager = new GymAccessManager();
//        String userId = "user123";
//
//        manager.checkIn(userId);
//
//        try {
//            Thread.sleep(5000); // Simulate time passing
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//
//        manager.checkOut(userId);
//    }
//}
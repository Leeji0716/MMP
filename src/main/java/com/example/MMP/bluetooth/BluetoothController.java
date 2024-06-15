package com.example.MMP.bluetooth;

import com.example.MMP.challenge.attendance.AttendanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

// BluetoothController.java
@RestController
@RequestMapping("/bluetooth")
@RequiredArgsConstructor
public class BluetoothController {
    private final AttendanceService attendanceService;

    @PostMapping("/attendance")
    public ResponseEntity<String> receiveAttendance(@RequestBody Map<String, Boolean> payload) {
        Boolean attendanceStatus = payload.get("attendanceStatus");
        if (attendanceStatus != null && attendanceStatus) {
            attendanceService.recordAttendance();
            return ResponseEntity.ok("Attendance recorded");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid attendance status");
        }
    }
}
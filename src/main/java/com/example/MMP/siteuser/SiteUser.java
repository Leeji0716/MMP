package com.example.MMP.siteuser;

import com.example.MMP.challenge.attendance.Attendance;
import com.example.MMP.challenge.challenge.Challenge;
import com.example.MMP.chat.ChatRoom;
import com.example.MMP.homeTraining.HomeTraining;
import com.example.MMP.lesson.Lesson;
import com.example.MMP.point.Point;
import com.example.MMP.ptGroup.PtGroup;
import com.example.MMP.transPass.TransPass;
import com.example.MMP.userPass.UserDayPass;
import com.example.MMP.userPass.UserPtPass;
import com.example.MMP.wod.Wod;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class SiteUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String userId;

    private String password;

    private String name;

    private String gender;

    private String number;

    private String birthDate;

    private String email;

    private String userRole;

    @OneToMany(mappedBy = "siteUser", cascade = CascadeType.REMOVE)
    @JsonManagedReference
    private List<UserPtPass> userPtPassList = new ArrayList<>();

    @OneToMany(mappedBy = "siteUser", cascade = CascadeType.REMOVE)
    @JsonManagedReference
    private List<UserDayPass> userDayPassList = new ArrayList<>();

    @OneToMany(mappedBy = "siteUser")
    @JsonManagedReference
    private List<Attendance> attendanceList = new ArrayList<>();

    @OneToMany(mappedBy = "writer", cascade = CascadeType.ALL)
    @JsonManagedReference
    @JsonIgnore
    private List<Wod> wodList;

    @OneToOne(mappedBy = "siteUser", cascade = CascadeType.ALL, fetch = FetchType.LAZY, optional = false)
    @JsonManagedReference
    @JsonIgnore
    private Point point;

    @ManyToMany(cascade = CascadeType.REMOVE)
    @JoinTable(
            name = "site_user_save_training",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "training_id")
    )
    @JsonBackReference
    private List<HomeTraining> saveTraining = new ArrayList<>();

    @OneToMany(mappedBy = "trainer")
    @JsonBackReference
    private List<Lesson> lessonList;

    @ManyToMany
    @JoinTable(
            name = "user_lesson",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "lesson_id")
    )
    @JsonBackReference
    private List<Lesson> lessonsAttending;


    @ManyToOne
    private PtGroup ptGroupUser;

    @OneToOne
    private PtGroup ptGroupTrainer;

    @ManyToMany
    @JsonManagedReference
    private List<TransPass> transPassList = new ArrayList<>();

    @ManyToMany(cascade = CascadeType.REMOVE)
    private List<ChatRoom> chatRoomList = new ArrayList<>();

    private LocalDate createDate;
}

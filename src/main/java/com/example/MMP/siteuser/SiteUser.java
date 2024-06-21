package com.example.MMP.siteuser;

import com.example.MMP.alarm.Alarm;
import com.example.MMP.challenge.attendance.Attendance;
import com.example.MMP.challenge.challengeUser.ChallengeUser;
import com.example.MMP.challengeGroup.ChallengeGroup;
import com.example.MMP.chat.ChatRoom;
import com.example.MMP.coupon.Coupon;
import com.example.MMP.homeTraining.HomeTraining;
import com.example.MMP.lesson.Lesson;
import com.example.MMP.point.Point;
import com.example.MMP.ptGroup.PtGroup;
import com.example.MMP.transPass.TransPass;
import com.example.MMP.userPass.UserDayPass;
import com.example.MMP.userPass.UserPtPass;
import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
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

    @OneToOne(mappedBy = "siteUser", cascade = CascadeType.ALL, fetch = FetchType.LAZY, optional = false)
    @JsonManagedReference
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



    @ManyToMany(mappedBy = "members")
    @JsonIgnore
    private List<PtGroup> ptGroupUser = new ArrayList<>();


    @OneToOne
    @JsonIgnore
    private PtGroup ptGroupTrainer;

    @ManyToMany
    @JsonManagedReference
    private List<TransPass> transPassList = new ArrayList<>();

    @ManyToMany
    @JoinTable(
            name = "user_chat_room",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "chat_room_id")
    )
    @JsonBackReference
    private List<ChatRoom> chatRoomList = new ArrayList<>();

    @OneToMany(mappedBy = "acceptUser")
    @JsonManagedReference
    private List<Alarm> alarmList = new ArrayList<>();

    private LocalDate createDate;

    @ManyToMany
    @JsonManagedReference
    private Set<ChallengeGroup> challengeGroups = new LinkedHashSet<>();

    @OneToMany(mappedBy = "siteUser", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonBackReference
    private List<ChallengeUser> challengeUsers = new ArrayList<>();

    @ManyToMany
    @JoinTable(
            name = "user_coupon",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "coupon_id")
    )
    @JsonBackReference
    private List<Coupon> couponList = new ArrayList<>();
}

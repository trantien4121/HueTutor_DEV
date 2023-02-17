package com.trantien.huetutor.models;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "tblTutor")
public class Tutor {
    @Id
    @Column(name="tutorId")
    @SequenceGenerator(  //Tạo quy tắc thêm mới bản ghi của userId
            name = "tutor_sequence",
            sequenceName = "tutor_sequence",
            allocationSize = 1 //increment by 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "tutor_sequence"
    )
    private Long tutorId;

    @Column(name = "job")
    private String job;
    @Column(name = "academicLevel")
    private String academicLevel;
    @Column(name="subject")
    private ArrayList<String> subject;
    @Column(name="addressTeach")
    private String addressTeach;
    @Column(name = "likeNumber")
    private Long likeNumber;

    @OneToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "userId", nullable = false)
    private User user;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "tutor")
    private Set<Class> listCLass = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "tutor")
    private Set<Rate> listRateAboutTutor = new HashSet<>();

    public Tutor(){}

    public Tutor(String job, String academicLevel, ArrayList<String> subject, String addressTeach, Long likeNumber) {
        this.job = job;
        this.academicLevel = academicLevel;
        this.subject = subject;
        this.addressTeach = addressTeach;
        this.likeNumber = likeNumber;
    }

    public Long getTutorId() {
        return tutorId;
    }

    public void setTutorId(Long tutorId) {
        this.tutorId = tutorId;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public String getAcademicLevel() {
        return academicLevel;
    }

    public void setAcademicLevel(String academicLevel) {
        this.academicLevel = academicLevel;
    }

    public ArrayList<String> getSubject() {
        return subject;
    }

    public void setSubject(ArrayList<String> subject) {
        this.subject = subject;
    }

    public String getAddressTeach() {
        return addressTeach;
    }

    public void setAddressTeach(String addressTeach) {
        this.addressTeach = addressTeach;
    }

    public Long getLikeNumber() {
        return likeNumber;
    }

    public void setLikeNumber(Long likeNumber) {
        this.likeNumber = likeNumber;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }



}

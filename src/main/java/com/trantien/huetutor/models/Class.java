package com.trantien.huetutor.models;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "tblClass")
public class Class {
    @Id
    @Column(name = "classId")
    @SequenceGenerator(  //Tạo quy tắc thêm mới bản ghi của userId
            name = "class_sequence",
            sequenceName = "class_sequence",
            allocationSize = 1 //increment by 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "class_sequence"
    )
    private Long classId;

    @Column(name = "subjectName")
    private String subjectName;
    @Column(name = "tuitionFee")
    private String tuitionFee;
    @Column(name= "gradeLevel")
    private String gradeLevel;
    @Column(name = "maxStudent")
    private Long maxStudent;
    @Column(name = "lessonTime")
    private String lessonTime;
    @Column(name = "isOnline")
    private boolean isOnline;
    @Column(name= "startDay")
    private LocalDate startDay;
    @Column(name = "endDay")
    private LocalDate endDay;

    @Column(name = "timeTable")
    private ArrayList<String>  timeTable;
    @Column(name = "status")
    private String status;

    @ManyToOne
    @JoinColumn(name = "tutorId")
    private Tutor tutor;

//    @ManyToMany(mappedBy = "registeredClass")
//    private Set<User> registeredUser;

    @OneToMany(mappedBy = "cla")
    private Set<UserClass> registeredClass ;


    public Class() {
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public Long getClassId() {
        return classId;
    }

    public void setClassId(Long classId) {
        this.classId = classId;
    }

    public String getTuitionFee() {
        return tuitionFee;
    }

    public void setTuitionFee(String tuitionFee) {
        this.tuitionFee = tuitionFee;
    }

    public String getGradeLevel() {
        return gradeLevel;
    }

    public void setGradeLevel(String gradeLevel) {
        this.gradeLevel = gradeLevel;
    }

    public Long getMaxStudent() {
        return maxStudent;
    }

    public void setMaxStudent(Long maxStudent) {
        this.maxStudent = maxStudent;
    }

    public String getLessonTime() {
        return lessonTime;
    }

    public void setLessonTime(String lessonTime) {
        this.lessonTime = lessonTime;
    }

    public boolean isOnline() {
        return isOnline;
    }

    public void setOnline(boolean online) {
        isOnline = online;
    }

    public LocalDate getStartDay() {
        return startDay;
    }

    public void setStartDay(LocalDate startDay) {
        this.startDay = startDay;
    }

    public LocalDate getEndDay() {
        return endDay;
    }

    public void setEndDay(LocalDate endDay) {
        this.endDay = endDay;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Tutor getTutor() {
        return tutor;
    }

    public void setTutor(Tutor tutor) {
        this.tutor = tutor;
    }

    public ArrayList<String> getTimeTable() {
        return timeTable;
    }

    public void setTimeTable(ArrayList<String> timeTable) {
        this.timeTable = timeTable;
    }

    @Override
    public String toString() {
        return "Class{" +
                "classId=" + classId +
                ", subjectName='" + subjectName + '\'' +
                ", tuitionFee='" + tuitionFee + '\'' +
                ", gradeLevel='" + gradeLevel + '\'' +
                ", maxStudent=" + maxStudent +
                ", lessonTime='" + lessonTime + '\'' +
                ", isOnline=" + isOnline +
                ", startDay=" + startDay +
                ", endDay=" + endDay +
                ", timeTable=" + timeTable +
                ", status='" + status +
                '}';
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }
}

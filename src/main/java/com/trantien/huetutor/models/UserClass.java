package com.trantien.huetutor.models;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name="tblUser_Class")
public class UserClass {

    @EmbeddedId
    private UserClassKey id = new UserClassKey();

    @ManyToOne
    @MapsId("userId")
    @JoinColumn(name = "userId")
    private User user;

    @ManyToOne
    @MapsId("classId")
    @JoinColumn(name = "classId")
    private Class cla;

    @Column(name = "registeredDay")
    private LocalDate registeredDay;

    public UserClass(){}

    public UserClassKey getId() {
        return id;
    }

    public void setId(UserClassKey id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Class getCla() {
        return cla;
    }

    public void setCla(Class cla) {
        this.cla = cla;
    }

    public LocalDate getRegisteredDay() {
        return registeredDay;
    }

    public void setRegisteredDay(LocalDate registeredDay) {
        this.registeredDay = registeredDay;
    }
}

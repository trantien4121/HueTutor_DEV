package com.trantien.huetutor.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import org.hibernate.annotations.CollectionId;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Entity
@Table(name="tblUser")
public class User {

    @Id
    @Column(name="userId")
    @SequenceGenerator(  //Tạo quy tắc thêm mới bản ghi của userId
            name = "user_sequence",
            sequenceName = "user_sequence",
            allocationSize = 1 //increment by 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "user_sequence"
    )
    private Long userId;
    @Column(name="email")
    private String email;
    @Column(name="fullName")
    private String fullName;
    @Column(name="gender")
    private int gender;
    @Column(name="address")
    private String address;
    @Column(name="age")
    private Long age;
    @Column(name="phoneNumber")
    private String phoneNumber;
    @Column(name="password")
    private String password;
    @Column(name="isAdmin")
    private boolean isAdmin;


    @Lob
    private byte[] image;


    public User(){}

    public User(String email, String fullName, int gender, String address, Long age, String phoneNumber, String password, boolean isAdmin, byte[] image) {
        this.email = email;
        this.fullName = fullName;
        this.gender = gender;
        this.address = address;
        this.age = age;
        this.phoneNumber = phoneNumber;
        this.password = password;
        this.isAdmin = isAdmin;
        this.image = image;
    }
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
    private Set<Advertisement> listAdvertisement = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
    private Set<Rate> listRateByUser = new HashSet<>();

    @OneToOne(fetch = FetchType.LAZY,
            cascade =  CascadeType.ALL,
            mappedBy = "user")
    private Tutor tutor;

//    @ManyToMany
//    @JoinTable(name = "tblUser_Class", joinColumns = @JoinColumn(name = "userId"), inverseJoinColumns = @JoinColumn(name = "classId"))
//    private Set<Class> registeredClass;

    @OneToMany(mappedBy = "user")
    private Set<UserClass> registeredClass;


    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Long getAge() {
        return age;
    }

    public void setAge(Long Age) {
        this.age = age;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", email='" + email + '\'' +
                ", fullName='" + fullName + '\'' +
                ", gender=" + gender +
                ", address='" + address + '\'' +
                ", age=" + age +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", password='" + password + '\'' +
                ", isAdmin=" + isAdmin +
                ", image=" + Arrays.toString(image) +
                '}';
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }
}

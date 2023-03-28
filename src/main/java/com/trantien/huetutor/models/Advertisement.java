package com.trantien.huetutor.models;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name="tblAdvertisement")
public class Advertisement {
    @Id
    @Column(name="advertisementId")
    @SequenceGenerator(  //Tạo quy tắc thêm mới bản ghi của userId
            name = "advertisement_sequence",
            sequenceName = "advertisement_sequence",
            allocationSize = 1 //increment by 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "advertisement_sequence"
    )
    private Long advertisementId;
    @Column(name="title")
    private String title;
    @Column(name="content")
    private String content;
    @Lob
    @Column(name="image")
    private byte[] image;

    @Column(name="postedDay")
    private LocalDate postedDay;


    public Advertisement(){}

//    public Advertisement(Long advertisementId, Long userId, String title, String content, String image) {
//        this.advertisementId = advertisementId;
//        this.userId = userId;
//        this.title = title;
//        this.content = content;
//        this.image = image;
//    }
    @ManyToOne
    @JoinColumn(name = "userId")
    private User user;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Long getAdvertisementId() {
        return advertisementId;
    }

    public void setAdvertisementId(Long advertisementId) {
        this.advertisementId = advertisementId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }
    public LocalDate getPostedDay() {
        return postedDay;
    }

    public void setPostedDay(LocalDate postedDay) {
        this.postedDay = postedDay;
    }
}

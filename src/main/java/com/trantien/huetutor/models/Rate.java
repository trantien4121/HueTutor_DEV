package com.trantien.huetutor.models;

import jakarta.persistence.*;

import java.util.Set;

@Entity
@Table(name="tblRate")
public class Rate {
    @Id
    @Column(name = "rateId")
    @SequenceGenerator(  //Tạo quy tắc thêm mới bản ghi của userId
            name = "rate_sequence",
            sequenceName = "rate_sequence",
            allocationSize = 1 //increment by 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "rate_sequence"
    )
    private Long rateId;
    @Column(name = "rateContent")
    private String rateContent;
    @Column(name="numStarOfRate")
    private Long numStarOfRate;

    @ManyToOne
    @JoinColumn(name = "userId")
    private User user;

    @ManyToOne
    @JoinColumn(name = "tutorId")
    private Tutor tutor;

    public Rate(){}

    public Long getRateId() {
        return rateId;
    }

    public void setRateId(Long rateId) {
        this.rateId = rateId;
    }

    public String getRateContent() {
        return rateContent;
    }

    public void setRateContent(String rateContent) {
        this.rateContent = rateContent;
    }

    public Long getNumStarOfRate() {
        return numStarOfRate;
    }

    public void setNumStarOfRate(Long numStarOfRate) {
        this.numStarOfRate = numStarOfRate;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Tutor getTutor() {
        return tutor;
    }

    public void setTutor(Tutor tutor) {
        this.tutor = tutor;
    }
}

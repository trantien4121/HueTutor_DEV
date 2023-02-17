package com.trantien.huetutor.models;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.io.Serializable;
@Embeddable
public class UserClassKey implements Serializable {
    @Column(name = "userId")
    private Long userId;

    @Column(name = "classId")
    private Long classId;

    public UserClassKey() {
    }

    public UserClassKey(Long userId, Long classId) {
        this.userId = userId;
        this.classId = classId;
    }

}

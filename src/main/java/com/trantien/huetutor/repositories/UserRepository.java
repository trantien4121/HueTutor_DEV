package com.trantien.huetutor.repositories;

import com.trantien.huetutor.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {
    List<User> findByFullName(String fullName);
    List<User> findByEmail(String email);
}

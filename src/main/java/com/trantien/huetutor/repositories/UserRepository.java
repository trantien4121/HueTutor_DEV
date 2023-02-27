package com.trantien.huetutor.repositories;

import com.trantien.huetutor.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    List<User> findByFullName(String fullName);
    //List<User> findByEmail(String email);
    Optional<User> findByEmail(String email);
}

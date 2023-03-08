package com.trantien.huetutor.repositories;

import com.trantien.huetutor.models.Advertisement;
import com.trantien.huetutor.models.Tutor;
import com.trantien.huetutor.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public interface TutorRepository extends JpaRepository<Tutor, Long> {
    List<Tutor> findByAcademicLevel(String academicLevel);
    Optional<Tutor> findByTutorIdAndUser(Long tutorId, User user);
    Optional<Tutor> findByUser(User user);
}

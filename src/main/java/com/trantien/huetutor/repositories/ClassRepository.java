package com.trantien.huetutor.repositories;

import com.trantien.huetutor.models.Class;
import com.trantien.huetutor.models.Tutor;
import com.trantien.huetutor.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ClassRepository extends JpaRepository<Class, Long> {
    Optional<Class> findByClassIdAndTutor(Long classId, Tutor tutor);
}

package com.trantien.huetutor.repositories;

import com.trantien.huetutor.models.Class;
import com.trantien.huetutor.models.Tutor;
import com.trantien.huetutor.models.User;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Optional;

public interface ClassRepository extends JpaRepository<Class, Long> {
    Optional<Class> findByClassIdAndTutor(Long classId, Tutor tutor);

    List<Class> findByTutor(Tutor tutor);

    Page<Class> findByTutor(Tutor tutor, Pageable pageable);

    Page<Class> findByTutorAndStatus(Tutor tutor, String status, Pageable pageable);
}

package com.trantien.huetutor.repositories;

import com.trantien.huetutor.models.Advertisement;
import com.trantien.huetutor.models.Rate;
import com.trantien.huetutor.models.Tutor;
import com.trantien.huetutor.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RateRepository extends JpaRepository<Rate, Long> {
    List<Rate> findByTutor(Tutor tutor);
    Optional<Rate> findByRateIdAndUser(Long rateId, User user);
    Optional<Rate> findByRateIdAndTutor(Long rateId, Tutor tutor);
}

package com.trantien.huetutor.repositories;

import com.trantien.huetutor.models.Advertisement;
import com.trantien.huetutor.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AdvertisementRepository extends JpaRepository<Advertisement, Long> {
    List<Advertisement> findByTitle(String title);
    Optional<Advertisement> findByAdvertisementIdAndUser(Long advertisementId, User user);
}

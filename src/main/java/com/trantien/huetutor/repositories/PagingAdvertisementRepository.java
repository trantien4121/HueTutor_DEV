package com.trantien.huetutor.repositories;

import org.springframework.data.repository.PagingAndSortingRepository;
import com.trantien.huetutor.models.*;

public interface PagingAdvertisementRepository extends PagingAndSortingRepository<Advertisement, Long> {
}

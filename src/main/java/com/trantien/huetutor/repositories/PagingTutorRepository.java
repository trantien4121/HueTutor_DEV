package com.trantien.huetutor.repositories;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import com.trantien.huetutor.models.Tutor;

@Repository
public interface PagingTutorRepository extends PagingAndSortingRepository<Tutor, Long> {
}

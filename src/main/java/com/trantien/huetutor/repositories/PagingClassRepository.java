package com.trantien.huetutor.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.repository.PagingAndSortingRepository;
import com.trantien.huetutor.models.Class;

import java.awt.print.Pageable;

public interface PagingClassRepository extends PagingAndSortingRepository<Class, Long> {
}

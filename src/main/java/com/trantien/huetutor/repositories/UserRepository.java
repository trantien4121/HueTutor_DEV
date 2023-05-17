package com.trantien.huetutor.repositories;

import com.trantien.huetutor.models.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    List<User> findByFullName(String fullName);
    //List<User> findByEmail(String email);
    Optional<User> findByEmail(String email);

    //Custom query
    @Query(value = "select s.* from tblUser s join tblTutor u on u.userId = s.userId where s.fullName like %:keyword%", nativeQuery = true)
    List<User> findByKeyword(String keyword);

    Page<User> findByFullNameContainingIgnoreCase(String fullName, Pageable pageable);
    @Query(value = "select s.* from tblUser s join tblTutor u on u.userId = s.userId where s.fullName like %:searchValue%",
            countQuery = "select count(*) from tblUser s join tblTutor u on u.userId = s.userId where s.fullName like %:searchValue%",
            nativeQuery = true)
    Page<User> findBySearchValue(String searchValue, Pageable pageable);
}

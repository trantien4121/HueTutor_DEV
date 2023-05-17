package com.trantien.huetutor.repositories;

import com.trantien.huetutor.models.Class;
import com.trantien.huetutor.models.User;
import com.trantien.huetutor.models.UserClass;
import com.trantien.huetutor.models.UserClassKey;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface UserClassRepository extends JpaRepository<UserClass, UserClassKey> {
//    Optional<UserClass> findByUserAndClass(User user, Class cla);
    List<UserClass> findByUser(User user);
    List<UserClass> findByCla(Class cla);
    Optional<UserClass> findByUserAndCla(User user, Class cla);
    Page<UserClass> findByUser(User user, Pageable pageable);

    @Query(value = "select count(*) as NumOfStudents from tblUser_Class where classId = :keyword group by :keyword", nativeQuery = true)
    Long countById(String keyword);

}

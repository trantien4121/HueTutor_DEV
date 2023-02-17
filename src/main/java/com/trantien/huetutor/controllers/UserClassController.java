package com.trantien.huetutor.controllers;

import com.trantien.huetutor.models.*;
import com.trantien.huetutor.models.Class;
import com.trantien.huetutor.repositories.ClassRepository;
import com.trantien.huetutor.repositories.UserClassRepository;
import com.trantien.huetutor.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.Local;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(path = "api/v1/ClassTeaches")
public class UserClassController {
    @Autowired
    UserRepository userRepository;

    @Autowired
    ClassRepository classRepository;

    @Autowired
    UserClassRepository userClassRepository;

    @GetMapping("")
    List<UserClass> getAllClassTeaches() {
        return userClassRepository.findAll();
    }

    @GetMapping("/{userId}")
    List<UserClass> getAllClassesOfUserById(@PathVariable Long userId) {
        Optional<User> foundUser = userRepository.findById(userId);
        if (foundUser.isPresent()) {
            List<UserClass> user_Class = userClassRepository.findByUser(foundUser.get());
            return user_Class;
        } else return null;
    }

    @PostMapping("{userId}/insertUserToClass/{classId}")
    ResponseEntity<ResponseObject> insertUserToClassTeach(@PathVariable(value = "userId") Long userId,
                                                          @PathVariable(value = "classId") Long classId,
                                                          @ModelAttribute UserClass userClass) {

        Optional<User> userJoin = userRepository.findById(userId);
        Optional<Class> classToJoin = classRepository.findById(classId);
        if (userJoin.isPresent() && classToJoin.isPresent()) {

            userClass.setUser(userJoin.get());
            userClass.setCla(classToJoin.get());
            LocalDate registeredDay = LocalDate.now();
            userClass.setRegisteredDay(registeredDay);
        }

        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("ok", "Insert User_Class successfully", userClassRepository.save(userClass))   //save là thêm
        );
    }

//    @DeleteMapping("{userId}/deleteUserToClass/{classId}")
//    public ResponseEntity<ResponseObject> deleteRate(@PathVariable (value = "userId") Long userId,
//                                                     @PathVariable (value = "classId") Long classId){
//        Optional<User> userOfClass = userRepository.findById(userId);
//        Optional<Class> classOfUser = classRepository.findById(classId);
//        if(userOfClass.isPresent() && classOfUser.isPresent()) {
//            User user = userOfClass.get();
//            Class cla = classOfUser.get();
//            Optional<UserClassKey> exists = userClassRepository.findByUserAndCla(user, cla);
//            if(exists.isPresent()){
//                userClassRepository.deleteById(exists);
//                return ResponseEntity.status(HttpStatus.OK).body(
//                        new ResponseObject("ok", "Delete Rate successfully", "")
//                );
//            }
//        }
//
//        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
//                new ResponseObject("failed", "Can't find rate of user to delete", "")
//        );
//    }
}

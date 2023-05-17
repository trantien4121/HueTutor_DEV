package com.trantien.huetutor.controllers;

import com.trantien.huetutor.models.*;
import com.trantien.huetutor.models.Class;
import com.trantien.huetutor.repositories.ClassRepository;
import com.trantien.huetutor.repositories.UserClassRepository;
import com.trantien.huetutor.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.Local;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.swing.text.html.Option;
import java.time.LocalDate;
import java.util.ArrayList;
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

    @CrossOrigin
    @GetMapping("/{userId}")
    List<UserClass> getAllClassesOfUserById(@PathVariable Long userId) {
        Optional<User> foundUser = userRepository.findById(userId);
        if (foundUser.isPresent()) {
            List<UserClass> user_Class = userClassRepository.findByUser(foundUser.get());
            return user_Class;
        } else return null;
    }

    @CrossOrigin
    @GetMapping("/getAllUsers/{classId}")
    List<UserClass> getAllUsersOfClassByClassId(@PathVariable Long classId){
        Optional<Class> foundClass = classRepository.findById(classId);
        if(foundClass.isPresent()){
            List<UserClass> userClass = userClassRepository.findByCla(foundClass.get());
            return userClass;
        }
        else return null;
    }

    @CrossOrigin
    @GetMapping("/PaginationAndFilter/getAllClassesOfUser/{userId}")
    ResponseEntity<ResponseObject> getPNFClassesOfUser(
            @PathVariable Long userId,
            @RequestParam(required = false) String status,
            @RequestParam(defaultValue = "0") int pageNo,
            @RequestParam(defaultValue = "6") int pageSize)
    {
        try{
            List<UserClass> classesOfUser = new ArrayList<UserClass>();
            Pageable pagingSort = PageRequest.of(pageNo, pageSize);
            Page<UserClass> pageTuts = null;

            Optional<User> foundUser  = userRepository.findById(userId);
            if(foundUser.isPresent()){
                pageTuts = userClassRepository.findByUser(foundUser.get(), pagingSort);
            }
            else{
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                        new ResponseObject("failed", "Can't find Class Of user with userId = " + "userId", "")
                );
            }
            classesOfUser = pageTuts.getContent();
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("NumOfPages = " + String.valueOf(pageTuts.getTotalPages()), "Query class successfully", classesOfUser)
            );
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new ResponseObject("failed", "Can't find Class Of user with userId = " + "userId", "")
            );
        }
    }

    @CrossOrigin
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

    @CrossOrigin
    @DeleteMapping("{userId}/deleteUserToClass/{classId}")
    public ResponseEntity<ResponseObject> deleteRate(@PathVariable (value = "userId") Long userId,
                                                     @PathVariable (value = "classId") Long classId){
        Optional<User> userOfClass = userRepository.findById(userId);
        Optional<Class> classOfUser = classRepository.findById(classId);
        if(userOfClass.isPresent() && classOfUser.isPresent()) {
            User user = userOfClass.get();
            Class cla = classOfUser.get();
            Optional<UserClass> exists = userClassRepository.findByUserAndCla(user, cla);
            if(exists.isPresent()){
                userClassRepository.delete(exists.get());
                return ResponseEntity.status(HttpStatus.OK).body(
                        new ResponseObject("ok", "Delete ClassOfUser successfully", "")
                );
            }
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new ResponseObject("failed", "Can't find ClassOfUser of user to delete", "")
        );
    }
    @GetMapping("/count")
    public ResponseEntity<ResponseObject> countByClassId(@RequestParam(value = "keyword") String keyword){
        Long numOfStudents = (userClassRepository.countById(keyword) == null) ? 0: userClassRepository.countById(keyword);
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("OK", "Query successfully!", numOfStudents)
        );
    }
}

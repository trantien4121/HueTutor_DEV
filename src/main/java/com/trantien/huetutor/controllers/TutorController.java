package com.trantien.huetutor.controllers;

import com.trantien.huetutor.models.Advertisement;
import com.trantien.huetutor.models.ResponseObject;
import com.trantien.huetutor.models.Tutor;
import com.trantien.huetutor.models.User;
import com.trantien.huetutor.repositories.TutorRepository;
import com.trantien.huetutor.repositories.UserRepository;
import com.trantien.huetutor.services.IStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(path = "api/v1/Tutors")
public class TutorController {
    @Autowired
    UserRepository userRepository;
    @Autowired
    TutorRepository tutorRepository;

    @Autowired
    private IStorageService storageService;

    @GetMapping("")
    List<Tutor> getAllTutors(){
        return tutorRepository.findAll();
    }

    @GetMapping("/{tutorId}")
    ResponseEntity<ResponseObject> findById(@PathVariable Long tutorId){
        Optional<Tutor> foundTutor = tutorRepository.findById(tutorId);
        return foundTutor.isPresent() ?
                ResponseEntity.status(HttpStatus.OK).body(
                        new ResponseObject("ok", "Query tutor successfully", foundTutor)
                ) :
                ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                        new ResponseObject("failed", "Can't find tutor with tutorId = " + tutorId, "")
                );
    }

    @GetMapping("/getRole/{userId}")    //Mới thêm
    ResponseEntity<ResponseObject> findTutorByUserId(@PathVariable Long userId){

        Optional<User> user = userRepository.findById(userId);
        if(user.isPresent()){
            Optional<Tutor> foundTutor = tutorRepository.findByUser(user.get());
            return foundTutor.isPresent() ?
                    ResponseEntity.status(HttpStatus.OK).body(
                            new ResponseObject("ok", "Query tutor successfully", foundTutor)
                    ) :
                    ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                            new ResponseObject("failed", "Can't find tutor with userId = " + userId, "")
                    );
        }
        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(
                new ResponseObject("failed", "Can't find tutor with userId = " + userId, "")
        );
    }


    @PostMapping("/{userId}/insert")
    public ResponseEntity<ResponseObject> insertTutor(@PathVariable (value = "userId") Long userId,
                                                              @ModelAttribute Tutor tutor){
        Optional<User> userDoTutor = userRepository.findById(userId);
        if(userDoTutor.isPresent()) {
            tutor.setUser(userDoTutor.get());
        }
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("ok", "Insert tutor successfully", tutorRepository.save(tutor))   //save là thêm
        );
    }
    @PutMapping("/{userId}/{tutorId}")
    ResponseEntity<ResponseObject> updateTutor(@ModelAttribute Tutor newTutor, @PathVariable Long userId,
                                               @PathVariable Long tutorId){
        Tutor updatedTutor = tutorRepository.findById(tutorId)
                .map(tutor -> {
                    tutor.setJob(newTutor.getJob());
                    tutor.setAcademicLevel(newTutor.getAcademicLevel());
                    tutor.setSubject(newTutor.getSubject());
                    tutor.setAddressTeach(newTutor.getAddressTeach());
                    tutor.setLikeNumber(newTutor.getLikeNumber());

                    Optional<User> userTutor = userRepository.findById(userId);
                    if(userTutor.isPresent()) {
                        tutor.setUser(userTutor.get());
                    }

                    return tutorRepository.save(tutor);
                }).orElseGet(() ->{
                    newTutor.setTutorId(tutorId);
                    return tutorRepository.save(newTutor);
                });
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("ok", "Update user successfully", updatedTutor)   //save là thêm
        );
    }

    @DeleteMapping("/{userId}/{tutorId}")
    ResponseEntity<ResponseObject> deleteTutor(@PathVariable Long userId, @PathVariable Long tutorId){

        Optional<User> userTutor = userRepository.findById(userId);
        if(userTutor.isPresent()) {
            User user = userTutor.get();
            Optional<Tutor> exists = tutorRepository.findByTutorIdAndUser(tutorId, user);
            if(exists.isPresent()){
                tutorRepository.deleteById(tutorId);
                return ResponseEntity.status(HttpStatus.OK).body(
                        new ResponseObject("ok", "Delete tutor successfully", "")
                );
            }
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new ResponseObject("failed", "Can't find tutor of user to delete", "")
        );
    }

}

package com.trantien.huetutor.controllers;

import com.trantien.huetutor.models.*;
import com.trantien.huetutor.models.Class;
import com.trantien.huetutor.repositories.RateRepository;
import com.trantien.huetutor.repositories.TutorRepository;
import com.trantien.huetutor.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.Timer;

@RestController
@RequestMapping(path ="api/v1/Rates")
public class RateController {
    @Autowired
    RateRepository rateRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    TutorRepository tutorRepository;

    @GetMapping("")
    List<Rate> getAllRates(){
        return rateRepository.findAll();
    }

    @GetMapping("/{rateId}")
    ResponseEntity<ResponseObject> findById(@PathVariable Long rateId){
        Optional<Rate> foundRate = rateRepository.findById(rateId);
        return foundRate.isPresent() ?
                ResponseEntity.status(HttpStatus.OK).body(
                        new ResponseObject("ok", "Query rate successfully", foundRate)
                ) :
                ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                        new ResponseObject("failed", "Can't find rate with id = " +  rateId, "")
                );
    }
    @GetMapping("/Tutor/{tutorId}")
    List<Rate> findByUserIdAndTutorId(@PathVariable Long tutorId){
        Optional<Tutor> foundTutor = tutorRepository.findById(tutorId);
        if(foundTutor.isPresent()){
            List<Rate> foundRateOfTutor = rateRepository.findByTutor(foundTutor.get());
            return foundRateOfTutor;
        }
        return null;
    }

    @PostMapping("{userId}/insertRate/{tutorId}")
    public ResponseEntity<ResponseObject> insertRate(@PathVariable (value = "userId") Long userId,
                                                     @PathVariable (value = "tutorId") Long tutorId,
                                                     @ModelAttribute Rate rate){
        Optional<User> userRate = userRepository.findById(userId);
        Optional<Tutor> tutorOfRate = tutorRepository.findById(tutorId);
        if(userRate.isPresent() && tutorOfRate.isPresent()) {
            rate.setUser(userRate.get());
            rate.setTutor(tutorOfRate.get());
        }

        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("ok", "Insert Rate successfully", rateRepository.save(rate))   //save là thêm
        );
    }

    @PutMapping("{userId}/updateRate/{rateId}")
    public ResponseEntity<ResponseObject> updateRate(@PathVariable (value = "userId") Long userId,
                                                     @PathVariable (value = "rateId") Long rateId,
                                                     @ModelAttribute Rate newRate){
        Rate updatedRate = rateRepository.findById(rateId)
                .map(rate -> {
                    rate.setRateContent(newRate.getRateContent());
                    rate.setNumStarOfRate((newRate.getNumStarOfRate()));
                    return rateRepository.save(rate);
                }).orElseGet(() ->{
                    newRate.setRateId(rateId);
                    return  rateRepository.save(newRate);
                });
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("ok", "Update rate successfully", updatedRate)   //save là thêm
        );
    }

    @DeleteMapping("{userId}/deleteRate/{rateId}")
    public ResponseEntity<ResponseObject> deleteRate(@PathVariable (value = "userId") Long userId,
                                                     @PathVariable (value = "rateId") Long rateId){
        Optional<User> userRate = userRepository.findById(userId);
        if(userRate.isPresent()) {
            User user = userRate.get();
            Optional<Rate> exists = rateRepository.findByRateIdAndUser(rateId, user);
            if(exists.isPresent()){
                rateRepository.deleteById(rateId);
                return ResponseEntity.status(HttpStatus.OK).body(
                        new ResponseObject("ok", "Delete Rate successfully", "")
                );
            }
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new ResponseObject("failed", "Can't find rate of user to delete", "")
        );
    }

}

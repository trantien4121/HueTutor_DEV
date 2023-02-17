package com.trantien.huetutor.controllers;

import com.trantien.huetutor.models.*;
import com.trantien.huetutor.models.Class;
import com.trantien.huetutor.repositories.ClassRepository;
import com.trantien.huetutor.repositories.TutorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(path = "api/v1/Classes")
public class ClassController {
    @Autowired
    TutorRepository tutorRepository;

    @Autowired
    ClassRepository classRepository;

    @GetMapping("")
    List<Class> getAllClasses(){
        return classRepository.findAll();
    }

    @GetMapping("/{classId}")
    ResponseEntity<ResponseObject> findById(@PathVariable Long classId){
        Optional<Class> foundCLass = classRepository.findById(classId);
        return foundCLass.isPresent() ?
                ResponseEntity.status(HttpStatus.OK).body(
                        new ResponseObject("ok", "Query class successfully", foundCLass)
                ) :
                ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                        new ResponseObject("failed", "Can't find Class with id = " + classId, "")
                );
    }

    @PostMapping("/{tutorId}/insert")
    public ResponseEntity<ResponseObject> insertCLass(@PathVariable (value = "tutorId") Long tutorId,
                                                      @ModelAttribute Class cla){
        Optional<Tutor> tutorTeach = tutorRepository.findById(tutorId);
        if(tutorTeach.isPresent()) {
            cla.setTutor(tutorTeach.get());
        }

        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("ok", "Insert class successfully", classRepository.save(cla))   //save là thêm
        );
    }

    @PutMapping("/{tutorId}/{classId}")
    ResponseEntity<ResponseObject> updateClass(@ModelAttribute Class newClass, @PathVariable Long tutorId,
                                               @PathVariable Long classId){
        Class updatedCLass = classRepository.findById(classId)
                .map(cla -> {
                    cla.setSubjectName(newClass.getSubjectName());
                    cla.setTuitionFee(newClass.getTuitionFee());
                    cla.setGradeLevel(newClass.getGradeLevel());
                    cla.setMaxStudent(newClass.getMaxStudent());
                    cla.setLessonTime(newClass.getLessonTime());
                    cla.setOnline(newClass.isOnline());
                    cla.setStartDay(newClass.getStartDay());
                    cla.setEndDay(newClass.getEndDay());
                    cla.setStatus(newClass.getStatus());
                    cla.setTimeTable(newClass.getTimeTable());

                    Optional<Tutor> tutorTeach = tutorRepository.findById(tutorId);
                    if(tutorTeach.isPresent()) {
                        cla.setTutor(tutorTeach.get());
                    }

                    return classRepository.save(cla);
                }).orElseGet(() ->{
                    newClass.setClassId(classId);
                    return classRepository.save(newClass);
                });
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("ok", "Update class successfully", updatedCLass)   //save là thêm
        );
    }

    @DeleteMapping("/{tutorId}/{classId}")
    ResponseEntity<ResponseObject> deleteTutor(@PathVariable Long tutorId, @PathVariable Long classId){

        Optional<Tutor> tutorTeach = tutorRepository.findById(tutorId);
        if(tutorTeach.isPresent()) {
            Tutor tutor = tutorTeach.get();
            Optional<Class> exists = classRepository.findByClassIdAndTutor(classId, tutor);
            if(exists.isPresent()){
                classRepository.deleteById(classId);
                return ResponseEntity.status(HttpStatus.OK).body(
                        new ResponseObject("ok", "Delete class successfully", "")
                );
            }
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new ResponseObject("failed", "Can't find class of user to delete", "")
        );
    }


}

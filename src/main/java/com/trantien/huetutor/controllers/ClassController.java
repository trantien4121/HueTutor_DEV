package com.trantien.huetutor.controllers;

import com.trantien.huetutor.models.*;
import com.trantien.huetutor.models.Class;
import com.trantien.huetutor.payloads.PagingResponse;
import com.trantien.huetutor.repositories.ClassRepository;
import com.trantien.huetutor.repositories.PagingClassRepository;
import com.trantien.huetutor.repositories.TutorRepository;
import com.trantien.huetutor.repositories.UserRepository;
import com.trantien.huetutor.services.PagingClassService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
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

    @CrossOrigin
    @GetMapping("/getAllClassesOfTutor/{tutorId}")
    ResponseEntity<ResponseObject> getAllClassesOfTutors(@PathVariable Long tutorId){

        Optional<Tutor> foundTutor = tutorRepository.findById(tutorId);

        if(foundTutor.isPresent()){
            List<Class> listClassFound = classRepository.findByTutor(foundTutor.get());
            return listClassFound.size()!=0 ?
                    ResponseEntity.status(HttpStatus.OK).body(
                            new ResponseObject("ok", "Query class successfully", listClassFound)
                    ) :
                    ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                            new ResponseObject("failed", "Can't find Class Of tutor with tutorId = " + "tutorId", "")
                    );
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new ResponseObject("failed", "Can't find Class Of tutor with tutorId = " + "tutorId", "")
        );
    }

    @CrossOrigin
    @GetMapping("/PaginationAndFilter/getClassesOfTutor/{tutorId}")
    ResponseEntity<ResponseObject> getPNFClassesOfTutor(
            @PathVariable Long tutorId,
            @RequestParam(required = false) String status,
            @RequestParam(defaultValue = "0") int pageNo,
            @RequestParam(defaultValue = "6") int pageSize,
            @RequestParam(defaultValue = "classId") String sortBy){

        try{
            List<Class> classesOfTutor = new ArrayList<Class>();
            Pageable pagingSort = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));
            Page<Class> pageTuts = null;

            Optional<Tutor> foundTutor = tutorRepository.findById(tutorId);
            if(foundTutor.isPresent()){
                if (status == null)
                    pageTuts = classRepository.findByTutor(foundTutor.get(), pagingSort);
                else
                    pageTuts = classRepository.findByTutorAndStatus(foundTutor.get(), status, pagingSort);
            }
            else{
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                        new ResponseObject("failed", "Can't find Class Of tutor with tutorId = " + "tutorId", "")
                );
            }
            classesOfTutor = pageTuts.getContent();
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("NumOfPages = " + String.valueOf(pageTuts.getTotalPages()), "Query class successfully", classesOfTutor)
            );
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new ResponseObject("failed", "Can't find Class Of tutor with tutorId = " + "tutorId", "")
            );
        }
    }

    @CrossOrigin
    @GetMapping("/PaginationAndFilter/getClasses")
    ResponseEntity<PagingResponse> getPNFClasses(
            @RequestParam(required = false) Long tutorId,
            @RequestParam(required = false) String status,
            @RequestParam(defaultValue = "0") int pageNo,
            @RequestParam(defaultValue = "8") int pageSize,
            @RequestParam(defaultValue = "classId") String sortBy){

        try{
            List<Class> classesOfTutor = new ArrayList<Class>();
            Pageable pagingSort = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));
            Page<Class> pageTuts = null;

            if(status==null){
                status = "";
            }
            if(tutorId==null){
                tutorId = 0L;
            }

            if(tutorId==0L && status.equals("")){
                pageTuts = (Page<Class>) classRepository.findAll(pagingSort);
            }
            else if(tutorId!=0L && status.equals("")){
                Optional<Tutor> foundTutor = tutorRepository.findById(tutorId);
                if(foundTutor.isPresent()){
                    pageTuts = classRepository.findByTutor(foundTutor.get(), pagingSort);
                }
                else{
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                            new PagingResponse("failed", "Can't find Class Of tutor with tutorId = " + "tutorId", 0L, 0L, "")
                    );
                }
            }
            else if(!status.equals("") && tutorId==0L){
                pageTuts = classRepository.findByStatus(status, pagingSort);
            }
            else {
                Optional<Tutor> foundTutor = tutorRepository.findById(tutorId);
                if(foundTutor.isPresent()){
                    pageTuts = classRepository.findByTutorAndStatus(foundTutor.get(), status, pagingSort);
                }
                else{
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                            new PagingResponse("failed", "Can't find Class Of tutor with tutorId = " + "tutorId", 0L, 0L, "")
                    );
                }
            }

            classesOfTutor = pageTuts.getContent();
            Long numOfPages = Long.parseLong(String.valueOf(pageTuts.getTotalPages()));
            return ResponseEntity.status(HttpStatus.OK).body(
                    new PagingResponse("OK", "Query class successfully", numOfPages, (long) pageNo, classesOfTutor)
            );
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new PagingResponse("failed", "Can't find Class Of tutor with tutorId = " + "tutorId", 0L, 0L, "")
            );
        }
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

    @CrossOrigin
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

    @CrossOrigin
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

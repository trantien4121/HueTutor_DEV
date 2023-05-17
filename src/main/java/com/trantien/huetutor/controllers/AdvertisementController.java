package com.trantien.huetutor.controllers;

import com.trantien.huetutor.models.Advertisement;
import com.trantien.huetutor.models.ResponseObject;
import com.trantien.huetutor.models.Tutor;
import com.trantien.huetutor.models.User;
import com.trantien.huetutor.payloads.PagingResponse;
import com.trantien.huetutor.repositories.AdvertisementRepository;
import com.trantien.huetutor.repositories.PagingAdvertisementRepository;
import com.trantien.huetutor.repositories.UserRepository;
import com.trantien.huetutor.services.IStorageService;
import com.trantien.huetutor.services.PagingAdvertisementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(path = "api/v1/Advertisements")
public class AdvertisementController {
    @Autowired
    AdvertisementRepository advRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    private IStorageService storageService;

    @Autowired
    private PagingAdvertisementService pagingAdvertisementService;

    @GetMapping("")
    List<Advertisement> getAllAdvertisement(){
        return advRepository.findAll();
    }
    @CrossOrigin
    @GetMapping("/getAdvertisementPaging")
    public ResponseEntity<List<Advertisement>> getAllTutorOfPaging(
            @RequestParam(defaultValue = "0") int pageNo,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(defaultValue = "advertisementId") String sortBy)
    {
        List<Advertisement> list = pagingAdvertisementService.getAllAds(pageNo, pageSize, sortBy);

        return new ResponseEntity<List<Advertisement>>(list, new HttpHeaders(), HttpStatus.OK);
    }

    @GetMapping("/{advertisementId}")
    ResponseEntity<ResponseObject> findById(@PathVariable Long advertisementId){
        Optional<Advertisement> foundAdvert = advRepository.findById(advertisementId);
        return foundAdvert.isPresent() ?
                ResponseEntity.status(HttpStatus.OK).body(
                        new ResponseObject("ok", "Query advertisement successfully", foundAdvert)
                ) :
                ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                        new ResponseObject("failed", "Can't find advertisement with id = " + advertisementId, "")
                );
    }

    @GetMapping("/image/{advertisementId}")
    public ResponseEntity<byte[]> readDetailImage(@PathVariable Long advertisementId){
        Optional<Advertisement> foundAdvert = advRepository.findById(advertisementId);
        if (foundAdvert.isPresent()){
            byte[] imageByte = foundAdvert.get().getImage();
            String fileName = new String(imageByte);

            byte[] bytes = storageService.readFileContent(fileName);
            return ResponseEntity
                    .ok()
                    .contentType(MediaType.IMAGE_JPEG)
                    .body(bytes);
        }
        else{
            return ResponseEntity.noContent().build();
        }
    }

    @PostMapping("/{userId}/insert")
    public ResponseEntity<ResponseObject> insertAdvertisement(@PathVariable (value = "userId") Long userId,
                                                              @ModelAttribute Advertisement advertisement, @RequestParam("file")MultipartFile file){
        Optional<User> userPostAdv = userRepository.findById(userId);
        if(userPostAdv.isPresent()) {
            advertisement.setUser(userPostAdv.get());
        }

        List<Advertisement> foundAdvertisement = advRepository.findByTitle(advertisement.getTitle().trim());
        if (foundAdvertisement.size() > 0 ) {
            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(
                    new ResponseObject("failed", "Advertisement already exists", "")
            );
        }
        String generatedFileName = "";
        if (file.isEmpty()){
            generatedFileName = "";
        }
        else generatedFileName = storageService.storeFile(file);
        byte[] imageData = generatedFileName.getBytes();
        advertisement.setImage(imageData);

        LocalDate lt = LocalDate.now();
        advertisement.setPostedDay(lt);

        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("ok", "Insert user successfully", advRepository.save(advertisement))   //save là thêm
        );
    }

    @PutMapping("/{userId}/{advertisementId}")
    ResponseEntity<ResponseObject> updateUser(@ModelAttribute Advertisement newAdv, @PathVariable Long userId, @PathVariable Long advertisementId,
                                              @RequestParam("file")MultipartFile file) {
        Advertisement updatedAdv = advRepository.findById(advertisementId)
                .map(advertisement -> {
                    advertisement.setTitle(newAdv.getTitle());
                    advertisement.setContent(newAdv.getContent());
                    advertisement.setPostedDay(newAdv.getPostedDay()    );

                    String generatedFileName = "";
                    if (file.isEmpty()){
                        generatedFileName = "";
                    }
                    else generatedFileName = storageService.storeFile(file);
                    byte[] imageData = generatedFileName.getBytes();
                    advertisement.setImage(imageData);

                    Optional<User> userPostAdv = userRepository.findById(userId);
                    if(userPostAdv.isPresent()) {
                        advertisement.setUser(userPostAdv.get());
                    }

                    return advRepository.save(advertisement);
                }).orElseGet(() ->{
                    newAdv.setAdvertisementId(advertisementId);
                    return advRepository.save(newAdv);
                });
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("ok", "Update user successfully", updatedAdv)   //save là thêm
        );
    }

    @DeleteMapping("/{userId}/{advertisementId}")
    ResponseEntity<ResponseObject> deleteUser(@PathVariable Long userId, @PathVariable Long advertisementId){

        Optional<User> userPostAdv = userRepository.findById(userId);
        if(userPostAdv.isPresent()) {
            User user = userPostAdv.get();
            Optional<Advertisement> exists = advRepository.findByAdvertisementIdAndUser(advertisementId, user);
            if(exists.isPresent()){
                advRepository.deleteById(advertisementId);
                return ResponseEntity.status(HttpStatus.OK).body(
                        new ResponseObject("ok", "Delete advertisement successfully", "")
                );
            }
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new ResponseObject("failed", "Can't find advertisement of user to delete", "")
        );
    }
    @GetMapping("/searchByValue")
    public ResponseEntity<PagingResponse> searchByValue(
            @RequestParam(value = "searchValue") String searchValue,
            @RequestParam(defaultValue = "0") int pageNo,
            @RequestParam(defaultValue = "6") int pageSize
    ){
        try{
            List<Advertisement> lstAdv = new ArrayList<Advertisement>();
            Pageable pagingSort = PageRequest.of(pageNo, pageSize);
            Page<Advertisement> pageTuts = null;
            Long numOfPages = 0L;

            pageTuts = advRepository.findByTitleContainingIgnoreCase(searchValue, pagingSort);

            lstAdv = pageTuts.getContent();
            numOfPages = Long.parseLong(String.valueOf(pageTuts.getTotalPages()));
            return ResponseEntity.status(HttpStatus.OK).body(
                    new PagingResponse("OK", "Query Blog successfully", numOfPages, (long) pageNo, lstAdv)
            );

        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new PagingResponse("Failed", "Can't find Blog with searchValue = " + searchValue, 0L, (long) pageNo, "")
            );
        }
    }

}

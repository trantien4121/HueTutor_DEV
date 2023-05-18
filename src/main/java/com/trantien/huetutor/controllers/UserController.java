package com.trantien.huetutor.controllers;

import com.trantien.huetutor.jwt.JwtTokenProvider;
import com.trantien.huetutor.models.Class;
import com.trantien.huetutor.models.ResponseObject;
import com.trantien.huetutor.models.Tutor;
import com.trantien.huetutor.models.User;
import com.trantien.huetutor.payloads.ChangePasswordRequest;
import com.trantien.huetutor.payloads.PagingResponse;
import com.trantien.huetutor.repositories.UserRepository;
import com.trantien.huetutor.services.IStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

@RestController     //Báo cho spring biết class này là controller
@RequestMapping(path = "/api/v1/Users")
public class UserController {
    @Autowired
    JwtTokenProvider tokenProvider;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    private UserRepository repository;
    @Autowired
    private IStorageService storageService;

    @GetMapping("")
    List<User> getAllUsers(){
        return repository.findAll();
    }
    //get detail product
    @GetMapping("/{userId}")
    //Lấy User với userId
    //Let's return an object with: data, message, status
    ResponseEntity<ResponseObject> findById(@PathVariable Long userId) {
        Optional<User> foundUser = repository.findById(userId);   //Optional có thể trả về giá trị null
        return foundUser.isPresent() ?
                ResponseEntity.status(HttpStatus.OK).body(
                        new ResponseObject("ok", "Query user successfully", foundUser)
                ) :
                ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                        new ResponseObject("failed", "Can't find user with id = " + userId, "")
                );
    }
    @GetMapping("/image/{userId}")
    public ResponseEntity<byte[]> readDetailImage(@PathVariable Long userId){
        Optional<User> foundUser = repository.findById(userId);
        if (foundUser.isPresent()){
            byte[] imageByte = foundUser.get().getImage();
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


    @PostMapping("/insert")
    ResponseEntity<ResponseObject> insertUser(@RequestParam String email, @RequestParam String fullName,
                                              @RequestParam int gender, @RequestParam String address, @RequestParam Long age,
                                              @RequestParam String phoneNumber, @RequestParam String password, @RequestParam boolean isAdmin,
                                              @RequestParam("file")MultipartFile file) {  //@Request body chính là kiểu dữ liệu truyền vào dạng body)
        //2 user must not be have a same email

        Optional<User> foundUser = repository.findByEmail(email.trim());    //cũ là List<User>
        if (foundUser.isPresent()) {    //cũ là foundUser.size()!=0
            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(
                    new ResponseObject("failed", "Email of User already exists", "")
            );
        }
        String generatedFileName = "";
        if (file.isEmpty()){
            generatedFileName = "";
        }
        else generatedFileName = storageService.storeFile(file);
        byte[] imageData = generatedFileName.getBytes();
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("ok", "Insert user successfully", repository.save(new User(email,
                        fullName, gender, address, age, phoneNumber, passwordEncoder.encode(password), isAdmin, imageData)))   //save là thêm
        );
    }

    @CrossOrigin
    @PutMapping("/changePassword/{accessToken}")
    ResponseEntity<ResponseObject> changePassword(@PathVariable String accessToken,
                                                  @RequestBody ChangePasswordRequest changePasswordRequest){
        Long userId  = tokenProvider.getUserIdFromJWT(accessToken);
        Optional<User> foundUser = repository.findById(userId);
        if(foundUser.isPresent()){
            String oldPasswordOfFoundUser = foundUser.get().getPassword();
            if(oldPasswordOfFoundUser.substring(0,1).equals("$")){
                if(passwordEncoder.matches(changePasswordRequest.getOldPassword(), oldPasswordOfFoundUser) == true)
                {
                    foundUser.get().setPassword(passwordEncoder.encode(changePasswordRequest.getNewPassword()));
                    repository.save(foundUser.get());
                    return ResponseEntity.status(HttpStatus.OK).body(
                            new ResponseObject("ok", "change password successfully", passwordEncoder.encode(changePasswordRequest.getNewPassword()))
                    );
                }
                else{
                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                            new ResponseObject("failed", "Old Password incorrect!", "")
                    );
                }
            }else{
                if(changePasswordRequest.getOldPassword().equals(oldPasswordOfFoundUser)){
                    foundUser.get().setPassword(passwordEncoder.encode(changePasswordRequest.getNewPassword()));
                    repository.save(foundUser.get());
                    return ResponseEntity.status(HttpStatus.OK).body(
                            new ResponseObject("ok", "change password successfully", passwordEncoder.encode(changePasswordRequest.getNewPassword()))
                    );
                }else{
                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                            new ResponseObject("failed", "Old Password incorrect!", "")
                    );
                }
            }
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new ResponseObject("failed", "Can't find user to change password", "")
        );
    }

    //update, upsert = update if found, otherwise insert
    @CrossOrigin
    @PutMapping("/{userId}")
    ResponseEntity<ResponseObject> updateUser(@ModelAttribute User newUser, @PathVariable Long userId,  @RequestParam("file")MultipartFile file) {
        User updatedUser = repository.findById(userId)
                .map(user -> {
                    user.setEmail(newUser.getEmail());
                    user.setFullName(newUser.getFullName());
                    user.setGender(newUser.getGender());
                    user.setAddress(newUser.getAddress());
                    user.setAge(newUser.getAge());
                    user.setPhoneNumber(newUser.getPhoneNumber());
//                    user.setPassword(newUser.getPassword());
                    user.setAdmin(newUser.isAdmin());

//                    String generatedFileName = "";
//                    if (file.isEmpty()){
//                        generatedFileName = "";
//                    }
//                    else generatedFileName = storageService.storeFile(file);
//                    byte[] imageData = generatedFileName.getBytes();
//                    user.setImage(imageData);

                    if(file.isEmpty()){
                        return repository.save(user);
                    }else{
                        String generatedFileName = storageService.storeFile(file);
                        byte[] imageData = generatedFileName.getBytes();
                        user.setImage(imageData);
                        return repository.save(user);
                    }

//                    return repository.save(user);
                }).orElseGet(() ->{
                    newUser.setUserId(userId);
                    return repository.save(newUser);
                });
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("ok", "Update user successfully", updatedUser)   //save là thêm
        );
    }

    //Delete a User => DELETE method
    @DeleteMapping("/{userId}")
    ResponseEntity<ResponseObject> deleteUser(@PathVariable Long userId){
        boolean exists = repository.existsById(userId);
        if(exists){
            repository.deleteById(userId);
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("ok", "Delete user successfully", "")
            );
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new ResponseObject("failed", "Can't find user to delete", "")
        );
    }

    @GetMapping("/search")
    public ResponseEntity<ResponseObject> search(@RequestParam(value = "keyword") String keyword){
        List<User> searchedUser = repository.findByKeyword(keyword);
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("OK", "Query successfully!", searchedUser)
        );
    }

    @GetMapping("/searchByValue")
    public ResponseEntity<PagingResponse> searchByValue(
            @RequestParam(value = "searchValue") String searchValue,
            @RequestParam(defaultValue = "0") int pageNo,
            @RequestParam(defaultValue = "6") int pageSize
          ){
        try{
            List<User> lstUsers = new ArrayList<User>();
            Pageable pagingSort = PageRequest.of(pageNo, pageSize);
            Page<User> pageTuts = null;
            Long numOfPages = 0L;

            pageTuts = repository.findBySearchValue(searchValue, pagingSort);

            lstUsers = pageTuts.getContent();
            numOfPages = Long.parseLong(String.valueOf(pageTuts.getTotalPages()));
            return ResponseEntity.status(HttpStatus.OK).body(
                    new PagingResponse("OK", "Query tutor successfully", numOfPages, (long) pageNo, lstUsers)
            );

        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new PagingResponse("Failed", "Can't find tutor with searchValue = " + searchValue, 0L, (long) pageNo, "")
            );
        }
    }

}

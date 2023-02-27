package com.trantien.huetutor.controllers;

import com.trantien.huetutor.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import com.trantien.huetutor.models.*;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping(path = "/api/v1/auth")
public class AuthController {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AuthenticationManager authenticationManager;

    @CrossOrigin
    @PostMapping("/login")
    public ResponseEntity<ResponseObject> login(@RequestBody User user) throws Exception {  //cũ là trả về ResponseEntity<String>

//        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
//                user.getEmail(), user.getPassword()));
//
//        SecurityContextHolder.getContext().setAuthentication(authentication
//        return new ResponseEntity<>("User signed-in successfully!.", HttpStatus.OK);

        try{
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    user.getEmail(), user.getPassword()));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("OK", "User login successfully", userRepository.findByEmail(user.getEmail()).get())
            );
        }catch(BadCredentialsException e){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(
                new ResponseObject("Failed", "Login failed: cause by " + e.getMessage(), "")
            );
        }
    }



}

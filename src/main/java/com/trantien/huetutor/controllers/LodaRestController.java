package com.trantien.huetutor.controllers;

import com.sun.security.auth.UserPrincipal;
import com.trantien.huetutor.jwt.JwtTokenProvider;
import com.trantien.huetutor.payloads.LoginRequest;
import com.trantien.huetutor.payloads.LoginResponse;
import com.trantien.huetutor.payloads.RandomStuff;
import com.trantien.huetutor.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import com.trantien.huetutor.models.*;

import java.util.Optional;

@RestController
@RequestMapping(path = "/api/v1/auth")
public class LodaRestController {
    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenProvider tokenProvider;

    @Autowired
    private UserRepository userRepository;

    @CrossOrigin
    @PostMapping("/login")
    public LoginResponse authenticateUser(@RequestBody LoginRequest loginRequest) {

        // Xác thực từ username và password.
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getEmail(),
                        loginRequest.getPassword()
                )
        );

        // Nếu không xảy ra exception tức là thông tin hợp lệ
        // Set thông tin authentication vào Security Context
        SecurityContextHolder.getContext().setAuthentication(authentication);
        // Trả về jwt cho người dùng.
        String jwt = tokenProvider.generateToken((CustomUserDetails) authentication.getPrincipal());
        return new LoginResponse(jwt);
    }

    @CrossOrigin
    @GetMapping("/userinfo")
    public ResponseEntity<String> getUserInfo() {

        // Kiểm tra xem access token có hợp lệ hay không
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            return ResponseEntity.ok(auth.getName());
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    // Api /api/random yêu cầu phải xác thực mới có thể request
    @GetMapping("/random")
    public RandomStuff randomStuff(){
        return new RandomStuff("JWT Hợp lệ mới có thể thấy được message này");
    }


    // Giãi mã token và lấy thông tin User
    @CrossOrigin
    @GetMapping("/getUserByAccessToken/{token}")
    public ResponseEntity<ResponseObject> getUserByAccessToken(@PathVariable String token){
        JwtTokenProvider jwtTokenProvider = new JwtTokenProvider();
        Long userId = jwtTokenProvider.getUserIdFromJWT(token);
        Optional<User> user  = userRepository.findById(userId);
        if(user.isPresent()){
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("ok", "Get user by access token successfully", user)
            );
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new ResponseObject("failed", "Can't find user by Access Token to delete", "")
        );
    }
}

package com.trantien.huetutor.controllers;

import com.trantien.huetutor.models.User;
import com.trantien.huetutor.models.ResponseObject;
import com.trantien.huetutor.repositories.UserRepository;
import com.trantien.huetutor.services.IEmailService;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping(path = "/api/v1/action")
public class ForgotAndResetPasswordController {
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private IEmailService emailService;

    @CrossOrigin
    @PostMapping("/forgot-password")
    public ResponseEntity<ResponseObject> forgotPassword(@RequestParam String email) throws MessagingException {
        Optional<User> user = userRepository.findByEmail(email);

        if(user.isPresent()){
            String token = UUID.randomUUID().toString();
            user.get().setResetToken(token);
            userRepository.save(user.get());

            String resetPasswordLink = "http://127.0.0.1:5500/resetPassword.html?token=" + token;

            String subject = "Reset your password";
            String body = "To reset your password, please click the link below: <br/><br/>"
                    + "<a href=\"" + resetPasswordLink + "\">Reset Password</a>";

            emailService.sendEmail(email, subject, body);

            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("ok", "Send Email successfully!", resetPasswordLink)
            );
        }
        else{
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    new ResponseObject("failed", "Send Email Failed!", "")
            );
        }
    }

    @PostMapping("/reset-password")
    public ResponseEntity<ResponseObject> resetPassword(@RequestParam String token, @RequestParam String newPassword) {
        Optional<User> user = userRepository.findByResetToken(token);

        if(user.isPresent()){
            user.get().setPassword(passwordEncoder.encode(newPassword));

            user.get().setResetToken(null);
            userRepository.save(user.get());

            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("ok", "Change your password successfully!", newPassword)
            );
        }
        else{
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("Failed", "Failed to change password successfully!", "")
            );
        }

    }
}

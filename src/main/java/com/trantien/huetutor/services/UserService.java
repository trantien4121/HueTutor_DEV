package com.trantien.huetutor.services;

import com.trantien.huetutor.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import com.trantien.huetutor.models.*;

import java.util.Optional;

@Service
public class UserService implements UserDetailsService {
    @Autowired

    private UserRepository userRepository;
    @Override
    public UserDetails loadUserByUsername(String username) {
        // Kiểm tra xem user có tồn tại trong database không?
        Optional<User> user = userRepository.findByEmail(username);
        if (!user.isPresent()) {
            throw new UsernameNotFoundException(username);
        }
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
//        String password = passwordEncoder.encode(user.get().getPassword());
        String password = user.get().getPassword();
        String passwordEnc = passwordEncoder.encode(user.get().getPassword());

        //Vì password có 2 dạng: được mã hóa và chưa mã hóa (do bước đầu chưa xử lý)
        if (password.substring(0, 1).equals("$")){
            user.get().setPassword(password);
        } else{
            user.get().setPassword(passwordEnc);
        }

//        user.get().setPassword(password);
        return new CustomUserDetails(user.get());
    }

}

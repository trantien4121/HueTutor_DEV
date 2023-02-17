package com.trantien.huetutor.database;

import com.trantien.huetutor.models.User;
import com.trantien.huetutor.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;

@Configuration      //Class chứa annotation này thì bên trong chứa các bean Methods, các bean methods này sẽ được gọi khi ứng dụng chạy
public class Database {
    //Logger: Thay cho dòng system.out.println
    private static final Logger logger = LoggerFactory.getLogger(Database.class);

    @Bean
    CommandLineRunner initDatabase(UserRepository userRepository){
        return new CommandLineRunner() {
            @Override
            public void run(String... args) throws Exception {
//                User userA = new User("trantien@gmail.com", "Trần Tiến", 1, "TP Huế", 21L, "0123456", "1234", false, null);
//                User userB = new User("qthai@gmail.com", "Đoàn Quang Thái", 1, "Phú Vang", 20L, "0456435", "0000", false, null);
//                logger.info("insert data: " + userRepository.save(userA));
//                logger.info("insert data: " + userRepository.save(userB));
                //Lưu 2 bản ghi vào database với hàm save

//                Date d = new Date();
//                DateFormat dateFormat = null;
//                dateFormat = new SimpleDateFormat("dd/MM/yyyy");
//                System.out.println(dateFormat.format(d));

                //get là lấy ra String
                //set là nhập vào String
            }
        };
    }
}

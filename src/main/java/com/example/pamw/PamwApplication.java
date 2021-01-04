package com.example.pamw;

import com.example.pamw.entity.User;
import com.example.pamw.repository.UserRepository;
import com.example.pamw.security.RoleEnum;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;
import java.util.Set;

@SpringBootApplication
public class PamwApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(PamwApplication.class, args);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(PamwApplication.class);
    }

    @Bean
    CommandLineRunner initDatabase(UserRepository userRepository, PasswordEncoder encoder) {

        return args -> {
            if (!userRepository.findByUsername("kurier").isPresent())
            {
                User user = new User(
                        "Imiekuriera",
                        "Nazwiskokuriera",
                        "kurier",
                        "email@email.com",
                        encoder.encode("kurier"),
                        "adres"
                );
                Set<RoleEnum> roles = new HashSet<>();
                roles.add(RoleEnum.COURIER);
                roles.add(RoleEnum.USER);
                user.setRoles(roles);
                userRepository.save(user);
            }
        };
    }
}

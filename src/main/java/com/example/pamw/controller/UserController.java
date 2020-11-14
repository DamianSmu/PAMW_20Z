package com.example.pamw.controller;

import com.example.pamw.UserRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    private final UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

   /* @GetMapping("/")
    @ResponseBody
    public String test() {
        userRepository.save(new User("key", "Damian"));
        return "LOL";
    }*/
}

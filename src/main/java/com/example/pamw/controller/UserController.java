package com.example.pamw.controller;

import com.example.pamw.payload.response.MessageResponse;
import com.example.pamw.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/api/user")
public class UserController {

    private final UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/check/{login}")
    public ResponseEntity<?> checkLoginAvailability(@PathVariable("login") String login) {
        String response = userRepository.findByUsername(login).isPresent() ? "taken" : "free";
        return ResponseEntity.ok(new MessageResponse(response));
    }
}

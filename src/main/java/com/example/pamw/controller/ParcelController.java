package com.example.pamw.controller;

import com.example.pamw.entity.Parcel;
import com.example.pamw.entity.User;
import com.example.pamw.payload.request.ParcelRequest;
import com.example.pamw.payload.response.AllParcelsResponse;
import com.example.pamw.payload.response.MessageResponse;
import com.example.pamw.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
@RequestMapping("/api/parcel")
public class ParcelController {

    private final UserRepository userRepository;

    public ParcelController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/all")
    @ResponseBody
    public ResponseEntity<?> getAll(Authentication authentication) {
        String username = authentication.getName();
        User sender = userRepository.findByUsername(username).get();
        return ResponseEntity.ok(new AllParcelsResponse(sender.getParcels()));
    }

    @DeleteMapping("/{id}")
    @ResponseBody
    public ResponseEntity<?> deleteParcel(Authentication authentication, @PathVariable String id) {
        String username = authentication.getName();
        User sender = userRepository.findByUsername(username).get();
        sender.getParcels().removeIf(parcel -> parcel.getId().equals(id));
        userRepository.save(sender);
        return ResponseEntity.ok(new MessageResponse("Deleted"));
    }

    @PostMapping("/")
    public ResponseEntity<?> addParcel(Authentication authentication, @RequestBody ParcelRequest parcelRequest) {
        String username = authentication.getName();
        User sender = userRepository.findByUsername(username).get();
        sender.getParcels().add(new Parcel(parcelRequest.getReceiver(), parcelRequest.getPostOffice(), parcelRequest.getSize()));
        userRepository.save(sender);
        return ResponseEntity.ok(new MessageResponse("Parcel added"));
    }
}

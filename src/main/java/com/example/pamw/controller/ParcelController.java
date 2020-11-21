package com.example.pamw.controller;

import com.example.pamw.entity.Parcel;
import com.example.pamw.entity.User;
import com.example.pamw.payload.response.AllParcelsResponse;
import com.example.pamw.repository.ParcelRepository;
import com.example.pamw.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

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
        List<Parcel> plist = new ArrayList<>();
        plist.add(new Parcel("Reciever1", "Postoffice1", "size1"));
        plist.add(new Parcel("Reciever2", "Postoffice2", "size2"));
        sender.setParcels(plist);

        return ResponseEntity.ok(new AllParcelsResponse(sender.getParcels()));
    }

}

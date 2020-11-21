package com.example.pamw.controller;

import com.example.pamw.entity.User;
import com.example.pamw.payload.request.LoginRequest;
import com.example.pamw.payload.request.SignupRequest;
import com.example.pamw.payload.response.LoginResponse;
import com.example.pamw.payload.response.MessageResponse;
import com.example.pamw.repository.UserRepository;
import com.example.pamw.security.JwtUtils;
import com.example.pamw.security.RoleEnum;
import com.example.pamw.security.UserDetailsImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/*@CrossOrigin(origins = "*", maxAge = 3600)*/
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final PasswordEncoder encoder;
    private final JwtUtils jwtUtils;

    public AuthController(AuthenticationManager authenticationManager, UserRepository userRepository, PasswordEncoder encoder, JwtUtils jwtUtils) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.encoder = encoder;
        this.jwtUtils = jwtUtils;
    }

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest, HttpServletResponse response, HttpServletRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());


        Cookie authCookie = new Cookie("authToken", jwt);
        authCookie.setMaxAge(7 * 24 * 60 * 60);
        authCookie.setDomain("localhost");
        authCookie.setPath("/api/");
        //authCookie.setSecure(true);
        //authCookie.setHttpOnly(true);
        response.addCookie(authCookie);


        return ResponseEntity.ok(new LoginResponse(userDetails.getUsername(), userDetails.getEmail(), roles));
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
        if (userRepository.findByUsername(signUpRequest.getUsername()).isPresent())
        {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Username is already taken!"));
        }

        if (userRepository.findByEmail(signUpRequest.getEmail()).isPresent())
        {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Email is already in use!"));
        }

        User user = new User(
                signUpRequest.getFirstname(),
                signUpRequest.getLastname(),
                signUpRequest.getUsername(),
                signUpRequest.getEmail(),
                encoder.encode(signUpRequest.getPassword()),
                signUpRequest.getAddress()
        );

        Set<String> strRoles = signUpRequest.getRoles();
        Set<RoleEnum> roles = new HashSet<>();

        if (strRoles == null) {
            roles.add(RoleEnum.USER);
        } else
        {
            strRoles.forEach(role -> {
                switch (role)
                {
                    case "admin":
                        roles.add(RoleEnum.ADMIN);
                        break;
                    case "user":
                        roles.add(RoleEnum.USER);
                        break;

                }
            });
        }

        user.setRoles(roles);
        userRepository.save(user);

        return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
    }

    @GetMapping("/logout")
    public ResponseEntity<?> logout( HttpServletResponse response, HttpServletRequest request) {
        Cookie authCookie = new Cookie("authToken", null);
        authCookie.setMaxAge(0);
        authCookie.setDomain("localhost");
        authCookie.setPath("/api/");
        //authCookie.setSecure(true);
        //authCookie.setHttpOnly(true);
        response.addCookie(authCookie);

        return ResponseEntity.ok(new MessageResponse("Logout successful"));
    }
}

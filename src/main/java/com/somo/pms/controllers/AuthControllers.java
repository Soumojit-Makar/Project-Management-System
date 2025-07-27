package com.somo.pms.controllers;

import com.somo.pms.dto.LoginRequest;
import com.somo.pms.dto.TokenResponse;
import com.somo.pms.dto.UserRequest;
import com.somo.pms.dto.UserResponse;
import com.somo.pms.services.UserService;
import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthControllers {

    private final UserService userService;
    public AuthControllers(UserService userService) {
        this.userService = userService;
    }
    @PostMapping("/register")
    public ResponseEntity<UserResponse> registerUser(@RequestBody UserRequest userRequest) throws BadRequestException {
        System.out.println(userRequest.toString());
        return new ResponseEntity<>(userService.createUser(userRequest), HttpStatus.CREATED);
    }
    @PostMapping("/login")
    public ResponseEntity<TokenResponse> loginUser(@RequestBody LoginRequest request) throws BadRequestException {
        return new ResponseEntity<>(userService.createToken(request), HttpStatus.ACCEPTED);
    }
}

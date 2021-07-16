package com.training.springbootbuyitem.controller;

import com.training.springbootbuyitem.entity.request.UserLoginDto;
import com.training.springbootbuyitem.entity.response.UserLoginResponseDto;
import com.training.springbootbuyitem.service.UserService;
import com.training.springbootbuyitem.utils.annotation.ServiceOperation;
import com.training.springbootbuyitem.configuration.auth.JwtTokenUtil;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RefreshScope
@RestController
@RequestMapping("user")
@CrossOrigin
@Slf4j
public class UserController {

//    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    private final String jwtSecret = "zdtlD3JK56m6wTTgsNFhqzjqP";
    private final String jwtIssuer = "example.io";

    @Autowired
    UserService userService;

    @Autowired
    private ModelMapper mapper;

    @GetMapping("/hello")
    public String hello() {
        return "Hello World";
    }

    @GetMapping("/login")
    @ServiceOperation("login")
    public ResponseEntity<UserLoginResponseDto> login(@RequestBody UserLoginDto authenticationRequest) {

        final UserDetails userDetails = userService
                .loadUserByUsername(authenticationRequest.getUsername());

        final String token = jwtTokenUtil.generateToken(userDetails);

//        return ResponseEntity.ok(new UserLoginResponseDto(token));

        return new ResponseEntity<>(mapper.map(new UserLoginResponseDto(token), UserLoginResponseDto.class), HttpStatus.OK);
    }
}

package com.training.springbootbuyitem.controller;

import com.training.springbootbuyitem.constant.ItemStorageConstant;
import com.training.springbootbuyitem.entity.model.User;
import com.training.springbootbuyitem.entity.request.CreateUserRequestDto;
import com.training.springbootbuyitem.entity.request.UserLoginDto;
import com.training.springbootbuyitem.entity.response.*;
import com.training.springbootbuyitem.enums.EnumOperation;
import com.training.springbootbuyitem.service.UserService;
import com.training.springbootbuyitem.utils.annotation.ServiceOperation;
import com.training.springbootbuyitem.configuration.auth.JwtTokenUtil;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RefreshScope
@RestController
@RequestMapping("user")
@CrossOrigin
@Slf4j
public class UserController {

//    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    UserService userService;

    @Autowired
    private ModelMapper mapper;

    @GetMapping("/hello")
    public String hello() {
        return "Hello World";
    }

    @PostMapping("/login")
    @ServiceOperation("login")
    public ResponseEntity<UserLoginResponseDto> login(@RequestBody UserLoginDto authenticationRequest) {

        final UserDetails userDetails = userService
                .loadUserByUsername(authenticationRequest.getUsername());

        final String token = jwtTokenUtil.generateToken(userDetails);

        return new ResponseEntity<>(mapper.map(new UserLoginResponseDto(token), UserLoginResponseDto.class), HttpStatus.OK);
    }

    @GetMapping("/logout")
    @ServiceOperation("logout")
    public ResponseEntity<UserLoginResponseDto> login(HttpSession httpSession) {

        httpSession.invalidate();
        SecurityContextHolder.clearContext();

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("")
    @ServiceOperation("createUSer")
    public ResponseEntity<CreateUserResponseDto> createUser(@RequestBody @Valid CreateUserRequestDto request) {
        MDC.put(ItemStorageConstant.OPERATION, EnumOperation.CreateItem.name());
        return new ResponseEntity<>(mapper.map(userService.save(mapper.map(request, User.class)), CreateUserResponseDto.class), HttpStatus.CREATED);
    }

    //    @Override
    @GetMapping("/{id}")
    @ServiceOperation("getUser")
    public ResponseEntity<GetUserResponseDto> getUser(@PathVariable("id") Long id, HttpSession session) {
        MDC.put(ItemStorageConstant.OPERATION, EnumOperation.GetUSer.name());
        log.info("SESSION INFO");
        log.info((String) session.getAttribute("MY_SESSION_MESSAGES"));
        log.info(SecurityContextHolder.getContext().getAuthentication().getName());
        return new ResponseEntity<>(mapper.map(userService.get(id), GetUserResponseDto.class), HttpStatus.OK);
    }

    //    @Override
    @GetMapping("/all")
    @ServiceOperation("listUsers")
    public ResponseEntity<List<GetUserResponseDto>> listUsers() {
        MDC.put(ItemStorageConstant.OPERATION, EnumOperation.ListUser.name());
        return new ResponseEntity<>(userService.list().stream().map(i -> mapper.map(i, GetUserResponseDto.class)).collect(
                Collectors.toList()), HttpStatus.OK);
    }

    //    @Override
    @PatchMapping("/{id}")
    @ServiceOperation("updateIUser")
    public ResponseEntity<UpdateUserResponseDto> updateUser(@PathVariable("id") Long id, @RequestBody User user) {
        MDC.put(ItemStorageConstant.OPERATION, EnumOperation.UpdateUser.name());
        user.setUserUid(id);
        return new ResponseEntity<>(mapper.map(userService.update(user), UpdateUserResponseDto.class), HttpStatus.OK);
    }

    //    @Override
    @DeleteMapping("/{id}")
    @ServiceOperation("deleteUser")
    public ResponseEntity<HttpStatus> deleteUser(@PathVariable("id") Long id) {
        MDC.put(ItemStorageConstant.OPERATION, EnumOperation.DeleteUser.name());
        userService.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}

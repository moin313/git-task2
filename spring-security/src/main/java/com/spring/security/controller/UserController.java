package com.spring.security.controller;

import com.spring.security.model.User;
import com.spring.security.model.UserRequest;
import com.spring.security.model.UserResponse;
import com.spring.security.service.IUserService;
import com.spring.security.util.Jwtutil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private IUserService service;

    @Autowired
    private Jwtutil util;

    @Autowired
    private AuthenticationManager authenticationManager;
    @PostMapping("/save")
    public ResponseEntity<String> saveUser(@RequestBody User user){
       Integer id=service.saveUser(user);
       String body="user'"+id+"' saved";
       return ResponseEntity.ok(body);
       //return new ResponseEntity<String>(body, HttpStatus.OK);
    }

    /*-----------------------LOGIN--------------------*/
    @PostMapping("/login")
    public ResponseEntity<UserResponse> login(@RequestBody UserRequest request){
        //TODO Validate
        System.out.println("CONTROLLER "+request.getUsername()+" "+request.getPassword());
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
           request.getUsername(), request.getPassword()
        ));
        String token = util.generateToken(request.getUsername());
        return ResponseEntity.ok(new UserResponse(token, " generated successfully...!"));
    }



    //ACCESSIBLE AFTER LOGIN ONLY
    @GetMapping("/welcome")
    public ResponseEntity<String> accesData(Principal p){
        System.out.println("eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhIiwiaXNzIjoiTW9pbiIsImlhdCI6MTY0OTMyOTQ2OSwiZXhwIjoxNjQ5MzMwMzY5fQ.D1RjYxd6XGS6TlNBJ2I0Wqbp-aaeUDm8r_mcW1ls0zPWYkuxWJw5k-bfeVOn-jdGQlUgig5GlJHRlnOqXqaU3A".equals("eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhIiwiaXNzIjoiTW9pbiIsImlhdCI6MTY0OTMyOTEyNSwiZXhwIjoxNjQ5MzMwMDI1fQ.5TOr8Q0VnM8FKckZo31Xh3PvVZlsU4Vl8Ce1OQgdrAbTE9SkbhweVo1-ErOCgkVuaJTQRgXv2T8dmWIplQ0zRA"));
        return ResponseEntity.ok("Hello User "+p.getName());
    }
}

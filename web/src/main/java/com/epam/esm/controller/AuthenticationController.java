package com.epam.esm.controller;

import com.epam.esm.repository.model.User;
import com.epam.esm.service.security.JWTCodec;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/v1/authentication",produces={MediaType.APPLICATION_JSON_VALUE})
public class AuthenticationController {
    @GetMapping(value = "/token")
    public ResponseEntity<String> retrieveJWT(@RequestBody User user){
        return new ResponseEntity<>(JWTCodec.createJWT("1",user.getName(),user.getPassword(),1000*60*5), HttpStatus.OK);
    }
    @GetMapping(value = "/otp")
    public ResponseEntity<String> retrieveOTP(@RequestBody User user){
        return new ResponseEntity<>(JWTCodec.createJWT("1",user.getName(),user.getPassword(),1000*60*5), HttpStatus.OK);
    }
}

package com.upgrad.proman.api.controller;


import com.upgrad.proman.api.model.SignupUserRequest;
import com.upgrad.proman.api.model.SignupUserResponse;
import com.upgrad.proman.service.entity.UserEntity;
import com.upgrad.proman.service.service.SignupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.time.ZonedDateTime;
import java.util.UUID;


@RestController
@RequestMapping("/")
public class SignUpController {

    @Autowired
    SignupService signupService;
    @RequestMapping(method = RequestMethod.POST, path = "/signup", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<SignupUserResponse> signup(final SignupUserRequest signupUserRequest) {

      final UserEntity userEntity = new UserEntity();
        userEntity.setUuid(UUID.randomUUID().toString());
        userEntity.setCreatedAt(ZonedDateTime.now());
        userEntity.setCreatedBy("api-backend");
        userEntity.setEmail(signupUserRequest.getEmailAddress());
        userEntity.setFirstName(signupUserRequest.getFirstName());
        userEntity.setLastName(signupUserRequest.getLastName());
        userEntity.setMobilePhone(signupUserRequest.getMobileNumber());
        userEntity.setPassword(signupUserRequest.getPassword());
        userEntity.setSalt("1234");
        userEntity.setStatus(4);
        final UserEntity createdUserEntity = signupService.createUser(userEntity);
        SignupUserResponse userResponse = new SignupUserResponse().id(createdUserEntity.getUuid()).status("registered");
        return new ResponseEntity<SignupUserResponse>(userResponse,HttpStatus.CREATED);
    }
}

package com.upgrad.proman.api.controller;


import com.upgrad.proman.api.model.CreateUserRequest;
import com.upgrad.proman.api.model.CreateUserResponse;
import com.upgrad.proman.api.model.UserDetailsResponse;
import com.upgrad.proman.api.model.UserStatusType;
import com.upgrad.proman.service.entity.UserEntity;
import com.upgrad.proman.service.exception.ForbiddenException;
import com.upgrad.proman.service.exception.ResourceNotFoundException;
import com.upgrad.proman.service.exception.UnauthorizedException;
import com.upgrad.proman.service.service.UserService;
import com.upgrad.proman.service.type.UserStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.ZonedDateTime;
import java.util.UUID;

@RestController
@RequestMapping("/")
public class UserAdminController {

    @Autowired
    UserService userService;

    @RequestMapping(method = RequestMethod.GET, value = "/user/{id}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<UserDetailsResponse> getUser(@PathVariable(name = "id") final String userUuid, @RequestHeader("authorization") final String authorization) throws ResourceNotFoundException, UnauthorizedException, ForbiddenException {
        String auth = authorization.split("Bearer ")[1];
        UserEntity userEntity = userService.getUserEntity(userUuid, auth);
        UserDetailsResponse userDetailsResponse = new UserDetailsResponse().id(userEntity.getUuid()).firstName(userEntity.getFirstName())
                .lastName(userEntity.getLastName()).emailAddress(userEntity.getEmail()).mobileNumber(userEntity.getMobilePhone()).status(UserStatusType.fromValue(String.valueOf(UserStatus.getEnum(userEntity.getStatus()))));
        return new ResponseEntity<>(userDetailsResponse,HttpStatus.OK);


    }


    @RequestMapping(method = RequestMethod.POST, path = "/user", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<CreateUserResponse> createUser(@RequestBody final CreateUserRequest userRequest) {
        UserEntity userEntity = new UserEntity();
        userEntity.setFirstName(userRequest.getFirstName());
        userEntity.setLastName(userRequest.getLastName());
        userEntity.setStatus(UserStatus.ACTIVE.getCode());
        userEntity.setUuid(UUID.randomUUID().toString());
        userEntity.setCreatedAt(ZonedDateTime.now());
        userEntity.setCreatedBy("api-backend");
        userEntity.setMobilePhone(userRequest.getMobileNumber());
        userEntity.setEmail(userRequest.getEmailAddress());

        UserEntity user = userService.createUser(userEntity);

        CreateUserResponse response = new CreateUserResponse().id(user.getUuid()).status(UserStatusType.ACTIVE);

        ResponseEntity<CreateUserResponse> userResponseResponseEntity = new ResponseEntity<>(response, HttpStatus.CREATED);
        return userResponseResponseEntity;

    }

}

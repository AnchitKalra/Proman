package com.upgrad.proman.api.controller;


import com.upgrad.proman.api.model.AuthorizedUserResponse;
import com.upgrad.proman.service.entity.UserAuthTokenEntity;
import com.upgrad.proman.service.entity.UserEntity;
import com.upgrad.proman.service.exception.AuthorizationFailedException;
import com.upgrad.proman.service.service.UserAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Base64;
import java.util.UUID;

@RestController
@RequestMapping("/")
public class UserAuthController {

    @Autowired
    private UserAuthService userAuthService;

    @RequestMapping(method = RequestMethod.POST, path = "/auth/login", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<AuthorizedUserResponse> userAuthorization(@RequestHeader("authorization") final String authorization) throws AuthorizationFailedException {
        //System.out.println("*************************************************************" + authorization + "*********************************************************************");
        byte[] decode = Base64.getDecoder().decode(authorization.split("Basic ")[1]);
        String details =new String(decode);
        //System.out.println("*************************************************************" + details + "*********************************************************************");
        String[] userDetails = details.split(":");
        UserAuthTokenEntity userAuthTokenEntity = userAuthService.getUserByEmail(userDetails[0], userDetails[1]);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("access-id", userAuthTokenEntity.getAccessToken());
        ResponseEntity<AuthorizedUserResponse> responseEntity = new ResponseEntity<AuthorizedUserResponse>(new AuthorizedUserResponse().emailAddress(userAuthTokenEntity.getUser().
                getEmail()).
                firstName(userAuthTokenEntity.getUser().getFirstName()).lastName(userAuthTokenEntity.getUser()
                .getLastName())
                .mobilePhone(userAuthTokenEntity.getUser().getMobilePhone()).
                id(UUID.fromString(userAuthTokenEntity.getUser().getUuid())).lastLoginTime(userAuthTokenEntity.getUser().getLastLoginAt()), httpHeaders, HttpStatus.OK);
                return responseEntity;
    }
}

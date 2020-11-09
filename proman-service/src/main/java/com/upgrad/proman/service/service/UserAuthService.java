package com.upgrad.proman.service.service;


import com.upgrad.proman.service.common.JwtTokenProvider;
import com.upgrad.proman.service.dao.UserDao;
import com.upgrad.proman.service.entity.UserAuthTokenEntity;
import com.upgrad.proman.service.entity.UserEntity;
import com.upgrad.proman.service.exception.AuthorizationFailedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;

@Service
public class UserAuthService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private PasswordCryptographyProvider cryptographyProvider;


    @Transactional(propagation = Propagation.REQUIRED)
    public UserAuthTokenEntity getUserByEmail(String email, String password) throws AuthorizationFailedException {
        UserEntity userEntity = userDao.getUserByEmail(email, password);
        if(userEntity == null) {
            throw new AuthorizationFailedException("ATH-001", "Incorrect Email");
        }
        String encryptedPassword = cryptographyProvider.encrypt(password, userEntity.getSalt());
        if(encryptedPassword.equals(userEntity.getPassword())) {
            JwtTokenProvider jwtTokenProvider = new JwtTokenProvider(encryptedPassword);
            UserAuthTokenEntity userAuthTokenEntity = new UserAuthTokenEntity();
            userAuthTokenEntity.setUser(userEntity);

            final ZonedDateTime now = ZonedDateTime.now();
            final ZonedDateTime expiresAt = now.plusHours(8);
            userAuthTokenEntity.setAccessToken(jwtTokenProvider.generateToken(userEntity.getUuid(), now, expiresAt));
            userAuthTokenEntity.setLoginAt(now);
            userAuthTokenEntity.setExpiresAt(expiresAt);
            userAuthTokenEntity.setCreatedBy("api-backend");
            userAuthTokenEntity.setCreatedAt(now);
            userDao.createUserAuthTokenEntity(userAuthTokenEntity);
            userDao.updateUserEntity(userEntity);
            userEntity.setLastLoginAt(now);

            return userAuthTokenEntity;
        }

        else {
            throw new AuthorizationFailedException("ATH-002", "Password incorrect");
        }
    }
}

package com.upgrad.proman.service.service;


import com.upgrad.proman.service.dao.UserDao;
import com.upgrad.proman.service.entity.RoleEntity;
import com.upgrad.proman.service.entity.UserAuthTokenEntity;
import com.upgrad.proman.service.entity.UserEntity;
import com.upgrad.proman.service.exception.ForbiddenException;
import com.upgrad.proman.service.exception.ResourceNotFoundException;
import com.upgrad.proman.service.exception.UnauthorizedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

    @Autowired
    UserDao userDao;

    @Autowired
    private PasswordCryptographyProvider passwordCryptographyProvider;

    public UserEntity getUserEntity(String userUuid, String authorizationToken) throws ResourceNotFoundException, UnauthorizedException, ForbiddenException {

        UserAuthTokenEntity userAuthTokenEntity = userDao.getUserByAuthToken(authorizationToken);
        if(userAuthTokenEntity == null) {
            throw new ForbiddenException("FBD-001", "Invalid Token");
        }
        RoleEntity role = userAuthTokenEntity.getUser().getRole();
        if(role.getUuid() == 101 && role!= null) {

            UserEntity userEntity = userDao.getUser(userUuid);
            if (userEntity == null) {
                throw new ResourceNotFoundException("user-001", "User Not Found");
            }
            return userEntity;
        }
        throw new UnauthorizedException("UNATH-001", "User is not an Administrator");
    }


    @Transactional(propagation = Propagation.REQUIRED)
    public UserEntity createUser(final UserEntity userEntity) {
        String password = userEntity.getPassword();
        if(password == null) {
            password = "proman@123";
        }
        String[] encrypt = passwordCryptographyProvider.encrypt(password);
        userEntity.setSalt(encrypt[0]);
        userEntity.setPassword(encrypt[1]);
        return userDao.createUser(userEntity);
    }
}

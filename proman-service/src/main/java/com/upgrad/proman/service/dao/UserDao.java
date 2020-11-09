package com.upgrad.proman.service.dao;

import com.upgrad.proman.service.entity.UserAuthTokenEntity;
import com.upgrad.proman.service.entity.UserEntity;
import org.springframework.stereotype.Repository;

import javax.jws.soap.SOAPBinding;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;



@Repository
public class UserDao {

    @PersistenceContext
    EntityManager entityManager;


    public UserEntity createUser(UserEntity userEntity) {
        entityManager.persist(userEntity);
        return userEntity;
    }


    public UserEntity getUser(String userUuid) {

        try {
            UserEntity userEntity = entityManager.createNamedQuery("userByUuid", UserEntity.class).setParameter("uuid", userUuid).getSingleResult();
            return userEntity;
        }
        catch (NoResultException noResultException) {
            return null;
        }



    }

    public UserEntity getUserByEmail(String email, String password) {
        try {
            return entityManager.createNamedQuery("userByEmail", UserEntity.class).setParameter("email", email)
                    .getSingleResult();
        } catch (NoResultException nre) {
            return null;
        }
    }

    public void createUserAuthTokenEntity(UserAuthTokenEntity userAuthTokenEntity) {
        entityManager.persist(userAuthTokenEntity);
        //return userAuthTokenEntity;
    }

    public void updateUserEntity(UserEntity userEntity) {
        entityManager.merge(userEntity);
    }
        public UserAuthTokenEntity getUserByAuthToken(String authorizationToken) {
        try {
            return entityManager.createNamedQuery("userAuthTokenByAccessToken", UserAuthTokenEntity.class)
                    .setParameter("accessToken", authorizationToken)
                    .getSingleResult();
        }
        catch (NoResultException noResultException) {
            return null;
        }
        }
}

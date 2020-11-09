package com.upgrad.proman.api.exception;


import com.upgrad.proman.api.model.ErrorResponse;
import com.upgrad.proman.service.exception.AuthorizationFailedException;
import com.upgrad.proman.service.exception.ForbiddenException;
import com.upgrad.proman.service.exception.ResourceNotFoundException;
import com.upgrad.proman.service.exception.UnauthorizedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class RestExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> resourceNotFoundException(ResourceNotFoundException resourceNotFoundException) {
        return new ResponseEntity<ErrorResponse>(
                new ErrorResponse().code(resourceNotFoundException.getCode()).message(resourceNotFoundException.getErrorMessage()), HttpStatus.NOT_FOUND
        );
    }

    @ExceptionHandler(AuthorizationFailedException.class)
    public ResponseEntity<ErrorResponse> authorizationFailedException(AuthorizationFailedException authorizationFailedException) {
        return new ResponseEntity<ErrorResponse>(
                new ErrorResponse().code(authorizationFailedException.getCode()).message(authorizationFailedException.getErrorMessage()), HttpStatus.UNAUTHORIZED
        );
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ErrorResponse> unauthorizationException(UnauthorizedException unauthorizedException) {
        return new ResponseEntity<ErrorResponse>(
                new ErrorResponse().code(unauthorizedException.getCode()).message(unauthorizedException.getErrorMessage()), HttpStatus.UNAUTHORIZED
        );
    }

    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<ErrorResponse> forbiddenException(ForbiddenException forbiddenException) {
        return new ResponseEntity<ErrorResponse>(
                new ErrorResponse().code(forbiddenException.getCode()).message(forbiddenException.getErrorMessage()), HttpStatus.FORBIDDEN
        );
    }
}

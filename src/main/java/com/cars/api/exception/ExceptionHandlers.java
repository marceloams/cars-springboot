package com.cars.api.exception;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class ExceptionHandlers extends ResponseEntityExceptionHandler {

    @ExceptionHandler({
            IllegalArgumentException.class
    })
    public ResponseEntity errorBadRequest(Exception e){
        return ResponseEntity.badRequest().build();
    }

    @Override
    protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        return new ResponseEntity<>(new ExceptionError("Action not allowed!"), HttpStatus.METHOD_NOT_ALLOWED);
    }
}

package com.example.demo.model;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value=HttpStatus.BAD_REQUEST, reason="Please make sure the two date fields are in correct format")
public class BadTimeException extends RuntimeException {
    
}

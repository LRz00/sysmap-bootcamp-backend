/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.lrz.sysbackend.exceptions;

import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.HttpStatus;

/**
 *
 * @author lara
 */
@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class MethodException extends RuntimeException{

    public MethodException(String message) {
        super(message);
    }
    
}

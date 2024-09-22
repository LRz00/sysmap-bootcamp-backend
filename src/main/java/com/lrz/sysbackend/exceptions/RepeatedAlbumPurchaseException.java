/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.lrz.sysbackend.exceptions;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;


/**
 *
 * @author lara
 */
@ResponseStatus(HttpStatus.CONFLICT)
public class RepeatedAlbumPurchaseException extends RuntimeException{

    public RepeatedAlbumPurchaseException(String message) {
        super(message);
    }
    
}

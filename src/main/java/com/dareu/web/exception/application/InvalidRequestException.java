/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dareu.web.exception.application;

/**
 *
 * @author MACARENA
 */
public class InvalidRequestException extends DareuException{

    public InvalidRequestException() {
    }

    public InvalidRequestException(String message) {
        super(message, ErrorCode.INVALID_REQUEST_ERROR);
    }

    public InvalidRequestException(String message, Throwable cause) {
        super(message, cause, ErrorCode.INVALID_REQUEST_ERROR);
    }
    
}

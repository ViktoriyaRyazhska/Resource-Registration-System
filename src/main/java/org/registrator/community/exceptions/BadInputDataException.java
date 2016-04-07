package org.registrator.community.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value=HttpStatus.BAD_REQUEST, reason="Bad input data") 
public class BadInputDataException extends RuntimeException{
    private static final long serialVersionUID = 2789343685557453426L;

}

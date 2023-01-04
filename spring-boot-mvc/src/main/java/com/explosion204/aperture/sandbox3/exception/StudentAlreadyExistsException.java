package com.explosion204.aperture.sandbox3.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class StudentAlreadyExistsException extends RuntimeException {
}

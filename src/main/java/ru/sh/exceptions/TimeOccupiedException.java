package ru.sh.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT, reason = "Requested action is not allowed")
public class TimeOccupiedException extends RuntimeException{
    public TimeOccupiedException(String message) {
        super(message);
    }
}

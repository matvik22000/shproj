package ru.sh.exceptions;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.UNAUTHORIZED, reason = "Wrong session cookie. Maybe it is expired")
public class AuthorisationException extends RuntimeException{
    private static final Log log = LogFactory.getLog(AuthorisationException.class);
    public AuthorisationException(String message) {
        super(message);
        log.debug("authorisation error: " + message);
    }
}

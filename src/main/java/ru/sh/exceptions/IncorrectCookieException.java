package ru.sh.exceptions;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.UNAUTHORIZED, reason = "Incorrect session cookie. Maybe it is expired")
public class IncorrectCookieException extends EschoolServerException{
    private static final Log log = LogFactory.getLog(IncorrectCookieException.class);
    public IncorrectCookieException(String message) {
        super(message);
        log.debug("incorrect cookie: " + message);
    }
}

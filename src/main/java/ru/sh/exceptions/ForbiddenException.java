package ru.sh.exceptions;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.FORBIDDEN, reason = "Requested action is not allowed")
public class ForbiddenException extends RuntimeException {
    private static final Log log = LogFactory.getLog(ForbiddenException.class);
    public ForbiddenException(String message) {
        super(message);
        log.debug("forbidden: " + message);
    }
}

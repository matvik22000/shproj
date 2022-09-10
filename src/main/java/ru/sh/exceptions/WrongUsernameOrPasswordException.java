package ru.sh.exceptions;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.UNAUTHORIZED, reason = "Wrong username or password")
public class WrongUsernameOrPasswordException extends EschoolServerException {
    private static final Log log = LogFactory.getLog(WrongUsernameOrPasswordException.class);
    public WrongUsernameOrPasswordException(String message) {
        super(message);
        log.debug("wrong username or password: " + message);
    }
}

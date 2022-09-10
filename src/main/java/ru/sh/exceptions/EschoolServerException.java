package ru.sh.exceptions;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_GATEWAY, reason = "Bad answer from Eschool server")
public class EschoolServerException extends RuntimeException{
    private static final Log log = LogFactory.getLog(EschoolServerException.class);
    public EschoolServerException(String message) {
        super(message);
        log.debug("eschool server exception: " + message);
    }

}

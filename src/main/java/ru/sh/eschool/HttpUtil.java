package ru.sh.eschool;

import org.springframework.beans.factory.annotation.Autowired;
import ru.sh.db.Database;
import ru.sh.db.Teacher;
import ru.sh.exceptions.*;
import okhttp3.Response;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;


class HttpUtil {

    private static final Log log = LogFactory.getLog(HttpUtil.class);

    public static void checkLoginStatus(Response response) {
        if (response.code() != 200) {
            if (response.code() == 401 || response.code() == 403) {
                throw new WrongUsernameOrPasswordException("response code " + response.code() + "; message " + response.message());
            } else {
                throw new EschoolServerException("response code " + response.code() + "; message " + response.message());
            }
        }
    }

    public static void checkRequestStatus(Response response) {
        if (response.code() != 200) {
            if (response.code() == 401 || response.code() == 403) {
                throw new IncorrectCookieException("response code " + response.code() + "; message " + response.message());
            } else {
                throw new EschoolServerException("response code " + response.code() + "; message " + response.message());
            }
        }
    }
}

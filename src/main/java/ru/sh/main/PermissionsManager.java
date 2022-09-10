package ru.sh.main;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.sh.db.Database;
import ru.sh.db.Teacher;
import ru.sh.eschool.User;
import ru.sh.exceptions.AuthorisationException;
import ru.sh.exceptions.ForbiddenException;

import javax.servlet.http.HttpSession;
import java.sql.SQLException;

@Component
public class PermissionsManager {
    @Autowired
    private Database db;

    public void checkSession(HttpSession session) {
        if (session.isNew()) {
            session.invalidate();
            throw new AuthorisationException("session is new");
        }
    }

    public void checkTeacher(HttpSession session) throws SQLException {
        this.checkSession(session);
        User user = (User) session.getAttribute("user");

        if (!db.isInTeacherList(user.getPrsId())) {
            session.invalidate();
            throw new ForbiddenException("you aren't a teacher");
        }

    }

    public void checkPrsId(HttpSession session, int prsId) {
        checkSession(session);
        User sessionUser = (User) session.getAttribute("user");
        if (sessionUser.getPrsId() != prsId) {
            session.invalidate();
            throw new ForbiddenException("Forbidden for user: " + prsId + " and session: " + sessionUser.getPrsId());
        }
    }

    public void checkAdminRights(HttpSession session) throws SQLException {
        checkSession(session);
        User user = (User) session.getAttribute("user");
        Teacher teacher = db.getTeacher(user.getPrsId());
        if (!teacher.isAdmin()) {
            session.invalidate();
            throw new ForbiddenException(String.format("Teacher %d %s is not admin ", teacher.getPrsId(), teacher.getFio()));
        }

    }
}

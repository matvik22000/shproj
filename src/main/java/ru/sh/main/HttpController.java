package ru.sh.main;

import org.springframework.web.bind.annotation.*;
import ru.sh.db.*;
import ru.sh.exceptions.EschoolServerException;
import ru.sh.exceptions.WrongUsernameOrPasswordException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import ru.sh.eschool.EschoolServer;
import ru.sh.eschool.User;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.constraints.Null;
import java.io.IOException;
import java.io.PrintWriter;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Date;


@CrossOrigin(allowCredentials = "true")
@Controller
public class HttpController {
    private static final Log log = LogFactory.getLog(HttpController.class);

    @Autowired
    private Database db;

    @Autowired
    private EschoolServer eschoolServer;

    @Autowired
    private PermissionsManager permissionsManager;


    @ResponseBody
    @RequestMapping(path = "/test", produces = "application/json", method = RequestMethod.GET)
    String test(HttpSession session) {
        return "{\"success\": \"true\"}";
    }

    @ResponseBody
    @RequestMapping(path = "/testpost", produces = "application/json", method = RequestMethod.POST)
    String testpost(HttpSession session) {
        return "{\"success\": \"true\"}";
    }

    @ResponseBody
    @RequestMapping(path = "/login", produces = "application/json", method = RequestMethod.GET)
    String login(
            @RequestParam String username,
            @RequestParam String password,
            final HttpServletRequest request
    ) throws IOException {

        HttpSession session = request.getSession();
        if (!session.isNew()) session.invalidate();

        session = request.getSession();
        try {
            User user = eschoolServer.login(username, password);
            session.setAttribute("user", user);
        } catch (WrongUsernameOrPasswordException e) {
            session.invalidate();
            throw e;
        } catch (EschoolServerException e) {
            session.setAttribute("user", new User("error", "error"));
        }
        return "{\"success\": \"true\"}";
    }

    @ResponseBody
    @RequestMapping(path = "/reserve", produces = "application/json", method = RequestMethod.POST)
    String reserve(
            @RequestParam String classNumber,
            @RequestParam int teacherId,
            @RequestParam String reason,
            @RequestParam("startTime") long startTimeInMilliseconds,
            @RequestParam("endTime") long endTimeInMilliseconds,
            @RequestParam(defaultValue = "0") int customerId,
            HttpSession session) throws SQLException {

        permissionsManager.checkPrsId(session, teacherId);
        db.reserveRoom(classNumber, teacherId, reason, new Date(startTimeInMilliseconds), new Date(endTimeInMilliseconds), customerId);
        return "success";
    }

    @ResponseBody
    @RequestMapping(path = "/reserve_period", produces = "application/json", method = RequestMethod.POST)
    String reservePeriod(
            @RequestParam String classNumber,
            @RequestParam int teacherId,
            @RequestParam String reason,
            @RequestParam("startTime") long startTimeInMilliseconds,
            @RequestParam("endTime") long endTimeInMilliseconds,
            @RequestParam int daysCount,
            @RequestParam boolean[] daysOfWeek,
            @RequestParam(defaultValue = "0") int customerId,
            HttpSession session) throws SQLException {


        log.debug(Arrays.toString(daysOfWeek));
        log.debug(daysOfWeek);
        permissionsManager.checkPrsId(session, teacherId);
        db.periodicalReservation(classNumber, teacherId, reason, startTimeInMilliseconds, endTimeInMilliseconds, customerId, daysCount, daysOfWeek);
        return "success";
    }

    @ResponseBody
    @RequestMapping(path = "/schedule_csv", produces = "text/plain;charset=UTF-8", method = RequestMethod.GET)
    void scheduleCsv(@RequestParam("startTime") long startTimeInMilliseconds,
                       @RequestParam("endTime") long endTimeInMilliseconds,
                       HttpServletResponse response) throws SQLException, IOException {
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Content-Disposition","attachment;filename=schedule.csv");
        response.setHeader("Content-encoding","UTF-8");
        String res = db.scheduleToCsv(new Date(startTimeInMilliseconds), new Date(endTimeInMilliseconds));
        PrintWriter out = response.getWriter();
        out.println(res);
        out.flush();
        out.close();

    }

    @ResponseBody
    @RequestMapping(path = "/update_reservation", produces = "application/json", method = RequestMethod.POST)
    String updateReservation(
            @RequestParam int reservationId,
            @RequestParam String classNumber,
            @RequestParam int teacherId,
            @RequestParam String reason,
            @RequestParam("startTime") long startTimeInMilliseconds,
            @RequestParam("endTime") long endTimeInMilliseconds,
            @RequestParam int customerId,
            HttpSession session) throws SQLException {

        int prsId = db.getReservationInfo(reservationId).getTeacherId();
        permissionsManager.checkPrsId(session, prsId);
        db.updateReservationInfo(reservationId, classNumber, teacherId, reason, new Date(startTimeInMilliseconds), new Date(endTimeInMilliseconds), customerId);
        return "success";
    }

    @ResponseBody
    @RequestMapping(path = "/get_reservation_id", produces = "application/json", method = RequestMethod.GET)
    String getReservationId(
            @RequestParam String classNumber,
            @RequestParam("startTime") long startTimeInMilliseconds,
            @RequestParam("endTime") long endTimeInMilliseconds) throws SQLException {

        return Integer.toString(db.getReservationId(classNumber, new Date(startTimeInMilliseconds), new Date(endTimeInMilliseconds)));
    }

    @ResponseBody
    @RequestMapping(path = "/delete_reservation", produces = "application/json", method = RequestMethod.GET)
    String deleteReservation(
            @RequestParam int reservationId,
            HttpSession session) throws SQLException {

        // TODO should customer may delete and change his reservations too?
        permissionsManager.checkPrsId(session, db.getReservationInfo(reservationId).getTeacherId());
        db.deleteReservation(reservationId);
        return "success";
    }

    @ResponseBody
    @RequestMapping(path = "/schedule", produces = "application/json", method = RequestMethod.GET)
    Reservation[] schedule(
            @RequestParam(name = "startTime") long startTimeInMilliseconds,
            @RequestParam(name = "endTime") long endTimeInMilliseconds) throws SQLException {

        return db.getReservations(new Date(startTimeInMilliseconds), new Date(endTimeInMilliseconds));
    }

    @ResponseBody
    @RequestMapping(path = "/add_room", produces = "application/json", method = RequestMethod.POST)
    String addRoom(

            @RequestParam String classNumber,
            @RequestParam int seats,
            @RequestParam int responsible,
            HttpSession session) throws SQLException {

        permissionsManager.checkAdminRights(session);
        db.addRoom(classNumber, seats, responsible);
        return "success";
    }

    @ResponseBody
    @RequestMapping(path = "/delete_room", produces = "application/json", method = RequestMethod.POST)
    String deleteRoom(
            @RequestParam String classNumber,
            HttpSession session) throws SQLException {
        permissionsManager.checkAdminRights(session);
        db.deleteRoom(classNumber);
        return "success";
    }

    @ResponseBody
    @RequestMapping(path = "/change_class_info", produces = "application/json", method = RequestMethod.POST)
    String changeClassInfo(
            @RequestParam String classNumber,
            @RequestParam int seats,
            @RequestParam int responsible,
            HttpSession session) throws SQLException {

        permissionsManager.checkAdminRights(session);
        db.updateRoomInfo(classNumber, seats, responsible);
        return "success";
    }

    @ResponseBody
    @RequestMapping(path = "/add_class_type", produces = "application/json", method = RequestMethod.POST)
    String addClassType(
            @RequestParam String classNumber,
            @RequestParam int classType,
            HttpSession session) throws SQLException {

        permissionsManager.checkAdminRights(session);
        db.addClassType(classNumber, classType);
        return "success";
    }

    @ResponseBody
    @RequestMapping(path = "/remove_class_type", produces = "application/json", method = RequestMethod.POST)
    String removeClassType(
            @RequestParam String classNumber,
            @RequestParam int classType,
            HttpSession session) throws SQLException {

        permissionsManager.checkAdminRights(session);
        db.removeClassType(classNumber, classType);
        return "success";
    }


    @ResponseBody
    @RequestMapping(path = "/update_teachers_list", produces = "application/json", method = RequestMethod.POST)
    String updateTeacherList(HttpSession session) throws IOException, NoSuchAlgorithmException, KeyManagementException, SQLException {

        permissionsManager.checkTeacher(session);
        User user = (User) session.getAttribute("user");
        eschoolServer.updateTeacherList(db, user);
        return "success";
    }

    @ResponseBody
    @RequestMapping(path = "/add_teacher", produces = "application/json", method = RequestMethod.POST)
    String addTeacher(
            @RequestParam int prsId,
            @RequestParam String fio,
            @RequestParam String anotherInfo,
            HttpSession session) throws SQLException {
        permissionsManager.checkAdminRights(session);
        db.addTeacher(prsId, fio, anotherInfo);
        return "success";
    }

    @ResponseBody
    @RequestMapping(path = "/delete_teacher", produces = "application/json", method = RequestMethod.POST)
    String deleteTeacher(
            @RequestParam int prsId,
            HttpSession session) throws SQLException {
        permissionsManager.checkAdminRights(session);
        db.deleteTeacher(prsId);
        return "success";
    }

    @ResponseBody
    @RequestMapping(path = "/get_prs_id", produces = "application/json", method = RequestMethod.GET)
    String getPrsId(HttpSession session) throws IOException, SQLException {

        permissionsManager.checkTeacher(session);
        return Integer.toString(eschoolServer.getPrsId((User) session.getAttribute("user")));
    }

    // TODO add personal settings for users(for example remember teacher's course)
    @ResponseBody
    @RequestMapping(path = "/get_teacher_list", produces = "application/json", method = RequestMethod.GET)
    Teacher[] getTeacherList() throws SQLException {

//        permissionsManager.checkTeacher(session);
//        disabled because client needs teacher list to get fio from id
        return db.getTeachersList();
    }

    @ResponseBody
    @RequestMapping(path = "/get_rooms_list", produces = "application/json", method = RequestMethod.GET)
    Room[] getRoomsList() throws SQLException {
        return db.getRoomsList();
    }

    @ResponseBody
    @RequestMapping(path = "/add_room_type", produces = "application/json", method = RequestMethod.POST)
    String addRoomType(
            @RequestParam String typeDescription,
            HttpSession session
    ) throws SQLException {

        permissionsManager.checkAdminRights(session);
        db.addRoomType(typeDescription);
        return "success";
    }


    @ResponseBody
    @RequestMapping(path = "/get_room_types_list", produces = "application/json", method = RequestMethod.GET)
    RoomType[] getRoomTypesList() throws SQLException {

        return db.getRoomTypesList();
    }

    @ResponseBody
    @RequestMapping(path = "/delete_room_type", produces = "application/json", method = RequestMethod.GET)
    String deleteRoomType(
            @RequestParam int typeId,
            HttpSession session) throws SQLException {

        permissionsManager.checkAdminRights(session);
        db.deleteRoomType(typeId);
        return "success";
    }

    @ResponseBody
    @RequestMapping(path = "/change_type_description", produces = "application/json", method = RequestMethod.POST)
    String changeTypeDescription(
            @RequestParam int typeId,
            @RequestParam String typeDescription,
            HttpSession session) throws SQLException {

        permissionsManager.checkAdminRights(session);
        db.changeRoomType(typeId, typeDescription);
        return "success";
    }

    @ResponseBody
    @RequestMapping(path = "/get_type_description", produces = "application/json", method = RequestMethod.GET)
    RoomType getTypeDescription(
            @RequestParam int typeId
    ) throws SQLException {

        return db.getTypeDescription(typeId);
    }

    @RequestMapping(value = "/doc", method = RequestMethod.GET)
    void doc(HttpServletResponse httpServletResponse) {
        httpServletResponse.setHeader("Location", "/doc/index.html");
        httpServletResponse.setStatus(302);
    }

    @RequestMapping(value = "/web", method = RequestMethod.GET)
    void web(HttpServletResponse httpServletResponse) {
        httpServletResponse.setHeader("Location", "/web/index.html");
        httpServletResponse.setStatus(302);
    }


}

package ru.sh.eschool;

import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.Request;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


public class User {
    private static final Log log = LogFactory.getLog(User.class);
    private String JSESSIONID;
    private String route;
    private ObjectMapper mapper = new ObjectMapper();
    private int prsId;

    public User(String JSESSIONID, String route) {
        this.JSESSIONID = JSESSIONID;
        this.route = route;
    }

    public User(String JSESSIONID, String route, int prsId) {
        this.JSESSIONID = JSESSIONID;
        this.route = route;
        this.prsId = prsId;
    }


    public Request buildRequest(Request.Builder builder) {
        String cookie = String.format("JSESSIONID=%s;route=%s", JSESSIONID, route);
        return builder.addHeader("Cookie", cookie).build();
    }

    public int getPrsId() {
        return prsId;
    }

    public void setPrsId(int prsId) {
        this.prsId = prsId;
    }

    public String getJSESSIONID() {
        return JSESSIONID;
    }

    public void setJSESSIONID(String JSESSIONID) { this.JSESSIONID = JSESSIONID; }

    public String getRoute() {
        return route;
    }

    public void setRoute(String route) { this.route = route; }

}

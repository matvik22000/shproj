package ru.sh.eschool;

import com.fasterxml.jackson.databind.ObjectMapper;
import kotlin.Pair;
import okhttp3.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;
import ru.sh.db.Database;
import ru.sh.exceptions.EschoolServerException;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.Objects;

@Component
public class EschoolServer {

    private static final Log log = LogFactory.getLog(EschoolServer.class);
    private final ObjectMapper mapper = new ObjectMapper();

    public User login(String username, String password) throws IOException {
        OkHttpClient client = new OkHttpClient();
        String url = "https://app.eschool.center/ec-server/login";
        RequestBody body = new FormBody.Builder()
                .add("username", username)
                .add("password", password)
                .build();

        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .header("User-Agent", "Java/9")
                .header("Accept", "text/html, image/gif, image/jpeg, *; q=.2, */*; q=.2")
                .build();
        try (Response response = client.newCall(request).execute()) {
            HttpUtil.checkLoginStatus(response);
            Headers headers = response.headers();
            String[] cookies = new String[2];
            for (Pair<? extends String, ? extends String> o : headers
            ) {
                if (o.getFirst().equals("Set-Cookie")) {
                    String name = o.getSecond().split(";")[0].split("=")[0];
                    String value = o.getSecond().split(";")[0].split("=")[1];
                    if (name.equals("route")) {
                        cookies[1] = value;
                    } else if (name.equals("JSESSIONID")) {
                        cookies[0] = value;
                    }
                }
            }
            User user = new User(cookies[0], cookies[1]);
            user.setPrsId(getPrsId(user));
            return user;
        }
//         catch (java.net.ConnectException exception) {
//            delete this statement (for test only)
//            throw new EschoolServerException(exception.toString());
//        }

    }

    public void updateTeacherList(Database db, User authorised_user) throws IOException, KeyManagementException, NoSuchAlgorithmException {
        OkHttpClient client = new OkHttpClient();
        String url = "https://app.eschool.center/ec-server/usr/olist";
        Request.Builder builder = new Request.Builder()
                .url(url)
                .get()
                .header("User-Agent", "Java/9")
                .header("Accept", "text/html, image/gif, image/jpeg, *; q=.2, */*; q=.2");
        Request request = buildRequest(builder, authorised_user);
        try (Response response = client.newCall(request).execute()) {
            HttpUtil.checkRequestStatus(response);
            String body = Objects.requireNonNull(response.body()).string();
            Person[] users = mapper.readValue(body, Person[].class);
            log.info("updating teacher list");
            for (Person user : users) {
                if (user.getIsEmployee()) {
                    try {
                        db.addTeacher(user.getPrsId(), user.getFio(), "");
                    } catch (SQLException ignored) {
                    }
                }
            }
        }
    }

    public int getPrsId(User user) throws IOException {
        String url_state = "https://app.eschool.center/ec-server/state?menu=false";
        OkHttpClient client = new OkHttpClient();
        Request.Builder builder = new Request.Builder()
                .url(url_state)
                .get()
                .header("User-Agent", "Java/9")
                .header("Accept", "text/html, image/gif, image/jpeg, *; q=.2, */*; q=.2");
        Request request = user.buildRequest(builder);
        try (Response response = client.newCall(request).execute()) {
            HttpUtil.checkRequestStatus(response);
            String body = Objects.requireNonNull(response.body()).string();
            return mapper.readValue(body, State.class).getUser().getPrsId();
        }
    }

    public Request buildRequest(Request.Builder builder, User user) {
        return user.buildRequest(builder);
    }


}

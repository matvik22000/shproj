

import okhttp3.*;

import javax.net.ssl.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class Test2 {
    public static final MediaType JSON
            = MediaType.get("application/json; charset=utf-8");

    public static void main(String[] args) throws IOException, KeyManagementException, NoSuchAlgorithmException {
        final TrustManager[] trustAllCerts = new TrustManager[]{
                new X509TrustManager() {
                    @Override
                    public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                    }

                    @Override
                    public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                    }

                    @Override
                    public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                        return new java.security.cert.X509Certificate[]{};
                    }
                }
        };
        final SSLContext sslContext = SSLContext.getInstance("SSL");
        sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
        // Create an ssl socket factory with our all-trusting manager
        final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.sslSocketFactory(sslSocketFactory, (X509TrustManager) trustAllCerts[0]);
        builder.hostnameVerifier(new HostnameVerifier() {
            @Override
            public boolean verify(String hostname, SSLSession session) {
                return true;
            }
        });
        OkHttpClient client = new OkHttpClient.Builder()
//                .proxy(new Proxy(Proxy.Type.HTTP, new InetSocketAddress("127.0.0.1", 8888)))
                .sslSocketFactory(sslSocketFactory, (X509TrustManager)trustAllCerts[0])
                .hostnameVerifier(new HostnameVerifier() {
                    @Override
                    public boolean verify(String hostname, SSLSession session) {
                        return true;
                    }
                })
                .build();


//        String json = "{\n" +
//                "  \"username\": \"Matvey_gurov\",\n" +
//                "  \"password\": \"d37645bd046b6b890cb4eb18b55d37027b0757c87b98ae4a5083e72b2b60aa70\"\n" +
//                "}";
        connect(" https://app.eschool.center/ec-server/login", "username=Matvey_gurov&password=d37645bd046b6b890cb4eb18b55d37027b0757c87b98ae4a5083e72b2b60aa70", "POST");
        String url = "https://app.eschool.center/ec-server/login";
//        String url = "http://127.0.0.1:5000/add_class";
//        RequestBody body = new FormBody.Builder()
//                .add("classNumber", "123")
//                .add("seats", "12")
//                .add("classType", "1")
//                .add("responsible", "1")
//                .build();
        RequestBody body = new FormBody.Builder()
                .add("username", "Matvey_gurov")
                .add("password", "d37645bd046b6b890cb4eb18b55d37027b0757c87b98ae4a5083e72b2b60aa70")
                .build();

        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .header("User-Agent", "Java/9")
                .header("Accept", "text/html, image/gif, image/jpeg, *; q=.2, */*; q=.2")
                .build();
        try (Response response = client.newCall(request).execute()) {
            System.out.println(response.body().string());
        }
    }


//        Object[] response = connect(" https://app.eschool.center/ec-server/login", "username=Matvey_gurov&password=d37645bd046b6b890cb4eb18b55d37027b0757c87b98ae4a5083e72b2b60aa70", "POST");
//        String body = (String) response[0];
//        System.out.println(body);
//        Object[] responseHeaders = response[1];
//        String[] cookies = response[1].split("] ");
//        for (Object o: cookies) {
//            System.out.println(o);
//        }
//        System.out.println(cookies[8]);
//        String route = String.valueOf(cookies[8].split("route=")[1].split(";")[0];
//        String jSessionId = String.valueOf(cookies[8]).split("ID=")[1].split(";")[0];
//        String url = "http://127.0.0.1:5000/add_class";
//        String query = "{\"classNumber\": \"test123\", \"seats\": 30, \"classType\": 1, \"responsible\": 1}";
//        HttpURLConnection con = (HttpURLConnection) new URL(url).openConnection();
//        con.setRequestMethod("POST");
//        con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
//        con.setDoOutput(true);
//        con.getOutputStream().write(query.getBytes());
//        con.connect();
//        if(con.getResponseCode() != 200) {
//            System.out.println("connect failed, code " + con.getResponseCode() + ", message: " + con.getResponseMessage());
//        }
//        if(con.getInputStream() != null) {
//            BufferedReader rd = new BufferedReader(new InputStreamReader(con.getInputStream()));
//            String line;
//            StringBuilder result = new StringBuilder();
//            while ((line = rd.readLine()) != null) {
//                result.append(line);
//            }
//            rd.close();
//            System.out.println("connect result: " + result.toString());
//        }


    static Object[] connect(String url, String query, String method) throws NoSuchAlgorithmException, KeyManagementException {
        System.out.println("connect " + url/*.replaceAll("https://app.eschool.center", "")*/ + ", query: " + query);
        TrustManager[] trustAllCerts = new TrustManager[]{
                new X509TrustManager() {
                    public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                        return new X509Certificate[0];
                    }

                    public void checkClientTrusted(
                            java.security.cert.X509Certificate[] certs, String authType) {
                    }

                    public void checkServerTrusted(
                            java.security.cert.X509Certificate[] certs, String authType) {
                    }
                }
        };
        SSLContext sc = SSLContext.getInstance("SSL");
        sc.init(null, trustAllCerts, new java.security.SecureRandom());
        HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

        try {
            HttpURLConnection con = (HttpURLConnection) new URL(url).openConnection(
//                    new Proxy(Proxy.Type.HTTP, new InetSocketAddress("127.0.0.1", 8888))
            );
            if (method.equals("GET")) {
                con.setRequestMethod("GET");
            } else {
                con.setRequestMethod("POST");
                con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                con.setDoOutput(true);
                con.getOutputStream().write(query.getBytes());
            }
            con.connect();
            if (con.getResponseCode() != 200) {
                System.out.println("connect failed, code " + con.getResponseCode() + ", message: " + con.getResponseMessage());
                return new String[]{"", ""};
            }
            if (con.getInputStream() != null) {
                BufferedReader rd = new BufferedReader(new InputStreamReader(con.getInputStream()));
                String line;
                StringBuilder result = new StringBuilder();
                while ((line = rd.readLine()) != null) {
                    result.append(line);
                }
                rd.close();
                System.out.println("connect result: " + result.toString());
                System.out.println(con.getResponseMessage());
                Map<String, List<String>> a = con.getHeaderFields();
                Object[] b = a.entrySet().toArray();
                System.out.println(b[8]);
                for (Object o : b
                ) {
                    System.out.println(o);
                }
                String route = String.valueOf(b[8]).split("route=")[1].split(";")[0];
                String jSessionId = String.valueOf(b[8]).split("ID=")[1].split(";")[0];
                Object[] ret = {result.toString(), b};
                return ret;
            } else
                return new Object[]{"", new Object[]{"", ""}};
        } catch (Exception e) {
            throw new RuntimeException("error in request");
        }
    }
}

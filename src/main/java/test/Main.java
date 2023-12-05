package test;

import org.apache.http.Header;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CookieStore;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static String PATTERN = "id=\"adminmenumain\" role=\"navigation\"";
    public static void main(String[] args) {
        CookieStore cookieStore = new BasicCookieStore();

        HttpClientContext context = HttpClientContext.create();
        context.setCookieStore(cookieStore);

        String location = "";

        try (CloseableHttpClient httpClient = HttpClients.custom()
                .build()) {

            HttpGet requestFirst = new HttpGet("http://185.231.68.86/wp-login.php");

            System.out.println("Request to http://185.231.68.86/wp-login.php");
            System.out.println("===========================================================");
            System.out.println("Method: " + requestFirst.getMethod());
            System.out.println("URI: " + requestFirst.getURI());
            System.out.println("===========================================================");

            try (CloseableHttpResponse response = httpClient.execute(requestFirst, context)) {
                Header[] headers = response.getAllHeaders();
                System.out.println("Response from http://185.231.68.86/wp-login.php");
                System.out.println("===========================================================");
                System.out.println(response.getStatusLine() + "\n");
                for (Header header : headers) {
                    System.out.println(header.getName() + ": " + header.getValue());
                    if ("Location".equalsIgnoreCase(header.getName())) {
                        location = header.getValue();
                        break;
                    }
                }
                System.out.println("===========================================================");
            } catch (IOException e) {
                e.printStackTrace();
            }


            HttpPost requestSecond = new HttpPost("http://185.231.68.86/wp-login.php");
            List<NameValuePair> params = new ArrayList<>();
            params.add(new BasicNameValuePair("log", "root"));
            params.add(new BasicNameValuePair("pwd", "urOJWZ*I7&QgwJu%93"));

            requestSecond.setEntity(new UrlEncodedFormEntity(params));

            System.out.println("Request to http://185.231.68.86/wp-login.php");
            System.out.println("===========================================================");
            System.out.println("Method: " + requestSecond.getMethod());
            System.out.println("URI: " + requestSecond.getURI());
            System.out.println("Body: " + EntityUtils.toString(requestSecond.getEntity()));
            System.out.println("===========================================================");

            try (CloseableHttpResponse response = httpClient.execute(requestSecond, context)) {
                Header[] headers = response.getAllHeaders();
                System.out.println("Response from http://185.231.68.86/wp-login.php");
                System.out.println("===========================================================");
                System.out.println(response.getStatusLine() + "\n");
                for (Header header : headers) {
                    System.out.println(header.getName() + ": " + header.getValue());
                    if ("Location".equalsIgnoreCase(header.getName())) {
                        location = header.getValue();
                        break;
                    }
                }
                System.out.println("===========================================================");

                if (response.getStatusLine().getStatusCode() != HttpStatus.SC_MOVED_TEMPORARILY){
                    System.out.println("Неправильный логин или пароль.");
                    return;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            HttpGet request = new HttpGet(location);

            System.out.println("Request to " + location);
            System.out.println("===========================================================");
            System.out.println("Method: " + request.getMethod());
            System.out.println("URI: " + request.getURI());
            System.out.println("===========================================================");

            try (CloseableHttpResponse response = httpClient.execute(request, context)) {
                System.out.println("Response from " + location);
                System.out.println("===========================================================");
                System.out.println(response.getStatusLine() + "\n");
                Header[] headers = response.getAllHeaders();
                for (Header header : headers) {
                    System.out.println(header.getName() + ": " + header.getValue());
                }

                System.out.println("===========================================================");

                String responseBody = EntityUtils.toString(response.getEntity());
                if(!responseBody.contains(PATTERN)){
                    System.out.println("Ошибка аутентификации");
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

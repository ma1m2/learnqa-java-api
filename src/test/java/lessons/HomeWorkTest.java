package lessons;

import io.restassured.RestAssured;
import io.restassured.http.Headers;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.lang.Thread.sleep;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class HomeWorkTest {
    /**
     * Ex9*: Selection password
     */
    @Test
    public void testSelectionPassword(){
        String[] commonPasswords = {"password", "123456", "12345678", "qwerty", "abc123", "monkey", "1234567", "letmein", "trustno1", "dragon"
                , "baseball", "111111", "iloveyou", "master", "sunshine", "ashley", "bailey", "passw0rd", "shadow", "123123"
                , "654321", "superman", "qazwsx", "michael", "Football", "password", "welcome", "jesus", "ninja", "mustang"
                , "password1", "123456789", "adobe123", "admin", "1234567890", "photoshop", "1234", "12345", "princess", "azerty"
                , "000000", "access", "696969", "batman", "1qaz2wsx", "login", "qwertyuiop", "solo", "starwars", "121212"
                , "flower", "hottie", "loveme", "zaq1zaq1", "hello", "freedom", "whatever", "666666", "654321", "!@#$%^&*"
                , "charlie", "aa123456", "donald", "qwerty123", "1q2w3e4r", "555555", "lovely", "7777777", "888888", "123qwe"};
        String wrong = "You are NOT authorized";
        String right = "You are authorized";
        String authCookie;
        String password;

        Map<String, String> authData = new HashMap<>();
        authData.put("login","super_admin");

        for (int i=0; i < commonPasswords.length; i++) {
            password = commonPasswords[i];
            authData.put("password", password);
            Response response = RestAssured
                    .given()
                    .body(authData)
                    .post("https://playground.learnqa.ru/ajax/api/get_secret_password_homework")
                    //.then().log().everything().extract().response();
                    .andReturn();
            authCookie = response.cookie("auth_cookie");
            System.out.println((i + 1) + "   " + authCookie);
            System.out.println("---------------------------------");

            Response responseAuthCookie = RestAssured
                    .given()
                    .cookie(authCookie)
                    .get("https://playground.learnqa.ru/ajax/api/check_auth_cookie")
                    //.then().log().everything().extract().response();
                    .andReturn();
            System.out.println("String in answer: " + responseAuthCookie.asString());

            if (responseAuthCookie.asString().equals(wrong)) {
                System.out.println("=======================================");
            } else {
                System.out.println("Password: " + password);
                System.out.println(responseAuthCookie.asString());
                return;
            }
        }
    }
    /**
     * Ex13: User Agent
     */
    @ParameterizedTest
    @CsvSource({
            "'Mozilla/5.0 (Linux; U; Android 4.0.2; en-us; Galaxy Nexus Build/ICL53F) AppleWebKit/534.30 (KHTML, like Gecko) Version/4.0 Mobile Safari/534.30',Mobile,No,Android",
            //"'Mozilla/5.0 (iPad; CPU OS 13_2 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) CriOS/91.0.4472.77 Mobile/15E148 Safari/604.1',Mobile,Chrome,iOS",
            //"'Mozilla/5.0 (compatible; Googlebot/2.1; +http://www.google.com/bot.html)',Googlebot,Unknown,Unknown",
            "'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.77 Safari/537.36 Edg/91.0.100.0',Web,Chrome,No",
            //"'Mozilla/5.0 (iPad; CPU iPhone OS 13_2_3 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/13.0.3 Mobile/15E148 Safari/604.1',Mobile,No,iPhone"
    })
    public void testUserAgent(String userAgent, String platExp, String browExp, String deviceExp){
        Response response = RestAssured
                .given()
                .header("user-agent",userAgent)
                .get("https://playground.learnqa.ru/ajax/api/user_agent_check")
                .then().log().everything().extract().response();

        String platform = response.jsonPath().getString("platform");
        String browser = response.jsonPath().getString("browser");
        String device = response.jsonPath().getString("device");
        assertEquals(platExp, platform, "Unexpected platform " + platform);
        assertEquals(browExp, browser, "Unexpected platform " + browser);
        assertEquals(deviceExp, device, "Unexpected platform " + device);

    }

    /**
     * Ex12 homework_header
     */
    @Test
    public void testHomeworkHeader(){
        Response response = RestAssured
                .get("https://playground.learnqa.ru/api/homework_header")
                .then().log().everything().extract().response();

        assertEquals(response.getHeaders().getValue("x-secret-homework-header"), "Some secret value", "Unexpected header");
    }
    /**
     * Ex11: homework_cookie
     */
    //https://playground.learnqa.ru/api/homework_cookie
    @Test
    public void testHomeworkCookie(){
        Response response = RestAssured
                .get("https://playground.learnqa.ru/api/homework_cookie")
                .then().log().everything().extract().response();

        System.out.println(response.getCookie("HomeWork"));
        assertEquals(response.getCookie("HomeWork"), "hw_value", "Unexpected cookie");

    }
    /**
     * Ex10: Short phrase
     */
    @ParameterizedTest
    @ValueSource(strings = {"What is your name?", "My name is Svetlana", "What are you?"})
    public void testLengthOfString(String str){
        Assertions.assertTrue(str.length() > 15, "The length of your phrase is less than 16 characters");
    }
    /**
     * Ex8*: Tokens
     */
    @Test
    public void testTokenJson() throws InterruptedException {
        String statusNotReady = "Job is NOT ready";
        String statusReady = "Job is ready";
        Map<String, String> params = new HashMap<>();

        //1. create a job
        JsonPath response = RestAssured
                .get("https://playground.learnqa.ru/ajax/api/longtime_job")
                .jsonPath();

        response.prettyPrint();
        String token = response.get("token");
        int seconds = response.get("seconds");

        params.put("token", token);

        JsonPath response2 = RestAssured
                .given()
                .queryParams(params)
                .get("https://playground.learnqa.ru/ajax/api/longtime_job")
                .jsonPath();

        response2.prettyPrint();

        //2. make sure that status is "Job is NOT ready"
        assertEquals(statusNotReady, response2.get("status"));

        //3. wait specific time
        sleep(1000 * seconds + 1000);

        JsonPath response3 = RestAssured
                .given()
                .queryParams(params)
                .get("https://playground.learnqa.ru/ajax/api/longtime_job")
                .jsonPath();
        response3.prettyPrint();

        //4. make sure that status is "Job is ready" and field "result" is present
        assertEquals(statusReady, response3.get("status"));
        Assertions.assertNotNull(response3.get("result"));
    }

    /**
     * Ex7:* Long Redirect StatusCode 200
     */
    @Test
    public void testLongRedirectStatesCode200(){
        String url = "https://playground.learnqa.ru/api/long_redirect";
        int statusCode = 0;

        while (statusCode != 200){
            Response response = RestAssured
                    .given()
                    .redirects()
                    .follow(false)
                    .get(url)
                    .andReturn();

            statusCode = response.getStatusCode();
            System.out.println(response.getHeader("Location") +
                    " States Code: " +statusCode);
            url = response.getHeader("Location");
        }
    }
    /**
     * Ex6:* Redirect
     */
    @Test
    public void testLongRedirect(){
        Response response = RestAssured
                .given()
                .redirects()
                .follow(false)
                .get("https://playground.learnqa.ru/api/long_redirect")
                .andReturn();

        System.out.println("\n" + response.getHeader("Location"));

    }
    /**
     * Ex5: Parsing JSON, take text of second message
     */
    @Test
    public void testParseJson(){
        JsonPath response = RestAssured
                .get("https://playground.learnqa.ru/api/get_json_homework")
                .jsonPath();

        List<String> messages = response.get("messages.message");
        System.out.println(messages.get(1));
    }
    /**
     * Ex4: GET-запрос
     */
    @Test
    public void testGetText(){
        Response response = RestAssured
                .get("https://playground.learnqa.ru/api/get_text")
                .andReturn();
        response.prettyPrint();
    }
}

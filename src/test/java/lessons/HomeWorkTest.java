package lessons;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.lang.Thread.sleep;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class HomeWorkTest {
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

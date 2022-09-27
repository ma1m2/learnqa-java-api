import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import java.util.List;

public class HomeWorkTest {
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

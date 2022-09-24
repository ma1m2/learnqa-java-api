import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

public class HelloWorldTest {
    @Test
    public void testHelloWorld(){
        System.out.println("Hello from Svetlana!");
    }

    @Test
    public void testGetHello(){
        Map<String, String> params = new HashMap<>();
        params.put("name", "John");

        Response response = RestAssured
                .given()
                .queryParams(params)
                .get("https://playground.learnqa.ru/api/hello")
                //.get("https://playground.learnqa.ru/api/hello?name=Svetlana")
                .andReturn();
        response.prettyPrint();
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

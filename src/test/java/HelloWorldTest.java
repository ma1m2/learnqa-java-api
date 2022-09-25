import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

public class HelloWorldTest {
    /**
     * 2l_07m Status Codes
     */
    @Test
    public void testRedirect(){
        Response response = RestAssured
                .given()
                .redirects()
                .follow(false) //if true statusCode will be 200
                .when()
                .get("https://playground.learnqa.ru/api/get_303")
                .andReturn();

        int statusCode = response.getStatusCode();
        System.out.println(statusCode);
    }
    @Test
    public void testStatusCode(){
        Response response = RestAssured
                .get("https://playground.learnqa.ru/api/something")
                .andReturn();

        int statusCode = response.getStatusCode();
        System.out.println(statusCode);
    }
    /**
     * 2l_06m CheckType
     */
    @Test
    public void testCheckTypePost(){
        Map<String, Object> body = new HashMap<>();
        body.put("param1","value1");
        body.put("param2","value2");

        Response response = RestAssured
                .given()
                .body(body)
                .post("https://playground.learnqa.ru/api/check_type")
                .andReturn();

        response.print();
    }
    @Test
    public void testCheckTypeGet(){
        Map<String, Object> params = new HashMap<>();
        params.put("param1","value1");
        params.put("param2","value2");

        Response response = RestAssured
                .given()
                .queryParams(params)
                .get("https://playground.learnqa.ru/api/check_type")
                .andReturn();

        response.print();
    }

    /**
     * 2l_05m JsonPath
     */
    @Test
    public void testJsonPathNegative(){
        Map<String, String> param = new HashMap<>();
        param.put("name", "Mark");
        JsonPath response = RestAssured
                .given()
                .queryParams(param)
                .get("https://playground.learnqa.ru/api/hello")
                .jsonPath();

        String answer2 = response.get("answer2");
        if(answer2 == null){
            System.out.println("The key 'answer2' is absent!");
        }else {
            System.out.println(answer2);
        }
    }
    @Test
    public void testJsonPath(){
        Map<String, Object> param = new HashMap<>();
        param.put("name", "Mark");
        JsonPath response = RestAssured
                .given()
                .queryParams(param)
                .get("https://playground.learnqa.ru/api/hello")
                .jsonPath();

        String answer = response.get("answer");
        System.out.println(answer);
    }
    @Test
    public void testHelloWorld(){
        System.out.println("Hello from Svetlana!");
    }

    @Test
    public void testGetHello(){
        Map<String, Object> param = new HashMap<>();
        param.put("name", "John");
        Response response = RestAssured
                .given()
                .queryParams(param)
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

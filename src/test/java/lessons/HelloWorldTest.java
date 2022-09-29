package lessons;

import io.restassured.RestAssured;
import io.restassured.http.Headers;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;


public class HelloWorldTest {
    /**
     * 3l_02m Parameterized tests
     */
    @ParameterizedTest
    @ValueSource(strings = {"", "John", "Pete"})
    public void testHelloParameterizedTest(String name){
        Map<String, Object> params = new HashMap<>();
        if(name.length() > 0){
            params.put("name", name);
        }

        JsonPath response = RestAssured
                .given()
                .queryParams(params)
                .get("https://playground.learnqa.ru/api/hello")
                .jsonPath();
        String answer = response.getString("answer");
        String expectedName = (name.length() > 0) ? name : "someone";
        assertEquals("Hello, " + expectedName, answer, "The answer is not expected");
        response.prettyPrint();
    }
    @Test
    public void testHelloWithOutName(){
        JsonPath response = RestAssured
                .get("https://playground.learnqa.ru/api/hello")
                .andReturn()
                .jsonPath();
        String answer = response.getString("answer");
        assertEquals("Hello, someone", answer, "The answer is not expected");
        response.prettyPrint();
    }
    @Test
    public void testHelloWithName(){
        Map<String, Object> param = new HashMap<>();
        param.put("name", "John");
        JsonPath response = RestAssured
                .given()
                .queryParams(param)
                .get("https://playground.learnqa.ru/api/hello")
                .jsonPath();
        String answer = response.getString("answer");
        assertEquals("Hello, " + param.get("name"), answer, "The answer is not expected");
        response.prettyPrint();
    }

    /**
     * 3l_01m JUnit and simple tests
     */
    @Test
    public void testJUnit200(){
        Response response = RestAssured
                .get("https://playground.learnqa.ru/api/map")
                .andReturn();
        assertEquals(200, response.getStatusCode(), "Unexpected States Code");
    }
    @Test
    public void testJUnit404(){
        Response response = RestAssured
                .get("https://playground.learnqa.ru/api/map2")
                .andReturn();
        assertEquals(404, response.getStatusCode(), "Unexpected States Code");
    }

    /**
     * 2l_09m Cookies
     */
    @Test
    public void testCheckCookiesWrongLogin(){
        Map<String, String> data = new HashMap<>();
        data.put("login", "secret_login_1");
        data.put("password", "secret_pass");
        Response responseForGet = RestAssured
                .given()
                .body(data)
                .when()
                .post("https://playground.learnqa.ru/api/get_auth_cookie")
                .andReturn();

        String responseCookie = responseForGet.getCookie("auth_cookie");
        System.out.println(responseCookie);

        Map<String, String> cookies = new HashMap<>();
        cookies.put("auth_cookie", responseCookie);
        Response responseForCheck = RestAssured
                .given()
                //.body(data)
                .cookies(cookies)
                .when()
                .post("https://playground.learnqa.ru/api/check_auth_cookie")
                .andReturn();

        responseForCheck.print();
    }
    @Test
    public void testCheckCookies(){
        Map<String, String> data = new HashMap<>();
        data.put("login", "secret_login");
        data.put("password", "secret_pass");
        Response responseForGet = RestAssured
                .given()
                .body(data)
                .when()
                .post("https://playground.learnqa.ru/api/get_auth_cookie")
                .andReturn();

        String responseCookie = responseForGet.getCookie("auth_cookie");
        System.out.println(responseCookie);

        Map<String, String> cookies = new HashMap<>();
        cookies.put("auth_cookie", responseCookie);
        Response responseForCheck = RestAssured
                .given()
                //.body(data)
                .cookies(cookies)
                .when()
                .post("https://playground.learnqa.ru/api/check_auth_cookie")
                .andReturn();

        responseForCheck.print();
    }
    @Test
    public void testWrongLogin(){
        Map<String, String> data = new HashMap<>();
        data.put("login", "secret_login1");
        data.put("password", "secret_pass");
        Response response = RestAssured
                .given()
                .body(data)
                .when()
                .post("https://playground.learnqa.ru/api/get_auth_cookie")
                .andReturn();

        System.out.println("\nPretty text");
        response.prettyPrint();
        System.out.println("\nHeaders");
        System.out.println(response.getHeaders());
        System.out.println("\nCookies");
        System.out.println(response.getCookies());
        System.out.println(response.getCookie("auth_cookie"));
    }
    @Test
    public void testCookies(){
        Map<String, String> data = new HashMap<>();
        data.put("login", "secret_login");
        data.put("password", "secret_pass");
        Response response = RestAssured
                .given()
                .body(data)
                .when()
                .post("https://playground.learnqa.ru/api/get_auth_cookie")
                .andReturn();

        System.out.println("\nPretty text");
        response.prettyPrint();
        System.out.println("\nHeaders");
        System.out.println(response.getHeaders());
        System.out.println("\nCookies");
        System.out.println(response.getCookies());
        System.out.println(response.getCookie("auth_cookie"));
    }
    /**
     * 2l_08m Headers and Location
     */
    @Test
    public void testLocationHeader(){
        Response response = RestAssured
                .given()
                .redirects()
                .follow(false)
                .when()
                .get("https://playground.learnqa.ru/api/get_303")
                .andReturn();

        response.print();
        String locationHeader = response.header("Location");
        System.out.println(locationHeader);
    }
    @Test
    public void testHeaders(){
        Map<String, String> headers = new HashMap<>();
        headers.put("myHeader1","myValue1");
        headers.put("myHeader2","myValue2");

        Response response = RestAssured
                .given()
                .headers(headers)
                .when()
                .get("https://playground.learnqa.ru/api/show_all_headers")
                .andReturn();

        response.prettyPrint();//headers in Request

        Headers responseHeader = response.getHeaders();//headers in Response
        System.out.println(responseHeader);
    }
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
/*        if(answer2 != null){
            System.out.println(answer2);
        }else {
            System.out.println("The key 'answer2' is absent!");
        }*/
        System.out.println(answer2);
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
}

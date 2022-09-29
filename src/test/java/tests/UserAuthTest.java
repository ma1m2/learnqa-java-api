package tests;

import io.restassured.RestAssured;
import io.restassured.http.Headers;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import lib.BaseTestCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UserAuthTest extends BaseTestCase {
    /**
     * 3l_05m @BeforeEach
     */

    public String cookie;
    public String header;
    public int userIdOnAuth;

    @BeforeEach
    public void loginUser(){
        Map<String,String> authData = new HashMap<>();
        authData.put("email", "vinkotov@example.com");
        authData.put("password", "1234");

        Response responseGetAuth = RestAssured
                .given()
                .body(authData)
                .post("https://playground.learnqa.ru/api/user/login")
                .then().log().everything().extract().response();


        this.cookie = this.getCookie(responseGetAuth, "auth_sid");
        this.header = this.getHeader(responseGetAuth, "x-csrf-token");
        this.userIdOnAuth = getIntFromJson(responseGetAuth, "user_id");
    }

    @Test
    public void testAuthUser(){
        JsonPath responseCheckAuth = RestAssured
                .given()
                .header("x-csrf-token", this.header)
                .cookie("auth_sid", this.cookie)
                .get("https://playground.learnqa.ru/api/user/auth")
                .jsonPath();

        responseCheckAuth.prettyPrint();
        int userIdOnCheck = responseCheckAuth.getInt("user_id");
        assertEquals(userIdOnAuth, userIdOnCheck, "Unexpected user ID " + userIdOnCheck);
    }

    @ParameterizedTest
    @ValueSource(strings = {"cookie", "header"})
    public void testNegativeAuthUser(String condition){
        RequestSpecification spec = RestAssured.given();
        spec.baseUri("https://playground.learnqa.ru/api/user/auth");

        if(condition.equals("cookie")){
            spec.cookie("auth_sid", this.cookie);
        } else if (condition.equals("header")) {
            spec.header("x-csrf-token", this.header);
        }else {
            throw new IllegalArgumentException("Condition value is known: " + condition);
        }

        JsonPath responseForCheck = spec.get().jsonPath();
        assertEquals(0, responseForCheck.getInt("user_id"), "Urer ID should be 0 for unauth user");

        responseForCheck.prettyPrint();

    }

}

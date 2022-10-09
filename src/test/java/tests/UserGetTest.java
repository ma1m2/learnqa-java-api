package tests;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import lib.BaseTestCase;
import lib.MyAssertions;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

public class UserGetTest extends BaseTestCase {
    /**
     * /l4m3-get_user
     */
    @Test
    public void testGetUserDataNotAuth(){
        Response response = RestAssured
                .get("https://playground.learnqa.ru/api/user/2")
                .andReturn();
        System.out.println(response.asString());
        //"username": "Vitaliy" firstName lastName email
        MyAssertions.assertJsonHasField(response, "username");
        MyAssertions.assertJsonNotField(response, "firstName");
        MyAssertions.assertJsonNotField(response, "lastName");
        MyAssertions.assertJsonNotField(response, "email");
    }

    @Test
    public void testGetUserDetailAuthUser(){
        Map<String, String> authData = new HashMap<>();
        authData.put("email", "vinkotov@example.com");
        authData.put("password", "1234");

        Response responseGetAuth = RestAssured
                .given()
                .body(authData)
                .post("https://playground.learnqa.ru/api/user/login")
                .andReturn();
        System.out.println(responseGetAuth.asString());

        String header = getHeader(responseGetAuth, "x-csrf-token");
        String cookie = getCookie(responseGetAuth, "auth_sid");

        Response responseUserData = RestAssured
                .given()
                .header("x-csrf-token",header)
                .cookie("auth_sid", cookie)
                .get("https://playground.learnqa.ru/api/user/2")
                .andReturn();
        System.out.println(responseUserData.asString());

        String[] expectedFields = {"username", "firstName", "lastName", "email"};
        MyAssertions.assertJsonHasFields(responseUserData, expectedFields);
    }


}

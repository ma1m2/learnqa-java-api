package tests;

import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import lib.BaseTestCase;
import lib.MyAssertions;
import lib.ApiCoreRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

@Epic("Getting user cases")
@Feature("Get user")
public class UserGetTest extends BaseTestCase {
    private String header;
    private String cookie;
    private final  ApiCoreRequest apiCoreRequest = new ApiCoreRequest();

    /**
     * Ex16
     */
    @Description("This test get user data by id=4 with authorization like user id=2")
    @DisplayName("Test negative get user data")
    @Test
    public void testGetOtherUserDetailAuthUser(){
        Map<String, String> authData = new HashMap<>();
        authData.put("email", "vinkotov@example.com");
        authData.put("password", "1234");

        Response responseGetAuth = apiCoreRequest
                .makePostRequestAuthData("https://playground.learnqa.ru/api/user/login", authData);

        System.out.println(responseGetAuth.asString());
        System.out.println("------------------");

        header = getHeader(responseGetAuth, "x-csrf-token");
        cookie = getCookie(responseGetAuth, "auth_sid");

        Response responseUserData = apiCoreRequest
                .makeGetRequest("https://playground.learnqa.ru/api/user/1", header, cookie);

        System.out.println(responseUserData.asString());

        MyAssertions.assertJsonHasField(responseUserData, "username");
        MyAssertions.assertJsonNotField(responseUserData, "firstName");
        MyAssertions.assertJsonNotField(responseUserData, "lastName");
        MyAssertions.assertJsonNotField(responseUserData, "email");
    }
    /**
     * /l4m3-get_user
     */
    @Description("This test get username by id=2 without authorization")
    @DisplayName("Test positive get username")
    @Test
    public void testGetUserDataNotAuth(){
        Response response = apiCoreRequest.makeGetRequestById("https://playground.learnqa.ru/api/user/2");

        System.out.println(response.asString());
        //"username": "Vitaliy" firstName lastName email
        MyAssertions.assertJsonHasField(response, "username");
        MyAssertions.assertJsonNotField(response, "firstName");
        MyAssertions.assertJsonNotField(response, "lastName");
        MyAssertions.assertJsonNotField(response, "email");
    }

    @Description("This test get user data by id=2 with authorization")
    @DisplayName("Test positive get user data")
    @Test
    public void testGetUserDetailAuthUser(){
        Map<String, String> authData = new HashMap<>();
        authData.put("email", "vinkotov@example.com");
        authData.put("password", "1234");

        Response responseGetAuth = apiCoreRequest
                .makePostRequestAuthData("https://playground.learnqa.ru/api/user/login", authData);

        System.out.println(responseGetAuth.asString());
        System.out.println("------------------");

        header = getHeader(responseGetAuth, "x-csrf-token");
        cookie = getCookie(responseGetAuth, "auth_sid");

        Response responseUserData = apiCoreRequest
                .makeGetRequest("https://playground.learnqa.ru/api/user/2", header, cookie);

        System.out.println(responseUserData.asString());

        String[] expectedFields = {"username", "firstName", "lastName", "email"};
        MyAssertions.assertJsonHasFields(responseUserData, expectedFields);
    }
}

package tests;

import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.restassured.RestAssured;
import io.restassured.http.Header;
import io.restassured.response.Response;
import lib.ApiCoreRequest;
import lib.BaseTestCase;
import lib.DataGenerator;
import lib.MyAssertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

@Epic("Delete User cases")
@Feature("Deleting user")
public class UserDeleteTest extends BaseTestCase {
    private final ApiCoreRequest apiCoreRequest = new ApiCoreRequest();
    private String cookie;
    private String header;
    private String userId;


    @Description("Try to delete first user while being authorized by another user")
    @DisplayName("Test negative delete non auth user")
    @Test
    public void testDeleteNonAuthUser(){
        //Generate User
        Map<String,String> userData = DataGenerator.getRegistrationData();
        userData.put("username", "tryToDeleteThisUser");

        Response response = apiCoreRequest
                .makePostRequestUserData(userData,"https://playground.learnqa.ru/api/user/");

        userId = response.jsonPath().getString("id");

        System.out.println(userId);
        System.out.println("--------------------------");

        //Generate Another User
        Map<String,String> anotherUserData = DataGenerator.getRegistrationData();
        Response anotherUserResponse = apiCoreRequest
                .makePostRequestUserData(anotherUserData,"https://playground.learnqa.ru/api/user/");

        String anotherUserId = anotherUserResponse.jsonPath().getString("id");

        System.out.println(anotherUserId);
        System.out.println("--------------------------");

        //Login Another user
        Map<String, String> anotherAuthData = new HashMap<>();
        anotherAuthData.put("email", anotherUserData.get("email"));
        anotherAuthData.put("password", anotherUserData.get("password"));

        Response responseAuth = apiCoreRequest
                .makePostRequestAuthData("https://playground.learnqa.ru/api/user/login", anotherAuthData);

        header = this.getHeader(responseAuth, "x-csrf-token");
        cookie = this.getCookie(responseAuth, "auth_sid");

        System.out.println(responseAuth.asString());
        System.out.println("--------------------------");

        //Try to delete first user
        Response responseDelete = apiCoreRequest.makeDeleteRequest(header, cookie, userId);

        //Check first user. It is existing
        Response responseGetUser = apiCoreRequest.makeGetRequestById("https://playground.learnqa.ru/api/user/" + userId);

        System.out.println(responseGetUser.asString());
        MyAssertions.assertJsonByName(responseGetUser, "username", userData.get("username"));
    }

    @Description("Delete new auth user")
    @DisplayName("Test positive delete new user")
    @Test
    public void testDeleteAuthUser(){
        //Generate User
        Map<String,String> userData = DataGenerator.getRegistrationData();
        Response response = apiCoreRequest
                .makePostRequestUserData(userData,"https://playground.learnqa.ru/api/user/");

        userId = response.jsonPath().getString("id");

        System.out.println(userId);

        //Login
        Map<String, String> authData = new HashMap<>();
        authData.put("email", userData.get("email"));
        authData.put("password", userData.get("password"));

        Response responseAuth = apiCoreRequest
                .makePostRequestAuthData("https://playground.learnqa.ru/api/user/login", authData);

        header = this.getHeader(responseAuth, "x-csrf-token");
        cookie = this.getCookie(responseAuth, "auth_sid");

        System.out.println(responseAuth.asString());

        //Delete this user
        Response responseDelete = apiCoreRequest.makeDeleteRequest(header,cookie, userId);

        //Check this user by id
        Response responseGetUser = apiCoreRequest.makeGetRequestById("https://playground.learnqa.ru/api/user/" + userId);

        MyAssertions.assertResponseTextEquals(responseGetUser, "User not found");
    }

    @Description("Try to delete user with id=2")
    @DisplayName("Test negative delete user id=2")
    @Test
    public void testDeleteId2user(){
        //Login
        Map<String,String> authData = new HashMap<>();
        authData.put("email", "vinkotov@example.com");
        authData.put("password", "1234");

        Response responseLogin = apiCoreRequest
                .makePostRequestAuthData("https://playground.learnqa.ru/api/user/login", authData);

        System.out.println(responseLogin.asString());

        header = this.getHeader(responseLogin, "x-csrf-token");
        cookie = this.getCookie(responseLogin, "auth_sid");
        userId = Integer.toString(getIntFromJson(responseLogin, "user_id"));

        Response responseDelete = apiCoreRequest.makeDeleteRequest(header, cookie, userId);

        System.out.println(responseDelete.asString());
        MyAssertions.assertResponseTextEquals(responseDelete
                , "Please, do not delete test users with ID 1, 2, 3, 4 or 5.");

    }
}

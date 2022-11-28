package tests;

import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import lib.BaseTestCase;
import lib.DataGenerator;
import lib.MyAssertions;
import lib.ApiCoreRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

@Epic("Edit user cases")
@Feature("Editing")
public class UserEditTest extends BaseTestCase {

    private final  ApiCoreRequest apiCoreRequest = new ApiCoreRequest();
    private String cookie;
    private String header;
    private String userId;

    @Description("Edit First Name authorized user with too short name One Symbol")
    @DisplayName("Test negative edit user first name with One Symbol")
    @Test
    public void testEditFirstNameOnOneSymbol(){
        //Generate User
        Map<String,String> userData = DataGenerator.getRegistrationData();
        Response response = apiCoreRequest
                .makePostRequestUserData(userData,"https://playground.learnqa.ru/api/user/");

        userId = response.jsonPath().getString("id");

        System.out.println(userId);
        System.out.println("--------------------------");

        //Login
        Map<String, String> authData = new HashMap<>();
        authData.put("email", userData.get("email"));
        authData.put("password", userData.get("password"));

        Response responseAuth = apiCoreRequest
                .makePostRequestAuthData("https://playground.learnqa.ru/api/user/login", authData);

        System.out.println(responseAuth.asString());
        System.out.println("--------------------------");

        //Edit User
        String newFirstName = "L";
        Map<String,String> editData = new HashMap<>();
        editData.put("firstName", newFirstName);

        header = this.getHeader(responseAuth, "x-csrf-token");
        cookie = this.getCookie(responseAuth, "auth_sid");

        Response responseEdit = apiCoreRequest
                .makePutRequest(header, cookie, editData, "https://playground.learnqa.ru/api/user/" + userId);

        System.out.println(responseEdit.asString());
        MyAssertions.assertJsonByName(responseEdit, "error", "Too short value for field firstName");
    }

    @Description("Edit email authorized user with new email without '@'")
    @DisplayName("Test negative edit user email without '@'")
    @Test
    public void testEditEmailWithoutAt(){
        //Generate User
        Map<String,String> userData = DataGenerator.getRegistrationData();
        Response response = apiCoreRequest
                .makePostRequestUserData(userData,"https://playground.learnqa.ru/api/user/");

        userId = response.jsonPath().getString("id");

        System.out.println(userId);
        System.out.println("--------------------------");

        //Login
        Map<String, String> authData = new HashMap<>();
        authData.put("email", userData.get("email"));
        authData.put("password", userData.get("password"));

        Response responseAuth = apiCoreRequest
                .makePostRequestAuthData("https://playground.learnqa.ru/api/user/login", authData);

        System.out.println(responseAuth.asString());
        System.out.println("--------------------------");

        //Edit User
        String email = "usernewemailOexample.com";
        Map<String,String> editData = new HashMap<>();
        editData.put("email", email);

        header = this.getHeader(responseAuth, "x-csrf-token");
        cookie = this.getCookie(responseAuth, "auth_sid");

        Response responseEdit = apiCoreRequest
                .makePutRequest(header, cookie, editData, "https://playground.learnqa.ru/api/user/" + userId);

        System.out.println(responseEdit.asString());
        MyAssertions.assertResponseTextEquals(responseEdit, "Invalid email format");
    }

    @Description("Try to edit user data while being authorized by another user")
    @DisplayName("Test negative edit first user")
    @Test
    public void testEditDataAuthOtherUser(){
        //Generate User
        Map<String,String> userData = DataGenerator.getRegistrationData();
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

        //Edit first User
        String newFirstName = "ChangedName";
        Map<String,String> editData = new HashMap<>();
        editData.put("firstName", newFirstName);

        Response responseEdit = apiCoreRequest
                .makePutRequest(header, cookie, editData, "https://playground.learnqa.ru/api/user/" + userId);

        System.out.println(responseEdit.asString());
        System.out.println("--------------------------");
        MyAssertions.assertResponseTextEquals(responseEdit, "");
    }

    @Description("Edit first name of non-auth user")
    @DisplayName("Test negative edit user")
    @Test
    public void testEditNonAuthUser(){
        //Generate User
        Map<String,String> userData = DataGenerator.getRegistrationData();
        Response response = apiCoreRequest
                .makePostRequestUserData(userData,"https://playground.learnqa.ru/api/user/");

        userId = response.jsonPath().getString("id");

        System.out.println(userId);

        //Edit User
        String newFirstName = "ChangedName";
        Map<String,String> editData = new HashMap<>();
        editData.put("firstName", newFirstName);

        Response responseEdit = apiCoreRequest
                .makePutRequestNoAuth(editData, "https://playground.learnqa.ru/api/user/" + userId);

        System.out.println(responseEdit.asString());
        MyAssertions.assertResponseTextEquals(responseEdit, "Auth token not supplied");

        //Get User
        Response responseNonAuthUser = apiCoreRequest
                .makeGetRequestById("https://playground.learnqa.ru/api/user/" + userId);

        System.out.println(responseNonAuthUser.asString());
        MyAssertions.assertJsonByName(responseNonAuthUser,"username", userData.get("username"));
    }

    /**
     * l4m4-change_user
     */
    //we cannot edit users with id < 10
    @Description("Edit FirstName's user")
    @DisplayName("Test positive edit user")
    @Test
    public void testEditJustCreatedUser(){
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

        System.out.println(responseAuth.asString());

        //Edit User
        String newFirstName = "ChangedName";
        Map<String,String> editData = new HashMap<>();
        editData.put("firstName", newFirstName);

        header = this.getHeader(responseAuth, "x-csrf-token");
        cookie = this.getCookie(responseAuth, "auth_sid");

        Response responseEdit = apiCoreRequest
                .makePutRequest(header, cookie, editData, "https://playground.learnqa.ru/api/user/" + userId);

        //Get User
        Response responseUserData = apiCoreRequest
                .makeGetRequest("https://playground.learnqa.ru/api/user/" + userId, header, cookie);

        System.out.println(responseUserData.asString());

        MyAssertions.assertJsonByName(responseUserData,"firstName", newFirstName);
    }
}

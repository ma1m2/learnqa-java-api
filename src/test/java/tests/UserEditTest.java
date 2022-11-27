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

package tests;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import lib.BaseTestCase;
import lib.DataGenerator;
import lib.MyAssertions;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

public class UserEditTest extends BaseTestCase {
    /**
     * l4m4-change_user
     */

    //we cannot edit users with id < 10

    @Test
    public void testEditJustCreatedUser(){
        //Generate User
        Map<String,String> userData = DataGenerator.getRegistrationData();
        JsonPath response = RestAssured
                .given()
                .body(userData)
                .post("https://playground.learnqa.ru/api/user/")
                .jsonPath();
        String userId = response.getString("id");
        System.out.println(userId);

        //Login
        Map<String, String> authData = new HashMap<>();
        authData.put("email", userData.get("email"));
        authData.put("password", userData.get("password"));

        Response responseAuth = RestAssured
                .given()
                .body(authData)
                .post("https://playground.learnqa.ru/api/user/login")
                .andReturn();

        //Edit User
        String newFirstName = "ChangedName";
        Map<String,String> editData = new HashMap<>();
        editData.put("firstName", newFirstName);

        Response responseEdit = RestAssured
                .given()
                .header("x-csrf-token", this.getHeader(responseAuth, "x-csrf-token"))
                .cookie("auth_sid",this.getCookie(responseAuth, "auth_sid"))
                .body(editData)
                .put("https://playground.learnqa.ru/api/user/" + userId)
                .andReturn();

        //Get User
        Response responseUserData = RestAssured
                .given()
                .header("x-csrf-token", this.getHeader(responseAuth, "x-csrf-token"))
                .cookie("auth_sid",this.getCookie(responseAuth, "auth_sid"))
                .get("https://playground.learnqa.ru/api/user/" + userId)
                .andReturn();

        System.out.println(responseUserData.asString());
        MyAssertions.assertJsonByName(responseUserData,"firstName", newFirstName);
    }
}

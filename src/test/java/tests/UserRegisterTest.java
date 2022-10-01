package tests;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import lib.BaseTestCase;
import lib.MyAssertions;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

public class UserRegisterTest extends BaseTestCase {
    @Test
    public void testCreateUserWithExistingEmail(){
        String email = "vinkotov@example.com";
        Map<String,String> userData = new HashMap<>();
        userData.put("email", email);
        userData.put("password", "123");
        userData.put("username", "learnqa");
        userData.put("firstName", "learnqa");
        userData.put("lastName", "learnqa");

        Response response = RestAssured
                .given()
                .body(userData)
                .post("https://playground.learnqa.ru/api/user/")
                .andReturn();
        MyAssertions.assertResponseTextEquals(response, "Users with email '"+ email +"' already exists");
        MyAssertions.assertResponseStatusCodeEquals(response,400);
    }

}

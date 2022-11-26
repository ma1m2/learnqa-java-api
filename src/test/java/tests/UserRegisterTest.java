package tests;

import io.qameta.allure.*;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import lib.BaseTestCase;
import lib.DataGenerator;
import lib.MyAssertions;
import lib.ApiCoreRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

@Epic("Registration user cases")
@Feature("Registration")
public class UserRegisterTest extends BaseTestCase {
    ApiCoreRequest apiCoreRequest = new ApiCoreRequest();
    @Description("Create user with exist Email 'vinkotov@example.com'")
    @DisplayName("Test negative create user")
    @Test
    public void testCreateUserWithExistingEmail(){
        String email = "vinkotov@example.com";
        Map<String,String> userData = new HashMap<>();
        userData.put("email", email);
        userData = DataGenerator.getRegistrationData(userData);

        Response response = apiCoreRequest.makePostRequestUserData(userData, "https://playground.learnqa.ru/api/user/");

        MyAssertions.assertResponseTextEquals(response, "Users with email '"+ email +"' already exists");
        MyAssertions.assertResponseStatusCodeEquals(response,400);
    }

    @Description("Create user with default data")
    @DisplayName("Test positive create user")
    @Test
    public void testCreateUserSuccessful(){
        Map<String,String> userData = DataGenerator.getRegistrationData();

        Response response = apiCoreRequest.makePostRequestUserData(userData, "https://playground.learnqa.ru/api/user/");

        response.prettyPrint();
        MyAssertions.assertResponseStatusCodeEquals(response,200);
        MyAssertions.assertJsonHasField(response,"id");
    }


}

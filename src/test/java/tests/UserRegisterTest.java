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
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.HashMap;
import java.util.Map;

@Epic("Registration user cases")
@Feature("Registration")
public class UserRegisterTest extends BaseTestCase {
    private final  ApiCoreRequest apiCoreRequest = new ApiCoreRequest();

    @Description("Create user without One Field")
    @DisplayName("Test negative create user")
    @ParameterizedTest
    @ValueSource(strings = {"email", "password", "username", "firstName", "lastName"})
    public void testCreateUserWithoutOneField(String field){
        Map<String,String> userData = DataGenerator.getRegistrationData();
        userData.remove(field);

        Response response = apiCoreRequest.makePostRequestUserData(userData, "https://playground.learnqa.ru/api/user/");

        MyAssertions.assertResponseTextEquals(response, "The following required params are missed: "+ field);
        MyAssertions.assertResponseStatusCodeEquals(response,400);
    }
    @Description("Create user with Name 251 symbols")
    @DisplayName("Test negative create user '251'")
    @Test
    public void testCreateUserWithTooLongName(){
        String username = "Fjklklkjlkjlkjljlkjlkjlkjlkjlkjlkjljsdfskjfskjfsdfksafaksfsfsdfkjlkjljlkjlkjlkjlkjlkjlkjljsdfskjfskjfsdfksafaksfsfsdfkjlkjljlkjlkjlkjlkjlkjlkjljsdfskjfskjfsdfksafaksfsfsdfkjlkjljlkjlkjlkjlkjlkjlkjljsdfskjfskjfsdfksafaksfsfsdfkjlkjljlkjlkjlkjlkjlkjlkjl";
        Map<String,String> userData = new HashMap<>();
        userData.put("username", username);
        userData = DataGenerator.getRegistrationData(userData);

        Response response = apiCoreRequest.makePostRequestUserData(userData, "https://playground.learnqa.ru/api/user/");

        MyAssertions.assertResponseTextEquals(response, "The value of 'username' field is too long");
        MyAssertions.assertResponseStatusCodeEquals(response,400);
    }
    @Description("Create user with One symbol Name")
    @DisplayName("Test negative create user 'A'")
    @Test
    public void testCreateUserWithShortName(){
        String username = "A";
        Map<String,String> userData = new HashMap<>();
        userData.put("username", username);
        userData = DataGenerator.getRegistrationData(userData);

        Response response = apiCoreRequest.makePostRequestUserData(userData, "https://playground.learnqa.ru/api/user/");

        MyAssertions.assertResponseTextEquals(response, "The value of 'username' field is too short");
        MyAssertions.assertResponseStatusCodeEquals(response,400);
    }

    @Description("Create user with Email without '@'")
    @DisplayName("Test negative create user '@'")
    @Test
    public void testCreateUserWithEmailWithoutAt(){
        String email = "vinkotOexample.com";
        Map<String,String> userData = new HashMap<>();
        userData.put("email", email);
        userData = DataGenerator.getRegistrationData(userData);

        Response response = apiCoreRequest.makePostRequestUserData(userData, "https://playground.learnqa.ru/api/user/");

        MyAssertions.assertResponseTextEquals(response, "Invalid email format");
        MyAssertions.assertResponseStatusCodeEquals(response,400);
    }

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

    @Description("Create user with default user data")
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

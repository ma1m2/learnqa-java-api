package lib;

import io.qameta.allure.Step;
import static io.restassured.RestAssured.given;

import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.http.Header;
import io.restassured.response.Response;

import java.util.Map;

public class ApiCoreRequest {
    //for class UserAuthTest testAuthUser()
    @Step("Make a Get-request whit token and cookie")
    public Response makeGetRequest(String url, String token, String cookie){
        return given()
                .filter(new AllureRestAssured())
                .header(new Header("x-csrf-token", token))
                .cookie("auth_sid", cookie)
                .get(url)
                .andReturn();
    }

    //for class UserAuthTest testNegativeAuthUser
    @Step("Make a Get-request whit cookie only")
    public Response makeGetRequestWithCookie(String url, String cookie){
        return given()
                .filter(new AllureRestAssured())
                .cookie("auth_sid", cookie)
                .get(url)
                .andReturn();
    }

    //for class UserAuthTest testNegativeAuthUser
    @Step("Make a Get-request whit token only")
    public Response makeGetRequestWithToken(String url, String token){
        return given()
                .filter(new AllureRestAssured())
                .header(new Header("x-csrf-token", token))
                .get(url)
                .andReturn();
    }

    //for class UserAuthTest loginUser()
    @Step("Make a Post-request whit authData")
    public Response makePostRequest(String url, Map<String, String> authData){
        return given()
                .filter(new AllureRestAssured())
                .body(authData)
                .post(url)
                .andReturn();
    }

    //for class UserRegisterTest testCreateUserSuccessful(), testCreateUserWithExistingEmail()
    @Step("Mfke a Post-request with userData")
    public Response makePostRequestUserData(Map<String,String> userData, String url){
        return given()
                .filter(new AllureRestAssured())
                .body(userData)
                .post(url)
                .andReturn();
    }

}

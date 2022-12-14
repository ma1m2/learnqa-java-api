package lib;

import io.qameta.allure.Step;
import static io.restassured.RestAssured.given;

import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.http.Header;
import io.restassured.response.Response;

import java.util.Map;

public class ApiCoreRequest {
    //for class UserDeleteTest
    @Step("Make a Delete-request")
    public Response makeDeleteRequest(String token, String cookie, String userID){
        return given()
                .filter(new AllureRestAssured())
                .header(new Header("x-csrf-token", token))
                .cookie("auth_sid", cookie)
                .delete("https://playground.learnqa.ru/api/user/" + userID);
    }


    @Step("Make a Put-request no Auth token")
    public Response makePutRequestNoAuth(Map<String,String> editData, String url){
        return given()
                .filter(new AllureRestAssured())
                .body(editData)
                .put(url)
                .andReturn();
    }
    //for class UserEditTest
    @Step("Make a Put-request")
    public Response makePutRequest(String token, String cookie, Map<String,String> editData, String url){
        return given()
                .filter(new AllureRestAssured())
                .header(new Header("x-csrf-token", token))
                .cookie("auth_sid", cookie)
                .body(editData)
                .put(url)
                .andReturn();
    }

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
    @Step("Make a Post-request with authData")
    public Response makePostRequestAuthData(String url, Map<String, String> authData){
        return given()
                .filter(new AllureRestAssured())
                .body(authData)
                .post(url)
                .andReturn();
    }

    //for class UserRegisterTest for all methods
    @Step("Make a Post-request with userData")
    public Response makePostRequestUserData(Map<String,String> userData, String url){
        return given()
                .filter(new AllureRestAssured())
                .body(userData)
                .post(url)
                .andReturn();
    }

    //for class UserGetTest
    @Step("Make a Get-request by user ID")
    public Response makeGetRequestById(String url){
        return given()
                .filter(new AllureRestAssured())
                .get(url)
                .andReturn();
    }

}

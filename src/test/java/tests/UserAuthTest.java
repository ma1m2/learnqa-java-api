package tests;

import io.qameta.allure.*;
import lib.MyAssertions;
import io.restassured.response.Response;
import lib.BaseTestCase;
import lib.ApiCoreRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.DisplayName;

@Epic("Authorisation cases")
@Feature("Authorisation")
public class UserAuthTest extends BaseTestCase {
    /**
     * 3l_05m @BeforeEach
     */
    public String cookie;
    public String header;
    public int userIdOnAuth;
    private final  ApiCoreRequest apiCoreRequest = new ApiCoreRequest();

    @BeforeEach
    public void loginUser(){
        Map<String,String> authData = new HashMap<>();
        authData.put("email", "vinkotov@example.com");
        authData.put("password", "1234");

        Response responseGetAuth = apiCoreRequest
                .makePostRequestAuthData("https://playground.learnqa.ru/api/user/login", authData);

        this.cookie = this.getCookie(responseGetAuth, "auth_sid");
        this.header = this.getHeader(responseGetAuth, "x-csrf-token");
        this.userIdOnAuth = getIntFromJson(responseGetAuth, "user_id");
    }

    @Test
    @Owner(value = "Пупкин Валерий Иванович")
    @Description("This test successfully authorize user by email and password")
    @DisplayName("Test positive auth user")
    public void testAuthUser(){
        Response responseCheckAuth = apiCoreRequest
                .makeGetRequest(
                        "https://playground.learnqa.ru/api/user/auth",
                        this.header,
                        this.cookie
                );

        MyAssertions.assertJsonByName(responseCheckAuth, "user_id", this.userIdOnAuth);
    }

    @Description("This test check authorisation status without sending auth cookie or token")
    @DisplayName("Test negative auth user")
    @ParameterizedTest
    @ValueSource(strings = {"cookie", "header"})
    public void testNegativeAuthUser(String condition){

        if(condition.equals("cookie")){
            Response responseForCheck = apiCoreRequest
                    .makeGetRequestWithCookie("https://playground.learnqa.ru/api/user/auth", this.cookie);
            MyAssertions.assertJsonByName(responseForCheck, "user_id", 0);
        } else if (condition.equals("header")) {
            Response responseForCheck = apiCoreRequest
                    .makeGetRequestWithToken("https://playground.learnqa.ru/api/user/auth",
                            this.header);
            MyAssertions.assertJsonByName(responseForCheck, "user_id", 0);
        }else {
            throw new  IllegalArgumentException("Condition value is not known: " + condition);
        }
    }
}

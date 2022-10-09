package lib;
import io.restassured.response.Response;

import static org.hamcrest.Matchers.hasKey;
import static org.hamcrest.Matchers.not;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class MyAssertions {
    /**
     * 3l_07m Assertions
     */
    public static void assertJsonByName(Response response, String name, int expectedValue){
        response.then().assertThat().body("$", hasKey(name));

        int actualValue = response.jsonPath().getInt(name);
        assertEquals(expectedValue, actualValue, "JSON actual Value is not equal to expected Value");
    }

    public static void assertResponseTextEquals(Response response, String expectedAnswer){
        assertEquals(expectedAnswer, response.asString(), "Response text is not as expected");
    }

    public static void assertResponseStatusCodeEquals(Response response, int expectedStatusCode){
        assertEquals(expectedStatusCode, response.getStatusCode(), "Status code is not as expected");
    }

    public static void assertJsonHasField(Response response, String expectedFieldName){
        response.then().assertThat().body("$", hasKey(expectedFieldName));
    }

    public static void assertJsonHasFields(Response response, String [] expectedFields){
        for(String expectedField : expectedFields){
            MyAssertions.assertJsonHasField(response, expectedField);
        }
    }

    public static void assertJsonNotField(Response response, String unexpectedFieldName){
        response.then().assertThat().body("$", not(hasKey(unexpectedFieldName)));
    }
}

package lib;
import io.restassured.response.Response;
import org.junit.jupiter.api.Assertions;

import static org.hamcrest.Matchers.hasKey;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class MyAssertions {
    /**
     * 3l_07m Assertions
     * @param response
     * @param name
     * @param expectedValue
     */
    public static void assertJsonByName(Response response, String name, int expectedValue){
        response.then().assertThat().body("$", hasKey(name));

        int actualValue = response.jsonPath().getInt(name);
        assertEquals(expectedValue, actualValue, "JSON actual Value is not equal to expected Value");
    }
}

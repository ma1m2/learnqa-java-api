import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.apache.commons.lang3.ArrayUtils.toArray;

public class HomeWorkTest {
    /**
     * Ex5: Parsing JSON, take text of second message
     */
    @Test
    public void testParseJson(){
        JsonPath response = RestAssured
                .get("https://playground.learnqa.ru/api/get_json_homework")
                .jsonPath();

        List<String> messages = response.get("messages.message");
        System.out.println(messages.get(1));
    }
    /**
     * Ex4: GET-запрос
     */
    @Test
    public void testGetText(){
        Response response = RestAssured
                .get("https://playground.learnqa.ru/api/get_text")
                .andReturn();
        response.prettyPrint();
    }
}

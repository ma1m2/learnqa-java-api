package lib;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

public class DataGenerator {

    public static String getRendomEmail(){
        String timeStampe = new SimpleDateFormat("yyMMddHHmmss").format(new java.util.Date());
        return "learnqa" + timeStampe + "@example.com";
    }

    public static Map<String, String> getRegistrationData(){
        Map<String, String> data = new HashMap<>();
        data.put("email", getRendomEmail());
        data.put("password", "123");
        data.put("username", "learnqa");
        data.put("firstName", "learnqa");
        data.put("lastName", "learnqa");

        return data;
    }

    public static Map<String, String> getRegistrationData(Map<String, String> nonDefaultValues){
        Map<String, String> defaultValues = getRegistrationData();

        String[] keys = {"email", "password", "username", "firstName", "lastName"};
        Map<String, String> userData = new HashMap<>();
        for(String key : keys){
            if(nonDefaultValues.containsKey(key)){
                userData.put(key, nonDefaultValues.get(key));
            }else {
                userData.put(key, defaultValues.get(key));
            }
        }

        return userData;
    }

}

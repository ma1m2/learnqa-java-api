package lib;

import java.text.SimpleDateFormat;

public class DataGenerator {

    public static String getRendomEmail(){
        String timeStampe = new SimpleDateFormat("yyMMddHHmmss").format(new java.util.Date());
        return "learnqa" + timeStampe + "@example.com";
    }

}

package utils;

import com.google.gson.JsonObject;
import okhttp3.*;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HttpClientUtil {

    private final static OkHttpClient HTTP_CLIENT = new OkHttpClient();
    public static String runSyncGetAndDelete(Request request, Boolean isGetMethod)
    {
        String responseMessage = "";
        Call call = HTTP_CLIENT.newCall(request);

        try {
            // blocking
            final Response response = call.execute();
            if(isGetMethod) {
                responseMessage = response.body().string();
                System.out.println(responseMessage);
            }

        } catch (IOException e) {
            //System.out.println("Ooops... error occured: " + e.getMessage());
        }

        return responseMessage;
    }
    public static String runSyncPostAndPut(Request request)
    {
        String responseMessage = "";
        Call call = HTTP_CLIENT.newCall(request);

        try {
            // blocking
            final Response response = call.execute();
            String jsonStringResponse = response.body().string();

            responseMessage = extractMessage(jsonStringResponse);
            JsonObject jsonResponse = new JsonObject();
            jsonResponse.addProperty("message", responseMessage);

            System.out.println(jsonResponse);

        } catch (IOException e) {
            //System.out.println("Ooops... error occured: " + e.getMessage());
        }

        return responseMessage;
    }

    // Extract the value of the "message" field from the JSON string
    private static String extractMessage(String jsonString) {
        // Define the regular expression pattern
        Pattern pattern = Pattern.compile("\"message\"\\s*:\\s*\"([^\"]*)\"");
        Matcher matcher = pattern.matcher(jsonString);

        // Check if the pattern matches
        if (matcher.find()) {
            // Extract and return the matched value
            return matcher.group(1);
        } else {
            // If no match found, return null or handle accordingly
            return null;
        }
    }

}

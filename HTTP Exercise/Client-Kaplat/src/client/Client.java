package client;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import okhttp3.*;
import utils.Constants;
import utils.HttpClientUtil;

public class Client {
    public static String sendDataUsingQueryParameters()
    {
        String responseFuncOne = "";

        String finalUrl =
                HttpUrl
                        .parse(Constants.GET_METHOD)
                        .newBuilder()
                        .addQueryParameter("id", "209297225")
                        .addQueryParameter("year", "1998")
                        .build()
                        .toString();

        Request request = new Request.Builder()
                .url(finalUrl)
                .get()
                .build();

        responseFuncOne = HttpClientUtil.runSyncGetAndDelete(request, true);

        return responseFuncOne;
    }
    public static String sendingDataToTheServerUsingABody(String responseFuncOne)
    {
        String responseFuncTwo = "";

        // Creating a JsonObject
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("id", 209297225);
        jsonObject.addProperty("year", 1998);
        jsonObject.addProperty("requestId", responseFuncOne);

        // Creating a Gson object
        Gson gson = new Gson();

        // Converting JsonObject to JSON string
        String jsonString = gson.toJson(jsonObject);
        RequestBody body = RequestBody.create(jsonString.getBytes());

        Request request = new Request.Builder()
                .url(Constants.POST_METHOD)
                .post(body)
                .build();

        responseFuncTwo = HttpClientUtil.runSyncPostAndPut(request);

        return responseFuncTwo;
    }
    public static String updateDataInTheServer(String responseFuncTwo)
    {
        String responseFuncThree = "";

        String finalUrl =
                HttpUrl
                        .parse(Constants.PUT_METHOD)
                        .newBuilder()
                        .addQueryParameter("id", responseFuncTwo)
                        .build()
                        .toString();

        // Creating a JsonObject
        JsonObject jsonObject = new JsonObject();
        int calculateID = ((209297225- 123503) % 92);
        int calculateYear = ((1998 + 123) % 45);
        jsonObject.addProperty("id", calculateID);
        jsonObject.addProperty("year", calculateYear);

        // Creating a Gson object
        Gson gson = new Gson();

        // Converting JsonObject to JSON string
        String jsonString = gson.toJson(jsonObject);
        RequestBody body = RequestBody.create(jsonString.getBytes());

        Request request = new Request.Builder()
                .url(finalUrl)
                .put(body)
                .build();

        //Call call = client.newCall(request);
        responseFuncThree = HttpClientUtil.runSyncPostAndPut(request);

        return responseFuncThree;
    }
    public static void deleteResource(String responseFuncThree)
    {
        String responseFour = "";

        String finalUrl =
                HttpUrl
                        .parse(Constants.DELETE_METHOD)
                        .newBuilder()
                        .addQueryParameter("id", responseFuncThree)
                        .build()
                        .toString();

        Request request = new Request.Builder()
                .url(finalUrl)
                .delete()
                .build();

        responseFour = HttpClientUtil.runSyncGetAndDelete(request, false);
    }

}
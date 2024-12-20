package client;

import static client.Client.*;

public class Main {
    public static void main(String[] args) {

        String responseFuncOne = sendDataUsingQueryParameters();
        String responseFuncTwo = sendingDataToTheServerUsingABody(responseFuncOne);
        String responseFuncThree = updateDataInTheServer(responseFuncTwo);
        deleteResource(responseFuncThree);
    }
}

package com.isssr.ticketing_system.slack;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.boot.json.JsonParseException;

import javax.net.ssl.HttpsURLConnection;
import java.io.*;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static com.google.common.net.HttpHeaders.USER_AGENT;
import static org.hibernate.validator.internal.util.Contracts.assertNotNull;

public class Slack {

    //private static final String TOKEN = "xoxp-4651591266-4651591272-657197710647-579a11474dc819b63ace1121645ed178";
    private static final String TOKEN = "xoxp-686271464771-697198201092-692052648561-949161345641d82c3f9c8e4e564e4ca4";

    // HTTP POST request
    public String createConversation(String name, String slackAccount) {

        String slackUserId = "686271464771.697680303792";//"4651591266.643933560034";

        if(slackAccount != null){
            slackUserId = slackAccount;
        }

        try {
            String url = "https://slack.com/api/conversations.create?token=" + TOKEN + "&name=" + name + "&user_ids=" + slackUserId + "&is_private=true";

            String response = doRequest(url);


            ObjectMapper mapper = new ObjectMapper();
            JsonNode actualObj = mapper.readTree(response);
            assertNotNull(actualObj);

            JsonNode jsonNode1 = actualObj.get("channel");

            return jsonNode1.get("id").getTextValue();


        } catch (IOException | JsonParseException e) {
            e.printStackTrace();
        }

        return "Error in Slack createConversation()";

    }

    public String sendMessage(String slackChannelId, String message, String username) {

        try {
            String url = "https://slack.com/api/chat.postMessage?token=" + TOKEN + "&channel=" + slackChannelId + "&text=" + encodeValue(message) + "&as_user=false&username=" + username.replaceAll(" ", "_");
            return doRequest(url);

        } catch (JsonParseException e) {
            e.printStackTrace();
        }

        return "Error in Slack sendMessage()";
    }


    private String doRequest(String url) {

        try {


        URL obj = new URL(url);
        HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();


        //add reuqest header
        con.setRequestMethod("POST");
        con.setRequestProperty("User-Agent", USER_AGENT);
        con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");

        // Send post request
        con.setDoOutput(true);
        DataOutputStream wr = new DataOutputStream(con.getOutputStream());
        wr.flush();
        wr.close();

        int responseCode = con.getResponseCode();
        System.out.println("\nSending 'POST' request to SLACK : " + url);
        System.out.println("Response Code : " + responseCode);

        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        //print result
        return response.toString();


        } catch (IOException | JsonParseException e) {
            e.printStackTrace();
        }

        return "Error in slack doRequest()";
    }


    private static String encodeValue(String value) {
        try {
            return URLEncoder.encode(value, StandardCharsets.UTF_8.toString());
        } catch (UnsupportedEncodingException ex) {
            throw new RuntimeException(ex.getCause());
        }
    }


    private String slackIdsToString(List<String> slackIds){
        String accounts = "";
        for (String s : slackIds) {
            accounts += s + ",";
        }
        return accounts;
    }


}

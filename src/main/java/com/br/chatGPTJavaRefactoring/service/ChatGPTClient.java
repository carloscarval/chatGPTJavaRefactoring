package com.br.chatGPTJavaRefactoring.service;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import static com.br.chatGPTJavaRefactoring.constants.Constants.INSTRUCTIONS;

public class ChatGPTClient {

    private ChatGPTClient() {}

    private static final String API_KEY = "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";
    private static final String API_URL = "https://api.openai.com/v1/chat/completions";

    public static void main(String[] args) throws Exception {
        File selectedFile = new File("src/main/java/com/example/javasimplifier/JavaRefactorApp.java");

        List<String> lines = Files.readAllLines(selectedFile.toPath(), StandardCharsets.UTF_8);
        String selectedFileContent = String.join("\n", lines);

        String response = sendRequestToChatGpt(INSTRUCTIONS, selectedFileContent, "");
        System.out.println(response);
    }

    public static String sendRequestToChatGpt(String instructions, String code, String selectedVersion) throws Exception {
        JSONObject payload = new JSONObject();
        JSONObject message = new JSONObject();
        JSONArray messageList = new JSONArray();

        message.put("role", "user");
        message.put("content", instructions + code);
        messageList.put(message);

        payload.put("model", "gpt-3.5-turbo");
        payload.put("messages", messageList);
        payload.put("temperature", 0.7);

        StringEntity inputEntity = new StringEntity(payload.toString(), ContentType.APPLICATION_JSON);

        HttpPost post = new HttpPost(API_URL);
        post.setEntity(inputEntity);
        post.setHeader("Authorization", "Bearer " + API_KEY);
        post.setHeader("Content-Type", "application/json");

        try (CloseableHttpClient httpClient = HttpClients.createDefault();
             CloseableHttpResponse response = httpClient.execute(post)) {
            HttpEntity resEntity = response.getEntity();
            String resJsonString = new String(resEntity.getContent().readAllBytes(), StandardCharsets.UTF_8);
            JSONObject resJson = new JSONObject(resJsonString);

            if (resJson.has("error")) {
                throw new RuntimeException(resJson.toString());
            }

            JSONArray responseArray = resJson.getJSONArray("choices");
            List<String> responseList = new ArrayList<>();

            for (int i = 0; i < responseArray.length(); i++) {
                JSONObject responseObj = responseArray.getJSONObject(i);
                String responseString = responseObj.getJSONObject("message").getString("content");
                responseList.add(responseString);
            }

            return responseList.get(0);
        } catch (IOException | JSONException e) {
            throw e;
        }
    }

    private static HttpURLConnection getHttpURLConnection(String requestBody) throws IOException {
        URL url = new URL(API_URL);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Authorization", "Bearer " + API_KEY);
        connection.setRequestProperty("Content-Type", "application/json");

        connection.setDoOutput(true);
        try (OutputStream os = connection.getOutputStream()) {
            byte[] input = requestBody.getBytes(StandardCharsets.UTF_8);
            os.write(input, 0, input.length);
        }
        return connection;
    }
}
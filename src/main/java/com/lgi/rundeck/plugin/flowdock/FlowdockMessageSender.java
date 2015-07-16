package com.lgi.rundeck.plugin.flowdock;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URLEncoder;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class FlowdockMessageSender {

    private final URI apiUrl;
    private final String sender;

    public FlowdockMessageSender(String sender, String flowToken) {
        this.sender = sender;
        if (flowToken == null || flowToken.isEmpty()) {
            throw new IllegalArgumentException("flowToken is null or empty");
        }
        this.apiUrl = URI.create("https://api.flowdock.com/messages/chat/" + flowToken);
    }

    public void chatMessage(String message, Collection<String> tags) {
        final Map<String, String> params = new HashMap<>();
        params.put("external_user_name", sender);
        params.put("content", message);
        final StringBuilder tagLine = new StringBuilder();
        for (String tag : tags) {
            tagLine.append(tag).append(",");
        }
        if (tagLine.length() > 0) {
            tagLine.deleteCharAt(tagLine.length() - 1);
        }
        params.put("tags", tagLine.toString());
        send(params);
    }

    private void send(Map<String, String> params) {
        try {
            postData(params);
        } catch (IOException e) {
            System.err.println("unable to send a flowdock message, apiUrl=" + apiUrl);
        }
    }

    private void postData(Map<String, String> params) throws IOException {
        final HttpURLConnection conn = (HttpURLConnection)apiUrl.toURL().openConnection();
        conn.setRequestMethod("POST");
        conn.setUseCaches(false);
        conn.setDoOutput(true);
        conn.setDoInput(true);
        conn.setRequestProperty("User-Agent", getClass().getSimpleName());

        try (OutputStreamWriter out = new OutputStreamWriter(conn.getOutputStream())) {
            out.write(mapToPostData(params));
        }

        try (BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
            //noinspection StatementWithEmptyBody
            while (in.readLine() != null);
        }

        conn.connect();
    }

    private static String mapToPostData(Map<String, String> map) throws UnsupportedEncodingException {
        final StringBuilder sb = new StringBuilder();
        for (Entry<String, String> entry : map.entrySet()) {
            sb.append(entry.getKey()).append("=").append(URLEncoder.encode(entry.getValue(), "UTF-8")).append("&");
        }
        if (sb.length() > 0) {
            sb.deleteCharAt(sb.length() - 1);
        }
        return sb.toString();
    }
}

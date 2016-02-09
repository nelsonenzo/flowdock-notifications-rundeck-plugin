package com.lgi.rundeck.plugins.flowdock;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URI;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FlowdockMessageSender {

    private final URI apiUrl = URI.create("https://api.flowdock.com/messages");
    private final String sender;
    private final String flowToken;

    public FlowdockMessageSender(String sender, String flowToken) {
        this.sender = sender;
        this.flowToken = flowToken;
    }

    public void postData(String message, Optional<String> maybeTags) throws IOException {

        final HttpURLConnection conn = (HttpURLConnection) apiUrl.toURL().openConnection();

        conn.setRequestMethod("POST");
        conn.setUseCaches(false);
        conn.setDoOutput(true);
        conn.setDoInput(true);
        conn.setRequestProperty("User-Agent", getClass().getSimpleName());
        conn.setRequestProperty("Content-Type", "application/json");

        final String request = buildRequest(flowToken, message, maybeTags);

        try (OutputStreamWriter out = new OutputStreamWriter(conn.getOutputStream())) {
            out.write(request);
        }

        try (BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
            //noinspection StatementWithEmptyBody
            while (in.readLine() != null) ;
        }

    }

    private static String buildRequest(String flowToken, String message, Optional<String> maybeTags) {

        final String tagsToBeSent = maybeTags
                .map(tags -> tags.split("\\s+"))
                .filter(tags -> tags.length > 0)
                .map(tags -> Stream.of(tags).map(tag -> String.format("\"%s\"", tag)).collect(Collectors.joining(",", "\"tags\": [", "],")))
                .orElse("");

        return String.format(
                "{\"flow_token\": \"%s\", \"event\": \"message\", %s \"content\": \"%s\"}",
                flowToken,
                tagsToBeSent,
                message);
    }

}

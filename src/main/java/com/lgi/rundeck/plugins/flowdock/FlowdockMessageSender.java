package com.lgi.rundeck.plugins.flowdock;

import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.google.common.collect.Iterables;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URI;

public class FlowdockMessageSender {

    private static final Splitter SPACE_SPLITTER = Splitter.on(" ").trimResults().omitEmptyStrings();
    private static final Joiner COMMA_JOINER = Joiner.on(",");

    private final URI apiUrl = URI.create("https://api.flowdock.com/messages");
    private final String sender;
    private final String flowToken;

    public FlowdockMessageSender(String sender, String flowToken) {
        this.sender = sender;
        this.flowToken = flowToken;
    }

    public void postData(String message, String tags) throws IOException {

        final HttpURLConnection conn = (HttpURLConnection) apiUrl.toURL().openConnection();

        conn.setRequestMethod("POST");
        conn.setUseCaches(false);
        conn.setDoOutput(true);
        conn.setDoInput(true);
        conn.setRequestProperty("User-Agent", getClass().getSimpleName());
        conn.setRequestProperty("Content-Type", "application/json");

        final String request = buildRequest(flowToken, message, tags);

        try (OutputStreamWriter out = new OutputStreamWriter(conn.getOutputStream())) {
            out.write(request);
        }

        try (BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
            //noinspection StatementWithEmptyBody
            while (in.readLine() != null) ;
        }

    }

    private static String buildRequest(String flowToken, String message, String tags) {

        final String tagsToBeSent;
        if (tags != null) {
            final Iterable<String> parsedTags = SPACE_SPLITTER.split(tags);
            final Iterable<String> transformedTags = Iterables.transform(parsedTags, new Function<String, String>() {
                @Override
                public String apply(String s) {
                    return s.startsWith("#") ?
                            String.format("\"%s\"", s) :
                            String.format("\"#%s\"", s);
                }
            });

            if (transformedTags.iterator().hasNext()) {
                tagsToBeSent = String.format("\"tags\": [%s],", COMMA_JOINER.join(transformedTags));
            } else {
                tagsToBeSent = "";
            }
        } else {
            tagsToBeSent = "";
        }

        return String.format(
                "{\"flow_token\": \"%s\", \"event\": \"message\", %s \"content\": \"%s\"}",
                flowToken,
                tagsToBeSent,
                message);
    }

}

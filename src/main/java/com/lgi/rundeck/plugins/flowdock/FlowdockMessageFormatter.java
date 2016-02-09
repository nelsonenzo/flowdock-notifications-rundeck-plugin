package com.lgi.rundeck.plugins.flowdock;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

public final class FlowdockMessageFormatter {

    private FlowdockMessageFormatter() {
    }

    public static String formatMessage(String trigger, Map executionData, Map config) {

        Configuration freeMarkerConfig = new Configuration();
        freeMarkerConfig.setClassForTemplateLoading(FlowdockNotificationPlugin.class, "/templates");

        final Map<String, Object> model = new HashMap<>();

        model.put("trigger", trigger);
        model.put("execution", executionData);
        model.put("config", config);

        final StringWriter writer = new StringWriter();
        try {
            final Template template = freeMarkerConfig.getTemplate("flowdock-message.ftl");
            template.process(model, writer);
        } catch (IOException e) {
            throw new RuntimeException("Cannot read Flowdock message template", e);
        } catch (TemplateException e) {
            throw new RuntimeException("Cannot fill the Flowdock message template with data ", e);
        }

        return writer.toString();
    }
}

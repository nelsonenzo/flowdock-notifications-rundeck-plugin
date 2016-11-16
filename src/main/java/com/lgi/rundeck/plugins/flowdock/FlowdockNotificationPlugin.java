package com.lgi.rundeck.plugins.flowdock;

import com.dtolabs.rundeck.core.plugins.Plugin;
import com.dtolabs.rundeck.plugins.descriptions.PluginDescription;
import com.dtolabs.rundeck.plugins.descriptions.PluginProperty;
import com.dtolabs.rundeck.plugins.notification.NotificationPlugin;

import java.io.IOException;
import java.util.Map;

@Plugin(service = "Notification", name = "flowdock-notifications")
@PluginDescription(title = "Flowdock Notifications Plugin", description = "Notifies a flow on a job status change.")
@SuppressWarnings("unused")
public class FlowdockNotificationPlugin implements NotificationPlugin {

    @PluginProperty(title = "Flowdock Flow Token", required = true)
    private String flowToken;

    @PluginProperty(title = "Flowdock Tags")
    private String tags;

    @Override
    public boolean postNotification(String trigger, Map executionData, Map config) {

        final String message = FlowdockMessageFormatter.formatMessage(trigger, executionData, config);

        final FlowdockMessageSender sender = new FlowdockMessageSender("Rundeck", flowToken);
        try {
            sender.postData(message, tags);
        } catch (IOException e) {
            throw new RuntimeException("Couldn't send message: " + message + " to FlowDock", e);
        }
        return true;
    }
}

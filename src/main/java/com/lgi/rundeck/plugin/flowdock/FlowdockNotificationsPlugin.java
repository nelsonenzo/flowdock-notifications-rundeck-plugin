package com.lgi.rundeck.plugin.flowdock;

import com.dtolabs.rundeck.core.plugins.Plugin;
import com.dtolabs.rundeck.plugins.descriptions.PluginDescription;
import com.dtolabs.rundeck.plugins.descriptions.PluginProperty;
import com.dtolabs.rundeck.plugins.notification.NotificationPlugin;

import java.util.Collections;
import java.util.Map;

@Plugin(service = "Notification", name = "flowdock-notifications")
@PluginDescription(title = "Flowdock Notifications Plugin", description = "Notifies a flow on a job status change.")
@SuppressWarnings("unused")
public class FlowdockNotificationsPlugin implements NotificationPlugin {

    @PluginProperty(title = "Flowdock Flow Token", required = true)
    private String flowToken;

    public boolean postNotification(String trigger, Map executionData, Map config) {
        final FlowdockMessageSender sender = new FlowdockMessageSender("Rundeck", flowToken);
        sender.chatMessage(executionData.toString(), Collections.<String>emptyList());
        return true;
    }
}

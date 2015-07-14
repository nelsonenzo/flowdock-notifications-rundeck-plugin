package com.lgi.rundeck.plugin.flowdock;

import com.dtolabs.rundeck.core.plugins.Plugin;
import com.dtolabs.rundeck.plugins.descriptions.PluginDescription;
import com.dtolabs.rundeck.plugins.notification.NotificationPlugin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

@Plugin(service = "Notification", name = "flowdock-notifications")
@PluginDescription(title = "Flowdock Notifications Plugin", description = "Notifies a flow on a job status change.")
@SuppressWarnings("unused")
public class FlowdockNotificationsPlugin implements NotificationPlugin {

    private static final Logger LOG = LoggerFactory.getLogger(FlowdockNotificationsPlugin.class);

    public boolean postNotification(String trigger, Map executionData, Map config) {
        return true;
    }
}

Installation:
=============

Build the plugin, by: `mvn clean package`

Copy the result jar to `rundeck/libext` directory, where Rundeck plugins are stored.

Check if the plugin was installed in: `Configure` - `List Plugins` - `Notification Plugins` (you should see the `Flowdock Notifications Plugin` listed there).

Usage:
======

When editing a job, check the `Send notification?` option,
and choose `Flowdock Notifications Plugin` in either: "On Success", "On Failure" or "On Start" triggers.

After the plugin will be selected there are few parameters to be specified:

* Flowdock Flow Token (mandatory) - this will direct the notification to a specific flow (identified by the token)
* Tags (optional) - can be entered to make the Flowdock search much easier


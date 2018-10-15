package com.dingocore;

import com.dingocore.mqtt.BusConnection;

public interface TopicNode {
    String topic();
    BusConnection getBusConnection();

    default String topic(String subtopic) {
        return topic() + "/" + subtopic;
    }

    default String $topic(String subtopic) {
        return topic("$" + subtopic);
    }



}

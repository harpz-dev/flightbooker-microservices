package com.team11.bookingservice.config;

import java.util.HashMap;
import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "booking.event.publisher")
public class BookingEventPublisherProperties {

    private Map<String, String> topicMapping = new HashMap<>();

    public Map<String, String> getTopicMapping() {
        return topicMapping;
    }

    public void setTopicMapping(Map<String, String> topicMapping) {
        this.topicMapping = topicMapping;
    }
}

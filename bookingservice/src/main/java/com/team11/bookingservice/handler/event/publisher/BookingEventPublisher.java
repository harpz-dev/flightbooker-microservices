package com.team11.bookingservice.handler.event.publisher;

import java.util.Map;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import com.team11.bookingservice.dto.BookingEventDTO;
import com.team11.bookingservice.exception.BookingException;

@Component
public class BookingEventPublisher implements EventPublisher<BookingEventDTO> {

    private final KafkaTemplate<String, BookingEventDTO> kafkaTemplate;
    private final Map<String, String> topicMapping = Map.of(
        "FLIGHT", "flight_booking_request",
        "PAYMENT", "payment_request"
    );

    public BookingEventPublisher(
        KafkaTemplate<String, BookingEventDTO> kafkaTemplate
        // , BookingEventPublisherProperties publisherProperties
    ) {
        this.kafkaTemplate = kafkaTemplate;
        // this.topicMapping = publisherProperties.getTopicMapping();
    }

    @Override
    public void publish(String topicKey, BookingEventDTO event) throws BookingException {
        String topic = topicMapping.get(topicKey);
        if (topic == null) {
            throw new BookingException(new IllegalArgumentException(), "No topic configured for event");
        }
        kafkaTemplate.send(topic, event);
    }
}

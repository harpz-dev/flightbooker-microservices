package com.team11.bookingservice.mapper;

import java.util.HashMap;

import org.springframework.stereotype.Component;

import com.team11.bookingservice.dto.BookingEventDTO;
import com.team11.bookingservice.dto.BookingRequestModel;
import com.team11.bookingservice.entity.BookingEntity;

@Component
public class BookingMapper {

    public BookingMapper() {}

    public BookingEntity toEntity(BookingRequestModel requestModel, Long userId) {
        return new BookingEntity(userId, requestModel.getRequestType(), new HashMap<>(requestModel.getDetails()));
    }

    public BookingEventDTO toDTO(BookingEntity entity) {
        return new BookingEventDTO(entity.getBookingId(), new HashMap<>(entity.getDetails()));
    }
}

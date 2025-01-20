package com.tekarch.TafBookingMS.services;

import com.tekarch.TafBookingMS.models.Bookings;

import java.util.List;
import java.util.Optional;

public interface BookingService {

    Bookings createABooking(Bookings booking);
    Bookings getABookingById(Long bookingId);
    List<Bookings> getBookingByUserId(Long userId);
 //   Bookings updateBookingsById(Long userId);
    void cancelBooking(Long bookingId);
}

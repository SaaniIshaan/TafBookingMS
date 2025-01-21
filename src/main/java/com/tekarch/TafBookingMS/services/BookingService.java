package com.tekarch.TafBookingMS.services;

import com.tekarch.TafBookingMS.models.Bookings;

import java.util.List;
import java.util.Optional;

public interface BookingService {

    Bookings createABooking(Bookings booking);
    Bookings getABookingById(Long bookingId);
    List<Bookings> getBookingByUserId(Long userId);
    List<Bookings> getAllBookings();
    Bookings updateBooking(Long bookingId, Bookings updatedBooking);
    void deleteBooking(Long bookingId);
    void cancelBooking(Long bookingId);
}

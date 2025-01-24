package com.tekarch.TafBookingMS.services;

import com.tekarch.TafBookingMS.models.BookingsDTO;

import java.util.List;

public interface BookingService {

    BookingsDTO createABooking(BookingsDTO booking);
    BookingsDTO getABookingById(Long bookingId);
    List<BookingsDTO> getBookingByUserId(Long userId);
    List<BookingsDTO> getAllBookings();
    BookingsDTO updateBooking(Long bookingId, BookingsDTO updatedBooking);
    void deleteBooking(Long bookingId);
    void cancelBooking(Long bookingId);
}

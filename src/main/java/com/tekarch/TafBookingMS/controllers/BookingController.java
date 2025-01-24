package com.tekarch.TafBookingMS.controllers;

import com.tekarch.TafBookingMS.models.BookingsDTO;
import com.tekarch.TafBookingMS.services.BookingServiceImpl;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/bookings")
@RestController
public class BookingController {

    private static final Logger logger = LogManager.getLogger(BookingServiceImpl.class);

    @Autowired
    private final BookingServiceImpl bookingServiceImpl;

    public BookingController(BookingServiceImpl bookingServiceImpl){
        this.bookingServiceImpl = bookingServiceImpl;
    }

    @PostMapping
    public ResponseEntity<BookingsDTO> createBooking(@RequestBody BookingsDTO booking) {
        BookingsDTO createdBooking = bookingServiceImpl.createABooking(booking);
        return new ResponseEntity<>(createdBooking, HttpStatus.CREATED);
    }

    @GetMapping("/{bookingId}")
    public ResponseEntity<BookingsDTO> getBookingById(@PathVariable Long bookingId) {
        BookingsDTO booking = bookingServiceImpl.getABookingById(bookingId);
        if (booking != null) {
            return new ResponseEntity<>(booking, HttpStatus.OK);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    //    return new ResponseEntity<>(booking, HttpStatus.OK);
    }

    // Get all bookings
    @GetMapping
    public ResponseEntity<List<BookingsDTO>> getAllBookings() {
        List<BookingsDTO> bookings = bookingServiceImpl.getAllBookings();
        return new ResponseEntity<>(bookings, HttpStatus.OK);
    }

    @GetMapping("/users/{userId}")
    public ResponseEntity<List<BookingsDTO>> getBookingByUserId(@PathVariable Long userId) {
        return new ResponseEntity<List<BookingsDTO>>(bookingServiceImpl.getBookingByUserId(userId), HttpStatus.OK);
    }

    // Update booking (e.g., change status or flight)
    @PutMapping("/{bookingId}")
    public ResponseEntity<BookingsDTO> updateBooking(@PathVariable Long bookingId, @RequestBody BookingsDTO updatedBooking) {
        BookingsDTO updated = bookingServiceImpl.updateBooking(bookingId, updatedBooking);
        return updated != null ? new ResponseEntity<>(updated, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/{bookingId}")
    public ResponseEntity<String> cancelBooking(@PathVariable Long bookingId) {
        bookingServiceImpl.cancelBooking(bookingId);
        String message = "Booking with ID " + bookingId + " has been successfully deleted";
        return ResponseEntity.ok(message);
    //    return ResponseEntity.noContent().build();
    }

/*    @DeleteMapping("/{bookingId}")
    public String cancelBooking(@PathVariable Long bookingId) {
        try {
            bookingServiceImpl.cancelBooking(bookingId);
            return "Booking with ID " + bookingId + " has been successfully canceled.";
        } catch (Exception e) {
            return "Failed to cancel booking with ID " + bookingId + ". Reason: " + e.getMessage();
        } */

    @ExceptionHandler
    public ResponseEntity<String> respondWithError(Exception e) {
        logger.error("Exception Occurred. Details : {}", e.getMessage());
        return new ResponseEntity<>("Exception Occurred. More Info :"
                + e.getMessage(), HttpStatus.BAD_REQUEST);
    }


}

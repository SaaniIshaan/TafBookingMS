package com.tekarch.TafBookingMS.controllers;

import com.tekarch.TafBookingMS.models.Bookings;
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
    public ResponseEntity<Bookings> createBooking(@RequestBody Bookings booking) {
        Bookings createdBooking = bookingServiceImpl.createABooking(booking);
        return new ResponseEntity<>(createdBooking, HttpStatus.CREATED);
    }

    @GetMapping("/bookingId")
    public ResponseEntity<Bookings> getBookingById(@PathVariable Long bookingId) {
        Bookings booking = bookingServiceImpl.getABookingById(bookingId);
        if (booking != null) {
            return new ResponseEntity<>(booking, HttpStatus.OK);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    //    return new ResponseEntity<>(booking, HttpStatus.OK);
    }

    @GetMapping("/users/{userId}")
    public ResponseEntity<List<Bookings>> getBookingByUserId(@PathVariable Long userId) {
        return new ResponseEntity<List<Bookings>>(bookingServiceImpl.getBookingByUserId(userId), HttpStatus.OK);
    }

    // Update booking (e.g., change status or flight)
    @PutMapping("/{bookingId}")
    public ResponseEntity<Bookings> updateBooking(@PathVariable Long bookingId, @RequestBody Bookings updatedBooking) {
        Bookings updated = bookingServiceImpl.updateBooking(bookingId, updatedBooking);
        return updated != null ? new ResponseEntity<>(updated, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/{bookingId}")
    public ResponseEntity<Void> cancelBooking(@PathVariable Long bookingId) {
        bookingServiceImpl.cancelBooking(bookingId);
        return ResponseEntity.noContent().build();
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

package com.tekarch.TafBookingMS.models;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@AllArgsConstructor
@Data
public class BookingsDTO {

    private Long bookingId;
    private Long userId;
    private Long flightId;
    private String status; // e.g., Booked, Cancelled
    private LocalDateTime createdAt = LocalDateTime.now();
    private LocalDateTime updatedAt;

 //   @ManyToOne
 //   @JoinColumn(name = "userId", nullable = false)
 //   private Users user;
 //   @ManyToOne
//    @JoinColumn(name = "flightId", nullable = false)
 //   private Flights flight;

}

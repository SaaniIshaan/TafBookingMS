package com.tekarch.TafBookingMS.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "bookings")
public class Bookings {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bookingId;
    @Column(nullable = false)
    private Long userId;
    @Column(nullable = false)
    private Long flightId;

 //   @ManyToOne
 //   @JoinColumn(name = "userId", nullable = false)
 //   private Users user;
 //   @ManyToOne
//    @JoinColumn(name = "flightId", nullable = false)
 //   private Flights flight;

    private String status; // e.g., Booked, Cancelled

    private LocalDateTime createdAt = LocalDateTime.now();
    private LocalDateTime updatedAt;


}

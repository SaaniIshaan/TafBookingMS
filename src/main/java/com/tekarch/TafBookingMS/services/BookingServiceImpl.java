package com.tekarch.TafBookingMS.services;

import com.tekarch.TafBookingMS.models.BookingsDTO;
import com.tekarch.TafBookingMS.models.FlightsDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@Service
public class BookingServiceImpl implements BookingService {

    @Autowired
    private final RestTemplate restTemplate;

    @Value("${datastore.ms.url}")
    String dataStoreServiceUrl;

    public BookingServiceImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

 /*   @Override
    public BookingsDTO createABooking(BookingsDTO booking) {
        String flightUrl = dataStoreServiceUrl + "/flights/" + booking.getFlightId();
        FlightsDTO flight = restTemplate.getForObject(flightUrl, FlightsDTO.class);

        if(flight != null && flight.getAvailableSeats() > 0) {
            flight.setAvailableSeats(flight.getAvailableSeats() - 1);
            restTemplate.put(flightUrl, flight);
            return restTemplate.postForObject(dataStoreServiceUrl + "/bookings", booking, BookingsDTO.class);
        }
        throw new RuntimeException("flight is fully booked or not available");

    }*/

    @Override
    public BookingsDTO createABooking(BookingsDTO booking) {
        // Correctly call the availableSeats endpoint in TafDatastoreService
        String flightUrl = dataStoreServiceUrl + "/flights/" + booking.getFlightId();  // Ensure the correct URL path
        Integer availableSeats = restTemplate.getForObject(flightUrl + "/availableSeats", Integer.class);

        if (availableSeats == null || availableSeats <= 0) {
            throw new RuntimeException("Flight is fully booked or not available.");
        }

        // Reduce the available seats by calling the endpoint in TafDatastoreService (ensure it's only called once)
        restTemplate.put(flightUrl + "/reduceSeats", null);

        // Create the booking in TafDatastoreService
        String bookingUrl = dataStoreServiceUrl + "/bookings";
        return restTemplate.postForObject(bookingUrl, booking, BookingsDTO.class);
    }

    @Override
    public BookingsDTO getABookingById(Long bookingId) {
        return restTemplate.getForObject(dataStoreServiceUrl + "/bookings/" + bookingId, BookingsDTO.class);
    }

    @Override
    public List<BookingsDTO> getBookingByUserId(Long userId) {
        BookingsDTO[] bookings = restTemplate.getForObject(dataStoreServiceUrl + "/bookings/users/" + userId, BookingsDTO[].class);
        return Arrays.asList(bookings);
    }

    @Override
    public List<BookingsDTO> getAllBookings() {
        String bookingUrl = dataStoreServiceUrl + "/bookings";
        BookingsDTO[] bookings= restTemplate.getForObject(bookingUrl, BookingsDTO[].class);
        return Arrays.asList(bookings);

    }

    @Override
    public BookingsDTO updateBooking(Long bookingId, BookingsDTO updatedBooking) {
        String bookingUrl = dataStoreServiceUrl + "/bookings/" + bookingId;
        restTemplate.put(bookingUrl, updatedBooking);
        return updatedBooking;
    }

    @Override
    public void deleteBooking(Long bookingId) {
        String bookingUrl = dataStoreServiceUrl + "/bookings/" + bookingId + "/cancel";
        restTemplate.put(bookingUrl, null);
    }

    @Override
    public void cancelBooking(Long bookingId) {
        BookingsDTO booking = restTemplate.getForObject(dataStoreServiceUrl + "/bookings/" + bookingId, BookingsDTO.class);
        if(booking != null && "Booked".equals(booking.getStatus())){
            booking.setStatus("Cancelled");
            restTemplate.put(dataStoreServiceUrl + "/bookings/" + bookingId, booking);

        String flightUrl = dataStoreServiceUrl + "/flights/" + booking.getFlightId();
        FlightsDTO flight = restTemplate.getForObject(flightUrl, FlightsDTO.class);
        if(flight != null){
            flight.setAvailableSeats(flight.getAvailableSeats() + 1);
            restTemplate.put(flightUrl, flight);

            }
        }

    }
}

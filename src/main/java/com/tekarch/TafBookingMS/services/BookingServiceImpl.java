package com.tekarch.TafBookingMS.services;

import com.tekarch.TafBookingMS.models.Bookings;
import com.tekarch.TafBookingMS.models.Flights;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class BookingServiceImpl implements BookingService {

    @Autowired
    private final RestTemplate restTemplate;

    @Value("${datastore.ms.url}")
    String dataStoreServiceUrl;

    public BookingServiceImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public Bookings createABooking(Bookings booking) {
        String flightUrl = dataStoreServiceUrl + "/flights/" + booking.getFlightId();
        Flights flight = restTemplate.getForObject(flightUrl, Flights.class);

        if(flight != null && flight.getAvailableSeats() > 0) {
            flight.setAvailableSeats(flight.getAvailableSeats() - 1);
            restTemplate.put(flightUrl, flight);
            return restTemplate.postForObject(dataStoreServiceUrl + "/bookings", booking, Bookings.class);
        }
        throw new RuntimeException("flight is fully booked or not available");

    }

    @Override
    public Bookings getABookingById(Long bookingId) {
        return restTemplate.getForObject(dataStoreServiceUrl + "/bookings/" + bookingId, Bookings.class);
    }

    @Override
    public List<Bookings> getBookingByUserId(Long userId) {
        Bookings[] bookings = restTemplate.getForObject(dataStoreServiceUrl + "/bookings/users/" + userId, Bookings[].class);
        return Arrays.asList(bookings);
    }

    @Override
    public List<Bookings> getAllBookings() {
        String bookingUrl = dataStoreServiceUrl + "/bookings";
        Bookings[] bookings= restTemplate.getForObject(bookingUrl, Bookings[].class);
        return Arrays.asList(bookings);

    }

    @Override
    public Bookings updateBooking(Long bookingId, Bookings updatedBooking) {
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
        Bookings booking = restTemplate.getForObject(dataStoreServiceUrl + "/bookings/" + bookingId, Bookings.class);
        if(booking != null && "Booked".equals(booking.getStatus())){
            booking.setStatus("Cancelled");
            restTemplate.put(dataStoreServiceUrl + "/bookings/" + bookingId, booking);

        String flightUrl = dataStoreServiceUrl + "/flights/" + booking.getFlightId();
        Flights flight = restTemplate.getForObject(flightUrl,Flights.class);
        if(flight != null){
            flight.setAvailableSeats(flight.getAvailableSeats() + 1);
            restTemplate.put(flightUrl, flight);

            }
        }

    }
}

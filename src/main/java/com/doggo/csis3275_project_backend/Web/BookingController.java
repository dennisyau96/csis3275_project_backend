package com.doggo.csis3275_project_backend.Web;

import com.doggo.csis3275_project_backend.Entities.Booking;
import com.doggo.csis3275_project_backend.Entities.GenericResponse;
import com.doggo.csis3275_project_backend.Services.BookingService;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api")
public class BookingController {
    private final BookingService bookingService;

    @Autowired
    public BookingController(BookingService bookingService){
        this.bookingService = bookingService;
    }

    @GetMapping("/getBookings")
    public GenericResponse<Page<Booking>> getBookings(@RequestHeader(name="Authorization") String rawToken, @RequestParam(defaultValue = "0") int page_no, @RequestParam(defaultValue = "10") int page_size, HttpServletResponse response) throws JsonProcessingException {
        return bookingService.getAllBookings(rawToken, page_no, page_size, response);
    }

    @GetMapping(path = "/getBooking/{bookingId}")
    public GenericResponse getBookingDetail(@RequestHeader(name="Authorization") String rawToken, @PathVariable String bookingId, HttpServletResponse response) throws JsonProcessingException{
        return bookingService.getBookingDetail(rawToken, bookingId, response);
    }

    @PostMapping("/book")
    public GenericResponse book(@RequestHeader(name="Authorization") String rawToken, @RequestBody Map<String, Object> json, HttpServletResponse response) throws JsonProcessingException {
        return bookingService.book(rawToken, json, response);
    }

    @PostMapping("/booking/action")
    public GenericResponse updateBooking(@RequestHeader(name="Authorization") String rawToken, @RequestBody Map<String, Object> json, HttpServletResponse response) throws JsonProcessingException {
        return bookingService.updateBooking(rawToken, json, response);
    }
}

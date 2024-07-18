package com.doggo.csis3275_project_backend.Services;

import com.doggo.csis3275_project_backend.DTO.BookingListDTO;
import com.doggo.csis3275_project_backend.Entities.*;
import com.doggo.csis3275_project_backend.Repositories.IBookingRepository;
import com.doggo.csis3275_project_backend.Repositories.ICustomerRepository;
import com.doggo.csis3275_project_backend.Repositories.IDogRepository;
import com.doggo.csis3275_project_backend.Repositories.ITimeslotRepository;
import com.doggo.csis3275_project_backend.exceptions.ErrorHelper;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletResponse;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;

import java.awt.print.Book;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class BookingService {
    private IBookingRepository bookingRepository;
    private IDogRepository dogRepository;
    private ITimeslotRepository timeslotRepository;
    private final JwtService jwtService;

    public BookingService(IBookingRepository bookingRepository, IDogRepository dogRepository, ITimeslotRepository timeslotRepository, JwtService jwtService) {
        this.bookingRepository = bookingRepository;
        this.dogRepository = dogRepository;
        this.timeslotRepository = timeslotRepository;
        this.jwtService = jwtService;
    }

    public GenericResponse<Page<Booking>> getBookings(String rawToken, int pageNo, int pageSize, HttpServletResponse response) throws JsonProcessingException, JSONException {
        String responseMessage = "";
        boolean responseResult = false;
        HashMap<String, Object> responseData = new HashMap<>();

        try {
            String token = jwtService.getToken(rawToken);
            String userId = jwtService.extractClaim(token, Claims::getId);

            List<BookingListDTO> bookings = bookingRepository.findBookingDetailsByRenter_id(userId);

            Pageable pageable = PageRequest.of(pageNo, pageSize);
            int start = (int) pageable.getOffset();
            int end = Math.min(start + pageable.getPageSize(), bookings.size());

            List<BookingListDTO> pageContent = bookings.subList(start, end);

            responseResult = true;

            responseData.put("bookings", new PageImpl<>(pageContent, pageable, bookings.size()));
            responseMessage = "success";
            responseResult = true;
        }
        catch (Exception e) {
            responseMessage = "An error occurred while processing your request.";
        }

        return GenericResponse.makeResponse(responseMessage, responseResult, responseData);
    }

    public GenericResponse getBookingDetail(String rawToken, String bookingID, HttpServletResponse response){
        String responseMessage = "success";
        boolean responseResult = false;
        HashMap<String, Object> responseData = new HashMap<>();

        try{
            String token = jwtService.getToken(rawToken);
            String userId = jwtService.extractClaim(token, Claims::getId);

            Booking booking = bookingRepository.findBy_idAndRenter_id(bookingID, userId);

            if(booking != null){
                Dog dog = dogRepository.findById(booking.getDog_id()).orElse(null);
                Timeslot timeslot = timeslotRepository.findById(booking.getTimeslot_id()).orElse(null);

                HashMap<String, Object> detailData = new HashMap<>();
                detailData.put("_id", booking.get_id());
                detailData.put("dog", dog.dogResponse());
                detailData.put("owner_id", booking.getOwner_id());
                detailData.put("timeslot", timeslot.timeslotResponse());
                detailData.put("booking_date", booking.getBooking_date());
                detailData.put("is_booking_confirmed", booking.isBooking_confirmed());

                responseResult = true;
                responseData.put("booking", detailData);
            }
            else{
                responseResult = false;
                responseMessage = "Booking data not found";
            }

        }
        catch (Exception e){
            ErrorHelper.handleError(e, "ERROR - " + getClass().getSimpleName());
            responseMessage = "Error. Contact administrator";
        }

        return GenericResponse.makeResponse(responseMessage, responseResult, responseData);
    }

    public GenericResponse book(String rawToken, Map<String, Object> json, HttpServletResponse response) throws JsonProcessingException, JSONException {
        String responseMessage = "";
        boolean responseResult = false;
        HashMap<String, Object> responseData = new HashMap<>();
        Timeslot timeslot = null;

        try {
            String token = jwtService.getToken(rawToken);
            String renterId = jwtService.extractClaim(token, Claims::getId);

            JSONObject requestJSON = new JSONObject(json);
            String dogId = requestJSON.getString("dog_id");
            String timeslotId = requestJSON.getString("timeslot_id");
            LocalDate today = LocalDate.now();

            Dog dog = dogRepository.findById(dogId).orElse(null);
            timeslot = timeslotRepository.findById(timeslotId).orElse(null);

            // only proceed if dog and timeslot exist
            if (dog != null && timeslot != null){
                // check if timeslot belongs to the dog
                if(dog.get_id().equals(timeslot.getDog_id())){
                    // check timeslot availability
                    if(! timeslot.isBooked()){
                        // update timeslot availability
                        timeslot.setBooked(true);
                        timeslotRepository.save(timeslot);

                        // create booking
                        Booking booking = new Booking(null, dog.get_id(), renterId, dog.getOwner_id(), timeslot.get_id(), today, false);
                        booking = bookingRepository.save(booking);

                        responseData.put("booking_id", booking.get_id());
                        responseData.put("dog", dog.dogResponse());
                        responseData.put("timeslot", timeslot.timeslotResponse());
                        responseData.put("booking_date", booking.getBooking_date());

                        responseResult = true;
                        responseMessage = "Booking successful";
                    }
                    else{
                        responseMessage = "Sorry, the dog or timeslot you choose is not available anyore. Please choose another one";
                    }
                }
                else{
                    responseMessage = "Sorry, the dog or timeslot you choose is not available anyore. Please choose another one";
                }
            }
            else{
                responseMessage = "Sorry, the dog or timeslot you choose is not available anyore. Please choose another one";
            }
        }
        catch (Exception e) {
            // release booking
            if(timeslot != null){
                timeslot.setBooked(false);
                timeslotRepository.save(timeslot);
            }

//            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            responseMessage = "An error occurred while processing your request.";
        }

        return GenericResponse.makeResponse(responseMessage, responseResult, responseData);
    }

    public GenericResponse updateBooking(String rawToken, Map<String, Object> json, HttpServletResponse response) throws JsonProcessingException, JSONException {
        String responseMessage = "";
        boolean responseResult = false;
        HashMap<String, Object> responseData = new HashMap<>();
        Booking booking = null;

        try {
            String token = jwtService.getToken(rawToken);
            Claims claims = jwtService.extractAllClaim(token);

            String ownerId = claims.getId();
            String role = claims.get("role").toString();

            JSONObject requestJSON = new JSONObject(json);
            String bookingID = requestJSON.getString("booking_id");
            String action = requestJSON.getString("action").toLowerCase();

            // check if the role is owner
            if(role.equals("OWNER")){
                booking = bookingRepository.findBy_idAndOwner_id(bookingID, ownerId);

                if(booking != null){
                    // owner can only approve booking, if the booking hasn't been approved
                    if(! booking.isBooking_confirmed()) {
                        if(action.equals("approve")){
                            booking.setBooking_confirmed(true);
                            bookingRepository.save(booking);

                            responseMessage = "success";
                            responseResult = true;
                        }
                        else if(action.equals("reject")){
                            Timeslot timeslot = timeslotRepository.findById(booking.getTimeslot_id()).orElse(null);

                            if(timeslot != null){
                                timeslot.setBooked(false);
                                timeslotRepository.save(timeslot);

                                responseResult = true;
                                responseMessage = "success";
                            }
                            else{
                                responseMessage = "Failed to release timeslot";
                            }
                        }
                        else{
                            responseMessage = "Please approve or reject booking";
                        }
                    }
                    else{
                        responseMessage = "Booking has been previously confirmed/rejected. You cannot make any changes to this booking";
                    }
                }
                else{
                    responseMessage = "Booking data not found";
                }
            }
            else{
                responseMessage = "Not authorized";
            }
        }
        catch (Exception e) {
            // release booking
            if(booking != null){
                booking.setBooking_confirmed(false);
                bookingRepository.save(booking);
            }

            responseMessage = "An error occurred while processing your request.";
        }

        return GenericResponse.makeResponse(responseMessage, responseResult, responseData);
    }

}

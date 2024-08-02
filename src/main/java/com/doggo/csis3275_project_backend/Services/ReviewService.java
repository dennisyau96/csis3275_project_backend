package com.doggo.csis3275_project_backend.Services;

import com.doggo.csis3275_project_backend.Entities.Booking;
import com.doggo.csis3275_project_backend.Entities.GenericResponse;
import com.doggo.csis3275_project_backend.Entities.Message;
import com.doggo.csis3275_project_backend.Repositories.IBookingRepository;
import com.doggo.csis3275_project_backend.Repositories.IMessageRepository;
import com.doggo.csis3275_project_backend.exceptions.ErrorHelper;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletResponse;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
public class ReviewService {
    private IBookingRepository bookingRepository;

    private final JwtService jwtService;
    public ReviewService(IBookingRepository bookingRepository, JwtService jwtService){
        this.bookingRepository = bookingRepository;
        this.jwtService = jwtService;
    }

    public GenericResponse createReview(String rawToken, Map<String, Object> json){
        String responseMessage = "";
        boolean responseResult = false;
        HashMap<String, Object> responseData = new HashMap<>();
        try{
            String token = jwtService.getToken(rawToken);
            String sender_id = jwtService.extractClaim(token, Claims::getId);

            JSONObject requestJSON = new JSONObject(json);
            String booking_id = requestJSON.getString("booking_id");
            double rating = requestJSON.getDouble("review_rating");
            String comment = requestJSON.getString("review_comment");
            Booking booking = bookingRepository.findById(booking_id).orElse(null);
            //assert booking != null;
            if(booking.isBooking_completed()){
                booking.setReview_rating(rating);
                booking.setReview_comment(comment);


                bookingRepository.save(booking);

                responseData.put("booking_id", booking.get_id());
                responseData.put("review_rating", booking.getReview_rating());
                responseData.put("review_comment", booking.getReview_comment());

                responseMessage = "comment success";
                responseResult = true;
            }
            else {
                responseMessage = "Your booking is not completed. Please try when it is completed.";
            }

        }
        catch (Exception e) {
            ErrorHelper.handleError(e, "ERROR - " + getClass().getSimpleName());
            responseMessage = "error";
        }
        return GenericResponse.makeResponse(responseMessage, responseResult, responseData);
    }
}

package com.doggo.csis3275_project_backend.Services;

import com.doggo.csis3275_project_backend.Entities.Booking;
import com.doggo.csis3275_project_backend.Entities.GenericResponse;
import com.doggo.csis3275_project_backend.Entities.Transaction;
import com.doggo.csis3275_project_backend.Repositories.IBookingRepository;
import com.doggo.csis3275_project_backend.Repositories.ITimeslotRepository;
import com.doggo.csis3275_project_backend.Repositories.ITransactionRepository;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletResponse;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class TransactionService {
    private ITransactionRepository transactionRepository;
    private IBookingRepository bookingRepository;
    private ITimeslotRepository timeslotRepository;
    private final JwtService jwtService;

    public TransactionService(ITransactionRepository transactionRepository,IBookingRepository bookingRepository, ITimeslotRepository timeslotRepository, JwtService jwtService){
        this.transactionRepository = transactionRepository;
        this.bookingRepository = bookingRepository;
        this.timeslotRepository = timeslotRepository;
        this.jwtService = jwtService;
    }
    public GenericResponse showBill(String rawToken,Map<String, Object> json, HttpServletResponse response){
        String responseMessage = "success";
        boolean responseResult = false;
        HashMap<String, Object> responseData = new HashMap<>();
        try{
            String token = jwtService.getToken(rawToken);
            String user_id = jwtService.extractClaim(token, Claims::getId);
            JSONObject requestJSON = new JSONObject(json);
            String booking_id = requestJSON.getString("booking_id");
            Booking booking= bookingRepository.findById(booking_id).orElse(null);

            if(booking != null&& booking.getRenter_id().equals(user_id)){}
                Transaction transaction = new Transaction();
                Double price = booking.getPrice();
                Double tax = price*0.12;
                transaction.setBooking_id(booking_id);
                transaction.setRenter_id(booking.getRenter_id());
                transaction.setPrice(price);
                transaction.setTax(tax);
                transaction.setTotal_amount(price+tax);
                transaction.setIs_paid(false);
                transactionRepository.save(transaction);

                responseData.put("price", transaction.getPrice());
                responseData.put("tax", transaction.getTax());
                responseData.put("total_amount", transaction.getTotal_amount());

                responseMessage = "Do you want to pay now?";
                responseResult = true;



        }
        catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            responseMessage = "An error occurred, please try again.";

        }



        return GenericResponse.makeResponse(responseMessage, responseResult, responseData);
    }

    public GenericResponse pay(String rawToken,Map<String, Object> json){
        String responseMessage = "success";
        boolean responseResult = false;
        HashMap<String, Object> responseData = new HashMap<>();
        try {

            String token = jwtService.getToken(rawToken);
            String user_id = jwtService.extractClaim(token, Claims::getId);
            JSONObject requestJSON = new JSONObject(json);
            String booking_id = requestJSON.getString("booking_id");
            String transaction_id = requestJSON.getString("transaction_id");
            Booking booking = bookingRepository.findById(booking_id).orElse(null);
            if (booking != null && booking.getBooking_confirmed()!=null && booking.getBooking_confirmed()) {
                Transaction transaction = transactionRepository.findById(transaction_id).orElse(null);
                if (transaction != null) {
                    transaction.setIs_paid(true);
                    transactionRepository.save(transaction);

                    responseMessage = "Bill is paid successfully";
                    responseResult = true;
                } else {
                    responseMessage = "Transaction not found";
                }
            } else {
                responseMessage = "Your booking is not confirmed yet";
            }
            /*if(booking != null && booking.getBooking_confirmed().equals(true)){
                Transaction transaction = transactionRepository.findById(transaction_id).orElse(null);
                transaction.setIs_paid(true);
                responseMessage = "Bill is paid successfully";
                responseResult = true;
            }
            else{
                responseMessage = "Your booking is not confirmed yet";
            }*/
        }
        catch (Exception e) {
            //response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            responseMessage = "An error occurred, please try again.";

        }

        return GenericResponse.makeResponse(responseMessage, responseResult, responseData);
    }







}

package com.doggo.csis3275_project_backend.Services;

import com.doggo.csis3275_project_backend.Entities.Customer;
import com.doggo.csis3275_project_backend.Entities.GenericResponse;
import com.doggo.csis3275_project_backend.Repositories.ICustomerRepository;
import com.doggo.csis3275_project_backend.exceptions.ErrorHelper;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletResponse;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
public class CustomerService {
    private ICustomerRepository customerRepository;
    private Customer customer;

    private final JwtService jwtService;

    public CustomerService(ICustomerRepository customerRepository, JwtService jwtService) {
        this.customerRepository = customerRepository;
        this.jwtService = jwtService;
    }

    public GenericResponse getUserData(String rawToken, HttpServletResponse response){
//        String token = jwtService.getToken(rawToken);
//        String id = jwtService.extractClaim(token, Claims::getId);
//        System.out.println(id);

        String responseMessage = "";
        boolean responseResult = false;
        HashMap<String, Object> responseData = new HashMap<>();

        try{
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            customer = (Customer) authentication.getPrincipal();

            responseData.put("username", customer.getUsername());
            responseData.put("firstName", customer.getFirstName());
            responseData.put("lastName", customer.getLastName());
            responseData.put("email", customer.getEmail());
            responseData.put("phone", customer.getPhone());
            responseData.put("profilePic", customer.getProfilePic());
            responseData.put("profile", customer.getProfile());

            responseResult = true;
            responseMessage = "success";
        }
        catch (Exception e){
            ErrorHelper.handleError(e, "ERROR - " + getClass().getSimpleName());
            responseMessage = "Error. Contact administrator";
        }

        return GenericResponse.makeResponse(responseMessage, responseResult, responseData);
    }
}

package com.doggo.csis3275_project_backend.Services;

import com.doggo.csis3275_project_backend.Entities.Customer;
import com.doggo.csis3275_project_backend.Repositories.ICustomerRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import org.json.JSONObject;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class CustomerService {
    private ICustomerRepository customerRepository;
    private Customer customer;
    private String message = "";

    public CustomerService(ICustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public String getUserData(HttpServletResponse response){
        JSONObject responseJson = new JSONObject();
        ObjectMapper mapper = new ObjectMapper();
        mapper.findAndRegisterModules();

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        customer = (Customer) authentication.getPrincipal();

        try{
            JSONObject customerJSON = new JSONObject(mapper.writeValueAsString(customer));
            responseJson.put("user", customerJSON);
        }
        catch (Exception e){
            message = "Error";
        }

        responseJson.put("message", message);

        return responseJson.toString();
    }
}

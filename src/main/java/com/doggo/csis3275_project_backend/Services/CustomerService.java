package com.doggo.csis3275_project_backend.Services;

import com.doggo.csis3275_project_backend.Entities.Customer;
import com.doggo.csis3275_project_backend.Repositories.ICustomerRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CustomerService {
    private ICustomerRepository customerRepository;
    private Customer customer;
    private String message = "";

    @Autowired
    public CustomerService(ICustomerRepository customerRepository){
        this.customerRepository = customerRepository;
    }

    public CustomerService(){}

    public String register(Customer customer, HttpServletResponse response) throws JsonProcessingException, JSONException {
        JSONObject responseJson = new JSONObject();
        ObjectMapper mapper = new ObjectMapper();
        mapper.findAndRegisterModules();

        //validate required fields
        if(customer.getUsername() == null || customer.getPassword().equals("")){
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            message = "Username not found";
        }
        //check if user exist
        else if(customerRepository.getCustomerByUsername(customer.getUsername()) != null){
            message = "Please logged in to your account";
        }
        else{
            customer = customerRepository.save(customer);
            JSONObject customerJSON = new JSONObject(mapper.writeValueAsString(customer));
            customerJSON.put("id", customer.getId().toHexString());
            responseJson.put("saveCustomerResponse", customerJSON);
            message = "success";
        }

        responseJson.put("message", message);

        return responseJson.toString();
    }

    public String getCustomer(String username, String password, HttpServletResponse response){
        JSONObject responseJson = new JSONObject();
        ObjectMapper mapper = new ObjectMapper();
        mapper.findAndRegisterModules();

        customer = customerRepository.getCustomerByUsername(username);
        if(customer != null && password.equals(customer.getPassword())){
            message = "success";
            responseJson.put("success", true);
        }
        else{
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            message = "Wrong username or password";
        }

        responseJson.put("message", message);

        return responseJson.toString();
    }
}

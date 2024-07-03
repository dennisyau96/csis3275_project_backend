package com.doggo.csis3275_project_backend.Web;


import com.doggo.csis3275_project_backend.Entities.Customer;
import com.doggo.csis3275_project_backend.Services.CustomerService;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api")
public class AuthenticationController {
    private Logger logger = LoggerFactory.getLogger(AuthenticationController.class);

    private final CustomerService customerService;

    @Autowired
    public AuthenticationController(CustomerService customerService){
        this.customerService = customerService;
    }

    @PostMapping(path = "/login")
    public String login(@RequestBody Customer customer, HttpServletResponse response) throws JsonProcessingException{
        return customerService.getCustomer(customer.getUsername(), customer.getPassword(), response);
    }
}

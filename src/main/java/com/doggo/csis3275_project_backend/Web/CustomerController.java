package com.doggo.csis3275_project_backend.Web;

import com.doggo.csis3275_project_backend.Entities.Customer;
import com.doggo.csis3275_project_backend.Services.AuthenticationService;
import com.doggo.csis3275_project_backend.Services.CustomerService;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/users")
public class CustomerController {
    private Logger logger = LoggerFactory.getLogger(CustomerController.class);

    private final CustomerService customerService;

    @Autowired
    public CustomerController(CustomerService customerService){
        this.customerService = customerService;
    }

    @GetMapping("/me")
    public String getAuthenticatedUserData(HttpServletResponse response){

        return customerService.getUserData(response);
    }


}

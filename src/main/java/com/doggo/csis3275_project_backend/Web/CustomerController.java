package com.doggo.csis3275_project_backend.Web;

import com.doggo.csis3275_project_backend.Entities.GenericResponse;
import com.doggo.csis3275_project_backend.Services.CustomerService;
import com.doggo.csis3275_project_backend.Services.JwtService;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/users")
public class CustomerController {
//    @RequestHeader (name="Authorization") String token
//    System.out.println(JwtHelper.getToken(token));

    private Logger logger = LoggerFactory.getLogger(CustomerController.class);

    private final CustomerService customerService;

    @Autowired
    public CustomerController(CustomerService customerService){
        this.customerService = customerService;
    }

    @GetMapping("/me")
    public GenericResponse getAuthenticatedUserData(@RequestHeader (name="Authorization") String rawToken, HttpServletResponse response){
        return customerService.getUserData(rawToken, response);
    }


}

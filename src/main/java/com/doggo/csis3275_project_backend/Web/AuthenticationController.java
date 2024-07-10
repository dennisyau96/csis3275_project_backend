package com.doggo.csis3275_project_backend.Web;


import com.doggo.csis3275_project_backend.Entities.Customer;
import com.doggo.csis3275_project_backend.Entities.GenericResponse;
import com.doggo.csis3275_project_backend.Services.AuthenticationService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {
    private Logger logger = LoggerFactory.getLogger(AuthenticationController.class);

    private final AuthenticationService authenticationService;

    @Autowired
    public AuthenticationController(AuthenticationService authenticationService){
        this.authenticationService = authenticationService;
    }

    @PostMapping(path = "/login")
    //public String login(@RequestBody Customer customer, HttpServletResponse response) throws JsonProcessingException{
    public GenericResponse login(@RequestBody Customer customer, HttpServletResponse response) throws JsonProcessingException{
        // call to function in "xxxxService"
        return authenticationService.login(customer.getUsername(), customer.getPassword(), response);
    }


    @PostMapping(path = "/register")
    public GenericResponse register(@RequestBody Customer customer, HttpServletResponse response) throws JsonProcessingException {
        return authenticationService.register(customer, response);
    }
}

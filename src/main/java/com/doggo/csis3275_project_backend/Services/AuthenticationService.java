package com.doggo.csis3275_project_backend.Services;

import com.doggo.csis3275_project_backend.Entities.Customer;
import com.doggo.csis3275_project_backend.Entities.GenericResponse;
import com.doggo.csis3275_project_backend.Repositories.ICustomerRepository;
import com.doggo.csis3275_project_backend.exceptions.ErrorHelper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
public class AuthenticationService {
    private ICustomerRepository customerRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private Customer customer;

    public AuthenticationService(ICustomerRepository customerRepository, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, JwtService jwtService){
        this.customerRepository = customerRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

//    public GenericResponse register(Customer customer, HttpServletResponse response) throws JsonProcessingException, JSONException {
    public GenericResponse register(Customer customer, HttpServletResponse response) {
        String responseMessage = "";
        boolean responseResult = false;
        HashMap<String, Object> responseData = new HashMap<>();

        try{
            //validate required fields
            if(customer.getUsername() == null || customer.getPassword().equals("")){
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                responseMessage = "The submitted data is incomplete. Please complete your data before register";
            }
            //check if user exist
            else if(customerRepository.getCustomerByUsername(customer.getUsername()) != null){
                responseMessage = "Please logged in to your account";
            }
            else{
                Customer newCustomer = new Customer(customer.getUsername(), passwordEncoder.encode(customer.getPassword()),
                        customer.getFirstName(), customer.getLastName(), customer.getEmail(), customer.getPhone());

                newCustomer = customerRepository.save(newCustomer);
//                newCustomer.setId(newCustomer.getId());
//                responseData.put("customer", newCustomer);

                responseData.put("id", newCustomer.getId());
                responseData.put("username", newCustomer.getUsername());
                responseData.put("firstName", newCustomer.getFirstName());
                responseData.put("lastName", newCustomer.getLastName());
                responseData.put("email", newCustomer.getEmail());
                responseData.put("phone", newCustomer.getPhone());

//            JSONObject customerJSON = new JSONObject(mapper.writeValueAsString(newCustomer));
//            customerJSON.put("id", newCustomer.getId().toHexString());
//            responseJson.put("saveCustomerResponse", customerJSON);
                responseMessage = "success";
                responseResult = true;
            }
        }
        catch (Exception e){
            ErrorHelper.handleError(e, "ERROR - " + getClass().getSimpleName());
            responseMessage = "Error. Contact administrator";
        }

//
//        responseJson.put("message", responseMessage);
//
//        return responseJson.toString();
        return GenericResponse.makeResponse(responseMessage, responseResult, responseData);
    }

    public GenericResponse login(String username, String password, HttpServletResponse response, HttpServletRequest request) {
        // on the start of the function in this class, always define these 3 variables (just copy paste)
        // 1. responseMessage = "";
        // 2. responseResult = false;
        // 3. responseData = new HashMap<>();
        //
        // then, use try catch
        // this is the template
        //
        // try {
        //      do something here
        //   }
        // catch(Exception e){
        //            handleError(e);
        //        }

        String responseMessage = "";
        boolean responseResult = false;
        HashMap<String, Object> responseData = new HashMap<>();

        try{
            // since this is a login function, the logic here is to do user authentication
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, password)
            );

            // query data from DB by calling the "repository"
            customer = customerRepository.getCustomerByUsername(username);
//            Map<String, Object> extraClaims = new HashMap<>();
//            extraClaims.put("id", customer.getId());

            // logic to check whether user logged in successfully
            // if the result of the query is null, meaning no data returned, then the user is not found
            // otherwise, we can found the user in DB, which means the login is success
            if(customer != null){
                // logic for session token. You don't have to worry about this. You won't deal with this in other APIs
                String jwtToken = jwtService.generateToken(customer, customer.getId());

                //HttpSession session = request.getSession();
                //session.setAttribute("userId", customer.getId());

                // this is what you have to set to indicate if the backend code successfully do the operation
                // if the backend code successfully do the operation, "responseMessage" value always "success"
                // otherwise, put meaningful message e.g. "Dog not found"
                //
                // possible value for "responseResult":
                // true -> if the backend code successfully do the operation
                // false -> the backend code failed to do the operation
                responseMessage = "success";
                responseResult = true;

                // in this function, the data that need to be returned to the user is "jwtToken"
                // use "responseData" variable for this. It is a key-value pair
                // syntax: responseData.put("key", value);
                responseData.put("token", jwtToken);
            }
            else{
//                response.setStatus(HttpServletResponse.SC_NOT_FOUND);

                // the else here means the query result is null, hence I return the following message to the user
                responseMessage = "Incorrect username or password";
            }
        }
        catch (AuthenticationException e){
            // same error message as above
            responseMessage = "Incorrect username or password";
        }
        catch(Exception e){
            // any other error will go to here
            // just do it like this (copy & paste)
            ErrorHelper.handleError(e, "ERROR - " + getClass().getSimpleName());
            responseMessage = "Error. Contact administrator";
        }

        // return the result. this always be the same. just copy paste it
        return GenericResponse.makeResponse(responseMessage, responseResult, responseData);
    }



}

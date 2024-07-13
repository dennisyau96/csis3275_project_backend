package com.doggo.csis3275_project_backend.Services;

import com.doggo.csis3275_project_backend.Entities.Customer;
import com.doggo.csis3275_project_backend.Entities.Dog;
import com.doggo.csis3275_project_backend.Entities.GenericResponse;
import com.doggo.csis3275_project_backend.Repositories.ICustomerRepository;
import com.doggo.csis3275_project_backend.Repositories.IDogRepository;
import com.doggo.csis3275_project_backend.exceptions.ErrorHelper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.bson.types.ObjectId;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Optional;

@Service
public class DogService {


    private IDogRepository dogRepository;
    private Dog dog;
    private String message = "";
    //private int dog_id;


    public DogService(IDogRepository dogRepository){
        this.dogRepository = dogRepository;
    }

    public GenericResponse<Page<Dog>> getDogs(int pageNo, int pageSize) {
        String responseMessage = "success";
        boolean responseResult = false;
        HashMap<String, Object> responseData = new HashMap<>();

        try{
            Pageable pageable = PageRequest.of(pageNo, pageSize);
            Page<Dog> dogs = dogRepository.findAll(pageable);
            responseResult = true;

            responseData.put("dogs", dogs);
        }
        catch (Exception e){
            ErrorHelper.handleError(e, "ERROR - " + getClass().getSimpleName());
            responseMessage = "Error. Contact administrator";
        }

        return GenericResponse.makeResponse(responseMessage, responseResult, responseData);
    }

    public GenericResponse getDog(String id, HttpServletResponse response){
        String responseMessage = "success";
        boolean responseResult = false;
        HashMap<String, Object> responseData = new HashMap<>();


        try{
            Optional<Dog> dog = dogRepository.findById(id);

            if(dog != null){
                responseResult = true;
                responseData.put("dogs", dog);
            }
            else{
                responseResult = false;
                responseMessage = "Dog not found";
            }

        }
        catch (Exception e){
            ErrorHelper.handleError(e, "ERROR - " + getClass().getSimpleName());
            responseMessage = "Error. Contact administrator";
        }

        return GenericResponse.makeResponse(responseMessage, responseResult, responseData);

//        JSONObject responseJson = new JSONObject();
//
//        try{
//
//            ObjectMapper mapper = new ObjectMapper();
//            mapper.findAndRegisterModules();
//            dog = dogRepository.getDogByName(name);
//
//            if(dog != null){
//                message = dog.getName();
//                responseJson.put("success", true);
//            }
//            else{
//                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
//                message = "Wrong username or password";
//
//            }
//        }
//        catch (Exception e){
//            ErrorHelper.handleError(e, "ERROR - " + getClass().getSimpleName());
//        }
//
//
//        responseJson.put("message", message);
//
//        return responseJson.toString();
    }

    public GenericResponse addDog(Dog dog, HttpServletResponse response) throws JsonProcessingException, JSONException {
        String responseMessage = "";
        boolean responseResult = false;
        HashMap<String, Object> responseData = new HashMap<>();

        try {
            // Retrieve the user ID from the session
            //HttpSession session = request.getSession();
            //Integer userId = (Integer) session.getAttribute("userId");

            // Check if the user ID is set
            /*if (userId == null) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                responseMessage = "User is not logged in.";
                return GenericResponse.makeResponse(responseMessage, responseResult, responseData);
            }*/

            //Data required Name, Breed, Age, Sex,Profile pic, rental price/hr, location, desexed, vaccinated, profile description
            if (dog.getName() == null || dog.getBreed() == null || dog.getAge() == null || dog.getSex() == null ||
                    dog.getProfile_pic() == null || dog.getRental_price_per_hour() == null || dog.getLocation() == null ||
                    dog.getDesexed() == null || dog.getVaccinated() == null || dog.getProfile_description() == null) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                responseMessage = "The submitted data is incomplete. Please complete required information";
            }
            else {
                // Set the owner ID in the dog object
                //dog.setOwner_id(userId);

                // Save the dog object
                Dog newDog = dogRepository.save(dog);

                // Populate the response data
                responseData.put("id", newDog.get_id());
                responseData.put("owner_id", newDog.getOwner_id());
                responseData.put("service_id", newDog.getService_id());
                responseData.put("name", newDog.getName());
                responseData.put("breed", newDog.getBreed());
                responseData.put("age", newDog.getAge());
                responseData.put("sex", newDog.getSex());
                responseData.put("available_timeslot", newDog.getAvailable_timeslot());
                responseData.put("additional_message", newDog.getAdditional_message());
                responseData.put("profile_pic", newDog.getProfile_pic());
                responseData.put("rental_price_per_hour", newDog.getRental_price_per_hour());
                responseData.put("location", newDog.getLocation());
                responseData.put("desexed", newDog.getDesexed());
                responseData.put("vaccinated", newDog.getVaccinated());
                responseData.put("average_rating", newDog.getAverage_rating());
                responseData.put("profile_description", newDog.getProfile_description());

                responseMessage = "Success";
                responseResult = true;
            }
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            responseMessage = "An error occurred while processing your request.";
        }

        return GenericResponse.makeResponse(responseMessage, responseResult, responseData);
    }

    /*public GenericResponse addDog(Dog dog, HttpServletResponse response, HttpServletRequest request) throws JsonProcessingException, JSONException {
        String responseMessage = "";
        boolean responseResult = false;
        HashMap<String, Object> responseData = new HashMap<>();

        try {
            if (dog.getName() == null) {
                //response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                responseMessage = "The submitted data is incomplete. Please complete required information";
            }
            else {

                Dog newDog = dogRepository.save(dog);

                responseData.put("id", newDog.get_id());
                responseData.put("dog_id", newDog.getDog_id());
                responseData.put("owner_id", newDog.getOwner_id());
                responseData.put("service_id", newDog.getService_id());
                responseData.put("name", newDog.getName());
                responseData.put("breed", newDog.getBreed());
                responseData.put("age", newDog.getAge());
                responseData.put("sex", newDog.getSex());
                responseData.put("available_timeslot", newDog.getAvailable_timeslot());
                responseData.put("additional_message", newDog.getAdditional_message());
                responseData.put("profile_pic", newDog.getProfile_pic());
                responseData.put("rental_price_per_hour", newDog.getRental_price_per_hour());
                responseData.put("location", newDog.getLocation());
                responseData.put("desexed", newDog.getDesexed());
                responseData.put("vaccinated", newDog.getVaccinated());
                responseData.put("average_rating", newDog.getAverage_rating());
                responseData.put("profile_description", newDog.getProfile_description());

                responseMessage = "Success";
                responseResult = true;
            }
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            responseMessage = "An error occurred while processing your request.";
        }

        return GenericResponse.makeResponse(responseMessage, responseResult, responseData);
    }*/
    public GenericResponse deleteDog(String _id, HttpServletResponse response){
        String responseMessage = "";
        boolean responseResult = false;
        HashMap<String, Object> responseData = new HashMap<>();

        try{
            dog = dogRepository.getDogBy_id(_id);

            if (dog.get_id() != null){
                dogRepository.delete(dog);
                responseMessage = "The dog is deleted";
                responseResult = true;
            }
        }
        catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            responseMessage = "An error occurred while processing your request.";

        }




        return GenericResponse.makeResponse(responseMessage, responseResult, responseData);
    }


}

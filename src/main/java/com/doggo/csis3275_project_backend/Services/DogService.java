package com.doggo.csis3275_project_backend.Services;

import com.doggo.csis3275_project_backend.Entities.Customer;
import com.doggo.csis3275_project_backend.Entities.Dog;
import com.doggo.csis3275_project_backend.Entities.GenericResponse;
import com.doggo.csis3275_project_backend.Repositories.ICustomerRepository;
import com.doggo.csis3275_project_backend.Repositories.IDogRepository;
import com.doggo.csis3275_project_backend.exceptions.ErrorHelper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
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
    private final JwtService jwtService;


    public DogService(IDogRepository dogRepository, JwtService jwtService){
        this.dogRepository = dogRepository;
        this.jwtService = jwtService;
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
    }

    public GenericResponse addDog(String rawToken, Dog dog, HttpServletResponse response) throws JsonProcessingException, JSONException {
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
            String token = jwtService.getToken(rawToken);
            String owner_id = jwtService.extractClaim(token, Claims::getId);

            //Data required Name, Breed, Age, Sex,Profile pic, rental price/hr, location, desexed, vaccinated, profile description
            if (dog.getName() == null || dog.getBreed() == null || dog.getAge() == null || dog.getSex() == null ||
                    dog.getProfile_pic() == null || dog.getRental_price_per_hour() == null || dog.getLocation() == null ||
                    dog.getDesexed() == null || dog.getVaccinated() == null || dog.getProfile_description() == null) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                responseMessage = "The submitted data is incomplete. Please complete required information";
            }
            else {
                // Set the owner ID in the dog object
                dog.setOwner_id(owner_id);


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
    public GenericResponse deleteDog(String rawToken,String _id, HttpServletResponse response){
        String responseMessage = "";
        boolean responseResult = false;
        HashMap<String, Object> responseData = new HashMap<>();
        String token = jwtService.getToken(rawToken);
        String owner_id = jwtService.extractClaim(token, Claims::getId);

        try{
            dog = dogRepository.getDogBy_id(_id);

            if (dog.get_id() != null && dog.getOwner_id().equals(owner_id)){
                dogRepository.delete(dog);
                responseMessage = "The dog profile is deleted";
                responseResult = true;
            }
            else{
                responseMessage = "You are not authorized to delete this dog";
            }
        }
        catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            responseMessage = "An error occurred while processing your request.";

        }




        return GenericResponse.makeResponse(responseMessage, responseResult, responseData);
    }
    public GenericResponse updateDog(String rawToken,String _id, Dog dog_update, HttpServletResponse response){
        String responseMessage = "";
        boolean responseResult = false;
        HashMap<String, Object> responseData = new HashMap<>();

        String token = jwtService.getToken(rawToken);
        String owner_id = jwtService.extractClaim(token, Claims::getId);

        try{
            Dog dog_original = dogRepository.getDogBy_id(_id);
            if (dog_original != null && dog_original.getOwner_id().equals(owner_id)){

                if (dog_update.getName() == null || dog_update.getBreed() == null || dog_update.getAge() == null || dog_update.getSex() == null ||
                        dog_update.getProfile_pic() == null || dog_update.getRental_price_per_hour() == null || dog_update.getLocation() == null ||
                        dog_update.getDesexed() == null || dog_update.getVaccinated() == null || dog_update.getProfile_description() == null) {
                    //response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    responseMessage = "The submitted data is incomplete. Please complete required information";
                }
                else {
                    dogRepository.save(dog_update);

                    responseData.put("id", dog_update.get_id());
                    responseData.put("owner_id", dog_update.getOwner_id());
                    responseData.put("service_id", dog_update.getService_id());
                    responseData.put("name", dog_update.getName());
                    responseData.put("breed", dog_update.getBreed());
                    responseData.put("age", dog_update.getAge());
                    responseData.put("sex", dog_update.getSex());
                    responseData.put("available_timeslot", dog_update.getAvailable_timeslot());
                    responseData.put("additional_message", dog_update.getAdditional_message());
                    responseData.put("profile_pic", dog_update.getProfile_pic());
                    responseData.put("rental_price_per_hour", dog_update.getRental_price_per_hour());
                    responseData.put("location", dog_update.getLocation());
                    responseData.put("desexed", dog_update.getDesexed());
                    responseData.put("vaccinated", dog_update.getVaccinated());
                    responseData.put("average_rating", dog_update.getAverage_rating());
                    responseData.put("profile_description", dog_update.getProfile_description());

                    responseMessage = "Updated the dog successfully";
                    responseResult = true;
                }

            }
            else{
                responseMessage = "You are not authorized to update this dog";
            }

        }
        catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            responseMessage = "An error occurred while processing your request.";

        }


        return GenericResponse.makeResponse(responseMessage, responseResult, responseData);
    }

    public GenericResponse addTimeSlot(HttpServletResponse response){
        String responseMessage = "success";
        boolean responseResult = false;
        HashMap<String, Object> responseData = new HashMap<>();

        try {

        }
        catch (Exception e){
            ErrorHelper.handleError(e, "ERROR - " + getClass().getSimpleName());
            responseMessage = "Error. Contact administrator";
        }

        return GenericResponse.makeResponse(responseMessage, responseResult, responseData);
    }
}

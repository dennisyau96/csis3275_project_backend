package com.doggo.csis3275_project_backend.Services;

import com.doggo.csis3275_project_backend.Entities.Dog;
import com.doggo.csis3275_project_backend.Entities.GenericResponse;
import com.doggo.csis3275_project_backend.Entities.Timeslot;
import com.doggo.csis3275_project_backend.Repositories.IDogRepository;
import com.doggo.csis3275_project_backend.Repositories.ITimeslotRepository;
import com.doggo.csis3275_project_backend.exceptions.ErrorHelper;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletResponse;
import org.bson.types.ObjectId;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

@Service
public class DogService {


    private IDogRepository dogRepository;
    private ITimeslotRepository timeslotRepository;
    private Dog dog;
    private String message = "";
    //private int dog_id;
    private final JwtService jwtService;


    public DogService(IDogRepository dogRepository, ITimeslotRepository timeslotRepository, JwtService jwtService){
        this.dogRepository = dogRepository;
        this.timeslotRepository = timeslotRepository;
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

                dog_update.setOwner_id(owner_id);
                if(dog_update.getService_id() == null){
                    dog_update.setService_id(dog_original.getService_id());
                }
                if(dog_update.getName() == null){
                    dog_update.setName(dog_original.getName());
                }
                if(dog_update.getBreed() == null){
                    dog_update.setBreed(dog_original.getBreed());
                }
                if(dog_update.getAge() == null){
                    dog_update.setAge(dog_original.getAge());
                }
                if(dog_update.getSex() == null){
                    dog_update.setSex(dog_original.getSex());
                }
                if(dog_update.getAdditional_message() == null){
                    dog_update.setAdditional_message(dog_original.getAdditional_message());
                }
                if(dog_update.getProfile_pic() == null){
                    dog_update.setProfile_pic(dog_original.getProfile_pic());
                }
                if(dog_update.getRental_price_per_hour() == null){
                    dog_update.setRental_price_per_hour(null);
                }
                if(dog_update.getLocation() == null){
                    dog_update.setLocation(dog_original.getLocation());
                }
                if(dog_update.getDesexed() == null){
                    dog_update.setDesexed(dog_original.getDesexed());
                }
                if(dog_update.getVaccinated() == null){
                    dog_update.setVaccinated(dog_original.getVaccinated());
                }
                if(dog_update.getAverage_rating() == null){
                    dog_update.setAverage_rating(dog_original.getAverage_rating());
                }
                if(dog_update.getProfile_description() == null){
                    dog_update.setProfile_description(dog_original.getProfile_description());
                }
                dogRepository.save(dog_update);
                responseData.put("id", dog_update.get_id());
                responseData.put("owner_id", dog_update.getOwner_id());
                responseData.put("service_id", dog_update.getService_id());
                responseData.put("name", dog_update.getName());
                responseData.put("breed", dog_update.getBreed());
                responseData.put("age", dog_update.getAge());
                responseData.put("sex", dog_update.getSex());
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



                /*if (dog_update.getName() == null || dog_update.getBreed() == null || dog_update.getAge() == null || dog_update.getSex() == null ||
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
                }*/

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

    public GenericResponse getTimeslot(String dogId, HttpServletResponse response){
        String responseMessage = "success";
        boolean responseResult = false;
        HashMap<String, Object> responseData = new HashMap<>();

        try {
            List<Timeslot> timeslots = timeslotRepository.findAllByDog_id(dogId);
            responseData.put("timeslots", timeslots);

            if(timeslots.size() > 0){
                responseResult = true;
            }
            else{
                responseMessage = "No available timeslots";
            }
        }

        catch (Exception e){
            ErrorHelper.handleError(e, "ERROR - " + getClass().getSimpleName());
            responseMessage = "Error. Contact administrator";
        }

        return GenericResponse.makeResponse(responseMessage, responseResult, responseData);
    }

    public GenericResponse addTimeslot(String rawToken, Map<String, Object> json, HttpServletResponse response){
        String responseMessage = "success";
        boolean responseResult = false;
        HashMap<String, Object> responseData = new HashMap<>();

        try {
            JSONObject requestJSON = new JSONObject(json);
            JSONArray timeslots = requestJSON.getJSONArray("timeslots");

            // check if there is timeslot
            if(timeslots.length() > 0){
                String dogId = requestJSON.getString("dog_id");

                // check if the dog belongs to the user
                String token = jwtService.getToken(rawToken);
                String owner_id = jwtService.extractClaim(token, Claims::getId);

                Dog dog = dogRepository.findBy_idAndOwner_id(dogId, owner_id);

                List<Timeslot> existTimeslots = timeslotRepository.findAllByDog_id(dogId);
                int duplicateCount = 0;

                if(dog != null){
                    List<Timeslot> newTimeslots = new ArrayList<>(timeslots.length());

                    for (int i=0; i<timeslots.length(); i++){
                        boolean duplicate = false;

//                    System.out.println(timeslots.get(i));
                        JSONObject tmp = timeslots.getJSONObject(i);
                        LocalDate date = LocalDate.parse(tmp.getString("date"));
                        LocalTime startTime = LocalTime.parse(tmp.getString("start_time"));
                        LocalTime endTime = LocalTime.parse(tmp.getString("end_time"));

                        Timeslot timeslot = new Timeslot(null, dogId, date, startTime, endTime, false);

                        // check for duplicate timeslot
                        for (Timeslot eTimeslot : existTimeslots){
                            // if same date, check the time
                            if(timeslot.getDate().isEqual(eTimeslot.getDate())){
                                // 2 conditions considered as duplicate
                                // if the new start time between the existing timeslot AND
                                // new end time between the existing timeslot
                                if((timeslot.getStart_time().isAfter(eTimeslot.getStart_time()) &&
                                        timeslot.getStart_time().isBefore(eTimeslot.getEnd_time())) ||
                                        (timeslot.getEnd_time().isAfter(eTimeslot.getStart_time()) &&
                                                timeslot.getEnd_time().isBefore(eTimeslot.getEnd_time()))){
                                    duplicate = true;
                                    duplicateCount++;
//                                responseMessage = "Successfully save some timeslots. Duplicated or overlapping timeslots are not saved";
                                    break;
                                }
                                // if start and end time exactly the same. it is duplicated
                                else if(timeslot.getStart_time().compareTo(eTimeslot.getStart_time()) == 0 ||
                                        timeslot.getEnd_time().compareTo(eTimeslot.getEnd_time()) == 0){
                                    duplicate = true;
                                    duplicateCount++;
//                                responseMessage = "Successfully save some timeslots. Duplicated or overlapping timeslots are not saved";
                                    break;
                                }
                            }
                        }

                        if(!duplicate){
                            newTimeslots.add(timeslot);
                        }
                    }

                    timeslotRepository.saveAll(newTimeslots);
                    // there are both duplicate and not duplicate data
                    if(duplicateCount > 0 && newTimeslots.size() > 0){
                        responseMessage = "Successfully save some timeslots. Duplicated or overlapping timeslots are not saved";
                        responseResult = true;
                    }
                    else if(duplicateCount > 0){ //only duplicate data
                        responseMessage = "Failed to save timeslots because of duplicate or overlapping timeslots";
                    }
                    else{ // no duplicate data
                        responseResult = true;
                    }

                    responseData.put("timeslots", newTimeslots);
                }
                else{
                    responseMessage = "Not authorized";
                }
            }
            else{
                responseMessage = "Failed. Please add new timeslots";
            }

        }
        catch (Exception e){
            ErrorHelper.handleError(e, "ERROR - " + getClass().getSimpleName());
            responseMessage = "Error. Contact administrator";
        }

        return GenericResponse.makeResponse(responseMessage, responseResult, responseData);
    }
}

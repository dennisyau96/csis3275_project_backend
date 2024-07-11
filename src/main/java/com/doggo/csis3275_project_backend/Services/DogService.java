package com.doggo.csis3275_project_backend.Services;

import com.doggo.csis3275_project_backend.Entities.Dog;
import com.doggo.csis3275_project_backend.Entities.GenericResponse;
import com.doggo.csis3275_project_backend.Repositories.ICustomerRepository;
import com.doggo.csis3275_project_backend.Repositories.IDogRepository;
import com.doggo.csis3275_project_backend.exceptions.ErrorHelper;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import org.bson.types.ObjectId;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

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

}

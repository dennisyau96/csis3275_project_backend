package com.doggo.csis3275_project_backend.Services;

import com.doggo.csis3275_project_backend.Entities.Dog;
import com.doggo.csis3275_project_backend.Repositories.ICustomerRepository;
import com.doggo.csis3275_project_backend.Repositories.IDogRepository;
import com.doggo.csis3275_project_backend.exceptions.ErrorHelper;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DogService {


    private IDogRepository dogRepository;
    private Dog dog;
    private String message = "";
    //private int dog_id;


    public DogService(IDogRepository dogRepository){
        this.dogRepository = dogRepository;
    }
//    public DogService(){}

    public String getDog(String name, HttpServletResponse response){
        JSONObject responseJson = new JSONObject();

        try{

            ObjectMapper mapper = new ObjectMapper();
            mapper.findAndRegisterModules();
            dog = dogRepository.getDogByName(name);
            if(dog != null){
                message = dog.getName();
                responseJson.put("success", true);
            }
            else{
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                message = "Wrong username or password";

            }
        }
        catch (Exception e){
            ErrorHelper.handleError(e, "ERROR - " + getClass().getSimpleName());
        }


        responseJson.put("message", message);

        return responseJson.toString();



    }

}

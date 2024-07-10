package com.doggo.csis3275_project_backend.Web;


import com.doggo.csis3275_project_backend.Entities.Dog;

import com.doggo.csis3275_project_backend.Services.DogService;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api")
public class DogController {
    private final DogService dogService;

    @Autowired
    public DogController(DogService dogService){
        this.dogService = dogService;
    }
    @PostMapping(path = "/showDog")
    public String showDog(@RequestBody Dog dog, HttpServletResponse response) throws JsonProcessingException{
        return dogService.getDog(dog.getName(),response);
    }

}

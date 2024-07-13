package com.doggo.csis3275_project_backend.Web;


import com.doggo.csis3275_project_backend.Entities.Dog;

import com.doggo.csis3275_project_backend.Entities.GenericResponse;
import com.doggo.csis3275_project_backend.Services.DogService;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
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

    @GetMapping("/getDogs")
    public GenericResponse<Page<Dog>> getDogs(@RequestParam(defaultValue = "0") int pageNo, @RequestParam(defaultValue = "10") int pageSize) {
        return dogService.getDogs(pageNo, pageSize);
    }

    @GetMapping(path = "/getDogDetail/{id}")
    public GenericResponse showDog(@PathVariable String id, HttpServletResponse response) throws JsonProcessingException{
        return dogService.getDog(id, response);
    }

    @PostMapping(path="/addDog")
    public GenericResponse addDog(@RequestBody Dog dog, HttpServletResponse response) throws JsonProcessingException{
        return dogService.addDog(dog, response);
    }
    @PostMapping(path="/deleteDog")
    public GenericResponse deleteDog(@RequestBody Dog dog, HttpServletResponse response) throws JsonProcessingException{
        return dogService.deleteDog(dog.get_id(), response);
    }



}

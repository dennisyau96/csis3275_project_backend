package com.doggo.csis3275_project_backend.Web;


import com.doggo.csis3275_project_backend.Entities.Dog;

import com.doggo.csis3275_project_backend.Entities.GenericResponse;
import com.doggo.csis3275_project_backend.Entities.Timeslot;
import com.doggo.csis3275_project_backend.Services.DogService;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

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
    public GenericResponse<Page<Dog>> getDogs(@RequestParam(defaultValue = "0") int page_no, @RequestParam(defaultValue = "10") int page_size) {
        return dogService.getDogs(page_no, page_size);
    }

    @GetMapping("/get-my-dogs")
    public GenericResponse<Page<Dog>> getDogsByOwner(@RequestHeader (name="Authorization1") String rawToken, @RequestParam(defaultValue = "0") int page_no, @RequestParam(defaultValue = "10") int page_size) throws JsonProcessingException {
        return dogService.getDogsByOwner(rawToken, page_no, page_size);
    }

    @GetMapping(path = "/getDogDetail/{id}")
    public GenericResponse showDog(@PathVariable String id, HttpServletResponse response) throws JsonProcessingException{
        return dogService.getDog(id, response);
    }

    @PostMapping(path="/addDog")
    public GenericResponse addDog(@RequestHeader (name="Authorization1") String rawToken,@RequestBody Dog dog, HttpServletResponse response) throws JsonProcessingException{
        return dogService.addDog(rawToken, dog, response);
    }
    @PostMapping(path="/deleteDog")
    public GenericResponse deleteDog(@RequestHeader (name="Authorization1") String rawToken,@RequestBody Dog dog, HttpServletResponse response) throws JsonProcessingException{
        return dogService.deleteDog(rawToken,dog.get_id(), response);
    }
    @PostMapping(path="/updateDog")
    public GenericResponse updateDog(@RequestHeader (name="Authorization1") String rawToken,@RequestBody Dog dog, HttpServletResponse response)throws JsonProcessingException{
        return dogService.updateDog(rawToken, dog.get_id(), dog, response);
    }
    @GetMapping(path="/getTimeslot/{dogId}")
    public GenericResponse getTimeslot(@PathVariable String dogId, HttpServletResponse response)throws JsonProcessingException{
        return dogService.getTimeslot(dogId, response);
    }
    @PostMapping(path="/addTimeslot")
    public GenericResponse addTimeslot(@RequestHeader (name="Authorization1") String rawToken, @RequestBody Map<String, Object> json, HttpServletResponse response)throws JsonProcessingException{
        return dogService.addTimeslot(rawToken, json, response);
    }
}

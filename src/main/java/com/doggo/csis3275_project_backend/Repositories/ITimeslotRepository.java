package com.doggo.csis3275_project_backend.Repositories;

import com.doggo.csis3275_project_backend.Entities.Timeslot;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ITimeslotRepository extends MongoRepository<Timeslot, String> {
    @Query(value = "{ 'dog_id' : ?0 }")
    List<Timeslot> findAllByDog_id(String dog_id);
}

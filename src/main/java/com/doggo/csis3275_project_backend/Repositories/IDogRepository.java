package com.doggo.csis3275_project_backend.Repositories;


import com.doggo.csis3275_project_backend.Entities.Dog;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface IDogRepository extends MongoRepository<Dog, String> {
    Dog getDogByName(String name);
}

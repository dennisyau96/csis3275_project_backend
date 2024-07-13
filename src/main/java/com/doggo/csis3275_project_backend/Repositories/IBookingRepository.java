package com.doggo.csis3275_project_backend.Repositories;

import com.doggo.csis3275_project_backend.Entities.Booking;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IBookingRepository extends MongoRepository<Booking, String> {
}

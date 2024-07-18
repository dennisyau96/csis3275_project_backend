package com.doggo.csis3275_project_backend.Repositories;

import com.doggo.csis3275_project_backend.DTO.BookingListDTO;
import com.doggo.csis3275_project_backend.Entities.Booking;
import com.doggo.csis3275_project_backend.Entities.Dog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IBookingRepository extends MongoRepository<Booking, String> {
    //@Query(value = "{ 'renter_id' : ?0 }")
    @Query(value = "{ 'renter_id' : ?0 }")
    Page<Booking> findAllByRenter_id(String renter_id, Pageable pageable);

    @Query(value = "{ '_id' : ?0, 'renter_id' : ?1 }")
    Booking findBy_idAndRenter_id(String booking_id, String renter_id);

    @Query(value = "{ '_id' : ?0, 'owner_id' : ?1 }")
    Booking findBy_idAndOwner_id(String booking_id, String owner_id);

    @Aggregation(pipeline = {
            "{$match: {renter_id:  ?0}}",
            "{$lookup: {from: 'dogs', localField: 'dog_id', foreignField: '_id', as: 'dog'}}",
            "{ '$unwind': { 'path': '$dog', 'preserveNullAndEmptyArrays': true } }",
            "{$lookup: {from: 'timeslots', localField: 'timeslot_id', foreignField: '_id', as: 'timeslot'}}",
            "{ '$unwind': { 'path': '$timeslot', 'preserveNullAndEmptyArrays': true } }",
            "{ '$project': { 'booking_date': 1, 'booking_confirmed': 1, 'dog': 1, 'timeslot': 1 } }"
    })
    List<BookingListDTO> findBookingDetailsByRenter_id(String renter_id);
}

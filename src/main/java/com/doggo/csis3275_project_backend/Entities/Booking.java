package com.doggo.csis3275_project_backend.Entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@Document(collection = "bookings")
public class Booking {
    @Field(targetType = FieldType.OBJECT_ID)
    private String _id;
    @Field(targetType = FieldType.OBJECT_ID)
    private String dog_id;
    @Field(targetType = FieldType.OBJECT_ID)
    private String renter_id;
    @Field(targetType = FieldType.OBJECT_ID)
    private String owner_id;
    @Field(targetType = FieldType.OBJECT_ID)
    private String timeslot_id;
    @Field(targetType = FieldType.OBJECT_ID)
    private Double price;
    private LocalDate booking_date;
    private Boolean booking_confirmed;
}

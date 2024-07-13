package com.doggo.csis3275_project_backend.Entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@AllArgsConstructor
@Document(collection = "timeslots")
public class Timeslot {
    @Field(targetType = FieldType.OBJECT_ID)
    private String _id;
    private String dog_id;
    private LocalDate date;
    private LocalTime start_time;
    private LocalTime end_time;
    private boolean is_booked;
}

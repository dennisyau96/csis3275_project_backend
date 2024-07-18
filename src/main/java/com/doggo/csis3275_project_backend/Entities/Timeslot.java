package com.doggo.csis3275_project_backend.Entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;

@Data
@AllArgsConstructor
@Document(collection = "timeslots")
public class Timeslot {
    @Field(targetType = FieldType.OBJECT_ID)
    private String _id;
    @Field(targetType = FieldType.OBJECT_ID)
    private String dog_id;
    private LocalDate date;
    private LocalTime start_time;
    private LocalTime end_time;
    private boolean booked;

    public Map<String, Object> timeslotResponse(){
        HashMap<String, Object> data = new HashMap<>();

        data.put("timeslot_id", _id);
        data.put("date", date);
        data.put("start_time", start_time);
        data.put("end_time", end_time);
        data.put("is_booked", booked);

        return data;
    }
}

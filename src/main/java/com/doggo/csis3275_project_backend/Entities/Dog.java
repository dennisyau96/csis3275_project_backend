package com.doggo.csis3275_project_backend.Entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;

@Data
@AllArgsConstructor
@Document(collection = "dogs")
public class Dog {
    @Field(targetType = FieldType.OBJECT_ID)
    private String _id;
    private int dog_id;
    private int owner_id;
    private int service_id;
    private String name;
    private String breed;
    private int age;
    private String sex;
    private String available_timeslot;
    private String additional_message;
    private String  profile_pic;
    private double rental_price_per_hour;
    private String location;
    private boolean desexed;
    private boolean vaccinated;
    private Double average_rating;
    private String profile_description;
}

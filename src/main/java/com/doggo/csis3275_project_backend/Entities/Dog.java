package com.doggo.csis3275_project_backend.Entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;

import java.util.HashMap;
import java.util.Map;

@Data
@AllArgsConstructor
@Document(collection = "dogs")
public class Dog {
    @Field(targetType = FieldType.OBJECT_ID)
    private String _id;
    @Field(targetType = FieldType.OBJECT_ID)
    private String owner_id;
    private Integer service_id;
    private String name;
    private String breed;
    private Integer age;
    private String sex;
    private String additional_message;
    private String  profile_pic;
    private Double rental_price_per_hour;
    private String location;
    private Boolean desexed;
    private Boolean vaccinated;
    private Double average_rating;
    private String profile_description;

    /*public Dog(String name, Integer serviceId, String breed, Integer age, String sex, String profilePic, Double rentalPricePerHour, String location, Boolean desexed, Boolean vaccinated, String profileDescription) {
        this.name = name;
        this.service_id = serviceId;
        this.breed = breed;
        this.age = age;
        this.sex = sex;
        this.profile_pic = profilePic;
        this.rental_price_per_hour = rentalPricePerHour;
        this.location = location;
        this.desexed = desexed;
        this.vaccinated = vaccinated;
        this.profile_description = profileDescription;
    }*/

    public Map<String, Object> dogResponse(){
        HashMap<String, Object> data = new HashMap<>();

        data.put("dog_id", _id);
        data.put("service_id", service_id);
        data.put("name", name);
        data.put("breed", breed);
        data.put("age", age);
        data.put("sex", sex);
        data.put("additional_message", additional_message);
        data.put("profile_pic", profile_pic);
        data.put("rental_price_per_hour", rental_price_per_hour);
        data.put("location", location);
        data.put("desexed", desexed);
        data.put("vaccinated", vaccinated);
        data.put("average_rating", average_rating);
        data.put("profile_description", profile_description);

        return data;
    }
}

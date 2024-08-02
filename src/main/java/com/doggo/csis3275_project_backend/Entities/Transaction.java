package com.doggo.csis3275_project_backend.Entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document
public class Transaction {
    @Field(targetType = FieldType.OBJECT_ID)
    private String _id;
    @Field(targetType = FieldType.OBJECT_ID)
    private String booking_id;
    @Field(targetType = FieldType.OBJECT_ID)
    private String renter_id;
    private Double price;
    private Double tax;
    private Double total_amount;
    private String complete_time;
    private Boolean is_paid;

}

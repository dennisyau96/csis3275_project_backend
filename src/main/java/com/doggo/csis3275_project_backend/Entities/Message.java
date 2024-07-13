package com.doggo.csis3275_project_backend.Entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;

@Data
@AllArgsConstructor
@Document(collection = "message")
public class Message {
    @Field(targetType = FieldType.OBJECT_ID)
    private String _id;
    @Field(targetType = FieldType.OBJECT_ID)
    private String sender_id;
    @Field(targetType = FieldType.OBJECT_ID)
    private String receiver_id;
    private String message_content;
    private String sent_time;


}

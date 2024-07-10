package com.doggo.csis3275_project_backend.Entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.json.JSONObject;

import java.util.HashMap;


@Data
@Builder
@AllArgsConstructor
public class GenericResponse<T> {
    private boolean success;
    private String message;
//    private T data;
    private HashMap<String, Object> data;

    // https://medium.com/@aedemirsen/generic-api-response-with-spring-boot-175434952086

//    public static <T> GenericResponse<T> empty(){
//        return makeResponse(null);
//    }

    public static <T> GenericResponse makeResponse(String message, boolean success, HashMap<String, Object> data){
        return GenericResponse.<T>builder()
                .message(message)
                .data(data)
                .success(success)
                .build();
    }
}

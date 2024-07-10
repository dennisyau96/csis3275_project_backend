package com.doggo.csis3275_project_backend.exceptions;

public class ErrorHelper {
    public static void handleError(Exception e, String context){
        System.out.println(context);
        e.printStackTrace(System.out);
    }
}

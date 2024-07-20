package com.doggo.csis3275_project_backend.exceptions;

import com.doggo.csis3275_project_backend.Entities.GenericResponse;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.beans.factory.parsing.Problem;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AccountStatusException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.security.InvalidParameterException;
import java.util.HashMap;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(Exception.class)
    public GenericResponse<Problem> handleSecurityException(Exception exception){
        String responseMessage = "";
//        ProblemDetail errorDetail = null;

        // TODO send this stack trace to an observability tool
//        exception.printStackTrace();

        ErrorHelper.handleError(exception, "ERROR - " + getClass().getSimpleName());

        if (exception instanceof Exception) {
//            errorDetail = ProblemDetail.forStatusAndDetail(HttpStatusCode.valueOf(500), exception.getMessage());
//            errorDetail.setProperty("description", "Unknown internal server error.");
            responseMessage = "An error occured while processing your request. Please try again.";
        }

        if (exception instanceof InvalidParameterException && exception.getMessage().equals("JWT token not found or not valid")) {
//            errorDetail = ProblemDetail.forStatusAndDetail(HttpStatusCode.valueOf(401), exception.getMessage());
//            errorDetail.setProperty("description", "The username or password is incorrect");
            responseMessage = "Please login before using this feature";
        }

        if (exception instanceof BadCredentialsException) {
//            errorDetail = ProblemDetail.forStatusAndDetail(HttpStatusCode.valueOf(401), exception.getMessage());
//            errorDetail.setProperty("description", "The username or password is incorrect");
            responseMessage = "The username or password is incorrect";
        }

        if (exception instanceof AccountStatusException) {
//            errorDetail = ProblemDetail.forStatusAndDetail(HttpStatusCode.valueOf(403), exception.getMessage());
//            errorDetail.setProperty("description", "The account is locked");
            responseMessage = "The account is locked";
        }

        if (exception instanceof AccessDeniedException) {
//            errorDetail = ProblemDetail.forStatusAndDetail(HttpStatusCode.valueOf(403), exception.getMessage());
//            errorDetail.setProperty("description", "You are not authorized to access this resource");
            responseMessage = "You are not authorized to access this resource";
        }

        if (exception instanceof SignatureException) {
//            errorDetail = ProblemDetail.forStatusAndDetail(HttpStatusCode.valueOf(403), exception.getMessage());
//            errorDetail.setProperty("description", "The JWT signature is invalid");
            responseMessage = "Your session is expired. Please login again. (Problem Detail: The JWT signature is invalid)";
        }

        if (exception instanceof io.jsonwebtoken.ExpiredJwtException) {
//            errorDetail = ProblemDetail.forStatusAndDetail(HttpStatusCode.valueOf(403), exception.getMessage());
//            errorDetail.setProperty("description", "The JWT token has expired");
            responseMessage = "Your session is expired. Please login again. (Problem Detail: the JWT token has expired)";
        }

        return GenericResponse.makeResponse(responseMessage, false, null);
//        return errorDetail;

    }
}

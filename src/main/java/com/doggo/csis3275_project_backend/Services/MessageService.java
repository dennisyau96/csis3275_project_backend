package com.doggo.csis3275_project_backend.Services;

import com.doggo.csis3275_project_backend.Entities.Dog;
import com.doggo.csis3275_project_backend.Entities.GenericResponse;
import com.doggo.csis3275_project_backend.Entities.Message;
import com.doggo.csis3275_project_backend.Repositories.IDogRepository;
import com.doggo.csis3275_project_backend.Repositories.IMessageRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletResponse;
import org.json.JSONException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;

@Service
public class MessageService {
    private IMessageRepository messageRepository;
    private Message message;


    private final JwtService jwtService;
    public MessageService(IMessageRepository messageRepository, JwtService jwtService){
        this.messageRepository = messageRepository;
        this.jwtService = jwtService;
    }


    public GenericResponse sendMessage(String rawToken,Message message,HttpServletResponse response) throws JsonProcessingException, JSONException {
        String responseMessage = "";
        boolean responseResult = false;
        HashMap<String, Object> responseData = new HashMap<>();
        try{
            String token = jwtService.getToken(rawToken);
            String sender_id = jwtService.extractClaim(token, Claims::getId);

            if(message.getMessage_content()==null||message.getReceiver_id()==null){
                responseMessage = "The message is empty /you did not choose a receiver";
            }
            else{
                message.setSender_id(sender_id);
                LocalDateTime sent_time = LocalDateTime.now();
                message.setSent_time(sent_time.toString());
                messageRepository.save(message);

                responseData.put("sender_id", message.getSender_id());
                responseData.put("receiver_id", message.getReceiver_id());
                responseData.put("message_content", message.getMessage_content());
                responseData.put("sent_time", sent_time.toString());


                responseMessage = "Success";
                responseResult = true;


            }
        }
        catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            responseMessage = "error";
        }
        return GenericResponse.makeResponse(responseMessage, responseResult, responseData);
    }
    public GenericResponse<Page<Message>> getMessages(String rawToken, HttpServletResponse response,int pageNo, int pageSize) throws JsonProcessingException, JSONException {
        String responseMessage = "success";
        boolean responseResult = false;
        HashMap<String, Object> responseData = new HashMap<>();
        try {
            String token = jwtService.getToken(rawToken);
            String user_id = jwtService.extractClaim(token, Claims::getId);
            Pageable pageable = PageRequest.of(pageNo, pageSize);
            Page<Message> messages= messageRepository.findAllByReceiver_id(user_id,pageable);
            responseResult = true;

            responseData.put("messageList", messages);



        }
        catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            responseMessage = "error";
        }

        return GenericResponse.makeResponse(responseMessage, responseResult, responseData);
    }

}

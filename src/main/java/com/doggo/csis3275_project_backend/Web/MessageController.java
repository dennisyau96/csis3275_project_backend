package com.doggo.csis3275_project_backend.Web;

import com.doggo.csis3275_project_backend.Entities.GenericResponse;
import com.doggo.csis3275_project_backend.Entities.Message;
import com.doggo.csis3275_project_backend.Services.MessageService;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api")
public class MessageController {
    private final MessageService messageService;
    @Autowired
    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    @PostMapping(path="/sendMessage")
    public GenericResponse sendMessage(@RequestHeader (name="Authorization1") String rawToken, @RequestBody Message message, HttpServletResponse response) throws JsonProcessingException {
        return messageService.sendMessage(rawToken,message,response);
    }

    @GetMapping(path = "/getMessages")
    public GenericResponse gatMessages(@RequestHeader (name="Authorization1") String rawToken, HttpServletResponse response,@RequestParam(defaultValue = "0") int page_no, @RequestParam(defaultValue = "10")int page_size) throws JsonProcessingException{
        return messageService.getMessages(rawToken, response, page_no, page_size);
    }
}

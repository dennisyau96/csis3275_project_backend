package com.doggo.csis3275_project_backend.Web;

import com.doggo.csis3275_project_backend.Entities.GenericResponse;
import com.doggo.csis3275_project_backend.Services.TransactionService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api")
public class TransactionController {
    private final TransactionService transactionService;
    @Autowired
    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping(path = "/showBill")
    public GenericResponse showBill(@RequestHeader (name="Authorization1") String rawToken,@RequestBody Map<String, Object> json, HttpServletResponse response){
        return transactionService.showBill(rawToken, json,response);
    }
    @PostMapping(path = "/pay")
    public GenericResponse pay(@RequestHeader (name="Authorization1") String rawToken,@RequestBody Map<String, Object> json, HttpServletResponse response){
        return transactionService.pay(rawToken,json);
    }
}

package com.doggo.csis3275_project_backend.Web;

import com.doggo.csis3275_project_backend.Services.BookingService;
import com.doggo.csis3275_project_backend.Services.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api")
public class BookingController {
    private final BookingService bookingService;

    @Autowired
    public BookingController(BookingService bookingService){
        this.bookingService = bookingService;
    }


}

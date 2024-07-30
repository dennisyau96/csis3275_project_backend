package com.doggo.csis3275_project_backend.Web;


import com.doggo.csis3275_project_backend.Entities.GenericResponse;
import com.doggo.csis3275_project_backend.Services.ReviewService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api")
public class ReviewController {
    private final ReviewService reviewService;
    @Autowired
    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }
    @PostMapping(path = "/review")
    public GenericResponse review(@RequestHeader(name="Authorization1") String rawToken, @RequestBody Map<String, Object> json){
        return reviewService.createReview(rawToken,json);
    }

}

package com.example.springmaster.controllers;

import com.example.springmaster.models.hotelInfo;
import com.example.springmaster.service.ActiveMethodTimeoutService;
import com.example.springmaster.service.TemplateExampleGetHotelsService;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/home")
public class HomeController {
    private final ActiveMethodTimeoutService activeMethodTimeoutService;
    private final TemplateExampleGetHotelsService templateExampleGetHotelsService;

    public HomeController(ActiveMethodTimeoutService activeMethodTimeoutService,
                          TemplateExampleGetHotelsService templateExampleGetHotelsService) {
        this.activeMethodTimeoutService = activeMethodTimeoutService;
        this.templateExampleGetHotelsService = templateExampleGetHotelsService;
    }

    @GetMapping("/hotels")
    public List<hotelInfo> getHotels(@RequestParam String lstDelaysTime){
        var splitListDelay = lstDelaysTime.split(",");
        List<Integer> lstDelay = Arrays.stream(splitListDelay)
                    .map(x -> Integer.parseInt(x)).collect(Collectors.toList());
        return activeMethodTimeoutService.getHotels(lstDelay);
    }

    @GetMapping("/hotels2")
    public List<String> getHotels2(){
        return templateExampleGetHotelsService.getAll();
    }
}

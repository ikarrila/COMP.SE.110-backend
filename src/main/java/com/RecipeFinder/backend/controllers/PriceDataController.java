package com.RecipeFinder.backend.controllers;

import com.RecipeFinder.backend.services.PriceDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/price")
public class PriceDataController {

    @Autowired
    private PriceDataService priceDataService;

    @GetMapping
    public void fetchAndPrintData() {
        // This will call the service method to fetch and print data
        priceDataService.fetchAndPrintPriceData();
    }
}

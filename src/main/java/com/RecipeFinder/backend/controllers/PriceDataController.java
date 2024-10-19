package com.RecipeFinder.backend.controllers;

import com.RecipeFinder.backend.models.PriceData;
import com.RecipeFinder.backend.services.PriceDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/price")
public class PriceDataController {

    @Autowired
    private PriceDataService priceDataService;

    @GetMapping("/all")
    public List<PriceData> getAllFoodPriceData() {
        return priceDataService.getAllFoodPriceData();
    }

    // TODO: /filtered

}

package com.RecipeFinder.backend.controllers;

import com.RecipeFinder.backend.models.PriceData;
import com.RecipeFinder.backend.services.PriceDataService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/price")
public class PriceDataController {

    @Autowired
    private PriceDataService priceDataService;

    @GetMapping
    public PriceData getAllFoodPriceData() {
        return priceDataService.getAllFoodPriceData();
    }

    @GetMapping("/filtered")
    public PriceData getFilteredPriceData(
            @RequestParam(value = "startMonth", required = false) String startMonth,
            @RequestParam(value = "endMonth", required = false) String endMonth,
            @RequestParam(value = "commodities", required = false) List<String> commodities) {
        return priceDataService.getFilteredPriceData(startMonth, endMonth, commodities);
    }

}

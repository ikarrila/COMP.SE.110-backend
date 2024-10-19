package com.RecipeFinder.backend.controllers;

import com.RecipeFinder.backend.models.PriceData;
import com.RecipeFinder.backend.services.PriceDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/price")
public class PriceDataController {

    @Autowired
    private PriceDataService priceDataService;

    @GetMapping
    public ResponseEntity<PriceData> getPriceData() {
        PriceData priceData = priceDataService.fetchPriceData();
        if (priceData != null) {
            return ResponseEntity.ok(priceData);
        } else {
            return ResponseEntity.internalServerError().build();
        }
    }
}

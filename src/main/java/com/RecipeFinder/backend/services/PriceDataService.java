package com.RecipeFinder.backend.services;

import com.RecipeFinder.backend.models.PriceData;
import com.RecipeFinder.backend.repositories.PriceDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PriceDataService {

    @Autowired
    private PriceDataRepository priceDataRepository;

    // Service method to get all food price data
    public PriceData getAllFoodPriceData() {
        return priceDataRepository.getAllFoodPriceData();
    }

    // Service method to get filtered food price data based on optional start and end months
    public PriceData getFilteredPriceData(String startMonth, String endMonth, List<String> commodities) {
        return priceDataRepository.getFilteredPriceData(startMonth, endMonth, commodities);
    }
}

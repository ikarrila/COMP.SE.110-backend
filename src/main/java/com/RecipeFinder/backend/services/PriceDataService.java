package com.RecipeFinder.backend.services;

import com.RecipeFinder.backend.models.PriceDataQuery;
import com.RecipeFinder.backend.models.PriceData;
import com.RecipeFinder.backend.repositories.PriceDataRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PriceDataService {

  @Autowired
  private PriceDataRepository priceDataRepository;

  public List<PriceData> getAllFoodPriceData() {
    // The actual data fetching is in the repository
    return priceDataRepository.getPriceData();
  }

  // TODO: getFilteredFoodPriceData(startDate, endDate, items[])
}

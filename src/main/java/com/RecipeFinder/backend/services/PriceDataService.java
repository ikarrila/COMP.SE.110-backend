package com.RecipeFinder.backend.services;

import com.RecipeFinder.backend.models.PriceData;
import com.RecipeFinder.backend.repositories.PriceDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PriceDataService {

    // Dependency Injection for repository layer, promoting loose coupling and testability.
    @Autowired
    private PriceDataRepository priceDataRepository;

    /**
     * Retrieves all available food price data.
     *
     * @return PriceData object containing all food price data.
     *
     * This method serves as a Facade to abstract lower-level details of data retrieval,
     * providing a unified interface for controllers to access food price data without 
     * concerning themselves with underlying API specifics or filtering.
     */
    public PriceData getAllFoodPriceData() {
        return priceDataRepository.getAllFoodPriceData();
    }

    /**
     * Retrieves food price data filtered by specified months and commodities.
     *
     * @param startMonth Optional start month for filtering data.
     * @param endMonth Optional end month for filtering data.
     * @param commodities List of commodity codes to filter the data by.
     * @return Filtered PriceData object.
     *
     * This method encapsulates complex query building and data access logic, delegating
     * those tasks to the repository layer. By using this Facade-style method, the 
     * service layer hides intricate details of query construction and data parsing, 
     * ensuring separation of concerns between data access and business logic.
     */
    public PriceData getFilteredPriceData(String startMonth, String endMonth, List<String> commodities) {
        return priceDataRepository.getFilteredPriceData(startMonth, endMonth, commodities);
    }
}

package com.RecipeFinder.backend.controllers;

import com.RecipeFinder.backend.models.PriceData;
import com.RecipeFinder.backend.services.PriceDataService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;
import java.util.List;

class PriceDataControllerTest {

    @Mock
    private PriceDataService priceDataService;

    @InjectMocks
    private PriceDataController priceDataController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllFoodPriceData() {
        PriceData mockPriceData = new PriceData();
        when(priceDataService.getAllFoodPriceData()).thenReturn(mockPriceData);

        PriceData response = priceDataController.getAllFoodPriceData();
        assertNotNull(response);
        verify(priceDataService, times(1)).getAllFoodPriceData();
    }

    @Test
    void testGetFilteredPriceData() {
        String startMonth = "2023M01";
        String endMonth = "2023M06";
        List<String> commodities = List.of("0111", "0112");
        PriceData mockFilteredData = new PriceData();
        
        when(priceDataService.getFilteredPriceData(startMonth, endMonth, commodities)).thenReturn(mockFilteredData);

        PriceData response = priceDataController.getFilteredPriceData(startMonth, endMonth, commodities);
        assertNotNull(response);
        verify(priceDataService, times(1)).getFilteredPriceData(startMonth, endMonth, commodities);
    }
}

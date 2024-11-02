package com.RecipeFinder.backend.services;

import com.RecipeFinder.backend.models.PriceData;
import com.RecipeFinder.backend.repositories.PriceDataRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;
import java.util.List;

class PriceDataServiceTest {

    @Mock
    private PriceDataRepository priceDataRepository;

    @InjectMocks
    private PriceDataService priceDataService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllFoodPriceData() {
        PriceData mockPriceData = new PriceData();

        when(priceDataRepository.getAllFoodPriceData()).thenReturn(mockPriceData);

        PriceData priceData = priceDataService.getAllFoodPriceData();
        assertNotNull(priceData);
        verify(priceDataRepository, times(1)).getAllFoodPriceData();
    }

    @Test
    void testGetFilteredPriceData() {
        String startMonth = "2023M01";
        String endMonth = "2023M06";
        List<String> commodities = List.of("0111", "0112");
        PriceData mockFilteredData = new PriceData();

        when(priceDataRepository.getFilteredPriceData(startMonth, endMonth, commodities)).thenReturn(mockFilteredData);

        PriceData filteredData = priceDataService.getFilteredPriceData(startMonth, endMonth, commodities);
        assertNotNull(filteredData);
        verify(priceDataRepository, times(1)).getFilteredPriceData(startMonth, endMonth, commodities);
    }
}

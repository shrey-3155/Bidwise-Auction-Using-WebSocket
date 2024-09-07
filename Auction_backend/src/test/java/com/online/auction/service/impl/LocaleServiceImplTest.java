package com.online.auction.service.impl;

import com.online.auction.exception.ServiceException;
import com.online.auction.model.City;
import com.online.auction.model.Country;
import com.online.auction.repository.CityRepository;
import com.online.auction.repository.CountryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;

import java.util.Collections;
import java.util.List;

import static com.online.auction.constant.AuctionConstants.CITY_NOT_FOUND_ERROR_MSG;
import static com.online.auction.constant.TestConstants.USA;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class LocaleServiceImplTest {

    @Mock
    private CityRepository cityRepository;

    @Mock
    private CountryRepository countryRepository;

    @InjectMocks
    private LocaleServiceImpl localeService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllCountriesTest() {
        List<Country> expectedCountries = List.of(new Country());
        when(countryRepository.findAll()).thenReturn(expectedCountries);

        List<Country> actualCountries = localeService.getAllCountries();

        assertEquals(expectedCountries, actualCountries);
        verify(countryRepository, times(1)).findAll();
    }

    @Test
    void getCitiesForCountrySuccessTest() throws ServiceException {
        Country country = new Country();
        country.setCountryName(USA);
        City city = new City();
        city.setCountry(country);
        List<City> expectedCities = List.of(city);
        when(cityRepository.findAllByCountryName(USA)).thenReturn(expectedCities);

        List<City> actualCities = localeService.getCitiesForCountry(USA);

        assertEquals(expectedCities, actualCities);
        verify(cityRepository, times(1)).findAllByCountryName(USA);
    }

    @Test
    void getCitiesForCountryWhenNoCitiesFoundTest() {
        when(cityRepository.findAllByCountryName(USA)).thenReturn(Collections.emptyList());

        ServiceException exception = assertThrows(ServiceException.class, () -> {
            localeService.getCitiesForCountry(USA);
        });

        assertEquals(HttpStatus.BAD_REQUEST.value(), exception.getStatusCode());
        assertEquals(CITY_NOT_FOUND_ERROR_MSG, exception.getErrorMessage());
        verify(cityRepository, times(1)).findAllByCountryName(USA);
    }

    @Test
    void getCitiesForCountryWhenCitiesIsNullTest() {
        when(cityRepository.findAllByCountryName(USA)).thenReturn(null);

        ServiceException exception = assertThrows(ServiceException.class, () -> {
            localeService.getCitiesForCountry(USA);
        });

        assertEquals(HttpStatus.BAD_REQUEST.value(), exception.getStatusCode());
        assertEquals(CITY_NOT_FOUND_ERROR_MSG, exception.getErrorMessage());
        verify(cityRepository, times(1)).findAllByCountryName(USA);
    }
}
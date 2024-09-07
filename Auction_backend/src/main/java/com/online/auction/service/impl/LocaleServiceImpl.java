package com.online.auction.service.impl;

import com.online.auction.exception.ServiceException;
import com.online.auction.model.City;
import com.online.auction.model.Country;
import com.online.auction.repository.CityRepository;
import com.online.auction.repository.CountryRepository;
import com.online.auction.service.LocaleService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

import static com.online.auction.constant.AuctionConstants.CITY_NOT_FOUND_ERROR_MSG;

/**
 * Implementation of the {@link LocaleService} interface.
 * Provides methods to interact with country and city data.
 */
@AllArgsConstructor
@Service
public class LocaleServiceImpl implements LocaleService {

    private CityRepository cityRepository;
    private CountryRepository countryRepository;

    /**
     * Retrieves all countries from the repository.
     *
     * @return a list of all {@link Country} entities.
     */
    @Override
    public List<Country> getAllCountries() {
        return countryRepository.findAll();
    }

    /**
     * Retrieves all cities for a given country name from the repository.
     *
     * @param countryName the name of the country for which to retrieve cities.
     * @return a list of {@link City} entities for the specified country.
     * @throws ServiceException if no cities are found for the given country name.
     */
    @Override
    public List<City> getCitiesForCountry(String countryName) throws ServiceException {
        List<City> cities = cityRepository.findAllByCountryName(countryName);
        if (Objects.isNull(cities) || cities.isEmpty()) {
            throw new ServiceException(HttpStatus.BAD_REQUEST, CITY_NOT_FOUND_ERROR_MSG);
        }
        return cities;
    }
}

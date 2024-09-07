package com.online.auction.service;

import com.online.auction.exception.ServiceException;
import com.online.auction.model.City;
import com.online.auction.model.Country;

import java.util.List;


/**
 * Service interface for managing locale-related operations.
 * <p>
 * This interface provides methods for retrieving information about countries and cities.
 * </p>
 */
public interface LocaleService {
    /**
     * Retrieves a list of all countries.
     * <p>
     * This method fetches all country records from the database.
     * </p>
     *
     * @return A list of {@link Country} objects representing all countries.
     */
    List<Country> getAllCountries();


    /**
     * Retrieves a list of cities for a specified country.
     * <p>
     * This method fetches cities that belong to the specified country.
     * If the country name does not exist, it may throw a {@link ServiceException}.
     * </p>
     *
     * @param countryName The name of the country for which to retrieve cities.
     * @return A list of {@link City} objects representing cities in the specified country.
     * @throws ServiceException If there is an error while retrieving the cities or if the country name is invalid.
     */
    List<City> getCitiesForCountry(String countryName) throws ServiceException;
}

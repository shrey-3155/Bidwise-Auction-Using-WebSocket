package com.online.auction.repository;

import com.online.auction.model.City;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for managing {@link City} entities.
 * <p>
 * This interface extends {@link JpaRepository} to provide CRUD operations and custom query methods
 * for {@link City} entities, with the primary key of type {@code Integer}.
 * </p>
 */
public interface CityRepository extends JpaRepository<City, Integer> {
    /**
     * Finds a city by its name.
     * <p>
     * This method retrieves a {@link City} entity with the specified city name.
     * </p>
     *
     * @param cityName The name of the city to be found.
     * @return An {@link Optional} containing the {@link City} entity if found, otherwise {@code Optional.empty()}.
     */
    Optional<City> findByCityName(String cityName);

    /**
     * Finds all cities within a specified country.
     * <p>
     * This method retrieves a list of {@link City} entities that belong to the country with the given name.
     * </p>
     *
     * @param countryName The name of the country for which to find all associated cities.
     * @return A list of {@link City} entities that belong to the specified country.
     */
    @Query("SELECT c FROM City c WHERE c.country.countryName = :countryName")
    List<City> findAllByCountryName(@Param("countryName") String countryName);
}

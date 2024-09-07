package com.online.auction.repository;

import com.online.auction.model.Country;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Repository interface for managing {@link Country} entities.
 * <p>
 * This interface extends {@link JpaRepository} to provide CRUD operations and a custom query method
 * for {@link Country} entities, with the primary key of type {@code Integer}.
 * </p>
 */
public interface CountryRepository extends JpaRepository<Country, Integer> {
    /**
     * Finds a country by its name.
     * <p>
     * This method retrieves a {@link Country} entity with the specified country name.
     * </p>
     *
     * @param countryName The name of the country to be found.
     * @return An {@link Optional} containing the {@link Country} entity if found, otherwise {@code Optional.empty()}.
     */
    Optional<Country> findByCountryName(String countryName);
}

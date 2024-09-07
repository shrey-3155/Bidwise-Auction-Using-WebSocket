package com.online.auction.config.application;

import com.online.auction.model.City;
import com.online.auction.model.Country;
import com.online.auction.model.ItemCategory;
import com.online.auction.repository.CityRepository;
import com.online.auction.repository.CountryRepository;
import com.online.auction.repository.ItemCategoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Component for initializing data into the application at startup.
 * <p>
 * This class implements {@link CommandLineRunner} and is executed after the application context is loaded.
 * It loads locale data (countries and cities) and item categories from the configuration into the database.
 * </p>
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {

    private final LocaleConfig localeConfig;
    private final CountryRepository countryRepository;
    private final CityRepository cityRepository;
    private final ItemCategoryRepository itemCategoryRepository;

    @Value("${app.itemCategories}")
    private String categories;

    /**
     * Executes the data initialization process after the application context is loaded.
     * <p>
     * This method invokes {@link #loadLocaleData()} and {@link #loadItemCategoriesData()} to populate the
     * database with initial data.
     * </p>
     *
     * @param args Command line arguments passed to the application (not used).
     * @throws Exception If an error occurs during data initialization.
     */
    @Override
    public void run(String... args) throws Exception {
        loadLocaleData();
        loadItemCategoriesData();
    }

    /**
     * Loads and initializes the locale data (countries and cities) into the database.
     * <p>
     * Fetches locale details from the {@link LocaleConfig} configuration and inserts new country and city
     * records into the database if they do not already exist.
     * </p>
     */
    private void loadLocaleData() {
        log.info("Initializing the initial Locale Data");

        //Fetching the Locale details from configuration
        Map<String, List<String>> locale = localeConfig.getLocale();
        for (Map.Entry<String, List<String>> entry : locale.entrySet()) {
            String countryName = entry.getKey();

            //Checking if the Country with the name is present
            Optional<Country> countryOptional = countryRepository.findByCountryName(countryName);
            List<String> cities = entry.getValue();
            if (countryOptional.isEmpty()) {

                //Saving the new Country in the database
                log.info("Inserting the country with name : {}", countryName);
                Country country = new Country();
                country.setCountryName(countryName);
                Country countryDb = countryRepository.save(country);
                log.info("Saved the country record : {}", countryDb);
                for (String cityName : cities) {
                    //checking if the city details are already present
                    Optional<City> cityOptional = cityRepository.findByCityName(cityName);
                    if (cityOptional.isEmpty()) {
                        //Inserting the new city details in the database
                        log.info("Inserting the city with the name : {}", cityName);
                        City city = new City();
                        city.setCityName(cityName);
                        city.setCountry(countryDb);
                        City cityDb = cityRepository.save(city);
                        log.info("Saved the city record : {}", cityDb);
                    } else {
                        log.info("The data is already present for city : {}", cityOptional.get());
                    }
                }
            } else {
                //Inserting the cities for the already existing country
                log.info("The data is already present for the country : {}", countryOptional.get());
                for (String cityName : cities) {
                    Optional<City> cityOptional = cityRepository.findByCityName(cityName);
                    if (cityOptional.isEmpty()) {
                        log.info("Inserting the city with the name : {}", cityName);
                        City city = new City();
                        city.setCityName(cityName);
                        city.setCountry(countryOptional.get());
                        City cityDb = cityRepository.save(city);
                        log.info("Saved the city record : {}", cityDb);
                    } else {
                        log.info("The data is already present for city : {}", cityOptional.get());
                    }
                }
            }
        }
    }

    /**
     * Loads and initializes the item categories data into the database.
     * <p>
     * Retrieves item categories from the {@code app.itemCategories} configuration property and inserts
     * them into the database if they do not already exist.
     * </p>
     */
    private void loadItemCategoriesData() {
        log.info("Initializing the Item Categories Data");
        List<String> itemCategories = Arrays.stream(categories.split(",")).toList();
        for (String itemCategoryName : itemCategories) {
            Optional<ItemCategory> itemCategoryDb = itemCategoryRepository.findByItemCategoryName(itemCategoryName);
            if (itemCategoryDb.isEmpty()) {
                log.info("Inserting the Item Category with the name: {}", itemCategoryName);
                ItemCategory itemCategory = itemCategoryRepository.save(ItemCategory.builder().itemCategoryName(itemCategoryName).build());
                log.info("Successfully inserted the Item Category: {}", itemCategory);
            } else {
                log.info("The Item Category is already present: {}", itemCategoryDb);
            }
        }
    }
}

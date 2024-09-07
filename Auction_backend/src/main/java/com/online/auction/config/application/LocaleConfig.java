package com.online.auction.config.application;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


/**
 * Configuration class for loading locale-related properties.
 * <p>
 * This class is used to map the locale configuration properties from the application properties file.
 * The properties are expected to be in a specific format, where each entry contains a country and its cities.
 * The format should be {@code countryName=cities}, where cities are comma-separated.
 * </p>
 */
@Configuration
@ConfigurationProperties(prefix = "app")
public class LocaleConfig {
    private String locale;

    /**
     * Retrieves the locale data from the configuration property and parses it into a map.
     * <p>
     * The locale data is expected to be a semicolon-separated list of entries, each in the format:
     * {@code countryName=cities}, where cities are comma-separated.
     * </p>
     *
     * @return A {@link Map} where the keys are country names and the values are lists of cities for each country.
     */
    public Map<String, List<String>> getLocale() {
        return Arrays.stream(locale.split(";"))
                .map(entry -> entry.split("="))
                .collect(Collectors.toMap(
                        entry -> entry[0].trim(),
                        entry -> Arrays.stream(entry[1].split(",")).map(String::trim).collect(Collectors.toList())
                ));
    }

    /**
     * Sets the locale configuration property.
     *
     * @param locale A string representing the locale data in the format: {@code countryName=cities;...}.
     */
    public void setLocale(String locale) {
        this.locale = locale;
    }
}

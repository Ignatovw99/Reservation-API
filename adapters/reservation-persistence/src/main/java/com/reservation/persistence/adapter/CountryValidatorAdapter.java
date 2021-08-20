package com.reservation.persistence.adapter;

import com.reservation.application.port.out.CountryValidatorPort;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@Slf4j
class CountryValidatorAdapter implements CountryValidatorPort {

    private final Set<String> countries;

    public CountryValidatorAdapter() {
        countries = initializeCountries();
    }

    private Set<String> initializeCountries() {
        log.info("Initializing countries");
        return Arrays.stream(Locale.getISOCountries())
                .map(isoCountry -> new Locale("", isoCountry)
                        .getDisplayCountry()
                        .toLowerCase())
                .collect(Collectors.toSet());
    }

    @Override
    public boolean isCountryValid(String country) {
        log.info("Checking if country {} is valid", country);
        String countryLower = country.toLowerCase();
        return countries.contains(countryLower);
    }
}

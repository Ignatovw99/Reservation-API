package com.reservation.persistence.adapter;

import com.reservation.application.port.out.CountryValidatorPort;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class CountryValidatorAdapter implements CountryValidatorPort {

    private final Set<String> isoCountries;

    public CountryValidatorAdapter() {
        isoCountries = initializeCountries();
    }

    private Set<String> initializeCountries() {
        return Arrays.stream(Locale.getISOCountries())
                .map(isoCountry -> new Locale("", isoCountry)
                        .getDisplayCountry()
                        .toLowerCase())
                .collect(Collectors.toSet());
    }

    @Override
    public boolean isCountryValid(String country) {
        String countryLower = country.toLowerCase();
        return isoCountries.contains(countryLower);
    }
}

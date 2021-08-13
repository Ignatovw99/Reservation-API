package com.reservation.persistence.adapter;

import com.reservation.application.port.out.CountryValidatorPort;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(classes = CountryValidatorAdapter.class)
public class CountryValidatorAdapterTest {

    @Autowired
    private CountryValidatorPort countryValidatorPort;

    @Test
    public void isCountryValid_whenTheGivenCountryIsNotValid_shouldReturnFalse() {
        assertFalse(countryValidatorPort.isCountryValid("Germania"));
        assertFalse(countryValidatorPort.isCountryValid("Englandia"));
        assertFalse(countryValidatorPort.isCountryValid("Top country"));
    }

    @Test
    public void isCountryValid_whenTheGivenCountryIsValid_shouldReturnTrue() {
        assertTrue(countryValidatorPort.isCountryValid("Germany"));
        assertTrue(countryValidatorPort.isCountryValid("Spain"));
        assertTrue(countryValidatorPort.isCountryValid("Greece"));
    }

    @Test
    public void isCountryValid_whenTheGivenCountryIsValidWithDifferentCase_shouldReturnTrue() {
        assertTrue(countryValidatorPort.isCountryValid("germany"));
        assertTrue(countryValidatorPort.isCountryValid("Germany"));
        assertTrue(countryValidatorPort.isCountryValid("geRMANy"));
    }
}

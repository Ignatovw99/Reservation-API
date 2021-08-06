package com.reservation.common.base;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class NumericJpaIdentifierTest {

    @Test
    public void testEmptyConstructor() {
        NumericJpaIdentifier numericIdentifier = new NumericJpaIdentifier();
        assertNotNull(numericIdentifier);
    }

    @Test
    public void testIdSetterAndGetter() {
        NumericJpaIdentifier numericIdentifier = new NumericJpaIdentifier();
        numericIdentifier.setId(32L);
        assertEquals(32L, numericIdentifier.getId());
    }

    @Test
    public void hashCode_whenTwoObjectsHaveTheSameId_shouldBeEqual() {
        NumericJpaIdentifier numericIdentifierFirst = new NumericJpaIdentifier();
        numericIdentifierFirst.setId(32L);

        NumericJpaIdentifier numericIdentifierSecond = new NumericJpaIdentifier();
        numericIdentifierSecond.setId(32L);

        assertEquals(numericIdentifierFirst.hashCode(), numericIdentifierSecond.hashCode());
    }

    @Test
    public void hashCode_whenTwoObjectsHaveDifferentIds_shouldNotBeEqual() {
        NumericJpaIdentifier numericIdentifierFirst = new NumericJpaIdentifier();
        numericIdentifierFirst.setId(32L);

        NumericJpaIdentifier numericIdentifierSecond = new NumericJpaIdentifier();
        numericIdentifierSecond.setId(34L);

        assertNotEquals(numericIdentifierFirst.hashCode(), numericIdentifierSecond.hashCode());
    }

    @Test
    public void equals_whenTheBothObjectAreDifferentTypes_shouldNotBeEqual() {
        NumericJpaIdentifier numericIdentifierFirst = new NumericJpaIdentifier();
        numericIdentifierFirst.setId(32L);

        String numericIdentifierSecond = "asd";

        boolean actual = numericIdentifierFirst.equals(numericIdentifierSecond);

        assertFalse(actual);
    }

    @Test
    public void equals_whenOneOfTheIdIsNull_shouldNotBeEqual() {
        NumericJpaIdentifier numericIdentifierFirst = new NumericJpaIdentifier();
        numericIdentifierFirst.setId(32L);

        NumericJpaIdentifier numericIdentifierSecond = new NumericJpaIdentifier();

        boolean actual = numericIdentifierFirst.equals(numericIdentifierSecond);

        assertFalse(actual);
    }

    @Test
    public void equals_whenTwoObjectsHaveDifferentIds_shouldNotBeEqual() {
        NumericJpaIdentifier numericIdentifierFirst = new NumericJpaIdentifier();
        numericIdentifierFirst.setId(32L);

        NumericJpaIdentifier numericIdentifierSecond = new NumericJpaIdentifier();
        numericIdentifierSecond.setId(34L);

        boolean actual = numericIdentifierFirst.equals(numericIdentifierSecond);

        assertFalse(actual);
    }


    @Test
    public void equals_whenTheBothObjectsHaveTheSameId_shouldBeEqual() {
        NumericJpaIdentifier numericIdentifierFirst = new NumericJpaIdentifier();
        numericIdentifierFirst.setId(32L);

        NumericJpaIdentifier numericIdentifierSecond = new NumericJpaIdentifier();
        numericIdentifierSecond.setId(32L);

        boolean actual = numericIdentifierFirst.equals(numericIdentifierSecond);

        assertTrue(actual);
    }
}

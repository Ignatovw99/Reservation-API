package com.reservation.common.base;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class NumericIdentifierTest {

    @Test
    public void testEmptyConstructor() {
        NumericIdentifier numericIdentifier = new NumericIdentifier();
        assertNotNull(numericIdentifier);
    }

    @Test
    public void testIdSetterAndGetter() {
        NumericIdentifier numericIdentifier = new NumericIdentifier();
        numericIdentifier.setId(32L);
        assertEquals(32L, numericIdentifier.getId());
    }

    @Test
    public void hashCode_whenTwoObjectsHaveTheSameId_shouldBeEqual() {
        NumericIdentifier numericIdentifierFirst = new NumericIdentifier();
        numericIdentifierFirst.setId(32L);

        NumericIdentifier numericIdentifierSecond = new NumericIdentifier();
        numericIdentifierSecond.setId(32L);

        assertEquals(numericIdentifierFirst.hashCode(), numericIdentifierSecond.hashCode());
    }

    @Test
    public void hashCode_whenTwoObjectsHaveDifferentIds_shouldNotBeEqual() {
        NumericIdentifier numericIdentifierFirst = new NumericIdentifier();
        numericIdentifierFirst.setId(32L);

        NumericIdentifier numericIdentifierSecond = new NumericIdentifier();
        numericIdentifierSecond.setId(34L);

        assertNotEquals(numericIdentifierFirst.hashCode(), numericIdentifierSecond.hashCode());
    }

    @Test
    public void equals_whenTheBothObjectAreDifferentTypes_shouldNotBeEqual() {
        NumericIdentifier numericIdentifierFirst = new NumericIdentifier();
        numericIdentifierFirst.setId(32L);

        String numericIdentifierSecond = "asd";

        boolean actual = numericIdentifierFirst.equals(numericIdentifierSecond);

        assertFalse(actual);
    }

    @Test
    public void equals_whenOneOfTheIdIsNull_shouldNotBeEqual() {
        NumericIdentifier numericIdentifierFirst = new NumericIdentifier();
        numericIdentifierFirst.setId(32L);

        NumericIdentifier numericIdentifierSecond = new NumericIdentifier();

        boolean actual = numericIdentifierFirst.equals(numericIdentifierSecond);

        assertFalse(actual);
    }

    @Test
    public void equals_whenTwoObjectsHaveDifferentIds_shouldNotBeEqual() {
        NumericIdentifier numericIdentifierFirst = new NumericIdentifier();
        numericIdentifierFirst.setId(32L);

        NumericIdentifier numericIdentifierSecond = new NumericIdentifier();
        numericIdentifierSecond.setId(34L);

        boolean actual = numericIdentifierFirst.equals(numericIdentifierSecond);

        assertFalse(actual);
    }


    @Test
    public void equals_whenTheBothObjectsHaveTheSameId_shouldBeEqual() {
        NumericIdentifier numericIdentifierFirst = new NumericIdentifier();
        numericIdentifierFirst.setId(32L);

        NumericIdentifier numericIdentifierSecond = new NumericIdentifier();
        numericIdentifierSecond.setId(32L);

        boolean actual = numericIdentifierFirst.equals(numericIdentifierSecond);

        assertTrue(actual);
    }
}

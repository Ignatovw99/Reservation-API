package com.reservation.common.validation;

import javax.validation.*;
import java.util.Set;

public class SelfValidating<T> {

    private Validator validator;

    protected SelfValidating() {
        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    /**
     * Evaluates all Bean Validations on the attributes of this instance.
     */
    protected void validateSelf() {
        Set<ConstraintViolation<T>> violations = validator.validate((T) this);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
    }
}

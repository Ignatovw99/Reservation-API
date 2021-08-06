package com.reservation.common.base;

import com.reservation.common.contract.Identifiable;
import lombok.AccessLevel;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
@Data
public class NumericIdentifier implements Identifiable<Long> {

    private Long id;
}

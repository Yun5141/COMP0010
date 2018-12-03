package com.trafficmon;

import java.math.BigDecimal;

public interface Actions {
    void deductCharge(Vehicle vehicle, BigDecimal charge) throws AccountNotRegisteredException, InsufficientCreditException;
    void penaltyNotice(Vehicle vehicle, BigDecimal charge);
    void investigations(Vehicle vehicle);
}

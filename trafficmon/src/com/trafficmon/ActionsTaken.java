package com.trafficmon;

import java.math.BigDecimal;

//calls the methods in the external library
public class ActionsTaken implements Actions {

    @Override
    public void deductCharge(Vehicle vehicle, BigDecimal charge) throws AccountNotRegisteredException, InsufficientCreditException {
        RegisteredCustomerAccountsService.getInstance().accountFor(vehicle).deduct(charge);
    }

    @Override
    public void penaltyNotice(Vehicle vehicle, BigDecimal charge) {
        OperationsTeam.getInstance().issuePenaltyNotice(vehicle, charge);
    }

    @Override
    public void investigations(Vehicle vehicle) {
        OperationsTeam.getInstance().triggerInvestigationInto(vehicle);
    }
}

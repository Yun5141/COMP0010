package com.trafficmon;
import org.jmock.Expectations;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import java.math.BigDecimal;
import java.util.List;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class CongestionChargeSystemTest {

    EventLogger eventLogger= EventLogger.getInstance();
    private final Vehicle vehicle = Vehicle.withRegistration("A123 XYZ");

    @Rule
    public JUnitRuleMockery context = new JUnitRuleMockery();

    Actions actions = context.mock(Actions.class);

    ActionsDecided actionsDecided=new ActionsDecided();

    @Before
    public void setEventLogger(){
        actionsDecided.changeActions(actions);
        eventLogger.vehicleEnteringZone(vehicle);
        eventLogger.vehicleLeavingZone(vehicle);
    }

    @Test
    public void AwillDeductChargeFromDriversAccount(){
        CongestionChargeSystem congestionChargeSystem=new CongestionChargeSystem(new myCalculate());
        context.checking(new Expectations(){{
            try {
                exactly(1).of(actions).deductCharge(vehicle,BigDecimal.valueOf(1));
            } catch (AccountNotRegisteredException e) {
                e.printStackTrace();
            } catch (InsufficientCreditException e) {
                e.printStackTrace();
            }

        }});
        congestionChargeSystem.calculateCharges();


    }
    @Test
    public void BwillIssuePenaltyNoticeIfAccountNotRegisteredExceptionIsRaised(){
        CongestionChargeSystem congestionChargeSystem=new CongestionChargeSystem(new myCalculate());
        context.checking(new Expectations(){{
                try {
                    allowing(actions).deductCharge(vehicle,BigDecimal.valueOf(1));
                } catch (AccountNotRegisteredException e) {
                    e.printStackTrace();
                } catch (InsufficientCreditException e) {
                    e.printStackTrace();
                }
                will(throwException(new AccountNotRegisteredException(vehicle)));
                exactly(1).of(actions).penaltyNotice(vehicle,BigDecimal.valueOf(1));

            }});
        congestionChargeSystem.calculateCharges();
    }

    @Test
    public void CwillIssuePenaltyNoticeIfInsufficientCreditExceptionIsRaised(){
        CongestionChargeSystem congestionChargeSystem=new CongestionChargeSystem(new myCalculate());
        context.checking(new Expectations(){{
            try {
                allowing(actions).deductCharge(vehicle,BigDecimal.valueOf(1));
            } catch (AccountNotRegisteredException e) {
                e.printStackTrace();
            } catch (InsufficientCreditException e) {
                e.printStackTrace();
            }
            will(throwException(new InsufficientCreditException(BigDecimal.valueOf(1))));
            exactly(1).of(actions).penaltyNotice(vehicle,BigDecimal.valueOf(1));

        }});
        congestionChargeSystem.calculateCharges();
    }
    @Test
    public void DwillTriggerInvestigationIfOrderingIsAbnormal(){
        Vehicle abnormalVehicle= Vehicle.withRegistration("VEX 123B");
        eventLogger.vehicleEnteringZone(abnormalVehicle);
        eventLogger.vehicleEnteringZone(abnormalVehicle);
        CongestionChargeSystem congestionChargeSystem=new CongestionChargeSystem(new myCalculate());
        context.checking(new Expectations(){{
            exactly(1).of(actions).investigations(abnormalVehicle);
            try {
                allowing(actions).deductCharge(vehicle,BigDecimal.valueOf(1));
            } catch (AccountNotRegisteredException e) {
                e.printStackTrace();
            } catch (InsufficientCreditException e) {
                e.printStackTrace();
            }
        }});

        congestionChargeSystem.calculateCharges();

    }

    @Test
    public void EwillCalculateChargesForAllCars(){
        Vehicle vehicle2= Vehicle.withRegistration("BCD EFGH");
        eventLogger.vehicleEnteringZone(vehicle2);
        eventLogger.vehicleLeavingZone(vehicle2);
        CongestionChargeSystem congestionChargeSystem=new CongestionChargeSystem(new myCalculate());
        context.checking(new Expectations(){{
            allowing(actions).investigations(Vehicle.withRegistration("VEX 123B"));

            try {
                exactly(1).of(actions).deductCharge(vehicle,BigDecimal.valueOf(1));
                exactly(1).of(actions).deductCharge(vehicle2, BigDecimal.valueOf(1));
            } catch (AccountNotRegisteredException e) {
                e.printStackTrace();
            } catch (InsufficientCreditException e) {
                e.printStackTrace();
            }
        }});

        congestionChargeSystem.calculateCharges();


    }

    private class myCalculate implements ChargeGenerator{

        @Override
        public BigDecimal calculateChargeForTimeInZone(List<ZoneBoundaryCrossing> crossings) {
            return BigDecimal.valueOf(1);
        }
    }



}
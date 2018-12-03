package com.trafficmon;

import org.joda.time.DateTime;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class ChargeByMinutesCalculatorTest {

    ChargeByMinutesCalculator calculateMethod = new ChargeByMinutesCalculator();
    BigDecimal chargeRate = calculateMethod.CHARGE_RATE_POUNDS_PER_MINUTE;

    int TimeSpentInsideTheZone = 50; //min

    @Test
    public void chargeByTimeSpentInTheZone() {

        List<ZoneBoundaryCrossing> eventLog =new ArrayList<ZoneBoundaryCrossing>();

        new APairOfNormalCrossingEventsCreator(
                new DateTime(2018, 9, 19, 6, 58,9,0), TimeSpentInsideTheZone, eventLog).create();

        BigDecimal charge = calculateMethod.calculateChargeForTimeInZone(eventLog);

        assertThat(charge,is(new BigDecimal(TimeSpentInsideTheZone).multiply(chargeRate)));
    }

    @Test
    public void wouldChargeByTheTotalOfTimeOnAGivenDay(){

        List<ZoneBoundaryCrossing> eventLog =new ArrayList<ZoneBoundaryCrossing>();

        DateTime firstEnteringTime = new DateTime(2018, 9, 19, 6, 58,9,0);
        new APairOfNormalCrossingEventsCreator(firstEnteringTime, TimeSpentInsideTheZone, eventLog).create();

        DateTime secondEnteringTime = firstEnteringTime.plusMinutes(TimeSpentInsideTheZone + 50); //The driver comes back after 50 min
        new APairOfNormalCrossingEventsCreator(secondEnteringTime, TimeSpentInsideTheZone, eventLog).create();

        BigDecimal charge = calculateMethod.calculateChargeForTimeInZone(eventLog);

        assertThat(charge,is(new BigDecimal(TimeSpentInsideTheZone * 2).multiply(chargeRate)));
    }

}

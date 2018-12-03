package com.trafficmon;

import org.joda.time.DateTime;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertTrue;

public class ChargeByTimePeriodCalculatorTest {

    ChargeByTimePeriodCalculator calculateMethod = new ChargeByTimePeriodCalculator();

    int AnHour = 60; //min

    @Test
    public void chargeSixPoundsIfEnteredBeforeTwoPM() {

        List<ZoneBoundaryCrossing> eventLog =new ArrayList<ZoneBoundaryCrossing>();

        DateTime enteringTime = new DateTime(2018, 9, 19, 9, 0,0,0); //enter at 9:00 am
        new APairOfNormalCrossingEventsCreator(enteringTime, AnHour, eventLog).create();

        BigDecimal charge = calculateMethod.calculateChargeForTimeInZone(eventLog);

        assertThat(charge,is(BigDecimal.valueOf(6)));
    }

    @Test
    public void chargeFourPoundsIfEnteredAfterTwoPM() {
        List<ZoneBoundaryCrossing> eventLog =new ArrayList<ZoneBoundaryCrossing>();

        DateTime enteringTime = new DateTime(2018, 9, 19, 15, 0,0,0); //enter at 3:00 pm
        new APairOfNormalCrossingEventsCreator(enteringTime, AnHour, eventLog).create();

        BigDecimal charge = calculateMethod.calculateChargeForTimeInZone(eventLog);

        assertThat(charge,is(BigDecimal.valueOf(4)));
    }

    @Test
    public void wouldNotBeChargedTwiceIfLeaveAndComeBackWithinFourHoursFromTheLeavingTime(){

        List<ZoneBoundaryCrossing> eventLog =new ArrayList<ZoneBoundaryCrossing>();

        DateTime firstEnteringTime = new DateTime(2018, 9, 19, 1, 0,0,0);  //enter at 1.00 am
        new APairOfNormalCrossingEventsCreator(firstEnteringTime, AnHour * 1, eventLog).create();
                                                                                        //exit at 2.00 pm

        BigDecimal charge1 = calculateMethod.calculateChargeForTimeInZone(eventLog);

        DateTime secondEnteringTime = firstEnteringTime.plusMinutes(AnHour * 3);  //enter at 3:00 am
        new APairOfNormalCrossingEventsCreator(secondEnteringTime, AnHour * 1, eventLog).create();

        BigDecimal charge2 = calculateMethod.calculateChargeForTimeInZone(eventLog);

        assertTrue(charge1.compareTo(charge2) == 0);
    }

    @Test
    public void wouldBeChargeTwiceIfLeaveAndComeBackOutOfFourHours(){

        List<ZoneBoundaryCrossing> eventLog =new ArrayList<ZoneBoundaryCrossing>();

        DateTime firstEnteringTime = new DateTime(2018, 9, 19, 6, 0,0,0);  //enter at 6:00 am
        new APairOfNormalCrossingEventsCreator(firstEnteringTime, AnHour * 1, eventLog).create();
                                                                                    //exit at 7:00 pm

        BigDecimal charge1 = calculateMethod.calculateChargeForTimeInZone(eventLog);

        DateTime secondEnteringTime = firstEnteringTime.plusMinutes(AnHour * 9);  //enter at 3:00 pm
        new APairOfNormalCrossingEventsCreator(secondEnteringTime, AnHour * 1, eventLog).create();
                                                                                        //exit at 4:00 pm
        BigDecimal charge2 = calculateMethod.calculateChargeForTimeInZone(eventLog);

        assertTrue(charge1.compareTo(charge2) != 0);
        assertThat(charge2, is(BigDecimal.valueOf(10)));
    }

    @Test
    public void wouldChargeTwelvePoundsIfStayInsideLongerThanFourHoursOnAGivenDay(){
        List<ZoneBoundaryCrossing> eventLog =new ArrayList<ZoneBoundaryCrossing>();

        DateTime firstEnteringTime = new DateTime(2018, 9, 19, 1, 0,0,0); //1:00 am
        new APairOfNormalCrossingEventsCreator(firstEnteringTime, AnHour * 3, eventLog).create();
                                                                                        //exit at 4:00 am

        DateTime secondEnteringTime = firstEnteringTime.plusMinutes(AnHour * 4);  //5:00 am
        new APairOfNormalCrossingEventsCreator(secondEnteringTime, AnHour * 3, eventLog).create();
                                                                                        //exit at 8:00 am
        //at the end of the day
        BigDecimal charge = calculateMethod.calculateChargeForTimeInZone(eventLog);
        assertThat(charge, is(BigDecimal.valueOf(12)));
    }

}

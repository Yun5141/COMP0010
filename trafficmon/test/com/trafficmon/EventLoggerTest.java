package com.trafficmon;

import org.joda.time.DateTime;
import org.joda.time.DateTimeUtils;
import org.junit.Test;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class EventLoggerTest{


    EventLogger eventLogger = EventLogger.getInstance();
    private final Vehicle vehicle = Vehicle.withRegistration("A123 XYZ");

    @Test
    public void addTheVehicleIsEnteringIntoTheLogger(){

        eventLogger.vehicleEnteringZone(vehicle);

        List<ZoneBoundaryCrossing> eventLog = eventLogger.getEventLog();

        assertTrue(eventLog.get(eventLog.size() - 1) instanceof EntryEvent);
        assertThat(eventLog.get(eventLog.size() - 1).getVehicle(),is(vehicle));
    }

    @Test
    public void loggingEnteringTime(){

        DateTime enteringTime = new DateTime(2018, 9, 19, 6, 58,9,0);
        DateTimeUtils.setCurrentMillisFixed(enteringTime.getMillis());

        eventLogger.vehicleEnteringZone(vehicle);

        List<ZoneBoundaryCrossing> eventLog = eventLogger.getEventLog();

        assertTrue(eventLog.get(eventLog.size() - 1) instanceof EntryEvent);
        assertThat(eventLog.get(eventLog.size() - 1).timestamp(), is(enteringTime.getMillis()));

        eventLog.clear();
    }


    @Test
    public void WouldNotRemoveTheVehicleIsLeavingFromTheLogger(){

        List<ZoneBoundaryCrossing> oldEventLog = eventLogger.getEventLog();

        eventLogger.vehicleEnteringZone(vehicle);
        eventLogger.vehicleLeavingZone(vehicle);

        List<ZoneBoundaryCrossing> eventLog = eventLogger.getEventLog();

        assertFalse(eventLog.size() - oldEventLog.size() == 0);
    }

    @Test
    public void loggingExitingTime(){

        DateTime enteringTime = new DateTime(2018, 9, 19, 6, 58,9,0);
        DateTimeUtils.setCurrentMillisFixed(enteringTime.getMillis());

        eventLogger.vehicleEnteringZone(vehicle);

        DateTime leavingTime = enteringTime.plusMinutes(50);
        DateTimeUtils.setCurrentMillisFixed(leavingTime.getMillis());

        eventLogger.vehicleLeavingZone(vehicle);

        List<ZoneBoundaryCrossing> eventLog = eventLogger.getEventLog();

        assertTrue(eventLog.get(eventLog.size() - 1) instanceof ExitEvent);
        assertThat(eventLog.get(eventLog.size() - 1).timestamp(),is(leavingTime.getMillis()));
    }

    @Test
    public void ifTheVehicleHasNotEnteredButNowLeavingThenDoNothing(){
        Vehicle ghostVehicle=Vehicle.withRegistration("ABC DEFG");

        List<ZoneBoundaryCrossing> oldEventLog = eventLogger.getEventLog();

        eventLogger.vehicleLeavingZone(ghostVehicle);

        List<ZoneBoundaryCrossing> eventLog = eventLogger.getEventLog();

        assertThat(eventLog.size() - oldEventLog.size(), is(0));
    }

}

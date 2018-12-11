package com.trafficmon;

import org.joda.time.DateTime;
import org.joda.time.DateTimeUtils;

import java.util.List;

/*
 * To help the two calculator tests log a pair of mock events without calling to the EventLogger class
 *
 * This class would create and log an entry event with the "enteringTime" provided ,
 * and log an exiting event with the time that is "timeSpentInTheZone" after the "enteringTime"
 * Both of the events would be logged at the given "eventLog"
 * */

public class APairOfNormalCrossingEventsCreator {

    private final DateTime enteringTime;
    private final int timeSpentInTheZone;
    private final List<ZoneBoundaryCrossing> eventLog;

    final Vehicle vehicle = Vehicle.withRegistration("A123 XYZ");

    public APairOfNormalCrossingEventsCreator(DateTime enteringTime, int timeSpentInTheZone, List<ZoneBoundaryCrossing> eventLog){

        this.enteringTime = enteringTime;
        this.timeSpentInTheZone = timeSpentInTheZone;
        this.eventLog = eventLog;
    }

    public void  create(){

        DateTimeUtils.setCurrentMillisFixed(this.enteringTime.getMillis());

        eventLog.add(new EntryEvent(vehicle));

        DateTime leavingTime = enteringTime.plusMinutes(this.timeSpentInTheZone);
        DateTimeUtils.setCurrentMillisFixed(leavingTime.getMillis());

        eventLog.add(new ExitEvent(vehicle));
    }
}

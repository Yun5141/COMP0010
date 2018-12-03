package com.trafficmon;

import org.joda.time.DateTime;
import org.joda.time.DateTimeUtils;

import java.util.List;

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

package com.trafficmon;

import java.util.ArrayList;
import java.util.List;

public class EventLogger{

    private static EventLogger instance;

    private final List<ZoneBoundaryCrossing> eventLog;

    private EventLogger(){
        this.eventLog = new ArrayList<ZoneBoundaryCrossing>();
    }

    public static synchronized EventLogger getInstance(){
        if(instance == null){
            instance = new EventLogger();
        }
        return instance;
    }

    public void vehicleEnteringZone(Vehicle vehicle) {
        eventLog.add(new EntryEvent(vehicle));
    }


    public void vehicleLeavingZone(Vehicle vehicle) {
        if (!previouslyRegistered(vehicle)) {   //本身就在city zone里面，没有进来过，只现在出去
            return;
        }
        eventLog.add(new ExitEvent(vehicle));
    }


    public List<ZoneBoundaryCrossing> getEventLog(){

        List<ZoneBoundaryCrossing> copy_eventLog = new ArrayList<>(this.eventLog);

        return copy_eventLog;
    }

    private boolean previouslyRegistered(Vehicle vehicle) {
        for (ZoneBoundaryCrossing crossing : eventLog) {
            if (crossing.getVehicle().equals(vehicle)) {
                return true;
            }
        }
        return false;
    }

}

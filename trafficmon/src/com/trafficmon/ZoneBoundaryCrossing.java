package com.trafficmon;
import org.joda.time.*;

public abstract class ZoneBoundaryCrossing {

    private final Vehicle vehicle;
    private final long time;

    public ZoneBoundaryCrossing(Vehicle vehicle) {
        this.vehicle = vehicle;
        this.time = DateTimeUtils.currentTimeMillis();
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public long timestamp() {
        return this.time;
    }

}
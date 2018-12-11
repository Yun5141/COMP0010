You can run the new system like this:


public class Examples {
    public static void main(String args[]){
        EventLogger eventLogger = EventLogger.getInstance();
        eventLogger.vehicleEnteringZone(Vehicle.withRegistration("A123 XYZ"));
        //some time passes
        eventLogger.vehicleLeavingZone(Vehicle.withRegistration("A123 XYZ"));

        eventLogger.vehicleEnteringZone(Vehicle.withRegistration("J091 4PY"));
        //some time passes
        eventLogger.vehicleLeavingZone(Vehicle.withRegistration("J091 4PY"));

        CongestionChargeSystem congestionChargeSystem = new CongestionChargeSystem(new ChargeByTimePeriodCalculator());
        congestionChargeSystem.calculateCharges();

    }
}

package com.trafficmon;

import java.math.BigDecimal;
import java.util.*;

public class CongestionChargeSystem {

    private final List<ZoneBoundaryCrossing> eventLog=EventLogger.getInstance().getEventLog();
    private final ChargeGenerator chargeGenerator;
    private final Actions actionsTaken=new ActionsDecided().getActions();

    public CongestionChargeSystem(ChargeGenerator chargeGenerator){
        this.chargeGenerator=chargeGenerator;
    }




    public void calculateCharges() {

        Map<Vehicle, List<ZoneBoundaryCrossing>> crossingsByVehicle = new HashMap<Vehicle, List<ZoneBoundaryCrossing>>();

        for (ZoneBoundaryCrossing crossing : eventLog) {
            if (!crossingsByVehicle.containsKey(crossing.getVehicle())) {
                crossingsByVehicle.put(crossing.getVehicle(), new ArrayList<ZoneBoundaryCrossing>());
            }
            crossingsByVehicle.get(crossing.getVehicle()).add(crossing);
        }

        for (Map.Entry<Vehicle, List<ZoneBoundaryCrossing>> vehicleCrossings : crossingsByVehicle.entrySet()) {
            Vehicle vehicle = vehicleCrossings.getKey();
            List<ZoneBoundaryCrossing> crossings = vehicleCrossings.getValue();

            if (!checkOrderingOf(crossings)) {
                actionsTaken.investigations(vehicle);
            } else {

                BigDecimal charge = chargeGenerator.calculateChargeForTimeInZone(crossings);

                try { actionsTaken.deductCharge(vehicle,charge);
                } catch (InsufficientCreditException ice) {
                    actionsTaken.penaltyNotice(vehicle, charge);
                } catch (AccountNotRegisteredException e) {
                    actionsTaken.penaltyNotice(vehicle, charge);
                }
            }
        }
    }

    private boolean checkOrderingOf(List<ZoneBoundaryCrossing> crossings) {

        ZoneBoundaryCrossing lastEvent = crossings.get(0);

        for (ZoneBoundaryCrossing crossing : crossings.subList(1, crossings.size())) {
            if (crossing.timestamp() < lastEvent.timestamp()) {
                return false;
            }
            if (crossing instanceof EntryEvent && lastEvent instanceof EntryEvent) {
                return false;
            }
            if (crossing instanceof ExitEvent && lastEvent instanceof ExitEvent) {
                return false;
            }
            lastEvent = crossing;
        }

        return true;
    }


}

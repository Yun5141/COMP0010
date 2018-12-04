package com.trafficmon;

import java.math.BigDecimal;
import java.util.List;
import org.joda.time.*;

public class ChargeByTimePeriodCalculator implements ChargeGenerator{

    public BigDecimal calculateChargeForTimeInZone(List<ZoneBoundaryCrossing> crossings) {

        BigDecimal charge = new BigDecimal(0);
        int totalTime=0;

        ZoneBoundaryCrossing lastEvent = crossings.get(0);

        charge = charge.add(getAddingCharge(lastEvent.timestamp()));

        for (ZoneBoundaryCrossing crossing : crossings.subList(1, crossings.size())) {

            if (crossing instanceof ExitEvent) {
                totalTime = totalTime + minutesBetween(lastEvent.timestamp(), crossing.timestamp());
                if(totalTime > 240) {
                    charge = BigDecimal.valueOf(12);
                    break;
                }
            }

            if (crossing instanceof EntryEvent) {
                if(minutesBetween(lastEvent.timestamp(),crossing.timestamp())>240)
                {
                    charge = charge.add(getAddingCharge(crossing.timestamp()));
                }
            }

            lastEvent = crossing;
        }

        return charge;
    }

    private int minutesBetween(long startTimeMs, long endTimeMs) {
        return (int) Math.ceil((endTimeMs - startTimeMs) / (1000.0 * 60.0));
    }

    private BigDecimal getAddingCharge(long timeMills)
    {
        DateTime dateTime = new DateTime(timeMills);
        int hour = dateTime.getHourOfDay();
        if(hour<14)
            return BigDecimal.valueOf(6);
        else return BigDecimal.valueOf(4);
    }


}


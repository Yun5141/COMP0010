package com.trafficmon;

import java.math.BigDecimal;
import java.util.List;

public interface ChargeGenerator {
    BigDecimal calculateChargeForTimeInZone(List<ZoneBoundaryCrossing> crossings);
}

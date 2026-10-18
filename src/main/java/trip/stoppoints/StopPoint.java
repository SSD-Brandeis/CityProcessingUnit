package trip.stoppoints;

import driver.Driver;
import trip.Vehicle;

/**
 * What stands at the end of a road: a stop sign, a red light, or the destination.
 */
public abstract class StopPoint {

    public abstract void goThrough(Driver driver, Vehicle vehicle);
}

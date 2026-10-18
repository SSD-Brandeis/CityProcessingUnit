package trip;

import driver.Driver;

/**
 * Location is a specific point on a road.
 * You can think it as (the address of) an instruction in a process's code.
 */
public record Location(Road road, int position) {

    /**
     * Drives the given driver's current vehicle to this location; the location itself does not change.
     * This represents the CPU executing the instruction at this address.
     *
     * @param driver the driver driving the vehicle
     * @return true if the vehicle reached this location, false otherwise
     */
    public boolean goTo(Driver driver) {
        return road.goTo(driver, position);
    }
}

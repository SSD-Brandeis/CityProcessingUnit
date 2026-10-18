package trip;

import driver.Driver;
import logbook.EventType;
import trip.stoppoints.StopPoint;

/**
 * A road, which takes as many ticks to drive as its length.
 * Think of it as one CPU burst.
 * At the end of a road we may have a {@code StopPoint}, which determines
 * the actions that must be taken by the driver.
 */
public class Road {
    protected final String name;
    private final int length;
    protected StopPoint stopPoint;

    public Road(String name, int length, StopPoint stopPoint) {
        this.name = name;
        this.length = length;
        this.stopPoint = stopPoint;
    }

    public Road(String name, int length) {
        this(name, length, null);
    }

    /**
     * Return the start Location at the road.
     * Imagine this as a base address where the program is loaded
     *
     * @return this road's start Location
     */
    public Location getStartLocation() {
        return new Location(this, 0);
    }

    public void setStopPoint(StopPoint stopPoint) {
        this.stopPoint = stopPoint;
    }

    /**
     * Simulates the given driver driving their current vehicle to the given position on the road.
     * This is done by calling {@code enter()} for the first position of the road and {@code exit()} for the last position.
     * On a road of length 1, the only position is both, so the road is entered and then exited.
     * Nothing is done for all other positions.
     *
     * @param driver the driver driving a vehicle to the position
     * @param position the position within the range of the road's length
     * @return true if the position was successfully reached; can only be false if enter fails
     */
    public boolean goTo(Driver driver, int position) {
        if (position == 0 && !enter(driver, driver.getVehicle())) {
            return false;
        }
        if (position == length - 1) {
            exit(driver, driver.getVehicle());
        }
        return true;
    }

    /**
     * Logs the enter event for the given driver and vehicle and returns true, since a road can always be entered.
     *
     * @param driver the driver driving the vehicle
     * @param vehicle the vehicle entering the road
     * @return {@code true}
     */
    public boolean enter(Driver driver, Vehicle vehicle) {
        driver.getLogbook().record(vehicle, this, EventType.ROAD_ENTER);
        return true;
    }

    /**
     * Logs the exit event for the given driver and vehicle, then calls this road's StopPoint's {@code goThrough()} method.
     *
     * @param driver the driver driving the vehicle
     * @param vehicle the vehicle exiting the road
     */
    public void exit(Driver driver, Vehicle vehicle) {
        driver.getLogbook().record(vehicle, this, EventType.ROAD_EXIT);
        if (stopPoint != null) {
            stopPoint.goThrough(driver, vehicle);
        }
    }

    public String getName() {
        return name;
    }

    public int getLength() {
        return length;
    }

    public StopPoint getStopPoint() {
        return stopPoint;
    }

    @Override
    public String toString() {
        return String.format("ROAD %s", name);
    }
}

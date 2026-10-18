package trip;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A vehicle's trip along its route: where the vehicle was left, how long it was driven each time, and its priority
 * number. See it as a process control block (PCB), where the vehicle ID is the process ID.
 */
public class Trip {

    private final Route route;
    private final Vehicle vehicle;
    private double priority;

    private Location nextAddress;
    private final List<Integer> stretchLengths = new ArrayList<>();

    public Trip(Route route, Vehicle vehicle, double priority) {
        this.route = route;
        this.vehicle = vehicle;
        this.priority = priority;
        nextAddress = route.getStartLocation();
    }

    public Trip(Route route, Vehicle vehicle) {
        this(route, vehicle, 0);
    }

    /**
     * Saves where the trip was left and how long it was driven for, so it can be continued later.
     * The dispatcher calls this each time the driver leaves the trip's vehicle.
     *
     * @param nextAddress the address to continue the trip from
     * @param stretchLength the number of ticks the trip was driven for since it was last picked
     */
    public void saveProgress(Location nextAddress, int stretchLength) {
        this.nextAddress = nextAddress;
        stretchLengths.add(stretchLength);
    }

    /**
     * Returns the length of the trip's most recent stretch.
     *
     * @return the number of ticks the trip was last driven for, or 0 if it has not been driven yet
     */
    public int getLastStretchLength() {
        return stretchLengths.isEmpty() ? 0 : stretchLengths.getLast();
    }

    /**
     * Returns the lengths of all of the trip's stretches so far, oldest first.
     *
     * @return the number of ticks the trip was driven for each time, which cannot be changed
     */
    public List<Integer> getStretchLengths() {
        return Collections.unmodifiableList(stretchLengths);
    }

    public Location getNextAddress() {
        return nextAddress;
    }

    public Route getRoute() {
        return route;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public double getPriority() {
        return priority;
    }

    public void setPriority(double priority) {
        this.priority = priority;
    }

    @Override
    public String toString() {
        return String.format("TRIP: %s being driven by %s", route, vehicle);
    }
}

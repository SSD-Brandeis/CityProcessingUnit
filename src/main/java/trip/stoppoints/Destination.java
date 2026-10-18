package trip.stoppoints;

import driver.Driver;
import driver.Message;
import trip.Vehicle;

/**
 * A vehicle's destination, at the end of its trip.
 * Similar to the HALT instruction that makes the process exit.
 */
public class Destination extends StopPoint {

    /**
     * Add a DESTINATION message for the given vehicle to the given driver's message queue.
     */
    public void goThrough(Driver driver, Vehicle vehicle) {
        driver.sendMessage(new Message(vehicle.id(), Message.Type.DESTINATION));
    }
}

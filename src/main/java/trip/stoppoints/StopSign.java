package trip.stoppoints;

import driver.Driver;
import driver.Message;
import trip.Vehicle;

/**
 * A stop sign. Represents a non-blocking instruction that yields the CPU.
 * The process can be picked again right away.
 */
public class StopSign extends StopPoint {

    /**
     * Add a STOP_SIGN message for the given vehicle to the given driver's message queue.
     */
    public void goThrough(Driver driver, Vehicle vehicle) {
        driver.sendMessage(new Message(vehicle.id(), Message.Type.STOP_SIGN));
    }
}

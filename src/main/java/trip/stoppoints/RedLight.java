package trip.stoppoints;

import driver.Driver;
import driver.Message;
import trip.Vehicle;

/**
 * A red light.
 * This represents a blocking instruction which invokes a device (such as a
 * disk) or a separate process to complete a task, and waits for it to complete.
 * Can also represent giving up the CPU for a specified duration (sleeping).
 */
public class RedLight extends StopPoint {
    private final int waitTime;

    public RedLight(int waitTime) {
        this.waitTime = waitTime;
    }

    /**
     * Add a RED_LIGHT message for the given vehicle to the given driver's message
     * queue. The light then stays red for this RedLight's waitTime number of ticks.
     * In the following tick, a GREEN_LIGHT message is added to represent the
     * completion of the work.
     */
    public void goThrough(Driver driver, Vehicle vehicle) {
        driver.sendMessage(new Message(vehicle.id(), Message.Type.RED_LIGHT));
        driver.getClock().setAlarm(waitTime + 1,
                () -> driver.sendMessage(new Message(vehicle.id(), Message.Type.GREEN_LIGHT)));
    }

    public int getWaitTime() {
        return waitTime;
    }
}

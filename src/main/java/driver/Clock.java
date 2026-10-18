package driver;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The city clock, which counts ticks. Just like a CPU/hardware clock.
 * In part A there is only one driver, so the driver itself moves the clock forward by calling {@code waitForTick()}.
 * <p>
 * The clock may run actions at the start of a given tick, before the driver does anything in that tick.
 * Red lights use this to signal green after a fixed number of ticks, and trips use it to arrive: for example,
 * {@code clock.setAlarmAt(4, () -> dispatcher.addNewTrip(trip))} makes a trip arrive at tick 4.
 */
public class Clock {

    private final Map<Integer, List<Runnable>> alarms = new HashMap<>();
    private int tickNum;

    /**
     * Starts the next tick, then runs every alarm set for that tick in the order the alarms were set.
     * In part A nothing else is running, so there is nothing to wait for and the tick starts immediately.
     */
    public void waitForTick() {
        tickNum++;
        List<Runnable> due = alarms.remove(tickNum);
        if (due != null) {
            due.forEach(Runnable::run);
        }
    }

    /**
     * Calls {@code waitForTick()} the given number of times.
     *
     * @param numTicks the number of ticks to start
     */
    public void waitForTicks(int numTicks) {
        for (int i = 0; i < numTicks; i++) {
            waitForTick();
        }
    }

    /**
     * Sets an alarm that runs the given action at the start of the given tick,
     * before the driver does anything in that tick.
     * Alarms set for the same tick run in the order they were set.
     *
     * @param tick   the tick to run the action at, which must be after the current tick
     * @param action the action to run
     */
    public void setAlarmAt(int tick, Runnable action) {
        if (tick <= tickNum) {
            throw new IllegalArgumentException("tick " + tick + " is not after the current tick " + tickNum);
        }
        alarms.computeIfAbsent(tick, t -> new ArrayList<>()).add(action);
    }

    /**
     * Sets an alarm that runs the given action at the start of the tick that is {@code numTicks} ticks from now.
     *
     * @param numTicks the number of ticks from now, at least 1
     * @param action   the action to run
     */
    public void setAlarm(int numTicks, Runnable action) {
        if (numTicks < 1) {
            throw new IllegalArgumentException("numTicks must be at least 1");
        }
        setAlarmAt(tickNum + numTicks, action);
    }

    /**
     * Returns whether any alarms have not run yet, such as trips that have not arrived.
     *
     * @return true if an alarm is set for a later tick, otherwise false
     */
    public boolean hasAlarms() {
        return !alarms.isEmpty();
    }

    /**
     * Returns the current tick number.
     *
     * @return the number of ticks that have been started; 0 before the first tick
     */
    public int getTickNum() {
        return tickNum;
    }
}

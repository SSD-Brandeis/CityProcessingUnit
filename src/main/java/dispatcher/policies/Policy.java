package dispatcher.policies;

import trip.Trip;

import java.util.List;

/**
 * Similar to a CPU scheduling algorithm. A policy is only the rule for which trip in the ready line is driven next.
 */
public interface Policy {

    /**
     * Picks the trip in the ready line that should be driven next. The ready line cannot be changed here.
     *
     * @param readyLine the trips in the ready line, in the order they joined it, oldest first; never empty
     * @return the trip to drive next, which must be one of the trips in the ready line
     */
    Trip pickNext(List<Trip> readyLine);

    /**
     * Returns the number of ticks a picked trip may be driven before the driver's timer stops it.
     *
     * @return the time slice, or 0 if this policy does not use the timer
     */
    default int timeSlice() {
        return 0;
    }
}

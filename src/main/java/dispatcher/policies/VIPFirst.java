package dispatcher.policies;

import trip.Trip;

import java.util.List;

/**
 * The VIP First policy.
 * The trip with the lowest priority number goes first, since it is the most important.
 * Trips with the same priority number go in the order they joined the ready line.
 */
public class VIPFirst implements Policy {

    @Override
    public Trip pickNext(List<Trip> readyLine) {
        // TODO
        return null;
    }
}

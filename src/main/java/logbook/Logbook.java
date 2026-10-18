package logbook;

import trip.Road;
import trip.Vehicle;

import java.util.LinkedList;
import java.util.Queue;


/**
 * The logbook: a queue of events in the order they happened. Used by the tests to verify behavior.
 */
public class Logbook {

    private final Queue<Event> queue = new LinkedList<>();
    private int eventCounter;

    /**
     * Creates an event with the given parameters and adds it to the logbook with
     * an event ID 1 greater than the previous event added to the logbook.
     * The first event added has ID 1.
     *
     * @param vehicle the event's vehicle
     * @param road    the event's road
     * @param type    the event's type
     */
    public void record(Vehicle vehicle, Road road, EventType type) {
        queue.add(new Event(vehicle, road, type, ++eventCounter));
    }

    public void record(Vehicle vehicle, EventType type) {
        record(vehicle, null, type);
    }

    public void record(EventType type) {
        record(null, null, type);
    }

    /**
     * Retrieves and removes the head of the logbook, or returns null if the logbook is empty.
     *
     * @return the head of the logbook, or null if the logbook is empty
     */
    public Event poll() {
        return queue.poll();
    }

    /**
     * Removes all events from the logbook and resets the ID counter.
     */
    public void clear() {
        queue.clear();
        eventCounter = 0;
    }

    /**
     * Returns the current event ID in the sequence. Starts at 0 when no events have been added to the logbook.
     *
     * @return the ID of the most recent event, or 0 if no events have been added
     */
    public int getCurrentEventId() {
        return eventCounter;
    }

    /**
     * Returns the current size of the logbook.
     *
     * @return the number of events in the logbook
     */
    public int size() {
        return queue.size();
    }
}

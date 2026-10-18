package logbook;

import trip.Road;
import trip.Vehicle;

import java.util.Objects;

/**
 * An event for logging.
 */
public class Event {

    private final Vehicle vehicle;
    private final Road road;
    private final EventType eventType;
    private final int id;

    public Event(Vehicle vehicle, Road road, EventType event, int id) {
        this.vehicle = vehicle;
        this.road = road;
        this.eventType = event;
        this.id = id;
    }

    public Event(Vehicle vehicle, EventType event, int id) {
        this(vehicle, null, event, id);
    }

    public Event(EventType event, int id) {
        this(null, null, event, id);
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public Road getRoad() {
        return road;
    }

    public EventType getEventType() {
        return eventType;
    }

    public int getId() {
        return id;
    }

    @Override
    public String toString() {
        return switch (eventType) {
            case TRIP_START, TRIP_COMPLETE -> String.format("%d: %s %s", id, vehicle, eventType);
            default -> String.format("%d: %s %s %s", id, vehicle, eventType, road);
        };
    }

    /**
     * Checks if the event IDs and event types are the same and if the roads and vehicles are the same objects.
     *
     * @param o the event to compare with
     * @return true if road, vehicle, event type, and event ID are all the same; otherwise false
     */
    @Override
    public boolean equals(Object o) {
        if (o instanceof Event) {
            Event event = (Event) o;
            return event.getId() == this.id && this.weakEquals(event);
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return Objects.hash(vehicle, road, eventType, id);
    }

    /**
     * Checks if the event types are the same and if the roads and vehicles are the same objects.
     * Does not compare the event ids.
     *
     * @param other the event to compare with
     * @return true if road, vehicle, and event type are the same; otherwise false
     */
    public boolean weakEquals(Event other) {
        return road == other.getRoad() && vehicle == other.getVehicle() && eventType == other.getEventType();
    }
}

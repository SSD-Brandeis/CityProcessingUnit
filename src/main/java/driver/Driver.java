package driver;

import logbook.Logbook;
import dispatcher.Dispatcher;
import trip.Location;
import trip.Route;
import trip.Trip;
import trip.Vehicle;

import java.util.LinkedList;
import java.util.Queue;

/**
 * The driver, which drives one vehicle at a time like a single core of CPU runs one process at a time.
 * At every tick, the driver either drives its current vehicle one step along its route, or goes to the dispatcher.
 */
public class Driver {

    private final Clock clock;
    private final Logbook logbook;
    private Dispatcher dispatcher;
    private final Queue<Message> messageQueue = new LinkedList<>();

    private Vehicle vehicle;
    private Route route;
    private Location nextAddress;

    private int timerTicksLeft;


    public Driver(Clock clock, Logbook logbook) {
        this.clock = clock;
        this.logbook = logbook;
    }

    public Clock getClock() {
        return clock;
    }

    public Logbook getLogbook() {
        return logbook;
    }

    public Dispatcher getDispatcher() {
        return dispatcher;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    /**
     * Sets the dispatcher to handle communication between driver and dispatcher, through {@code runTick()}.
     * This method is called automatically when constructing a Dispatcher and should not be called manually.
     *
     * @param dispatcher the dispatcher to be used
     */
    public void setDispatcher(Dispatcher dispatcher) {
        this.dispatcher = dispatcher;
    }

    /**
     * Sets the driver's state (vehicle, route, and nextAddress) based on the information stored in the given trip.
     *
     * @param trip the trip to drive when {@code runTick()} is called, or null to leave the driver without a vehicle
     */
    public void setTrip(Trip trip) {
        if (trip != null) {
            vehicle = trip.getVehicle();
            route = trip.getRoute();
            nextAddress = trip.getNextAddress();
        } else {
            vehicle = null;
            route = null;
            nextAddress = null;
        }
    }

    /**
     * Calls {@code runTick()} in a loop until all trips in the driver's dispatcher are finished and the clock has no
     * alarms left to run, such as trips that have not arrived yet.
     */
    public void driveAllTrips() {
        while (dispatcher.getNumTrips() > 0 || clock.hasAlarms()) {
            runTick();
        }
    }

    /**
     * Runs one tick. First, the clock starts the tick, which runs any alarms set for it, such as trips arriving and red
     * lights turning green. Then the driver does one of two things.
     * If there are any messages, or if the driver is currently not in a vehicle, the dispatcher's {@code dispatch()} is called.
     * Otherwise, the driver goes to the next address and sets the new next address by calling the current route's {@code goTo()} method.
     * If the timer is on (applicable for RoundRobin), a tick of driving also counts down the timer.
     */
    public void runTick() {
        if (dispatcher == null) {
            throw new IllegalStateException("dispatcher is null");
        }
        clock.waitForTick();
        if (vehicle == null || !messageQueue.isEmpty()) {
            dispatcher.dispatch();
        } else {
            nextAddress = route.goTo(nextAddress, this);
            countDownTimer();
        }
    }

    /**
     * Sets the timer to add a {@code TIMER} message for the current vehicle after the given number of ticks of
     * driving. Ticks spent at the dispatcher do not count. Setting the timer replaces any time left on it.
     * Like a hardware timer that interrupts the running process when its time slice runs out.
     *
     * @param numTicks the number of ticks of driving before the message, at least 1
     */
    public void startTimer(int numTicks) {
        if (numTicks < 1) {
            throw new IllegalArgumentException("numTicks must be at least 1");
        }
        timerTicksLeft = numTicks;
    }

    /**
     * Returns the number of ticks of driving left before the timer adds a {@code TIMER} message. Used for testing.
     *
     * @return the ticks left on the timer, or 0 if the timer is off
     */
    public int getTimerTicksLeft() {
        return timerTicksLeft;
    }

    private void countDownTimer() {
        if (timerTicksLeft > 0) {
            timerTicksLeft--;
            if (timerTicksLeft == 0) {
                sendMessage(new Message(vehicle.id(), Message.Type.TIMER));
            }
        }
    }

    /**
     * Adds the given message to the driver's message queue.
     *
     * @param message the message to add
     */
    public void sendMessage(Message message) {
        messageQueue.add(message);
    }

    /**
     * Returns the driver's message queue, which holds the messages
     * the dispatcher has not handled yet, oldest first.
     *
     * @return the queue where messages the dispatcher handles are put
     */
    public Queue<Message> getMessageQueue() {
        return messageQueue;
    }

    /**
     * Returns an address the driver will drive to the next time it drives its current vehicle.
     * The dispatcher saves this in the current trip before the driver leaves the vehicle.
     *
     * @return the current vehicle's next address along its route
     */
    public Location getNextAddress() {
        return nextAddress;
    }
}

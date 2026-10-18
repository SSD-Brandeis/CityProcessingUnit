package driver;

/**
 * A message for the dispatcher: what happened, and the ID of the vehicle it happened to.
 * Think of it as an interrupt.
 */
public record Message(int vehicleId, Type type) {

    public enum Type {
        STOP_SIGN,      // vehicle reached a stop sign
        RED_LIGHT,      // vehicle reached a red light
        GREEN_LIGHT,    // the red light this vehicle (vehicleId) was waiting at turned green
        DESTINATION,    // vehicle reached its destination
        TIMER           // vehicle's time slice ran out (only applicable for round robin)
    }
}

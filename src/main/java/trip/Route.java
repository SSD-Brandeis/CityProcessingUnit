package trip;

import driver.Driver;

import java.util.ArrayList;
import java.util.List;

/**
 * A list of connected Roads
 * You can imagine this as a set of instructions of a process.
 */
public class Route {
    private final String name;
    private final List<Road> roads;
    private final int length;

    public Route(String name, List<Road> roads) {
        this.name = name;
        this.roads = new ArrayList<>(roads);
        length = roads.stream().mapToInt(Road::getLength).sum();
    }

    public Road getRoad(int index) {
        return roads.get(index);
    }

    public List<Road> getRoads() {
        return roads;
    }

    public Location getStartLocation() {
        return roads.getFirst().getStartLocation();
    }

    /**
     * Calls {@code address.goTo()} with the given driver, and returns the next address to go to.
     *
     * @param address the address to go to
     * @param driver the driver to pass as a parameter
     * @return the next address to go to (either the next address in the route or the same address given,
     * depending on the return value of {@code address.goTo()}), or null after the route's last address
     * @throws IllegalStateException if the route has already ended, which happens only if its last road does not
     * end at a {@code Destination}
     */
    public Location goTo(Location address, Driver driver) {
        if (address == null) {
            throw new IllegalStateException(this + " has ended, but its last road does not end at a Destination");
        }
        return address.goTo(driver) ? getNextAddress(address) : address;
    }

    private Location getNextAddress(Location address) {
        int idx = roads.indexOf(address.road());
        if (idx == -1) {
            throw new IllegalArgumentException("No such road in route");
        }
        int position = address.position() + 1;
        if (position < address.road().getLength()) {
            return new Location(address.road(), position);
        } else if (idx + 1 < roads.size()){
            return roads.get(idx + 1).getStartLocation();
        }
        return null;
    }

    @Override
    public String toString() {
        return String.format("ROUTE %s (length = %d, num_roads = %d)", name, length, roads.size());
    }
}

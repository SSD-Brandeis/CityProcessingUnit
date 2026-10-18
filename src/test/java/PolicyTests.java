import dispatcher.policies.FirstInFirstOut;
import dispatcher.policies.Policy;
import dispatcher.policies.RoundRobin;
import dispatcher.policies.ShortestNextStretchFirst;
import dispatcher.policies.VIPFirst;
import org.junit.jupiter.api.*;
import trip.Road;
import trip.Route;
import trip.Trip;
import trip.Vehicle;
import trip.stoppoints.Destination;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests each policy on its own, without a driver or a dispatcher.
 * Each test makes a ready line and checks which trip the policy picks from it.
 */
@Timeout(value = 1, threadMode = Timeout.ThreadMode.SEPARATE_THREAD)
public class PolicyTests {

    private List<Trip> trips;

    @BeforeEach
    public void initTrips() {
        trips = IntStream.range(0, 5)
                .mapToObj((i) -> new Trip(new Route(Integer.toString(i), List.of(new Road("1", 5, new Destination()))),
                        new Vehicle(i + 1)))
                .collect(Collectors.toCollection(ArrayList::new));
    }

    /**
     * Returns a ready line of the trips with the given indices, in the given order. It cannot be changed.
     */
    private List<Trip> readyLine(int... indices) {
        return Arrays.stream(indices).mapToObj(trips::get).toList();
    }

    /**
     * Gives the trip with the given index stretches of the given lengths, as the dispatcher would.
     */
    private void drive(int index, int... stretchLengths) {
        Trip trip = trips.get(index);
        for (int stretchLength : stretchLengths) {
            trip.saveProgress(trip.getNextAddress(), stretchLength);
        }
    }

    @Nested
    class FirstInFirstOutTests {

        @Test
        public void testPicksOldest() {
            Policy policy = new FirstInFirstOut();
            assertSame(trips.get(0), policy.pickNext(readyLine(0, 1, 2)));
            assertSame(trips.get(3), policy.pickNext(readyLine(3, 1, 0)));
            assertSame(trips.get(4), policy.pickNext(readyLine(4)));
        }

        @Test
        public void testIgnoresPriorityAndStretches() {
            Policy policy = new FirstInFirstOut();
            trips.get(2).setPriority(5);
            trips.get(0).setPriority(1);
            drive(2, 10);
            assertSame(trips.get(2), policy.pickNext(readyLine(2, 0)));
            assertEquals(0, policy.timeSlice());
        }
    }

    @Nested
    class VIPFirstTests {

        @Test
        public void testPriority() {
            Policy policy = new VIPFirst();
            trips.get(0).setPriority(4);
            trips.get(1).setPriority(3);
            assertSame(trips.get(1), policy.pickNext(readyLine(0, 1)));
            assertSame(trips.get(1), policy.pickNext(readyLine(1, 0)));
            assertEquals(0, policy.timeSlice());
        }

        @Test
        public void testFifoForSamePriority() {
            Policy policy = new VIPFirst();
            trips.get(0).setPriority(3);
            trips.get(1).setPriority(1);
            trips.get(2).setPriority(1);
            trips.get(3).setPriority(1);
            trips.get(4).setPriority(2);
            assertSame(trips.get(1), policy.pickNext(readyLine(0, 1, 2, 3, 4)));
            assertSame(trips.get(2), policy.pickNext(readyLine(0, 2, 3, 4, 1)));
            assertSame(trips.get(3), policy.pickNext(readyLine(4, 0, 3, 1)));
            assertSame(trips.get(4), policy.pickNext(readyLine(0, 4)));
        }
    }

    @Nested
    class ShortestNextStretchFirstTests {

        @Test
        public void testEstimates() {
            ShortestNextStretchFirst policy = new ShortestNextStretchFirst(0.9);
            Trip trip = trips.getFirst();
            assertEquals(0, policy.getEstimate(trip));

            int[] stretchLengths = {5, 10, 7};
            double[] expectedEstimates = {5, 5.5, 5.65};
            for (int i = 0; i < stretchLengths.length; i++) {
                drive(0, stretchLengths[i]);
                assertEquals(expectedEstimates[i], policy.getEstimate(trip), 0.00000001);
            }
            assertEquals(0, policy.timeSlice());
        }

        @Test
        public void testFirstStretchIsTheEstimate() {
            ShortestNextStretchFirst policy = new ShortestNextStretchFirst(0.5);
            drive(0, 8);
            assertEquals(8, policy.getEstimate(trips.get(0)), 0.00000001);
            drive(0, 4);
            assertEquals(6, policy.getEstimate(trips.get(0)), 0.00000001);
        }

        @Test
        public void testOrder() {
            ShortestNextStretchFirst policy = new ShortestNextStretchFirst(0.9);
            drive(0, 10);
            drive(1, 5);
            drive(2, 4, 20);
            assertEquals(5.6, policy.getEstimate(trips.get(2)), 0.000001);

            assertSame(trips.get(1), policy.pickNext(readyLine(0, 1, 2))); // 5 is shorter than 5.6 and 10
            assertSame(trips.get(2), policy.pickNext(readyLine(0, 2)));
            assertSame(trips.get(3), policy.pickNext(readyLine(0, 1, 2, 3))); // A new trip's estimate is 0
        }

        @Test
        public void testFifoForSameEstimate() {
            ShortestNextStretchFirst policy = new ShortestNextStretchFirst(0.5);
            drive(0, 5);
            drive(1, 5);
            drive(2, 5);
            trips.get(1).setPriority(-10); // The priority number is not used
            assertSame(trips.get(2), policy.pickNext(readyLine(2, 0, 1)));
            assertSame(trips.get(0), policy.pickNext(readyLine(0, 1)));
            assertSame(trips.get(3), policy.pickNext(readyLine(0, 3, 4)));
        }
    }

    @Nested
    class RoundRobinTests {

        @Test
        public void testTimeSlice() {
            assertEquals(2, new RoundRobin(2).timeSlice());
            assertEquals(7, new RoundRobin(7).timeSlice());
        }

        @Test
        public void testPicksOldest() {
            Policy policy = new RoundRobin(3);
            assertSame(trips.get(0), policy.pickNext(readyLine(0, 1, 2)));
            assertSame(trips.get(2), policy.pickNext(readyLine(2, 0)));
        }
    }
}

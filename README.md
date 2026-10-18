# City Processing Unit

This project is a small simulation of **CPU scheduling**. A single driver (one CPU core) has many vehicles
(processes) to drive, but can drive only one at a time. Each vehicle's trip is a list of roads, and each road ends at
a stop sign, a red light, or the vehicle's destination. Whenever something happens that needs a decision, the
**dispatcher** (the scheduler) chooses which vehicle the driver drives next, following a **policy** (a scheduling
algorithm).

You write the dispatcher and four policies: First In, First Out; VIP First; Shortest Next Stretch First; and Round
Robin. Everything runs on one thread.

| In the city | In an operating system |
|---|---|
| driver | CPU core |
| vehicle and its ID | process and its process ID |
| trip | process control block (PCB) |
| route, made of roads | program, made of CPU bursts |
| stop sign / red light / destination | yield / wait for I/O / exit |
| message to the dispatcher | interrupt |
| dispatcher / policy | scheduler / scheduling algorithm |
| ready line / waiting list | ready queue / wait queue |
| timer and time slice | hardware timer and quantum |

The handout explains the city's rules in full. **Read it before you start:**
[City Processing Unit handout](https://moodle.brandeis.edu/mod/resource/view.php?id=396722) 

## Structural Overview

```
src/main/java
├── dispatcher/
│   ├── Dispatcher.java                     Task 1: handles messages and picks the next trip
│   └── policies/
│       ├── Policy.java                     the interface every policy implements (given)
│       ├── FirstInFirstOut.java            Task 2
│       ├── VIPFirst.java                   Task 3
│       ├── ShortestNextStretchFirst.java   Task 4
│       └── RoundRobin.java                 Task 5
├── driver/
│   ├── Driver.java                         drives one step per tick, or goes to the dispatcher
│   ├── Clock.java                          counts ticks and runs alarms (trip arrivals, lights turning green)
│   └── Message.java                        STOP_SIGN, RED_LIGHT, GREEN_LIGHT, DESTINATION, or TIMER for a vehicle
├── trip/
│   ├── Trip.java                           where a vehicle was left, its stretch lengths, and its priority
│   ├── Route.java                          the roads a trip drives, in order
│   ├── Road.java                           a road; it takes as many ticks to drive as its length
│   ├── Location.java                       one spot on a road
│   ├── Vehicle.java                        a vehicle ID
│   └── stoppoints/                         StopPoint, StopSign, RedLight, Destination
└── logbook/                                Logbook, Event, EventType: a record of what happened, read by the tests
src/test/java
├── DispatcherTests.java
├── PolicyTests.java
└── DriverTests.java
```

**The dispatcher and its policy.** `Dispatcher` keeps the ready line, the waiting list, and the finished trips, and
does all the work of moving trips between them. A policy only answers one question: given the ready line, which trip
should be driven next? A dispatcher is made with the policy it follows:

```java
Dispatcher dispatcher = new Dispatcher(driver, new RoundRobin(2));
```

## Tasks for You

Write the five classes below. The Javadoc comment above each class and method says exactly what it must do, so read
those comments along with the handout.

| Task | File | What you write |
|---|---|---|
| 1 | `dispatcher/Dispatcher.java` | `dispatch()`, `addNewTrip()`, the fields you need, and the methods that report the ready line, waiting list, and finished trips |
| 2 | `dispatcher/policies/FirstInFirstOut.java` | `pickNext()` |
| 3 | `dispatcher/policies/VIPFirst.java` | `pickNext()` |
| 4 | `dispatcher/policies/ShortestNextStretchFirst.java` | `pickNext()` and `getEstimate()` |
| 5 | `dispatcher/policies/RoundRobin.java` | `timeSlice()` |

- **Do not change any other file**, including `Policy.java` and the tests. The next part of the assignment replaces
  several of the given files, so your changes to them would be lost.
- Everything runs on one thread: you do not need threads, locks, or anything from `java.util.concurrent`. You may use
  any collection from `java.util`.
- You can do the tasks in any order. Task 1 and the policies are tested separately (see below).

## How to Test

**Requirements:** Java 21 or newer (`java -version`) and Maven (`mvn -v`).

From project root folder:

```
mvn test                                          # run all the tests
mvn test -Dtest=DispatcherTests                   # run one test class
mvn test -Dtest='PolicyTests$VIPFirstTests'       # run one nested test class (keep the quotes)
mvn test -Dtest='PolicyTests$VIPFirstTests#testPriority'   # run one test
```
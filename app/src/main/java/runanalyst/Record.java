package runanalyst;

import java.time.Duration;
import java.util.LinkedList;
import java.util.List;

public class Record {
    private final int targetInMeters;
    private List<GPXLocation> locations = new LinkedList<>();
    private int bestStartIdx = 0, bestEndIdx = 0, currentStartIdx = 0;
    private long timeInSeconds, bestTimeInSeconds = 0;
    private int distanceInMeters, bestDistanceInMeters = 0;

    public Record(int pTargetInMeters) {
        targetInMeters = pTargetInMeters;
    }

    public void addLocation(GPXLocation currentLocation) {
        locations.add(currentLocation);
        if (locations.size() > 2) {
            GPXLocation OldLast = locations.get(locations.size() - 2);
            if (distanceInMeters < targetInMeters) {
                // Add the locations until the distance of the record is reached
                distanceInMeters += Utils.distanceInMeters(OldLast, currentLocation);
                timeInSeconds += Duration.between(OldLast.getTime(), currentLocation.getTime()).getSeconds();
            } else {
                // Compute the time and the distance to remove
                GPXLocation firstLocation = locations.get(currentStartIdx);
                GPXLocation secondLocation = locations.get(currentStartIdx + 1);
                long startTime = Duration.between(firstLocation.getTime(), secondLocation.getTime()).getSeconds();
                int startDistance = Utils.distanceInMeters(firstLocation, secondLocation);
                // Compute the time with the new location
                long newTime = Duration.between(OldLast.getTime(), currentLocation.getTime()).getSeconds();
                timeInSeconds = timeInSeconds - startTime + newTime;
                // Compute the distance with the new location
                int newDistance = Utils.distanceInMeters(OldLast, currentLocation);
                distanceInMeters = distanceInMeters - startDistance + newDistance;
                // Move the index of the first location
                currentStartIdx++;
            }
            if (distanceInMeters >= targetInMeters) {
                if (bestTimeInSeconds == 0) {
                    // Initialize the time and the distance of the record
                    bestDistanceInMeters = distanceInMeters;
                    bestTimeInSeconds = timeInSeconds;
                    bestStartIdx = currentStartIdx;
                    bestEndIdx = locations.size() - 1;
                } else {
                    // Multiplier to increase the precision of the integer division
                    int mult = 1000000;
                    if (bestDistanceInMeters * mult / bestTimeInSeconds < distanceInMeters * mult / timeInSeconds) {
                        // The current location improves the record
                        bestStartIdx = currentStartIdx;
                        bestEndIdx = locations.size() - 1;
                        bestDistanceInMeters = distanceInMeters;
                        bestTimeInSeconds = timeInSeconds;
                    }
                }
            }
        } else if (locations.size() == 2) {
            distanceInMeters += Utils.distanceInMeters(locations.get(0), currentLocation);
            timeInSeconds += Duration.between(locations.get(0).getTime(), currentLocation.getTime()).getSeconds();
        }
    }

    public int getRecordDistance() {
        return targetInMeters;
    }

    public long getRecordTime() {
        return bestTimeInSeconds * targetInMeters / bestDistanceInMeters;
    }

    public long getRealTime() {
        return bestTimeInSeconds;
    }

    public int getRealDistance() {
        return bestDistanceInMeters;
    }

    public String printInfo() {
        StringBuilder builder = new StringBuilder();
        if (bestDistanceInMeters < targetInMeters) {
            builder.append("[" + Utils.formatDistanceInMeters(targetInMeters) + "]: " + "N/A");
        } else {
            builder.append("[" + Utils.formatDistanceInMeters(targetInMeters) + "]: "
                    + Utils.formatTimeInSeconds(getRecordTime()) + "\n");
            for (int b = bestStartIdx; b <= bestEndIdx; b++) {
                builder.append(" . " + locations.get(b).getTime());
            }
        }
        return builder.toString();
    }

    public String printRecordWithDetails() {
        StringBuilder builder = new StringBuilder();
        if (bestDistanceInMeters < targetInMeters) {
            builder.append("[" + Utils.formatDistanceInMeters(targetInMeters) + "]: " + "N/A");
        } else {
            builder.append("[" + Utils.formatDistanceInMeters(targetInMeters) + "]: "
                    + Utils.formatTimeInSeconds(getRecordTime())
                    + "\n");
            for (int b = bestStartIdx; b < bestEndIdx + 1; b++) {
                builder.append(". " + locations.get(b).getTime() + "\n");
            }
        }
        return builder.toString();
    }

    @Override
    public String toString() {
        if (bestDistanceInMeters < targetInMeters) {
            return "[" + Utils.formatDistanceInMeters(targetInMeters) + "]: " + "N/A";

        } else {
            return "[" + Utils.formatDistanceInMeters(targetInMeters) + "]: "
                    + Utils.formatTimeInSeconds(getRecordTime());
        }
    }
}
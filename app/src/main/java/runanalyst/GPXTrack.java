package runanalyst;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class GPXTrack {
    private String name = "anonymous";
    private List<GPXLocation> locations = new ArrayList<>();
    private int totalDistanceMeters = 0;
    private long totalTimeSeconds = 0;
    private final RollingRecords records;

    public GPXTrack() {
        records = new RollingRecords(100, 800, 1000, 1500, 5000);
    }

    public void addLocation(GPXLocation loc) {
        locations.add(loc);
    }

    public void computeTotalDistance() {
        totalDistanceMeters = 0;
        totalTimeSeconds = 0;
        for (int l = 1; l < locations.size(); l++) {
            GPXLocation l1 = locations.get(l - 1);
            GPXLocation l2 = locations.get(l);
            totalDistanceMeters += Utils.distanceInMeters(l2, l1);
            totalTimeSeconds += Duration.between(l1.getTime(), l2.getTime()).getSeconds();
        }
    }

    public void computeRecords() {
        records.computeRecords(locations);
    }

    public void setName(String n) {
        name = n;
    }

    public int getTotalDistanceMeters() {
        return totalDistanceMeters;
    }

    public long getTotalTimeSeconds() {
        return totalTimeSeconds;
    }

    public String printPace() {
        float paceSeconds = totalTimeSeconds / ((float) totalDistanceMeters / 1000);
        return Utils.formatTimeInSeconds((long) paceSeconds);
    }

    public String printInfo() {
        StringBuilder builder = new StringBuilder();
        builder.append("Name: " + name + "\n");
        builder.append("Date: " + locations.get(0).getTime() + "\n");
        builder.append("Total Distance: " + Utils.formatDistanceInMeters(totalDistanceMeters) + "\n");
        builder.append("Total Time: " + Utils.formatTimeInSeconds(totalTimeSeconds) + "\n");
        builder.append("Pace: " + printPace() + "\n");
        return builder.toString();
    }

    public String printRecords() {
        return records.toString();
    }

    @Override
    public String toString() {
        if (locations.isEmpty()) {
            return "[" + name + "][0]";
        } else {
            return "[" + name + "@" + locations.get(0).getTime() + "][" + locations.size() + "]";
        }
    }
}

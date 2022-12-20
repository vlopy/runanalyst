package runanalyst.gpx;

import java.time.Duration;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import runanalyst.properties.PropertyFile;

public class GPXTrack {
    private String name = "anonymous";
    private List<GPXLocation> locations = new ArrayList<>();
    private int totalDistanceMeters = 0;
    private long totalTimeSeconds = 0;
    private final RollingRecords allRecords;
    private final int SAMPLE_SIZE_METERS = 50;
    private final List<Sample> samples = new LinkedList<>();

    public GPXTrack() {
        PropertyFile props = PropertyFile.getInstance();
        allRecords = new RollingRecords(props.getRecordList());
    }

    public void addLocation(GPXLocation loc) {
        locations.add(loc);
    }

    // Analyze the GPX file
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

    public void computeSamples() {
        int distance = 0;
        long time = 0;
        int number = 0;
        for (int i = 1; i < locations.size(); i++) {
            distance += Utils.distanceInMeters(locations.get(i - 1), locations.get(i));
            time += Duration.between(locations.get(i - 1).getTime(), locations.get(i).getTime()).getSeconds();
            number++;
            if (distance >= SAMPLE_SIZE_METERS) {
                samples.add(new Sample(distance, (int) time, number));
                distance = 0;
                time = 0;
                number = 0;
            }
        }
        if (distance > 0) {
            samples.add(new Sample(distance, (int) time, number));
        }
    }

    public void computeRecords() {
        allRecords.computeRecords(locations);
    }

    // Setters & Getters of the track
    public void setName(String n) {
        name = n;
    }

    public String getName() {
        return name;
    }

    public long getFirstTime() {
        return locations.get(0).getTime().getEpochSecond();
    }

    public int getTotalDistanceMeters() {
        return totalDistanceMeters;
    }

    public long getTotalTimeSeconds() {
        return totalTimeSeconds;
    }

    public List<Record> getRecords() {
        return allRecords.getRecords();
    }

    public List<Sample> getSamples() {
        return samples;
    }

    // Display information
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

    public String printRecordsWithDetails() {
        return allRecords.printRecordsWithDetails();
    }

    public String printRecords() {
        return allRecords.toString();
    }

    public String printSamples() {
        StringBuilder builder = new StringBuilder();
        int totalDistance = 0;
        for (Sample s : samples) {
            totalDistance += s.getDistanceInMeters();
            builder.append(Utils.formatDistanceInMeters(totalDistance) + ": " + s.toString() + "\n");
        }
        return builder.toString();
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

package runanalyst.gpx;

public class Sample {
    private final int distanceInMeters;
    private final int timeInSeconds;
    private final int numberOfGPXPoints;

    public Sample(int pDist, int pTime, int pNumber) {
        distanceInMeters = pDist;
        timeInSeconds = pTime;
        numberOfGPXPoints = pNumber;
    }

    public long getTimeInSeconds() {
        return timeInSeconds;
    }

    public int getDistanceInMeters() {
        return distanceInMeters;
    }

    public int getNumberOfGPXPoints() {
        return numberOfGPXPoints;
    }

    public int computeSpeed() {
        return distanceInMeters / timeInSeconds;
    }

    public int computePace() {
        return 1000 * timeInSeconds / distanceInMeters;
    }

    @Override
    public String toString() {
        return Utils.formatTimeInSeconds(computePace()) + " | " + distanceInMeters + "m | "
                + timeInSeconds + "s | " + numberOfGPXPoints + " points";
    }
}

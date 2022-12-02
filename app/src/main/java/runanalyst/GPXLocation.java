package runanalyst;

import java.time.Instant;

public class GPXLocation {
    private double latitude;
    private double longitude;
    private double elevation;
    private Instant time;

    public GPXLocation(double lat, double lon, double alt, Instant t) {
        latitude = lat;
        longitude = lon;
        elevation = alt;
        time = t;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public double getElevation() {
        return elevation;
    }

    public Instant getTime() {
        return time;
    }

    @Override
    public String toString() {
        return "(" + latitude + ", " + longitude + "@" + time + ")";
    }

}

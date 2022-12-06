package runanalyst.gpx;

import com.google.common.hash.HashingOutputStream;

public class Utils {
    private static final double EARTH_RADIUS_KM = 6371;

    private static double degreesToRadians(double degrees) {
        return degrees * Math.PI / 180;
    }

    public static int distanceInMeters(GPXLocation l1, GPXLocation l2) {
        double dLat = degreesToRadians(l2.getLatitude() - l1.getLatitude());
        double dLon = degreesToRadians(l2.getLongitude() - l1.getLongitude());
        double lat1 = degreesToRadians(l1.getLatitude());
        double lat2 = degreesToRadians(l2.getLatitude());
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.sin(dLon / 2) * Math.sin(dLon / 2) * Math.cos(lat1) * Math.cos(lat2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return (int) (EARTH_RADIUS_KM * c * 1000);
    }

    public static String formatTimeInSeconds(long time) {
        long hours = time / 3600;
        long mins = (time - hours * 3600) / 60;
        long seconds = time % 60;
        StringBuilder builder = new StringBuilder();

        if (hours > 0) {
            builder.append(hours + "h");
        }
        if (mins > 0 || hours > 0 && mins == 0 && seconds > 0) {
            builder.append(String.format("%02d", mins) + "min");
        }
        if (seconds > 0) {
            builder.append(String.format("%02d", seconds) + "s");
        }
        return builder.toString();
    }

    public static String formatDistanceInMeters(int distance) {
        StringBuilder builder = new StringBuilder();
        int km = distance / 1000;
        int m = distance % 1000;
        if (km > 0) {
            builder.append(km + "km");
        }
        if (m > 0) {
            builder.append(m + "m");
        }
        return builder.toString();
    }
}
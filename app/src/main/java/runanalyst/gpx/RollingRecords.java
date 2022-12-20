package runanalyst.gpx;

import java.util.ArrayList;
import java.util.List;

public class RollingRecords {
    private List<Record> records = new ArrayList<>();

    public RollingRecords(List<Integer> pDistInMeters) {
        for (Integer recordDistance : pDistInMeters) {
            records.add(new Record(recordDistance));
        }
    }

    public void computeRecords(List<GPXLocation> pLocations) {
        // Initialize the records with the first two locations
        for (GPXLocation loc : pLocations) {
            for (Record r : records) {
                r.addLocation(loc);
            }
        }
    }

    public List<Record> getRecords() {
        return records;
    }

    public String printRecordsWithDetails() {
        StringBuilder builder = new StringBuilder();
        for (Record r : records) {
            builder.append(r.printRecordWithDetails() + "\n");
        }
        return builder.toString();

    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        for (Record r : records) {
            builder.append(r.toString() + "\n");
        }
        return builder.toString();
    }
}
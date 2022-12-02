package runanalyst;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

public class RollingRecords {
    private List<Record> records = new ArrayList<>();

    public RollingRecords(int... pDistInMeters) {
        for (int i = 0; i < pDistInMeters.length; i++) {
            records.add(new Record(pDistInMeters[i]));
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

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        for (Record r : records) {
            builder.append(r.toString() + "\n");
        }
        return builder.toString();
    }
}
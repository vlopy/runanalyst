package runanalyst;

import java.time.Instant;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class GPXHandler extends DefaultHandler {
    // Buffer to add the characters while reading
    private StringBuffer buffer = new StringBuffer();
    // Latitude of the current position
    private double lat;
    // Longitude of the current position
    private double lon;
    // Altitude of the current position
    private double alt;
    // Time of the current position
    private Instant timestamp;
    // The list of the locations extracted from the GPX file
    private GPXTrack track = new GPXTrack();

    @Override
    public void startElement(final String URI, final String LOCAL_NAME, final String Q_NAME,
            final Attributes ATTRIBUTES) throws SAXException {
        buffer.setLength(0);
        if (Q_NAME.equals("trkpt")) {
            lat = Double.parseDouble(ATTRIBUTES.getValue("lat"));
            lon = Double.parseDouble(ATTRIBUTES.getValue("lon"));
        }
    }

    @Override
    public void endElement(final String URI, final String LOCAL_NAME, final String Q_NAME) throws SAXException {
        if (Q_NAME.equals("trkpt")) {
            track.addLocation(new GPXLocation(lat, lon, alt, timestamp));
        } else if (Q_NAME.equals("alt")) {
            alt = Double.parseDouble(buffer.toString());
        } else if (Q_NAME.equals("time")) {
            timestamp = Instant.parse(buffer.toString());
        } else if (Q_NAME.equals("name")) {
            System.out.println(buffer.toString());
            track.setName(buffer.toString());
        }
    }

    @Override
    public void characters(final char[] CHARS, final int START, final int LENGTH) throws SAXException {
        buffer.append(CHARS, START, LENGTH);
    }

    public GPXTrack getTrack() {
        return track;
    }
}

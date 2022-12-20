package runanalyst.gpx;

import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.SAXException;

public class GPXParser {
  private SAXParserFactory factory = SAXParserFactory.newInstance();

  public GPXTrack parse(InputStream gpxStream) throws ParserConfigurationException, SAXException, IOException {
    SAXParser saxParser = factory.newSAXParser();
    GPXHandler handler = new GPXHandler();
    saxParser.parse(gpxStream, handler);
    return handler.getTrack();
  }
}

package runanalyst.properties;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

public class PropertyFile {
    private static final PropertyFile instance = new PropertyFile();
    private static final String filename = "runanalyst.properties";
    private static Properties props = new Properties();
    private static List<String> allProperties = new LinkedList<>() {
        {
            add("db.url");
            add("db.user");
            add("db.password");
            add("records.list");
        }
    };

    private PropertyFile() {
    }

    public static void init() throws IOException, PropertyException {
        InputStream input = new FileInputStream(filename);
        props.load(input);
        for (String prop : allProperties) {
            if (props.getProperty(prop) == null) {
                throw new PropertyException(
                        "Property '" + prop + "' is missing in the property file '" + filename + "'");
            }
        }
    }

    public static PropertyFile getInstance() {
        return instance;
    }

    public void checkMissingProperties() {

    }

    public String getDatabaseURL() {
        return props.getProperty("db.url");
    }

    public String getDatabaseUser() {
        return props.getProperty("db.user");
    }

    public String getDatabasePassword() {
        return props.getProperty("db.password");
    }

    public List<Integer> getRecordList() {
        String[] recordStr = props.getProperty("records.list").replaceAll(" ", "").split(",");
        List<Integer> recordInt = new ArrayList<>();
        for (String target : recordStr) {
            recordInt.add(Integer.parseInt(target));
        }
        return recordInt;
    }
}
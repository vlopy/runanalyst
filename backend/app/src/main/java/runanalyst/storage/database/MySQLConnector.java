package runanalyst.storage.database;

import java.sql.Statement;
import java.util.Calendar;

import runanalyst.gpx.GPXTrack;
import runanalyst.gpx.Record;
import runanalyst.gpx.Sample;
import runanalyst.storage.PersistentStorage;
import runanalyst.storage.StorageException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MySQLConnector implements PersistentStorage {
    private final String dbURL, dbUser, dbPwd;
    private Connection connector;

    public MySQLConnector(String url, String user, String pwd) throws ClassNotFoundException, SQLException {
        dbURL = url;
        dbUser = user;
        dbPwd = pwd;
        connector = DriverManager.getConnection(dbURL, dbUser, dbPwd);
    }

    @Override
    public void init() throws StorageException {
        System.out.println("initializing...");
        try (Statement st = connector.createStatement()) {
            ResultSet result = st.executeQuery("SHOW TABLES");
            if (result.next() == false) {
                String tableQuery = "CREATE TABLE tracks (" +
                        "timestamp INT PRIMARY KEY," +
                        "distance_m INT NOT NULL," +
                        "time_s INT NOT NULL," +
                        "name TEXT)";
                st.executeUpdate(tableQuery);
                tableQuery = "CREATE TABLE records (" +
                        "track_id INT NOT NULL," +
                        "distance_m INT NOT NULL," +
                        "time_s INT NOT NULL)";
                st.executeUpdate(tableQuery);
            }
        } catch (SQLException e) {
            throw new StorageException("Can not initialize the persistent storage:", e);
        }
    }

    @Override
    public void save(GPXTrack track) throws StorageException {
        this.save(track, null);
    }

    @Override
    public void save(GPXTrack track, String trackName) throws StorageException {
        try (Statement st = connector.createStatement()) {
            // Check the track does not already exist
            String selectQuery = "SELECT * FROM tracks WHERE timestamp = " + track.getFirstTime();
            ResultSet toUpdate = st.executeQuery(selectQuery);
            if (toUpdate.next() == false) {
                // Insert the track information
                String insertQuery;
                if (trackName != null && trackName.length() > 0) {
                    insertQuery = "INSERT INTO tracks VALUES (" +
                            track.getFirstTime() + ", " +
                            track.getTotalDistanceMeters() + ", " +
                            track.getTotalTimeSeconds() + ", " +
                            trackName + ")";
                } else {
                    insertQuery = "INSERT INTO tracks VALUES (" +
                            track.getFirstTime() + ", " +
                            track.getTotalDistanceMeters() + ", " +
                            track.getTotalTimeSeconds() + ", " +
                            "'" + track.getName() + "')";
                }
                st.executeUpdate(insertQuery);
                // Insert the records performed during this track
                for (Record r : track.getRecords()) {
                    if (r.isComplete()) {
                        insertQuery = "INSERT INTO records VALUES (" +
                                track.getFirstTime() + ", " +
                                r.getRecordDistance() + ", " +
                                r.getRecordTime() + ")";
                        st.executeUpdate(insertQuery);
                    }
                }
                // Create a table to register information about the sampling
                Calendar cal = Calendar.getInstance();
                cal.setTimeInMillis(track.getFirstTime() * 1000);
                String tableQuery = "CREATE TABLE IF NOT EXISTS samples_" + cal.get(Calendar.YEAR) + " (" +
                        "track_id INT NOT NULL," +
                        "sample_idx INT NOT NULL," +
                        "distance_m INT NOT NULL," +
                        "time_s INT NOT NULL)";
                st.executeUpdate(tableQuery);
                // Insert the speed computed every sample time
                StringBuilder builder = new StringBuilder();
                builder.append("INSERT INTO samples_" + cal.get(Calendar.YEAR) + " VALUES ");
                for (int i = 0; i < track.getSamples().size(); i++) {
                    Sample s = track.getSamples().get(i);
                    builder.append("(" + track.getFirstTime() + ", " + i + ", " + s.getDistanceInMeters() + ", "
                            + s.getTimeInSeconds() + "), ");
                }
                // Remove the last comma
                builder.setLength(builder.length() - 2);
                st.executeUpdate(builder.toString());
            } else {
                throw new StorageException("A track with the same departure time already exists!");
            }
        } catch (SQLException e) {
            throw new StorageException("Saving " + track.getName() + " failed!", e);
        }
    }
}
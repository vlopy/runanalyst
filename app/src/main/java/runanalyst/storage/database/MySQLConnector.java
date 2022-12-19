package runanalyst.storage.database;

import java.sql.Statement;

import runanalyst.gpx.GPXTrack;
import runanalyst.gpx.Record;
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
                        "distance_m INT," +
                        "time_s INT," +
                        "name TEXT)";
                st.executeUpdate(tableQuery);
                tableQuery = "CREATE TABLE records (" +
                        "id INT PRIMARY KEY AUTO_INCREMENT," +
                        "timestamp INT NOT NULL," +
                        "distance_m INT," +
                        "time_s INT)";
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
            String selectQuery = "SELECT * FROM tracks WHERE timestamp = " + track.getFirstTime();
            ResultSet toUpdate = st.executeQuery(selectQuery);
            if (toUpdate.next() == false) {
                String insertQuery;
                if (trackName != null && trackName.length() > 0) {
                    insertQuery = "INSERT INTO tracks (timestamp, distance_m, time_s, name) VALUES (" +
                            track.getFirstTime() + ", " +
                            track.getTotalDistanceMeters() + ", " +
                            track.getTotalTimeSeconds() + ", " +
                            trackName + ")";
                } else {
                    insertQuery = "INSERT INTO tracks (timestamp, distance_m, time_s, name) VALUES (" +
                            track.getFirstTime() + ", " +
                            track.getTotalDistanceMeters() + ", " +
                            track.getTotalTimeSeconds() + ", " +
                            "'" + track.getName() + "')";
                }
                st.executeUpdate(insertQuery);
                ResultSet getTrackId = st.executeQuery(selectQuery);
                getTrackId.next();
                for (Record r : track.getRecords()) {
                    if (r.isComplete()) {
                        insertQuery = "INSERT INTO records (timestamp, distance_m, time_s) VALUES (" +
                                getTrackId.getInt(1) + ", " +
                                r.getRecordDistance() + ", " +
                                r.getRecordTime() + ")";
                        st.executeUpdate(insertQuery);
                    }
                }
            } else {
                throw new StorageException("A track with the same departure time already exists!");
            }
        } catch (SQLException e) {
            throw new StorageException("Saving " + track.getName() + " failed!", e);
        }
    }
}
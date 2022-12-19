package runanalyst.storage;

import runanalyst.gpx.GPXTrack;

public interface PersistentStorage {
    // Initialize the backend storage
    public void init() throws StorageException;

    // Save the GPXTrack information to the storage
    public void save(GPXTrack track) throws StorageException;

    // Save the GPXTrack information to the storage
    public void save(GPXTrack track, String trackName) throws StorageException;

}
